package com.main.repository.db;

import  com.main.model.User;
import  com.main.model.validators.Validator;

import com.main.repository.Repository;
import com.main.repository.RepositoryException;

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
        if (id==null)
            throw new IllegalArgumentException("entity must be not null");
        String sqlSelect = "select * from users where id=?";
        try(Connection connection = DriverManager.getConnection(url,username,password);
             PreparedStatement psSelect = connection.prepareStatement(sqlSelect)){
            psSelect.setLong(1,id);
            ResultSet resultSet = psSelect.executeQuery();
            if(resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                return new User(id,firstName, lastName);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
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

                User utilizator = new User(id,firstName, lastName);
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
        User found = this.findOne(id);
        if(found == null)
            return null;
        String sqlDelete = "delete from users where (id=?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement psDelete = connection.prepareStatement(sqlDelete)){
            psDelete.setLong(1, id);
            psDelete.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return found;
    }

    @Override
    public Integer size() {
        String sqlCount = "select count(*) from users";
        try(Connection connection = DriverManager.getConnection(url,username,password);
             PreparedStatement psCount = connection.prepareStatement(sqlCount)){
            ResultSet resultSet = psCount.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public User update(User entity) {
        User oldState = this.findOne(entity.getId());
        if(oldState == null)
            return null;
        String sqlUpdate = "update users set first_name=?, last_name=? where id=?";
        try(Connection connection = DriverManager.getConnection(url,username,password);
             PreparedStatement psUpdate = connection.prepareStatement(sqlUpdate)){
            psUpdate.setString(1,entity.getFirstName());
            psUpdate.setString(2,entity.getLastName());
            psUpdate.setLong(3,entity.getId());
            psUpdate.executeUpdate();

        }catch(SQLException e){
            e.printStackTrace();
        }
        return oldState;
    }
}