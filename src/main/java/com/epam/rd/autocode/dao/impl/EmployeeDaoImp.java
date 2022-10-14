package com.epam.rd.autocode.dao.impl;

import com.epam.rd.autocode.ConnectionSource;
import com.epam.rd.autocode.dao.EmployeeDao;
import com.epam.rd.autocode.domain.Department;
import com.epam.rd.autocode.domain.Employee;
import com.epam.rd.autocode.domain.FullName;
import com.epam.rd.autocode.domain.Position;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeDaoImp implements EmployeeDao {
    private ConnectionSource connectionSource = ConnectionSource.instance();
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FIRSTNAME = "firstname";
    private static final String COLUMN_LASTNAME = "lastname";
    private static final String COLUMN_MIDDLE_NAME = "middlename";
    private static final String COLUMN_POSITION = "position";
    private static final String COLUMN_MANAGER = "manager";
    private static final String COLUMN_HIRE_DATE = "hiredate";
    private static final String COLUMN_SALARY = "salary";
    private static final String COLUMN_DEPARTMENT = "department";
    private static final int ID_SELECT_INDEX = 1;
    private static final int DEPARTMENT_SELECT_INDEX = 1;
    private static final int MANAGER_SELECT_INDEX = 1;
    private static final int ID_INSERT_INDEX = 1;
    private static final int FIRSTNAME_INSERT_INDEX = 2;
    private static final int LASTNAME_INSERT_INDEX = 3;
    private static final int MIDDLENAME_INSERT_INDEX = 4;
    private static final int POSITION_INSERT_INDEX = 5;
    private static final int MANAGER_INSERT_INDEX = 6;
    private static final int HIREDATE_INSERT_INDEX = 7;
    private static final int SLARY_INSERT_INDEX = 8;
    private static final int DEPARTMENT_INSERT_INDEX = 9;
    private static final int ID_UPDATE_INDEX = 9;
    private static final int FIRSTNAME_UPDATE_INDEX = 1;
    private static final int LASTNAME_UPDATE_INDEX = 2;
    private static final int MIDDLENAME_UPDATE_INDEX = 3;
    private static final int POSITION_UPDATE_INDEX = 4;
    private static final int MANAGER_UPDATE_INDEX = 5;
    private static final int HIREDATE_UPDATE_INDEX = 6;
    private static final int SLARY_UPDATE_INDEX = 7;
    private static final int DEPARTMENT_UPDATE_INDEX = 8;

    private static final int ID_DELETE_INDEX = 1;
    private static final String SELECT_BY_ID =
            "SELECT id, firstname, lastname, middlename, position, manager, hiredate, salary, department " +
            "FROM employee " +
            "WHERE id = ?;";
    private static final String SELECT_BY_DEPARTMENT =
            "SELECT id, firstname, lastname, middlename, position, manager, hiredate, salary, department " +
            "FROM employee " +
            "WHERE department = ?;";
    private static final String SELECT_BY_MANAGER =
            "SELECT id, firstname, lastname, middlename, position, manager, hiredate, salary, department " +
            "FROM employee " +
            "WHERE manager = ?;";
    private static final String SELECT_ALL =
            "SELECT id, firstname, lastname, middlename, position, manager, hiredate, salary, department " +
            "FROM employee;";
    private static final String INSERT =
            "INSERT INTO employee (id, firstname, lastname, middlename, position, manager, hiredate, salary, department) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private static final String UPDATE =
            "UPDATE employee SET " +
            "firstname = ?, lastname = ?, middlename = ?, position = ?, manager = ?, hiredate = ?, salary = ?, department = ? " +
            "WHERE id = ?;";
    private static final String DELETE =
            "DELETE FROM employee " +
            "WHERE id = ?;";

    @Override
    public Employee getById(BigInteger id) {
        Employee employee = null;
        try (PreparedStatement statement = connectionSource.createConnection().prepareStatement(SELECT_BY_ID)){
            statement.setInt(ID_SELECT_INDEX, id.intValue());
            try (ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    employee = createEmployee(resultSet);
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return employee;
    }

    private Employee getManagerById(BigInteger id){
        Employee employee = null;
        try (PreparedStatement statement = connectionSource.createConnection().prepareStatement(SELECT_BY_ID)){
            statement.setInt(ID_SELECT_INDEX, id.intValue());
            try (ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    employee = createManager(resultSet);
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return employee;
    }

    @Override
    public List<Employee> getAll() {
        List<Employee> employees = new ArrayList<>();
        try (PreparedStatement statement = connectionSource.createConnection().prepareStatement(SELECT_ALL)){
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                Employee employee = createEmployee(resultSet);
                employees.add(employee);
            }
        } catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return employees;
    }

    @Override
    public Employee save(Employee employee) {
        if(getById(employee.getId()) != null){
            return update(employee);
        } else {
            return insert(employee);
        }
    }

    private Employee insert(Employee employee){
        try (PreparedStatement statement = connectionSource.createConnection().prepareStatement(INSERT)){
            FullName fullName = employee.getFullName();
            statement.setInt(ID_INSERT_INDEX, employee.getId().intValue());
            statement.setString(FIRSTNAME_INSERT_INDEX, fullName.getFirstName());
            statement.setString(LASTNAME_INSERT_INDEX, fullName.getLastName());
            statement.setString(MIDDLENAME_INSERT_INDEX, fullName.getMiddleName());
            statement.setString(POSITION_INSERT_INDEX, employee.getPosition().toString());
            statement.setInt(MANAGER_INSERT_INDEX, employee.getManager().getId().intValue());
            statement.setString(HIREDATE_INSERT_INDEX, employee.getHired().toString());
            statement.setBigDecimal(SLARY_INSERT_INDEX, employee.getSalary());
            statement.setInt(DEPARTMENT_INSERT_INDEX, employee.getDepartment().getId().intValue());
            statement.executeUpdate();
            return employee;
        } catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private Employee update(Employee employee){
        try (PreparedStatement statement = connectionSource.createConnection().prepareStatement(UPDATE)){
            FullName fullName = employee.getFullName();
            statement.setInt(ID_UPDATE_INDEX, employee.getId().intValue());
            statement.setString(FIRSTNAME_UPDATE_INDEX, fullName.getFirstName());
            statement.setString(LASTNAME_UPDATE_INDEX, fullName.getLastName());
            statement.setString(MIDDLENAME_UPDATE_INDEX, fullName.getMiddleName());
            statement.setString(POSITION_UPDATE_INDEX, employee.getPosition().toString());
            statement.setInt(MANAGER_UPDATE_INDEX, employee.getManager().getId().intValue());
            statement.setString(HIREDATE_UPDATE_INDEX, employee.getHired().toString());
            statement.setBigDecimal(SLARY_UPDATE_INDEX, employee.getSalary());
            statement.setInt(DEPARTMENT_UPDATE_INDEX, employee.getDepartment().getId().intValue());
            statement.execute();
            return employee;
        } catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Employee employee) {
        try (PreparedStatement statement = connectionSource.createConnection().prepareStatement(DELETE)){
            statement.setInt(ID_DELETE_INDEX, employee.getId().intValue());
            statement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Employee> getByDepartment(Department department) {
        List<Employee> employees = new ArrayList<>();
        try (PreparedStatement statement = connectionSource.createConnection().prepareStatement(SELECT_BY_DEPARTMENT)){
            statement.setInt(DEPARTMENT_SELECT_INDEX, department.getId().intValue());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                Employee employee = createEmployee(resultSet);
                employees.add(employee);
            }
        } catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return employees;
    }

    @Override
    public List<Employee> getByManager(Employee employee) {
        List<Employee> employees = new ArrayList<>();
        try (PreparedStatement statement = connectionSource.createConnection().prepareStatement(SELECT_BY_MANAGER)){
            statement.setInt(MANAGER_SELECT_INDEX, employee.getManager().getId().intValue());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                Employee emp = createEmployee(resultSet);
                employees.add(emp);
            }
        } catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return employees;
    }

    @Override
    public Employee getByIdWithFullChain(BigInteger id) {
        return null;
    }

    private Employee createEmployee(ResultSet resultSet){
        Employee employee = null;
        try{
            BigInteger id = BigInteger.valueOf(resultSet.getInt(COLUMN_ID));
            FullName fullName = createFullName(resultSet);
            Position position = getPosition(resultSet.getString(COLUMN_POSITION));
            LocalDate hired = LocalDate.parse(resultSet.getString(COLUMN_HIRE_DATE));
            BigDecimal salary = resultSet.getBigDecimal(COLUMN_SALARY);
            BigInteger departmentId = BigInteger.valueOf(resultSet.getInt(COLUMN_DEPARTMENT));
            Department department = new DepartmentDaoImp().getById(departmentId);
            BigInteger managerId = BigInteger.valueOf(resultSet.getInt(COLUMN_MANAGER));
            Employee manager = getManagerById(managerId);
            employee = new Employee(id, fullName, position, hired, salary, manager, department);
        } catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return employee;
    }

    private Employee createManager(ResultSet resultSet){
        Employee employee = null;
        try{
            BigInteger id = BigInteger.valueOf(resultSet.getInt(COLUMN_ID));
            FullName fullName = createFullName(resultSet);
            Position position = getPosition(resultSet.getString(COLUMN_POSITION));
            LocalDate hired = LocalDate.parse(resultSet.getString(COLUMN_HIRE_DATE));
            BigDecimal salary = resultSet.getBigDecimal(COLUMN_SALARY);
            BigInteger departmentId = BigInteger.valueOf(resultSet.getInt(COLUMN_DEPARTMENT));
            Department department = new DepartmentDaoImp().getById(departmentId);
            employee = new Employee(id, fullName, position, hired, salary, null, department);
        } catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return employee;
    }


    private FullName createFullName(ResultSet resultSet) throws SQLException {
        String firstName = resultSet.getString(COLUMN_FIRSTNAME);
        String lastName = resultSet.getString(COLUMN_LASTNAME);
        String middleName = resultSet.getString(COLUMN_MIDDLE_NAME);
        return  new FullName(firstName, lastName, middleName);
    }

    private Position getPosition(String position){
        switch (position){
            case "PRESIDENT": return Position.PRESIDENT;
            case "MANAGER": return Position.MANAGER;
            case "ANALYST": return Position.ANALYST;
            case "CLERK": return Position.CLERK;
            case "SALESMAN": return Position.SALESMAN;
        }
        return null;
    }
}
