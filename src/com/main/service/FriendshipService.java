package com.main.service;

import com.main.algo.Graph;
import com.main.model.Friendship;
import com.main.model.Tuple;
import com.main.model.User;
import com.main.model.validators.Validator;
import com.main.repository.Repository;
import com.main.repository.RepositoryException;

import java.util.ArrayList;
import java.util.Objects;

public class FriendshipService {
    public final UserService userService;
    final Repository<Tuple<Long,Long>, Friendship> friendshipRepository;
    private final Validator<Friendship> friendshipValidator;

    private Graph graph;
    public FriendshipService(Repository<Tuple<Long,Long>, Friendship> friendshipRepository,
                             Validator<Friendship> friendshipValidator,
                             UserService userService) {
       this.friendshipRepository = friendshipRepository;
       this.friendshipValidator = friendshipValidator;
        this.userService = userService;
        for(Friendship fr : friendshipRepository.findAll()){
            this.add(fr);
        }
    }
    public Friendship add(Friendship entity){
        friendshipValidator.validate(entity);
        Long id1 = entity.getId().getLeft();
        User user1 = userService.findOne(id1);
        Long id2 = entity.getId().getRight();
        User user2 = userService.findOne(id2);
        if(user1 == null || user2 == null)
            throw new RepositoryException("User(s) doesn't exist!\n");
        user1.addFriend(user2);
        user2.addFriend(user1);
        return friendshipRepository.save(entity);
    }

    public User deleteUser(User user){
        User deleted = userService.delete(user);
        if(deleted != null) {
            ArrayList<Friendship> list = new ArrayList<>();
            for (Friendship fr : friendshipRepository.findAll()) {
                list.add(fr);
            }
            for(Friendship fr : list){
                if (Objects.equals(fr.getId().getLeft(), user.getId()) ||
                        Objects.equals(fr.getId().getRight(), user.getId())) {
                    friendshipRepository.delete(fr.getId());
                }
            }
        }
        return deleted;
    }

    public Friendship delete(Friendship entity){
        friendshipValidator.validate(entity);
        Long id1 = entity.getId().getLeft();
        User user1 = userService.findOne(id1);
        Long id2 = entity.getId().getRight();
        User user2 = userService.findOne(id2);
        if(user1 == null || user2 == null)
            throw new RepositoryException("User(s) doesn't exist!\n");
        user1.removeFriend(user2);
        user2.removeFriend(user1);
        return friendshipRepository.delete(entity.getId());
    }
    public void runGraph(){
        graph = new Graph(this);
        graph.runConnectedComponents();
    }
    public ArrayList<ArrayList<Long>> getCommunities(){
        return graph.getCommunities();
    }
    public int getBiggestCommunity(){
        return graph.maxSize();
    }
    public Iterable<Friendship> getAllEntities(){
        return friendshipRepository.findAll();
    }
}
