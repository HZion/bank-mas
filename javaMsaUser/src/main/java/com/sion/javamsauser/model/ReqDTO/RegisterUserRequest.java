package com.sion.javamsauser.model.ReqDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterUserRequest {
    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 50, message = "Username must be between 4 and 50 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 4, message = "Password must be at least 4 characters")
    private String password;
}