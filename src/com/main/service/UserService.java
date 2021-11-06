package com.main.service;

import com.main.model.User;
import com.main.model.validators.Validator;
import com.main.repository.Repository;

public class UserService {
    public final Repository<Long, User> userRepo;
    public final Validator<User> userValidator;
    public UserService(Repository<Long, User> userRepo, Validator<User> userValidator){
        this.userRepo = userRepo;
        this.userValidator = userValidator;
    }

    public User add(User entity){
        userValidator.validate(entity);
        return userRepo.save(entity);
    }
    public User delete(User entity){
        return userRepo.delete(entity.getId());
    }
    public Iterable<User> getAllEntities(){
        return userRepo.findAll();
    }
    public User findOne(Long id){
        return userRepo.findOne(id);
    }
    public Integer size(){
        return userRepo.size();
    }
}
