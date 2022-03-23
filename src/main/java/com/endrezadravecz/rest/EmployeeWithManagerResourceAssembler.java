package com.endrezadravecz.rest;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
class EmployeeWithManagerResourceAssembler implements SimpleRepresentationModelAssembler<EmployeeWithManager> {

    @Override
    public void addLinks(EntityModel<EmployeeWithManager> resource) {
        resource.add(linkTo(methodOn(EmployeeController.class).findDetailedEmployee(resource.getContent().getId())).withSelfRel());
        resource.add(linkTo(methodOn(EmployeeController.class).findOne(resource.getContent().getId())).withRel("summary"));
        resource.add(linkTo(methodOn(EmployeeController.class).findAllDetailedEmployees()).withRel("detailedEmployees"));
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<EmployeeWithManager>> resources) {
        resources.add(linkTo(methodOn(EmployeeController.class).findAllDetailedEmployees()).withSelfRel());
        resources.add(linkTo(methodOn(EmployeeController.class).findAll()).withRel("employees"));
        resources.add(linkTo(methodOn(ManagerController.class).findAll()).withRel("managers"));
        resources.add(linkTo(methodOn(RootController.class).root()).withRel("root"));
    }
}
