package com.main.view;

import com.main.service.FriendshipService;
import com.main.service.ServiceException;
import com.main.service.UserService;
import com.main.model.Friendship;
import com.main.model.User;
import com.main.model.validators.ValidationException;
import com.main.repository.RepositoryException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * a basic command line UI
 */
public class UI extends Thread{
    private static final Map<Integer, Method> cmdList = new HashMap<>();
    private static FriendshipService friendshipService;
    private static UserService userService;
    private static final Scanner keyboard = new Scanner(System.in);

    /**
     * a basic command line UI
     * @param friendshipService controller for friendships and users
     */
    public UI(FriendshipService friendshipService){
        UI.friendshipService = friendshipService;
        userService = UI.friendshipService.userService;
        try {
            cmdList.put(1,UI.class.getMethod("addUser"));
            cmdList.put(2,UI.class.getMethod("deleteUser"));
            cmdList.put(3,UI.class.getMethod("showUsers"));
            cmdList.put(4,UI.class.getMethod("addFriendship"));
            cmdList.put(5,UI.class.getMethod("deleteFriendship"));
            cmdList.put(6,UI.class.getMethod("showAllFriendships"));
            cmdList.put(7,UI.class.getMethod("showCommunities"));
            cmdList.put(8,UI.class.getMethod("biggestCommunity"));
            cmdList.put(9,UI.class.getMethod("help"));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    public static void addUser(){
        System.out.println("ID:");
        keyboard.nextLine();
        Long ID = keyboard.nextLong();
        System.out.println("First name:");
        keyboard.nextLine();
        String firstName = keyboard.nextLine();
        System.out.println("Last name:");
        String lastName = keyboard.nextLine();
        User user = new User(firstName,lastName);
        user.setId(ID);
        if(userService.add(user) != null)
            throw new RepositoryException("User already exists.");
    }
    public static void deleteUser(){
        System.out.println("ID:");
        keyboard.nextLine();
        Long ID = keyboard.nextLong();
        User user = new User("", "");
        user.setId(ID);
        if(friendshipService.deleteUser(user) == null)
            throw new RepositoryException("User doesn't exist.");
    }
    public static void showUsers(){
        for(Object u : userService.getAllEntities())
            System.out.println(u);
    }

    public static void addFriendship(){
        System.out.println("ID user 1:");
        keyboard.nextLine();
        Long ID1 = keyboard.nextLong();
        System.out.println("ID user 2:");
        keyboard.nextLine();
        Long ID2 = keyboard.nextLong();
        Friendship friendship = new Friendship(ID1,ID2);
        friendshipService.add(friendship);
    }
    public static void deleteFriendship(){
        System.out.println("ID user 1:");
        keyboard.nextLine();
        Long ID1 = keyboard.nextLong();
        System.out.println("ID user 2:");
        keyboard.nextLine();
        Long ID2 = keyboard.nextLong();
        Friendship friendship = new Friendship(ID1,ID2);
        friendshipService.delete(friendship);
    }
    public static void showAllFriendships(){
        for(Friendship fr : friendshipService.getAllEntities())
            System.out.println(fr);
    }

    public static void showCommunities(){
        friendshipService.runGraph();
        for(List<Long> idList : friendshipService.getCommunities()){
            System.out.println("Community:");
            for(Long id : idList){
                User user = friendshipService.userService.findOne(id);
                if(user != null)
                    System.out.println(user);
            }
        }
    }
    public static void biggestCommunity(){
        friendshipService.runGraph();
        System.out.println("Biggest community is made of " +
                friendshipService.getBiggestCommunity() + " users");
    }

    public static void help(){
        System.out.println("1 = add user");
        System.out.println("2 = delete user");
        System.out.println("3 = show all users");
        System.out.println("4 = add friendship");
        System.out.println("5 = delete friendship");
        System.out.println("6 = show all friendships");
        System.out.println("7 = show all communities");
        System.out.println("8 = size of biggest community");
        System.out.println("9 = help");
        System.out.println("0 = exit");
    }
    public void start(){
        help();
        while(true){
            System.out.print(">>>");
            try{
                int cmd = keyboard.nextInt();
                if(cmd == 0){
                    return;
                }
                cmdList.get(cmd).invoke(null);
            }catch(InputMismatchException | NullPointerException ex){
                System.out.println("Wrong input.");
            }catch(ValidationException | RepositoryException | ServiceException ex){
                System.out.println(ex.getMessage());
            }catch(InvocationTargetException ex){
                System.out.println(ex.getCause().getMessage());
            }catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
