package com.endrezadravecz.rest.assembler;

import com.endrezadravecz.rest.controller.EmployeeController;
import com.endrezadravecz.rest.controller.ManagerController;
import com.endrezadravecz.rest.controller.RootController;
import com.endrezadravecz.rest.model.Manager;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ManagerRepresentationModelAssembler extends SimpleIdentifiableRepresentationModelAssembler<Manager> {

    ManagerRepresentationModelAssembler() {
        super(ManagerController.class);
    }

    @Override
    public void addLinks(EntityModel<Manager> resource) {
        super.addLinks(resource);

        resource.getContent().getId().ifPresent(id -> {
            resource.add(linkTo(methodOn(EmployeeController.class).findEmployees(id)).withRel("employees"));
        });
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<Manager>> resources) {

        super.addLinks(resources);

        resources.add(linkTo(methodOn(EmployeeController.class).findAll()).withRel("employees"));
        resources.add(linkTo(methodOn(EmployeeController.class).findAllDetailedEmployees()).withRel("detailedEmployees"));
        resources.add(linkTo(methodOn(RootController.class).root()).withRel("root"));
    }
}
