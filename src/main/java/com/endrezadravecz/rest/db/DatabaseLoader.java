package com.endrezadravecz.rest.db;

import com.endrezadravecz.rest.db.repository.EmployeeRepository;
import com.endrezadravecz.rest.db.repository.ManagerRepository;
import com.endrezadravecz.rest.model.Employee;
import com.endrezadravecz.rest.model.Manager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
class DatabaseLoader {

    @Bean
    CommandLineRunner initDatabase(EmployeeRepository employeeRepository, ManagerRepository managerRepository) {
        return args -> {

            Manager gary = managerRepository.save(new Manager("Gary"));

            Employee al = employeeRepository.save(new Employee("Al Bundy", "shoe salesman", gary));
            Employee griff = employeeRepository.save(new Employee("Griff", "associate shoe salesman", gary));

            gary.setEmployees(Arrays.asList(al, griff));
            managerRepository.save(gary);

            Manager mrShimokawa = managerRepository.save(new Manager("Mr. Shimokawa"));

            Employee marcy = employeeRepository.save(new Employee("Marcy D'Arcy", "branch manager", mrShimokawa));

            mrShimokawa.setEmployees(Arrays.asList(marcy));

            managerRepository.save(mrShimokawa);
        };
    }
}
