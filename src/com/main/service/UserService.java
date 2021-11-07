package com.main.service;

import com.main.model.User;
import com.main.repository.Repository;
import com.main.repository.RepositoryException;

public class UserService {
    public final Repository<Long, User> userRepo;
    public UserService(Repository<Long, User> userRepo){
        this.userRepo = userRepo;
    }

    public User add(User entity){
        User res = userRepo.save(entity);
        if(res != null)
            throw new RepositoryException("User already exists.");
        return res;
    }
    public User delete(User entity){
        User res = userRepo.delete(entity.getId());
        if(res == null)
            throw new RepositoryException("User doesn't exist.");
        return res;
    }
    public Iterable<User> getAllEntities(){
        return userRepo.findAll();
    }
    public User findOne(Long id){
        User res = userRepo.findOne(id);
        if(res == null)
            throw new RepositoryException("User doesn't exist.");
        return res;
    }
    public Integer size(){
        return userRepo.size();
    }
}
