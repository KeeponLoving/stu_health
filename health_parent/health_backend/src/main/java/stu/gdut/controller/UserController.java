package stu.gdut.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import stu.gdut.constant.MessageConstant;
import stu.gdut.entity.Result;

@RestController
@RequestMapping("/user")
public class UserController {

    // 获取当前登录用户的用户名
    @GetMapping("/username")
    public Result getUserName(){
        // getAuthentication()得到的是认证信息对象，getPrincipal()得到的是User对象
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user!=null){
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS, user.getUsername());
        }
        return new Result(false, MessageConstant.GET_USERNAME_FAIL);
    }
}
