package com.example.management.model;

import com.example.management.controller.CaseManagementController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CaseModelAssembler  implements RepresentationModelAssembler<Case, EntityModel<Case>> {
    @Override
    public EntityModel<Case> toModel(Case _case){
        return EntityModel.of(_case,
                linkTo(methodOn(CaseManagementController.class).getOneCase(_case.getId())).withSelfRel(),
                linkTo(methodOn(CaseManagementController.class).closeCase(_case.getId())).withRel("close case"),
                linkTo(methodOn(CaseManagementController.class).getAllCases()).withRel("all cases"),
                linkTo(methodOn(CaseManagementController.class).getCaseUser(_case.getId())).withRel("user")
                );
    }
}
