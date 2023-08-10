package com.abhijit.newsapp.service;

import com.abhijit.newsapp.exception.UserNotFoundException;
import com.abhijit.newsapp.model.User;
import com.abhijit.newsapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(User user) {
        long id = user.getId();
        User user2 = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id %d not found", id)));
        user2.setUsername(user.getUsername());
        user2.setName(user.getName());
        user2.setEmail(user.getEmail());
        user2.setRole(user.getRole());
        user2.setCountry(user.getCountry());
        return userRepository.save(user2);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Long getIdByUsername(String username) {
        User u = userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new UserNotFoundException(String.format("User with username %s not found", username)));
        return u.getId();
    }

    @Override
    public Long getIdByEmail(String email) {
        User u = userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new UserNotFoundException(String.format("User with email %s not found", email)));
        return u.getId();
    }

    @Override
    public boolean hasUserWithUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean hasUserWithEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User validateAndGetUserByUsername(String username) {
        return getUserByUsername(username)
                .orElseThrow(
                        () -> new UserNotFoundException(String.format("User with username %s not found", username)));
    }

    @Override
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    @Override
    public Optional<User> validUsernameAndPassword(String username, String password) {
        return getUserByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()));
    }
}
