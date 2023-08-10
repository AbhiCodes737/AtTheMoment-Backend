package com.abhijit.newsapp.service;

import com.abhijit.newsapp.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getUsers();

    public User updateUser(User user);

    Optional<User> getUserByUsername(String username);

    public Long getIdByUsername(String username);

    public Long getIdByEmail(String email);

    boolean hasUserWithUsername(String username);

    boolean hasUserWithEmail(String email);

    User validateAndGetUserByUsername(String username);

    User saveUser(User user);

    void deleteUser(User user);

    Optional<User> validUsernameAndPassword(String username, String password);
}
