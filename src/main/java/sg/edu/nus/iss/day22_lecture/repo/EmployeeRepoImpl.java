package sg.edu.nus.iss.day22_lecture.repo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.day22_lecture.model.Dependant;
import sg.edu.nus.iss.day22_lecture.model.Employee;

@Repository
public class EmployeeRepoImpl implements EmployeeRepo {

    @Autowired
    JdbcTemplate jdbcTemplate;

    String selectSQL = "select * from employee";
    String selectByIdSQL = "select * from employee where id = ?";
    String insertSQL = "insert into employee (first_name, last_name, salary) values (?, ?, ?)";
    String updateSQL = "update employee set first_name = ?, last_name = ?, salary = ? where id = ?";
    String deleteSQL = "delete from employee where id = ?";

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

    @Override
    public Boolean save(Employee employee) {
        Boolean saved = false;

        saved = jdbcTemplate.execute(insertSQL, new PreparedStatementCallback<Boolean>() {

            @Override
            public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                ps.setString(1, employee.getFirstName());
                ps.setString(2, employee.getLastName());
                ps.setInt(3, employee.getSalary());
                Boolean rslt = ps.execute();
                return rslt;
            }

        });

        return saved;
    }

    @Override
    public int update(Employee employee) {
        int updated = 0;

        updated = jdbcTemplate.update(updateSQL, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, employee.getFirstName());
                ps.setString(2, employee.getLastName());
                ps.setInt(3, employee.getSalary());
                ps.setInt(4, employee.getId());
            }

        });

        return updated;
    }

    @Override
    public int deleteById(Integer id) {
        int deleted = 0;

        PreparedStatementSetter pss = new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setInt(1, id);
            }
        };

        deleted = jdbcTemplate.update(deleteSQL, pss);

        return deleted;
    }

}
