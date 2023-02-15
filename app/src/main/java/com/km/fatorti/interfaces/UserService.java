package com.km.fatorti.interfaces;

import com.km.fatorti.model.User;

import java.io.Serializable;
import java.util.List;

/**
 * used to store user information
 * @author Aws Ayyash
 */

public interface UserService extends Serializable {

    User findUser(String userName);
    void addUser(User newUser);
    void addAll(List<User> users);
    List<User> getAll();


}
