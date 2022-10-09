package stu.gdut.service.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import stu.gdut.dao.PermissionMapper;
import stu.gdut.dao.RoleMapper;
import stu.gdut.dao.UserMapper;
import stu.gdut.domain.Permission;
import stu.gdut.domain.Role;
import stu.gdut.domain.User;
import stu.gdut.service.UserService;

/**
 * 用户服务
 */
@DubboService(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private PermissionMapper permissionMapper;

    // 根据用户名查询数据库获取用户信息和关联的角色信息，同时需要查询角色关联的权限信息
    @Override
    public User findByUsername(String username) {
        // 查询用户基本信息，不包含用户的角色
        User user = userMapper.findByUsername(username);
        if(user == null)
            return null;
        Integer userId = user.getId();
        // 根据用户ID查询对应的角色
        var roles = roleMapper.findByUserId(userId);
        for (Role role : roles) {
            Integer roleId = role.getId();
            // 根据角色ID查询关联的权限
            var permissions = permissionMapper.findByRoleId(roleId);
            // 让角色关联权限
            role.setPermissions(permissions);
        }
        // 让用户关联角色
        user.setRoles(roles);
        return user;
    }
}
