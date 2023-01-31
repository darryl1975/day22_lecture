package sg.edu.nus.iss.day22_lecture.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.nus.iss.day22_lecture.model.Employee;
import sg.edu.nus.iss.day22_lecture.repo.EmployeeRepo;

@RequestMapping("/api/employees")
@RestController
public class EmployeeController {
    @Autowired
    EmployeeRepo empRepo;

    @GetMapping("/")
    public List<Employee> retrieveEmployees() {

        List<Employee> employees = empRepo.retrieveEmployeeList();

        if (employees.isEmpty()) {
            return null;
        } else {
            return employees;
        }

    }
}
