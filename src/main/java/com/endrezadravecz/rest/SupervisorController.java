package com.endrezadravecz.rest;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SupervisorController {

    private final ManagerController controller;

    public SupervisorController(ManagerController controller) {
        this.controller = controller;
    }

    @GetMapping("/supervisors/{id}")
    public ResponseEntity<EntityModel<Supervisor>> findOne(@PathVariable Long id) {

        EntityModel<Manager> managerResource = controller.findOne(id).getBody();

        EntityModel<Supervisor> supervisorResource = EntityModel.of(new Supervisor(managerResource.getContent()), managerResource.getLinks());

        return ResponseEntity.ok(supervisorResource);
    }
}
