package com.main.repository.db;

import  com.main.model.User;
import  com.main.model.validators.Validator;

import com.main.repository.Repository;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class UserDbRepository implements Repository<Long, User> {
    private final String url;
    private final String username;
    private final String password;
    private final Validator<User> validator;

    public UserDbRepository(String url, String username, String password, Validator<User> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }
    @Override
    public User findOne(Long id) {
        User found = null;
        String sqlSelect = "select * from users where id=?";
        try(Connection connection = DriverManager.getConnection(url,username,password);
             PreparedStatement psSelect = connection.prepareStatement(sqlSelect)){
            psSelect.setLong(1,id);
            ResultSet resultSet = psSelect.executeQuery();
            if(resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                found = new User(firstName, lastName);
                found.setId(id);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return found;
    }

    @Override
    public Iterable<User> findAll() {
        Set<User> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from users");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");

                User utilizator = new User(firstName, lastName);
                utilizator.setId(id);
                users.add(utilizator);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public User save(User entity) {
        this.validator.validate(entity);
        String sql = "insert into users (first_name, last_name ) values (?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User delete(Long id) {
        User deleted = null;
        String sqlSelect = "select * from users where (id=?)";
        String sqlDelete = "delete from users where (id=?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement psSelect = connection.prepareStatement(sqlSelect);
             PreparedStatement psDelete = connection.prepareStatement(sqlDelete)
             ) {
            psSelect.setLong(1,id);
            ResultSet resultSet = psSelect.executeQuery();
            if (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                deleted = new User(firstName, lastName);
                deleted.setId(id);
            }
            psDelete.setLong(1, id);
            psDelete.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deleted;
    }

    @Override
    public Integer size() {
        Integer s = null;
        String sqlCount = "select count(*) as size from users";
        try(Connection connection = DriverManager.getConnection(url,username,password);
             PreparedStatement psCount = connection.prepareStatement(sqlCount)){
            ResultSet resultSet = psCount.executeQuery();
            resultSet.next();
            s = resultSet.getInt("size");
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return s;
    }

    @Override
    public User update(User entity) {
        String sqlUpdate = "update users set first_name=?, last_name=? " +
                "where id=?";
        User oldState = null;
        try(Connection connection = DriverManager.getConnection(url,username,password);
             PreparedStatement psUpdate = connection.prepareStatement(sqlUpdate)){
            psUpdate.setString(1,entity.getFirstName());
            psUpdate.setString(2,entity.getLastName());
            psUpdate.setLong(3,entity.getId());
            oldState = this.findOne(entity.getId());
            psUpdate.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return oldState;
    }
}