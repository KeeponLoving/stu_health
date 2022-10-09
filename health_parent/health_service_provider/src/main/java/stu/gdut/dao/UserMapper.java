package stu.gdut.dao;

import stu.gdut.domain.User;

public interface UserMapper {
    public User findByUsername(String username);
}
