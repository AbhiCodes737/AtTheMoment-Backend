package com.abhijit.newsapp.rest;

import com.abhijit.newsapp.exception.DuplicatedUserInfoException;
import com.abhijit.newsapp.exception.UserNotFoundException;
import com.abhijit.newsapp.model.User;
import com.abhijit.newsapp.rest.dto.AuthResponse;
import com.abhijit.newsapp.rest.dto.LoginRequest;
import com.abhijit.newsapp.rest.dto.SignUpRequest;
import com.abhijit.newsapp.security.WebSecurityConfig;
import com.abhijit.newsapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        Optional<User> userOptional = userService.validUsernameAndPassword(loginRequest.getUsername(),
                loginRequest.getPassword());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return ResponseEntity
                    .ok(new AuthResponse(user.getId(), user.getUsername(), user.getName(), user.getRole(), user.getCountry()));
        } else {
            throw new UserNotFoundException(String.format("Wrong Credentials"));
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    public AuthResponse signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userService.hasUserWithUsername(signUpRequest.getUsername())) {
            throw new DuplicatedUserInfoException(
                    String.format("Username %s is already been used", signUpRequest.getUsername()));
        }
        if (userService.hasUserWithEmail(signUpRequest.getEmail())) {
            throw new DuplicatedUserInfoException(
                    String.format("Email %s is already been used", signUpRequest.getEmail()));
        }

        User user = userService.saveUser(createUser(signUpRequest));
        return new AuthResponse(user.getId(), user.getUsername(), user.getName(), user.getRole(), user.getCountry());
    }

    private User createUser(SignUpRequest signUpRequest) {
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(signUpRequest.getPassword());
        user.setName(signUpRequest.getName());
        user.setEmail(signUpRequest.getEmail());
        user.setRole(WebSecurityConfig.USER);
        user.setCountry(signUpRequest.getCountry());
        return user;
    }
}
