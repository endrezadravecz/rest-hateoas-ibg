package com.endrezadravecz.rest.controller;

import com.endrezadravecz.rest.assembler.EmployeeRepresentationModelAssembler;
import com.endrezadravecz.rest.db.repository.EmployeeRepository;
import com.endrezadravecz.rest.db.repository.ManagerRepository;
import com.endrezadravecz.rest.dto.EmployeeCreationDTO;
import com.endrezadravecz.rest.exception.EntityNotFoundException;
import com.endrezadravecz.rest.model.Employee;
import com.endrezadravecz.rest.model.Manager;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Links;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class EmployeeController {

    private final EmployeeRepository employeeRepository;
    private final ManagerRepository managerRepository;
    private final EmployeeRepresentationModelAssembler assembler;

    EmployeeController(EmployeeRepository employeeRepository, ManagerRepository managerRepository, EmployeeRepresentationModelAssembler assembler) {
        this.employeeRepository = employeeRepository;
        this.managerRepository = managerRepository;
        this.assembler = assembler;
    }

    @GetMapping("/employees")
    public ResponseEntity<CollectionModel<EntityModel<Employee>>> findAll() {
        return ResponseEntity.ok(assembler.toCollectionModel(employeeRepository.findAll()));
    }

    @PostMapping("/employees")
    public ResponseEntity<EntityModel<Employee>> createEmployee(@Valid @RequestBody EmployeeCreationDTO employee) throws EntityNotFoundException {
        final Optional<Manager> manager = managerRepository.findById(employee.getManagerId());
        if (manager.isPresent()) {
            final Employee convertedEmployee = new Employee(employee.getName(), employee.getRole(), manager.get());
            final Employee savedEmployee = employeeRepository.save(convertedEmployee);
            final URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedEmployee.getId()).toUri();
            return ResponseEntity.created(location).body(assembler.toModel(savedEmployee));
        }
        throw new EntityNotFoundException("Manager with id " + employee.getManagerId() + " was not found.");
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<EntityModel<Employee>> findOne(@PathVariable long id) {
        return employeeRepository.findById(id).map(assembler::toModel).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<EntityModel<Employee>> replaceEmployee(@Valid @RequestBody EmployeeCreationDTO employee, @PathVariable long id) throws EntityNotFoundException {
        final Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isPresent()) {
            final Optional<Manager> manager = managerRepository.findById(employee.getManagerId());
            if (manager.isPresent()) {
                final Employee savedEmployee = optionalEmployee.get();
                savedEmployee.setName(employee.getName());
                savedEmployee.setRole(employee.getRole());
                savedEmployee.setManager(manager.get());
                employeeRepository.save(savedEmployee);
                return ResponseEntity.ok(assembler.toModel(savedEmployee));
            } else {
                throw new EntityNotFoundException("Manager with id " + employee.getManagerId() + " was not found.");
            }
        } else {
            return createEmployee(employee);
        }
    }

    @GetMapping("/managers/{id}/employees")
    public ResponseEntity<CollectionModel<EntityModel<Employee>>> findEmployees(@PathVariable long id) {
        CollectionModel<EntityModel<Employee>> collectionModel = assembler.toCollectionModel(employeeRepository.findByManagerId(id));
        Links newLinks = collectionModel.getLinks().merge(Links.MergeMode.REPLACE_BY_REL, linkTo(methodOn(EmployeeController.class).findEmployees(id)).withSelfRel());
        return ResponseEntity.ok(CollectionModel.of(collectionModel.getContent(), newLinks));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
