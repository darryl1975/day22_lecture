package sg.edu.nus.iss.day22_lecture.repo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.day22_lecture.model.Dependant;
import sg.edu.nus.iss.day22_lecture.model.Employee;

@Repository
public class EmployeeRepoImpl implements EmployeeRepo {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Employee> retrieveEmployeeList() {
        String selectSQL = "select e.id emp_id, e.first_name, e.last_name, e.salary, " +
                "d.id dep_id, d.fullname, d.relationship, d.birth_date  " +
                "from employee e " +
                "inner join dependant d " +
                "on e.id = d.employee_id ";

        return jdbcTemplate.query(selectSQL, new ResultSetExtractor<List<Employee>>() {

            @Override
            public List<Employee> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<Employee> employees = new ArrayList<Employee>();

                while (rs.next()) {
                    Employee emp = new Employee();
                    emp.setId(rs.getInt("emp_id"));
                    emp.setFirstName(rs.getString("first_name"));
                    emp.setLastName(rs.getString("last_name"));
                    emp.setSalary(rs.getInt("salary"));
                    emp.setDependants(new ArrayList<Dependant>());

                    Dependant dep = new Dependant();
                    dep.setId(rs.getInt("dep_id"));
                    dep.setFullname(rs.getString("fullname"));
                    dep.setRelationship(rs.getString("relationship"));
                    dep.setBirthDate(rs.getDate("birth_date"));

                    if (employees.size() == 0) {
                        emp.getDependants().add(dep);
                        employees.add(emp);
                    } else {
                        List<Employee> eList = employees.stream().filter(e -> e.getId() == emp.getId()).collect(Collectors.toList());

                        if (eList.size() == 0) {
                            emp.getDependants().add(dep);
                            employees.add(emp);
                        } else {
                            int idx = employees.indexOf(eList.get(0));

                            if (idx >= 0) {
                                employees.get(idx).getDependants().add(dep);
                            }
                        }
                    }


                }
                return employees;
            }

        });

    }

}
