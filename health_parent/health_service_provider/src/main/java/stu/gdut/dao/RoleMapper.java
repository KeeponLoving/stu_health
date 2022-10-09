package stu.gdut.dao;

import stu.gdut.domain.Role;

import java.util.Set;

public interface RoleMapper {
    public Set<Role> findByUserId(Integer userId);
}
