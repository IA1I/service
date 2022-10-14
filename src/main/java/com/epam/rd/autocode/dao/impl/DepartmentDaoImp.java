package com.epam.rd.autocode.dao.impl;

import com.epam.rd.autocode.ConnectionSource;
import com.epam.rd.autocode.dao.DepartmentDao;
import com.epam.rd.autocode.domain.Department;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoImp implements DepartmentDao {
    private ConnectionSource connectionSource = ConnectionSource.instance();
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_LOCATION = "location";
    private static final int ID_SELECT_INDEX = 1;
    private static final int ID_INSERT_INDEX = 1;
    private static final int NAME_INSERT_INDEX = 2;
    private static final int LOCATION_INSERT_INDEX = 3;
    private static final int ID_UPDATE_INDEX = 3;
    private static final int NAME_UPDATE_INDEX = 1;
    private static final int LOCATION_UPDATE_INDEX = 2;
    private static final int ID_DELETE_INDEX = 1;
    private static final String SELECT_BY_ID =
            "SELECT id, name, location " +
            "FROM department " +
            "WHERE id = ?;";
    private static final String SELECT_ALL =
            "SELECT id, name, location " +
            "FROM department;";
    private static final String INSERT =
            "INSERT INTO department (id, name, location) " +
            "VALUES (?, ?, ?);";
    private static final String UPDATE =
            "UPDATE department SET " +
            "name = ?, location = ? " +
            "WHERE id = ?;";
    private static final String DELETE =
            "DELETE FROM department " +
            "WHERE id = ?;";
    @Override
    public Department getById(BigInteger id) {
        Department department = null;
        try (PreparedStatement statement = connectionSource.createConnection().prepareStatement(SELECT_BY_ID)){
            statement.setInt(ID_SELECT_INDEX, id.intValue());
            try (ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    department = createDepartment(resultSet);
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return department;
    }

    @Override
    public List<Department> getAll() {
        List<Department> departments = new ArrayList<>();
        try (PreparedStatement statement = connectionSource.createConnection().prepareStatement(SELECT_ALL)){
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                Department department = createDepartment(resultSet);
                departments.add(department);
            }

        } catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return departments;
    }

    @Override
    public Department save(Department department) {
        if(getById(department.getId()) != null){
            return update(department);
        } else {
            return insert(department);
        }
    }

    private Department insert(Department department){
        try (PreparedStatement statement = connectionSource.createConnection().prepareStatement(INSERT)){
            statement.setInt(ID_INSERT_INDEX, department.getId().intValue());
            statement.setString(NAME_INSERT_INDEX, department.getName());
            statement.setString(LOCATION_INSERT_INDEX, department.getLocation());
            statement.executeUpdate();
            return department;
        } catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private Department update(Department department){
        try (PreparedStatement statement = connectionSource.createConnection().prepareStatement(UPDATE)){
            statement.setInt(ID_UPDATE_INDEX, department.getId().intValue());
            statement.setString(NAME_UPDATE_INDEX, department.getName());
            statement.setString(LOCATION_UPDATE_INDEX, department.getLocation());
            statement.executeUpdate();
            return department;
        } catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Department department) {
        try (PreparedStatement statement = connectionSource.createConnection().prepareStatement(DELETE)){
            statement.setInt(ID_DELETE_INDEX, department.getId().intValue());
            statement.execute();
        } catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private Department createDepartment(ResultSet resultSet){
        Department department = null;
        try {
            BigInteger id = BigInteger.valueOf(resultSet.getInt(COLUMN_ID));
            String name = resultSet.getString(COLUMN_NAME);
            String location = resultSet.getString(COLUMN_LOCATION);
            department = new Department(id, name, location);
        } catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return department;
    }
}
