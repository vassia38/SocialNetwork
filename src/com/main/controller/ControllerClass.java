package com.main.controller;

import com.main.algo.Graph;
import com.main.model.Friendship;
import com.main.model.User;
import com.main.repository.RepositoryException;
import com.main.service.FriendshipService;
import com.main.service.UserService;

import java.util.ArrayList;
import java.util.Objects;

public class ControllerClass implements Controller{
    public final UserService userService;
    public final  FriendshipService friendshipService;
    Graph graph;

    public ControllerClass(UserService userService, FriendshipService friendshipService){
        this.userService = userService;
        this.friendshipService = friendshipService;
    }

    @Override
    public User addUser(User entity) {
        return userService.add(entity);
    }

    @Override
    public User deleteUser(User entity) {
        User deleted = userService.delete(entity);
        if(deleted != null) {
            ArrayList<Friendship> list = new ArrayList<>();
            for (Friendship fr : friendshipService.getAllEntities()) {
                list.add(fr);
            }
            for(Friendship fr : list){
                if (Objects.equals(fr.getId().getLeft(), entity.getId()) ||
                        Objects.equals(fr.getId().getRight(), entity.getId())) {
                    friendshipService.delete(fr);
                }
            }
        }
        return deleted;
    }

    @Override
    public User findUserById(Long id) {
        return userService.findOne(id);
    }

    @Override
    public Iterable<User> getAllUsers() {
        return userService.getAllEntities();
    }

    @Override
    public void addFriendship(Friendship entity) {
        Long id1 = entity.getId().getLeft();
        User user1 = userService.findOne(id1);
        Long id2 = entity.getId().getRight();
        User user2 = userService.findOne(id2);
        if(user1 == null || user2 == null)
            throw new RepositoryException("User(s) doesn't exist!\n");
        Friendship fr = friendshipService.add(entity);
        if(fr != null)
            throw new RepositoryException("Friendship Already exists!\n");

    }

    @Override
    public void deleteFriendship(Friendship entity) {
        Long id1 = entity.getId().getLeft();
        User user1 = userService.findOne(id1);
        Long id2 = entity.getId().getRight();
        User user2 = userService.findOne(id2);
        if(user1 == null || user2 == null)
            throw new RepositoryException("User(s) doesn't exist!\n");
        Friendship fr = friendshipService.delete(entity);
        if(fr == null)
            throw new RepositoryException("Friendship doesn't exist!\n");
    }

    @Override
    public Iterable<Friendship> getAllFriendships() {
        return friendshipService.getAllEntities();
    }
    private void runGraph(){
        graph = new Graph(this);
        graph.runConnectedComponents();
    }
    @Override
    public ArrayList<ArrayList<Long>> getAllCommunities(){
        this.runGraph();
        return graph.getCommunities();
    }
    @Override
    public int getBiggestCommunitySize(){
        this.runGraph();
        return graph.maxSize();
    }
}
