package com.example.management.model;

import com.example.management.controller.CaseManagementController;
import com.example.management.controller.UserManagementController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModelAssembler  implements RepresentationModelAssembler<User, EntityModel<User>> {
    @Override
    public EntityModel<User> toModel(User user){
        return EntityModel.of(user,
                linkTo(methodOn(UserManagementController.class).getOneUser(user.getId())).withSelfRel(),
                linkTo(methodOn(CaseManagementController.class).getAllCases()).withRel("all cases"),
                linkTo(methodOn(UserManagementController.class).getAllUsers()).withRel("all users")

                );
    }
}
