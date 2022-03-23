package com.endrezadravecz.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Value;

import java.util.List;
import java.util.stream.Collectors;

@Value
@JsonPropertyOrder({"id", "name", "employees"})
class Supervisor {

    @JsonIgnore
    private final Manager manager;

    public Long getId() {
        return this.manager.getId().orElseThrow(() -> new RuntimeException("Couldn't find anything"));
    }

    public String getName() {
        return this.manager.getName();
    }

    public List<String> getEmployees() {
        return manager.getEmployees().stream().map(employee -> employee.getName() + "::" + employee.getRole()).collect(Collectors.toList());
    }
}
