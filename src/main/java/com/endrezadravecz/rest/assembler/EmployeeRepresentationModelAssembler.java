package com.endrezadravecz.rest.assembler;

import com.endrezadravecz.rest.controller.EmployeeController;
import com.endrezadravecz.rest.controller.ManagerController;
import com.endrezadravecz.rest.controller.RootController;
import com.endrezadravecz.rest.model.Employee;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class EmployeeRepresentationModelAssembler extends SimpleIdentifiableRepresentationModelAssembler<Employee> {

    EmployeeRepresentationModelAssembler() {
        super(EmployeeController.class);
    }

    @Override
    public void addLinks(EntityModel<Employee> resource) {

        super.addLinks(resource);

        resource.getContent().getId().ifPresent(id -> {
            resource.add(linkTo(methodOn(ManagerController.class).findManager(id)).withRel("manager"));
            resource.add(linkTo(methodOn(EmployeeController.class).findDetailedEmployee(id)).withRel("detailed"));
        });
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<Employee>> resources) {

        super.addLinks(resources);

        resources.add(linkTo(methodOn(EmployeeController.class).findAllDetailedEmployees()).withRel("detailedEmployees"));
        resources.add(linkTo(methodOn(ManagerController.class).findAll()).withRel("managers"));
        resources.add(linkTo(methodOn(RootController.class).root()).withRel("root"));
    }
}
