package stu.gdut.service;

import stu.gdut.domain.User;

public interface UserService {
    public User findByUsername(String username);
}
