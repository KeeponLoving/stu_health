package stu.gdut.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;
import stu.gdut.constant.MessageConstant;
import stu.gdut.constant.RedisMessageConstant;
import stu.gdut.domain.Member;
import stu.gdut.entity.Result;
import stu.gdut.service.MemberService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * 处理会员相关操作
 */
@RestController
@RequestMapping("/member")
public class MemberController {
    @Autowired
    private JedisPool jedisPool;

    @DubboReference
    private MemberService memberService;

    // 手机快速登录
    @PostMapping("/logininfo")
    public Result login(HttpServletResponse response, @RequestBody Map map){
        String telephone = (String) map.get("telephone");
        // 从Redis中获取保存的验证码
        String validateCodeInRedis = jedisPool.getResource().get(telephone+ RedisMessageConstant.SENDTYPE_LOGIN);
        String validateCode = (String) map.get("validateCode");
        if(validateCodeInRedis != null && validateCodeInRedis.equals(validateCode)){
            // 验证码输入成功
            // 判断当前用户是否为会员（查会员表）
            Member member = memberService.findByTelephone(telephone);
            if(member == null){
                // 不是会员，自动完成注册（自动将当前用户信息保存到会员表）
                member.setRegTime(new Date());
                member.setPhoneNumber(telephone);
                memberService.add(member);
            }
            // 向客户端浏览器写入cookie，内容为手机号
            Cookie cookie = new Cookie("login_member_telephone", telephone);
            cookie.setPath("/");//路径
            cookie.setMaxAge(60*60*24*30);//有效期30天
            response.addCookie(cookie);
            // 将会员信息保存到Redis
            try {
                String json = new ObjectMapper().writeValueAsString(member);
                // cookie保持30分钟
                jedisPool.getResource().setex(telephone, 60*30, json);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return new Result(true, MessageConstant.LOGIN_SUCCESS);
        }else{
            // 验证码输入错误
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
    }
}
