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

public class EmployeeServiceImp implements EmployeeService {
    private EmployeeDao employeeDao = new EmployeeDaoImp();

    @Override
    public List<Employee> getAllSortByHireDate(Paging paging) {
        List<Employee> employees = employeeDao.getAll();
        employees.sort(Comparator.comparing(Employee :: getHired));
        return getSublist(employees, paging);
    }

    @Override
    public List<Employee> getAllSortByLastname(Paging paging) {
        return null;
    }

    @Override
    public List<Employee> getAllSortBySalary(Paging paging) {
        return null;
    }

    @Override
    public List<Employee> getAllSortByDepartmentNameAndLastname(Paging paging) {
        return null;
    }

    @Override
    public List<Employee> getByDepartmentSortByHireDate(Department department, Paging paging) {
        return null;
    }

    @Override
    public List<Employee> getByDepartmentSortBySalary(Department department, Paging paging) {
        return null;
    }

    @Override
    public List<Employee> getByDepartmentSortByLastname(Department department, Paging paging) {
        return null;
    }

    @Override
    public List<Employee> getByManagerSortByLastname(Employee manager, Paging paging) {
        return null;
    }

    @Override
    public List<Employee> getByManagerSortByHireDate(Employee manager, Paging paging) {
        return null;
    }

    @Override
    public List<Employee> getByManagerSortBySalary(Employee manager, Paging paging) {
        return null;
    }

    @Override
    public Employee getWithDepartmentAndFullManagerChain(Employee employee) {
        return null;
    }

    @Override
    public Employee getTopNthBySalaryByDepartment(int salaryRank, Department department) {
        return null;
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
