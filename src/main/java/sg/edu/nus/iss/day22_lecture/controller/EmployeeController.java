package sg.edu.nus.iss.day22_lecture.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("/")
    public ResponseEntity<Boolean> createEmployee(@RequestBody Employee employee) {
        Employee emp = employee;
        Boolean result = empRepo.save(emp);

        if (result) {
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }

    @PutMapping("/")
    public ResponseEntity<Integer> updateEmployee(@RequestBody Employee employee) {
        Employee emp = employee;
        int updated = empRepo.update(emp);

        if (updated == 1) {
            return new ResponseEntity<>(1, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteEmployee(@PathVariable("id") Integer id) {
        int deleteResult = 0;

        deleteResult = empRepo.deleteById(id);

        if (deleteResult == 0) {
            return new ResponseEntity<>(0, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(1, HttpStatus.OK);
        }
        
    }
}
