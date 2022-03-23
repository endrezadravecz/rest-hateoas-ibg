package com.endrezadravecz.rest;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

class DatabaseLoader {

    @Bean
    CommandLineRunner initDatabase(EmployeeRepository employeeRepository, ManagerRepository managerRepository) {
        return args -> {

            Manager gandalf = managerRepository.save(new Manager("Gandalf"));

            Employee frodo = employeeRepository.save(new Employee("Frodo", "ring bearer", gandalf));
            Employee bilbo = employeeRepository.save(new Employee("Bilbo", "burglar", gandalf));

            gandalf.setEmployees(Arrays.asList(frodo, bilbo));
            managerRepository.save(gandalf);

            Manager saruman = managerRepository.save(new Manager("Saruman"));

            Employee sam = employeeRepository.save(new Employee("Sam", "gardener", saruman));

            saruman.setEmployees(Arrays.asList(sam));

            managerRepository.save(saruman);
        };
    }
}
