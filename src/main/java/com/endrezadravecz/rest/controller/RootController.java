package com.endrezadravecz.rest.controller;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public
class RootController {

    @GetMapping("/")
    public ResponseEntity<RepresentationModel> root() {

        RepresentationModel model = new RepresentationModel();

        model.add(linkTo(methodOn(RootController.class).root()).withSelfRel());
        model.add(linkTo(methodOn(EmployeeController.class).findAll()).withRel("employees"));
        model.add(linkTo(methodOn(EmployeeController.class).findAllDetailedEmployees()).withRel("detailedEmployees"));
        model.add(linkTo(methodOn(ManagerController.class).findAll()).withRel("managers"));

        return ResponseEntity.ok(model);
    }
}
