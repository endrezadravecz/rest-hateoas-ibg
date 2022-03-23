package com.endrezadravecz.rest;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
class ManagerController {

    private final ManagerRepository repository;
    private final ManagerRepresentationModelAssembler assembler;

    ManagerController(ManagerRepository repository, ManagerRepresentationModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/managers")
    ResponseEntity<CollectionModel<EntityModel<Manager>>> findAll() {
        return ResponseEntity.ok(assembler.toCollectionModel(repository.findAll()));

    }

    @GetMapping("/managers/{id}")
    ResponseEntity<EntityModel<Manager>> findOne(@PathVariable long id) {
        return repository.findById(id).map(assembler::toModel).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/employees/{id}/manager")
    ResponseEntity<EntityModel<Manager>> findManager(@PathVariable long id) {
        return ResponseEntity.ok(assembler.toModel(repository.findByEmployeesId(id)));
    }
}
