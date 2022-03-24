package com.endrezadravecz.rest.controller;

import com.endrezadravecz.rest.assembler.ManagerRepresentationModelAssembler;
import com.endrezadravecz.rest.db.repository.ManagerRepository;
import com.endrezadravecz.rest.dto.ManagerCreationDTO;
import com.endrezadravecz.rest.model.Manager;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
public class ManagerController {

    private final ManagerRepository repository;
    private final ManagerRepresentationModelAssembler assembler;

    ManagerController(ManagerRepository repository, ManagerRepresentationModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/managers")
    public ResponseEntity<CollectionModel<EntityModel<Manager>>> findAll() {
        return ResponseEntity.ok(assembler.toCollectionModel(repository.findAll()));
    }

    @PostMapping("/managers")
    public ResponseEntity<Object> createManager(@RequestBody ManagerCreationDTO manager) {
        final Manager savedManager = repository.save(new Manager(manager.getName()));
        final URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedManager.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/managers/{id}")
    ResponseEntity<EntityModel<Manager>> findOne(@PathVariable long id) {
        return repository.findById(id).map(assembler::toModel).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/employees/{id}/manager")
    public ResponseEntity<EntityModel<Manager>> findManager(@PathVariable long id) {
        return ResponseEntity.ok(assembler.toModel(repository.findByEmployeesId(id)));
    }
}
