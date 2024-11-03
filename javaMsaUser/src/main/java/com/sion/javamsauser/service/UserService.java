package com.sion.javamsauser.service;

import com.sion.javamsauser.model.User;

public interface UserService {
    User registerUser(String username, String password);
    User loginUser(String username, String password);
    String generateToken(User user);

}
