package com.abhijit.newsapp.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EditRequest {
    @Schema(example = "1")
    @NotNull
    private Long id;

    @Schema(example = "user3")
    @NotBlank
    private String username;

    @Schema(example = "User3")
    @NotBlank
    private String name;

    @Schema(example = "user3@mycompany.com")
    @Email
    private String email;

    @Schema(example = "user")
    @NotBlank
    private String password;

    @Schema(example = "user")
    @NotBlank
    private String role;

    @Schema(example = "user")
    @NotBlank
    private String country;
}
