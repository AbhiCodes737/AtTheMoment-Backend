package com.abhijit.newsapp.rest.dto;

public record UserDto(Long id, String username, String name, String email, String password, String role, String country) {
}