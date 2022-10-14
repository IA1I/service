package com.epam.rd.autocode;

import com.epam.rd.autocode.dao.EmployeeDao;
import com.epam.rd.autocode.dao.impl.EmployeeDaoImp;
import com.epam.rd.autocode.domain.Department;
import com.epam.rd.autocode.domain.Employee;
import com.epam.rd.autocode.domain.FullName;
import com.epam.rd.autocode.domain.Position;
import com.epam.rd.autocode.service.EmployeeService;
import com.epam.rd.autocode.service.Paging;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class EmployeeServiceImp implements EmployeeService {
    private EmployeeDao employeeDao = new EmployeeDaoImp();
    private static final Comparator<Employee> SORT_BY_HIRE_DATE = Comparator.comparing(Employee :: getHired);
    private static final Comparator<Employee> SORT_BY_LASTNAME= Comparator.comparing(e -> e.getFullName().getLastName());
    private static final Comparator<Employee> SORT_BY_SALARY= Comparator.comparing(Employee :: getSalary);
    private static final Comparator<Employee> REVERSE_SORT_BY_SALARY= Comparator.comparing(Employee :: getSalary).reversed();
    private static final Comparator<Employee> SORT_BY_DEPARTMENT_NAME = (o1, o2) -> {
        if(o1.getDepartment() != null && o2.getDepartment() != null){
            return o1.getDepartment().getName().compareTo(o2.getDepartment().getName());
        } else if(o1.getDepartment() == null){
            return -1;
        } else {
            return 1;
        }
    };
//    private static Predicate<Employee> GET_BY_DEPARTMENT_ID = e -> e.getDepartment().getId().equals(department.getId());

    @Override
    public List<Employee> getAllSortByHireDate(Paging paging) {
        List<Employee> employees = employeeDao.getAll();
        employees.sort(SORT_BY_HIRE_DATE);
        return getSublist(employees, paging);
    }

    @Override
    public List<Employee> getAllSortByLastname(Paging paging) {
        List<Employee> employees = employeeDao.getAll();
        employees.sort(SORT_BY_LASTNAME);
        return getSublist(employees, paging);
    }

    @Override
    public List<Employee> getAllSortBySalary(Paging paging) {
        List<Employee> employees = employeeDao.getAll();
        employees.sort(SORT_BY_SALARY);
        return getSublist(employees, paging);
    }

    @Override
    public List<Employee> getAllSortByDepartmentNameAndLastname(Paging paging) {
        List<Employee> employees = employeeDao.getAll();
        employees.sort(SORT_BY_LASTNAME);
        employees.sort(SORT_BY_DEPARTMENT_NAME);
        return getSublist(employees, paging);
    }

    @Override
    public List<Employee> getByDepartmentSortByHireDate(Department department, Paging paging) {
        List<Employee> employees = employeeDao.getByDepartment(department);
        employees.sort(SORT_BY_HIRE_DATE);
        return getSublist(employees, paging);
    }

    @Override
    public List<Employee> getByDepartmentSortBySalary(Department department, Paging paging) {
        List<Employee> employees = employeeDao.getByDepartment(department);
        employees.sort(SORT_BY_SALARY);
        return getSublist(employees, paging);
    }

    @Override
    public List<Employee> getByDepartmentSortByLastname(Department department, Paging paging) {
        List<Employee> employees = employeeDao.getByDepartment(department);
        employees.sort(SORT_BY_LASTNAME);
        return getSublist(employees, paging);
    }

    @Override
    public List<Employee> getByManagerSortByLastname(Employee manager, Paging paging) {
        List<Employee> employees = employeeDao.getByManager(manager);
        employees.sort(SORT_BY_LASTNAME);
        return getSublist(employees, paging);
    }

    @Override
    public List<Employee> getByManagerSortByHireDate(Employee manager, Paging paging) {
        List<Employee> employees = employeeDao.getByManager(manager);
        employees.sort(SORT_BY_HIRE_DATE);
        return getSublist(employees, paging);
    }

    @Override
    public List<Employee> getByManagerSortBySalary(Employee manager, Paging paging) {
        List<Employee> employees = employeeDao.getByManager(manager);
        employees.sort(SORT_BY_SALARY);
        return getSublist(employees, paging);
    }

    @Override
    public Employee getWithDepartmentAndFullManagerChain(Employee employee) {
        return employeeDao.getByIdWithFullChain(employee.getId());
    }

    @Override
    public Employee getTopNthBySalaryByDepartment(int salaryRank, Department department) {
        List<Employee> employees = employeeDao.getByDepartment(department);
        employees.sort(REVERSE_SORT_BY_SALARY);
        int lastIndex = salaryRank-1;
        return employees.get(lastIndex);
    }

    private <T> List<T> getSublist(List<T> list, Paging paging) {
        int fromIndex = getFromIndex(paging);
        int toIndex = Math.min(list.size(), getToIndex(paging));
        return list.subList(fromIndex, toIndex);
    }

    private int getFromIndex(Paging paging) {
        return (paging.page - 1) * paging.itemPerPage;
    }

    private int getToIndex(Paging paging) {
        return getFromIndex(paging) + paging.itemPerPage;
    }
}
