package stu.gdut.service;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import stu.gdut.domain.Permission;
import stu.gdut.domain.Role;
import stu.gdut.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class SpringSecurityUserService implements UserDetailsService {
    // 使用Dubbo通过网络远程调用服务提供方获取数据库中的用户信息
    @DubboReference
    private UserService userService;

    // 根据用户名查询数据库获取用户信息
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        if(user == null){
            // 用户不存在
            return null;
        }

        List<GrantedAuthority> list = new ArrayList<>();
        // 动态为当前用户授权
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            // 遍历角色集合，为用户授予角色
            list.add(new SimpleGrantedAuthority(role.getKeyword()));
            Set<Permission> permissions = role.getPermissions();
            for (Permission permission : permissions) {
                // 遍历权限集合，为用户授权
                list.add(new SimpleGrantedAuthority(permission.getKeyword()));
            }
        }

        // 三个参数，用户名、用户密码（加密）、权限集合
        var securityUser = new org.springframework.security.core.userdetails.User(username, user.getPassword(), list);
        return securityUser;
    }
}
