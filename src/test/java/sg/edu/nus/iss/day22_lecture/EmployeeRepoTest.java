package sg.edu.nus.iss.day22_lecture;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import sg.edu.nus.iss.day22_lecture.model.Employee;
import sg.edu.nus.iss.day22_lecture.repo.EmployeeRepo;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class EmployeeRepoTest {
    @Autowired
    EmployeeRepo empRepo;

    @Test
    public void retrieveAllEmployees() {
        List<Employee> empList = empRepo.retrieveEmployeeList();
        empList.forEach((emp) -> System.out.println(emp));
        assertTrue(empList.size() > 0);
    }
}
