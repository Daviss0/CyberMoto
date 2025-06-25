package com.cybermoto.service;


import com.cybermoto.entity.User;

public interface UserService {
    User loginUser(String email, String password);
    void saveUser(User user);
}
