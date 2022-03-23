package com.endrezadravecz.rest.db.repository;

import com.endrezadravecz.rest.model.Manager;
import org.springframework.data.repository.CrudRepository;

public interface ManagerRepository extends CrudRepository<Manager, Long> {

    Manager findByEmployeesId(Long id);
}
