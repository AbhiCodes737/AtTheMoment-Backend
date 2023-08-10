package com.abhijit.newsapp.mapper;

import com.abhijit.newsapp.model.User;
import com.abhijit.newsapp.rest.dto.UserDto;
import org.springframework.stereotype.Service;

@Service
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto toUserDto(User user) {
        if (user == null) {
            return null;
        }
        return new UserDto(user.getId(), user.getUsername(), user.getName(), user.getEmail(), user.getPassword(), user.getRole(), user.getCountry());
    }
}
