package com.endrezadravecz.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Optional;

@Data
@Entity
@NoArgsConstructor
public class Employee {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String role;

    @JsonIgnore
    @ManyToOne
    private Manager manager;

    public Employee(String name, String role, Manager manager) {
        this.name = name;
        this.role = role;
        this.manager = manager;
    }

    public Optional<Long> getId() {
        return Optional.ofNullable(this.id);
    }
}
