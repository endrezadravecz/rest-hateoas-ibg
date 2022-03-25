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

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

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
    public ResponseEntity<EntityModel<Manager>> createManager(@Valid @RequestBody ManagerCreationDTO manager) {
        final Manager savedManager = repository.save(new Manager(manager.getName()));
        final URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedManager.getId()).toUri();
        return ResponseEntity.created(location).body(assembler.toModel(savedManager));
    }

    @GetMapping("/managers/{id}")
    ResponseEntity<EntityModel<Manager>> findOne(@PathVariable long id) {
        return repository.findById(id).map(assembler::toModel).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/managers/{id}")
    public ResponseEntity<EntityModel<Manager>> replaceManager(@Valid @RequestBody ManagerCreationDTO manager, @PathVariable long id) {
        final Optional<Manager> optionalManager = repository.findById(id);
        if (optionalManager.isPresent()) {
            final Manager savedManager = optionalManager.get();
            savedManager.setName(manager.getName());
            repository.save(savedManager);
            return ResponseEntity.ok(assembler.toModel(savedManager));
        }
        return createManager(manager);
    }

    @GetMapping("/employees/{id}/manager")
    public ResponseEntity<EntityModel<Manager>> findManager(@PathVariable long id) {
        return ResponseEntity.ok(assembler.toModel(repository.findByEmployeesId(id)));
    }
}
