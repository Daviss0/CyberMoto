package com.cybermoto.service;


import com.cybermoto.entity.User;

public interface UserService {
    void saveUser(User user) throws IllegalArgumentException;
    void toggleUserStatus(Long userId);
}
