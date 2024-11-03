package com.sion.javamsauser.model.ResDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignupResponse {
    private Long userId;
    private String username;
}