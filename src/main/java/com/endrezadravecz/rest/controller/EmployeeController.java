package com.endrezadravecz.rest.controller;

import com.endrezadravecz.rest.assembler.EmployeeRepresentationModelAssembler;
import com.endrezadravecz.rest.assembler.EmployeeWithManagerResourceAssembler;
import com.endrezadravecz.rest.db.repository.EmployeeRepository;
import com.endrezadravecz.rest.model.Employee;
import com.endrezadravecz.rest.model.EmployeeWithManager;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Links;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public
class EmployeeController {

    private final EmployeeRepository repository;
    private final EmployeeRepresentationModelAssembler assembler;
    private final EmployeeWithManagerResourceAssembler employeeWithManagerResourceAssembler;

    EmployeeController(EmployeeRepository repository, EmployeeRepresentationModelAssembler assembler, EmployeeWithManagerResourceAssembler employeeWithManagerResourceAssembler) {
        this.repository = repository;
        this.assembler = assembler;
        this.employeeWithManagerResourceAssembler = employeeWithManagerResourceAssembler;
    }

    @GetMapping("/employees")
    public ResponseEntity<CollectionModel<EntityModel<Employee>>> findAll() {
        return ResponseEntity.ok(assembler.toCollectionModel(repository.findAll()));
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<EntityModel<Employee>> findOne(@PathVariable long id) {
        return repository.findById(id).map(assembler::toModel).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/managers/{id}/employees")
    public ResponseEntity<CollectionModel<EntityModel<Employee>>> findEmployees(@PathVariable long id) {
        CollectionModel<EntityModel<Employee>> collectionModel = assembler.toCollectionModel(repository.findByManagerId(id));
        Links newLinks = collectionModel.getLinks().merge(Links.MergeMode.REPLACE_BY_REL, linkTo(methodOn(EmployeeController.class).findEmployees(id)).withSelfRel());
        return ResponseEntity.ok(CollectionModel.of(collectionModel.getContent(), newLinks));
    }

    @GetMapping("/employees/detailed")
    public ResponseEntity<CollectionModel<EntityModel<EmployeeWithManager>>> findAllDetailedEmployees() {
        return ResponseEntity.ok(employeeWithManagerResourceAssembler.toCollectionModel(StreamSupport.stream(repository.findAll().spliterator(), false).map(EmployeeWithManager::new).collect(Collectors.toList())));
    }

    @GetMapping("/employees/{id}/detailed")
    public ResponseEntity<EntityModel<EmployeeWithManager>> findDetailedEmployee(@PathVariable Long id) {
        return repository.findById(id).map(EmployeeWithManager::new).map(employeeWithManagerResourceAssembler::toModel).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
