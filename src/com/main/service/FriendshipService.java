package com.main.service;

import com.main.algo.Graph;
import com.main.model.Friendship;
import com.main.model.Tuple;
import com.main.model.User;
import com.main.model.validators.Validator;
import com.main.repository.Repository;
import com.main.repository.RepositoryException;

import java.util.ArrayList;

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
        return friendshipRepository.save(entity);
    }

    public Friendship delete(Friendship entity){
        friendshipValidator.validate(entity);
        return friendshipRepository.delete(entity.getId());
    }
    private void runGraph(){
        graph = new Graph(this);
        graph.runConnectedComponents();
    }
    public ArrayList<ArrayList<Long>> getAllCommunities(){
        this.runGraph();
        return graph.getCommunities();
    }
    public int getBiggestCommunitySize(){
        this.runGraph();
        return graph.maxSize();
    }
    public Iterable<Friendship> getAllEntities(){
        return friendshipRepository.findAll();
    }
}
