package com.example.management.controller;

import com.example.management.exception.UserNotFoundException;
import com.example.management.model.User;
import com.example.management.model.UserModelAssembler;
import com.example.management.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@CrossOrigin(origins = "http://localhost:7000")
@RestController
@RequestMapping("/api")
public class UserManagementController {
    private final UserRepository userRepository;
    private final UserModelAssembler userAssembler;

    @Autowired
    public UserManagementController(UserModelAssembler userAssembler, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.userAssembler = userAssembler;
    }

    @GetMapping("/users/{id}")
    @Operation(summary = "Get user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content)})
    public EntityModel<User> getOneUser(@PathVariable("id") long id) {
        User user = userRepository.findById(id) //
                .orElseThrow(() -> new UserNotFoundException(id));

        return userAssembler.toModel(user);
    }

    @GetMapping("/users")
    @Operation(summary = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the users",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))})
    })
    public CollectionModel<EntityModel<User>> getAllUsers() {
        List<EntityModel<User>> users = userRepository.findAll().stream()
                .map(userAssembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(users,
                linkTo(methodOn(UserManagementController.class).getAllUsers()).withSelfRel());
    }


}
