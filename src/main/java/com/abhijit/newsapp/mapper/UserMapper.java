package com.abhijit.newsapp.mapper;

import com.abhijit.newsapp.model.User;
import com.abhijit.newsapp.rest.dto.UserDto;

public interface UserMapper {

    UserDto toUserDto(User user);
}