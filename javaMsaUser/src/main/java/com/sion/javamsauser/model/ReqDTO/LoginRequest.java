package com.sion.javamsauser.model.ReqDTO;

import lombok.Data;


@Data

public class LoginRequest {
    private String username;
    private String password;
}