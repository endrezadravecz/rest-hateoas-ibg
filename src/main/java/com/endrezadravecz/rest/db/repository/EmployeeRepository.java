package com.endrezadravecz.rest.db.repository;

import com.endrezadravecz.rest.model.Employee;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {

    List<Employee> findByManagerId(Long id);

}
