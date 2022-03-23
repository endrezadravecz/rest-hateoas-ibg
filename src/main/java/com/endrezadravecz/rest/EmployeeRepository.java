package com.endrezadravecz.rest;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

interface EmployeeRepository extends CrudRepository<Employee, Long> {

    List<Employee> findByManagerId(Long id);

}
