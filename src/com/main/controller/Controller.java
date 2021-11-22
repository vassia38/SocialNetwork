package com.main.controller;


import com.main.model.*;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public interface Controller {
    void addUser(User entity);
    User deleteUser(User entity);
    User updateUser(User entity, String firstName, String lastName);
    User findUserById(Long id);
    User findUserByUsername(String username);
    Iterable<User> getAllUsers();
    void addFriendship(Friendship entity);
    Friendship deleteFriendship(Friendship entity);
    Friendship updateFriendship(Friendship entity);
    Iterable<Friendship> getAllFriendships();
    ArrayList<ArrayList<Long>> getAllCommunities();
    int getBiggestCommunitySize();
    Stream<FriendshipDTO> getRightFriends(User user, List<Friendship> friendshipList);
    Stream<FriendshipDTO> getLeftFriends(User user, List<Friendship> friendshipList);
    Stream<FriendshipDTO> getRightFriendsMonth(User user, Month month, List<Friendship> friendshipList);
    Stream<FriendshipDTO> getLeftFriendsMonth(User user, Month month, List<Friendship> friendshipList);
    void sendMessage(User source, List<String> destinationUsernames, String message, LocalDateTime date, Long repliedMessageId);
    Iterable<Message> getAllMesagesOfUser(String username);
    Iterable<Message> getConversation(String username1, String username2);
    void addRequest(Request request);
    void answerRequest(Request request, String answer);
    Iterable<Request> showRequests(User user);
    Iterable<Request> showAllRequests();
    Friendship findFriendshipById(Tuple<Long,Long> id);
}
