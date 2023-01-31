package sg.edu.nus.iss.day22_lecture.repo;

import java.util.List;

import sg.edu.nus.iss.day22_lecture.model.Employee;

public interface EmployeeRepo {
    
    List<Employee> retrieveEmployeeList();
}
