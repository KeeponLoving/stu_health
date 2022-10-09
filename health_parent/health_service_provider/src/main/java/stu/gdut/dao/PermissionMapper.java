package stu.gdut.dao;

import stu.gdut.domain.Permission;

import java.util.Set;

public interface PermissionMapper {
    public Set<Permission> findByRoleId(int roleId);
}
