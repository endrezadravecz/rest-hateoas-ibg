package com.endrezadravecz.rest.controller;

import com.endrezadravecz.rest.assembler.EmployeeRepresentationModelAssembler;
import com.endrezadravecz.rest.assembler.EmployeeWithManagerResourceAssembler;
import com.endrezadravecz.rest.db.repository.EmployeeRepository;
import com.endrezadravecz.rest.db.repository.ManagerRepository;
import com.endrezadravecz.rest.dto.EmployeeCreationDTO;
import com.endrezadravecz.rest.model.Employee;
import com.endrezadravecz.rest.model.EmployeeWithManager;
import com.endrezadravecz.rest.model.Manager;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Links;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class EmployeeController {

    private final EmployeeRepository employeeRepository;
    private final ManagerRepository managerRepository;
    private final EmployeeRepresentationModelAssembler assembler;
    private final EmployeeWithManagerResourceAssembler employeeWithManagerResourceAssembler;

    EmployeeController(EmployeeRepository employeeRepository, ManagerRepository managerRepository, EmployeeRepresentationModelAssembler assembler, EmployeeWithManagerResourceAssembler employeeWithManagerResourceAssembler) {
        this.employeeRepository = employeeRepository;
        this.managerRepository = managerRepository;
        this.assembler = assembler;
        this.employeeWithManagerResourceAssembler = employeeWithManagerResourceAssembler;
    }

    @GetMapping("/employees")
    public ResponseEntity<CollectionModel<EntityModel<Employee>>> findAll() {
        return ResponseEntity.ok(assembler.toCollectionModel(employeeRepository.findAll()));
    }

    @PostMapping("/employees")
    public ResponseEntity<Object> createEmployee(@RequestBody EmployeeCreationDTO employee) {
        final Optional<Manager> manager = managerRepository.findById(employee.getManagerId());
        if (manager.isPresent()) {
            final Employee convertedEmployee = new Employee(employee.getName(), employee.getRole(), manager.get());
            final Employee savedEmployee = employeeRepository.save(convertedEmployee);
            final URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedEmployee.getId()).toUri();
            return ResponseEntity.created(location).build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<EntityModel<Employee>> findOne(@PathVariable long id) {
        return employeeRepository.findById(id).map(assembler::toModel).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/managers/{id}/employees")
    public ResponseEntity<CollectionModel<EntityModel<Employee>>> findEmployees(@PathVariable long id) {
        CollectionModel<EntityModel<Employee>> collectionModel = assembler.toCollectionModel(employeeRepository.findByManagerId(id));
        Links newLinks = collectionModel.getLinks().merge(Links.MergeMode.REPLACE_BY_REL, linkTo(methodOn(EmployeeController.class).findEmployees(id)).withSelfRel());
        return ResponseEntity.ok(CollectionModel.of(collectionModel.getContent(), newLinks));
    }

    @GetMapping("/employees/detailed")
    public ResponseEntity<CollectionModel<EntityModel<EmployeeWithManager>>> findAllDetailedEmployees() {
        return ResponseEntity.ok(employeeWithManagerResourceAssembler.toCollectionModel(StreamSupport.stream(employeeRepository.findAll().spliterator(), false).map(EmployeeWithManager::new).collect(Collectors.toList())));
    }

    @GetMapping("/employees/{id}/detailed")
    public ResponseEntity<EntityModel<EmployeeWithManager>> findDetailedEmployee(@PathVariable Long id) {
        return employeeRepository.findById(id).map(EmployeeWithManager::new).map(employeeWithManagerResourceAssembler::toModel).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
