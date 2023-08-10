package com.abhijit.newsapp.rest;

import com.abhijit.newsapp.exception.DuplicatedUserInfoException;
import com.abhijit.newsapp.mapper.UserMapper;
import com.abhijit.newsapp.model.User;
import com.abhijit.newsapp.rest.dto.EditRequest;
import com.abhijit.newsapp.rest.dto.UserDto;
import com.abhijit.newsapp.security.CustomUserDetails;
import com.abhijit.newsapp.security.WebSecurityConfig;
import com.abhijit.newsapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static com.abhijit.newsapp.config.SwaggerConfig.BASIC_AUTH_SECURITY_SCHEME;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Operation(security = { @SecurityRequirement(name = BASIC_AUTH_SECURITY_SCHEME) })
    @GetMapping("/me")
    public UserDto getCurrentUser(@AuthenticationPrincipal CustomUserDetails currentUser) {
        return userMapper.toUserDto(userService.validateAndGetUserByUsername(currentUser.getUsername()));
    }

    @Operation(security = { @SecurityRequirement(name = BASIC_AUTH_SECURITY_SCHEME) })
    @GetMapping
    public List<UserDto> getUsers() {
        return userService.getUsers().stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Operation(security = { @SecurityRequirement(name = BASIC_AUTH_SECURITY_SCHEME) })
    @GetMapping("/{username}")
    public UserDto getUser(@PathVariable String username) {
        return userMapper.toUserDto(userService.validateAndGetUserByUsername(username));
    }

    @Operation(security = { @SecurityRequirement(name = BASIC_AUTH_SECURITY_SCHEME) })
    @DeleteMapping("/{username}")
    public UserDto deleteUser(@PathVariable String username) {
        User user = userService.validateAndGetUserByUsername(username);
        userService.deleteUser(user);
        return userMapper.toUserDto(user);
    }

    @Operation(security = { @SecurityRequirement(name = BASIC_AUTH_SECURITY_SCHEME) })
    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable Long id, @RequestBody EditRequest e) {
        if (userService.hasUserWithUsername(e.getUsername())
                && userService.getIdByUsername(e.getUsername()) != id) {
            throw new DuplicatedUserInfoException(String.format("Username %s is already been used", e.getUsername()));
        }
        if (userService.hasUserWithEmail(e.getEmail()) && userService.getIdByEmail(e.getEmail()) != id) {
            throw new DuplicatedUserInfoException(String.format("Email %s is already been used", e.getEmail()));
        }
        User user = userService.updateUser(setUser(e, id));
        return userMapper.toUserDto(user);
    }

    private User setUser(EditRequest e, Long id) {
        User user = new User();
        user.setId(id);
        user.setUsername(e.getUsername());
        user.setPassword(e.getPassword());
        user.setName(e.getName());
        user.setEmail(e.getEmail());
        user.setRole(e.getRole());
        user.setCountry(e.getCountry());
        return user;
    }
}
