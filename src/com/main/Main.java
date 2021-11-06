package com.main;

import com.main.service.FriendshipService;
import com.main.service.UserService;
import com.main.model.Friendship;
import com.main.model.Tuple;
import com.main.model.User;
import com.main.model.validators.PrietenieValidator;
import com.main.model.validators.UtilizatorValidator;
import com.main.repository.Repository;
import com.main.repository.file.FriendshipFile;
import com.main.repository.file.UserFile;
import com.main.view.UI;

public class Main {

    public static void main(String[] args) {
        String userFileName="data/users.csv";
        String friendshipFileName="data/friendships.csv";
        UtilizatorValidator userValidator = new UtilizatorValidator();
        PrietenieValidator friendshipValidator = new PrietenieValidator();
        Repository<Long, User> userRepo = new UserFile(userFileName, userValidator);
        Repository<Tuple<Long,Long>, Friendship> friendshipRepo= new FriendshipFile(friendshipFileName, friendshipValidator);
        UserService userService = new UserService(userRepo, userValidator);
        FriendshipService friendshipService = new FriendshipService(friendshipRepo, friendshipValidator, userService);
        UI ui = new UI(friendshipService);
        ui.start();
        System.out.println("Sayonara");
    }
}
