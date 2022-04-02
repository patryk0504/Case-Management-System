package com.example.management.controller;

import com.example.management.exception.CaseNotFoundException;
import com.example.management.model.*;
import com.example.management.repository.CaseRepository;
import com.example.management.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@CrossOrigin(origins = "http://localhost:7000")
@RestController
@RequestMapping("/api")
public class CaseManagementController {
    private final CaseRepository caseRepository;
    private final UserRepository userRepository;
    private final CaseModelAssembler caseAssembler;
    private final UserModelAssembler userAssembler;


    @Autowired
    public CaseManagementController(CaseRepository caseRepository, CaseModelAssembler caseAssembler, UserModelAssembler userAssembler, UserRepository userRepository) {
        this.caseRepository = caseRepository;
        this.caseAssembler = caseAssembler;
        this.userRepository = userRepository;
        this.userAssembler = userAssembler;
    }

    @GetMapping("/cases")
    @Operation(summary = "Get all cases")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the cases",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))})
    })
    public CollectionModel<EntityModel<Case>> getAllCases() {
        List<EntityModel<Case>> cases = caseRepository.findAll().stream()
                .map(caseAssembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(cases,
                linkTo(methodOn(CaseManagementController.class).getAllCases()).withSelfRel());
    }

    @GetMapping("/cases/{id}")
    @Operation(summary = "Get case by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the case",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "404", description = "Case not found",
                    content = @Content)})
    public EntityModel<Case> getOneCase(@PathVariable("id") long id) {
        Case _case = caseRepository.findById(id) //
                .orElseThrow(() -> new CaseNotFoundException(id));

        return caseAssembler.toModel(_case);
    }

    @GetMapping("/cases/uuid/{id}")
    @Operation(summary = "Get case by unique id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the case",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "404", description = "Case not found",
                    content = @Content)})
    public EntityModel<Case> getCaseByUUID(@PathVariable("id") String id) {
        Case _case = caseRepository.findByUuid(UUID.fromString(id))
                .orElseThrow(() -> new CaseNotFoundException(id));

        return caseAssembler.toModel(_case);
    }

    @GetMapping("/cases/{id}/user")
    @Operation(summary = "Get user connected to the case")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "404", description = "Case not found",
                    content = @Content)})
    public EntityModel<User> getCaseUser(@PathVariable("id") long id) {
        Case _case = caseRepository.findById(id) //
                .orElseThrow(() -> new CaseNotFoundException(id));
        User _user = _case.getUser();
        return userAssembler.toModel(_user);
    }

    @PostMapping("/cases")
    @Operation(summary = "Create a case with given parameters",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "My description here.",
            content = @Content(examples = @ExampleObject("{\n" +
                    "    \"_user\" : {\n" +
                    "        \"username\" : \"testUsername\",\n" +
                    "        \"email\" : \"testEmail@wp.pl\"\n" +
                    "    },\n" +
                    "    \"_case\":{\n" +
                    "        \"title\" : \"testTitle\",\n" +
                    "        \"description\" : \"simpleDescription\",\n" +
                    "        \"severity\" : \"low\"\n" +
                    "}\n" +
                    "}"))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Case created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class)
                    )}),
            @ApiResponse(responseCode = "500", description = "Something went wrong",
                    content = @Content)})
    public ResponseEntity<?> createCase(@RequestBody RequestWrapper requestWrapper) {
        User _user = requestWrapper.get_user();
        Case _case = requestWrapper.get_case();
        User userEntity = userRepository.findByUsernameAndEmail(_user.getUsername(), _user.getEmail());
        _case.setUser(Objects.requireNonNullElse(userEntity, _user));
        EntityModel<Case> caseEntityModelCase = caseAssembler.toModel(caseRepository.save(_case));
        return ResponseEntity.created(caseEntityModelCase.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(caseEntityModelCase);
    }




    @PutMapping("/cases/{id}")
    @Operation(summary = "Update the case with given id",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "My description here.",
                    content = @Content(examples = @ExampleObject("{\n" +
                            "    \"title\" : \"testTitle2\",\n" +
                            "    \"description\" : \"simpleDescription2\",\n" +
                            "    \"severity\" : \"LOW\",\n" +
                            "    \"status\" : \"DONE\"\n" +
                            "}"))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Case updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "404", description = "Case not found",
                    content = @Content)})
    public ResponseEntity<?> updateCase(@PathVariable("id") long id, @RequestBody Case updatedCase) {
        Case _case = caseRepository.findById(id) //
                .orElseThrow(() -> new CaseNotFoundException(id));
        if (updatedCase.getTitle() != null) _case.setTitle(updatedCase.getTitle());
        if (updatedCase.getDescription() != null) _case.setDescription(updatedCase.getDescription());
        if (updatedCase.getSeverity() != null) _case.setSeverity(updatedCase.getSeverity());
        if (updatedCase.getStatus() != null) _case.setStatus(updatedCase.getStatus());
        EntityModel<Case> caseEntityModel = caseAssembler.toModel(caseRepository.save(_case));
        return ResponseEntity.created(caseEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(caseEntityModel);
    }

    @DeleteMapping("/cases/{id}")
    @Operation(summary = "Remove closed case with given id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Case removed",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "404", description = "Case not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Case isn't closed",
                    content = @Content)
    })
    public ResponseEntity<String> removeClosedCase(@PathVariable("id") long id) {
        Optional<Case> optionalCase = caseRepository.findById(id);
        Case _case = caseRepository.findById(id) //
                .orElseThrow(() -> new CaseNotFoundException(id));

        if (_case.getStatus() == Status.DONE) {
            caseRepository.deleteById(id);
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>("Case status must be closed!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    @DeleteMapping("/cases")
    @Operation(summary = "Remove all closed cases")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cases removed - number of remmoved cases",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))})
    })
    public ResponseEntity<String> removeAllClosedCases() {
        Long numOfDeleted = caseRepository.removeAllByStatus(Status.DONE);
        return new ResponseEntity<>("Successfully removed " + numOfDeleted + " records", HttpStatus.OK);
    }

    @PatchMapping("/cases/{id}/close")
    @Operation(summary = "Close the case with given id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Case closed",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "404", description = "Case not found",
                    content = @Content)})
    public ResponseEntity<?> closeCase(@PathVariable("id") long id) {
        Case _case = caseRepository.findById(id) //
                .orElseThrow(() -> new CaseNotFoundException(id));
        _case.setStatus(Status.DONE);
        EntityModel<Case> caseEntityModel = caseAssembler.toModel(caseRepository.save(_case));
        return ResponseEntity.created(caseEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(caseEntityModel);
    }
}
