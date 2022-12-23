package com.km.fatorti.interfaces.impl;

import com.km.fatorti.interfaces.UserService;
import com.km.fatorti.model.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * used to store user information
 * @author Aws Ayyash
 */

public class UserServiceDA implements UserService, Serializable {

    private List<User> dummyUsers;

    public UserServiceDA() {
        this.dummyUsers = new ArrayList<>(5);

        dummyUsers.add(new User("Aws","Ayyash",
                "aws@gmail.com",
                "aws.ayyash",
                "123456789"));

        dummyUsers.add(new User("Zaid","Khamis",
                "zaid@gmail.com",
                "zaid.khamis",
                "123456789"));
        dummyUsers.add(new User("Karim","Halayqa",
                "karim@gmail.com",
                "karim.halayqa",
                "123456789"));
    }

    @Override
    public User findUser(String userName) {

        for (User user: dummyUsers ) {
            if (user.getUserName().equals(userName)){
                return user;
            }

        }
        return null; // this means, my userName (USER) does not exist -Not registered-
    }

    @Override
    public void addUser(User newUser) {
        dummyUsers.add(newUser);
    }
}
