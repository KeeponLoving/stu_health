package stu.gdut.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;
import stu.gdut.constant.MessageConstant;
import stu.gdut.constant.RedisMessageConstant;
import stu.gdut.entity.Result;
import stu.gdut.utils.SMSUtils;
import stu.gdut.utils.ValidateCodeUtils;

/**
 * 验证码操作
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {
    @Autowired
    private JedisPool jedisPool;

    // 用户在线体检预约发送验证码
    @PostMapping("/orderCode")
    public Result send4Order(String telephone){
        // 使用工具类获取验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(4);
        // 给用户发送验证码
        try {
            SMSUtils.sendShortMessage(telephone, String.valueOf(validateCode));
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        // 把验证码保存到redis（5分钟）
        jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_ORDER, 300, String.valueOf(validateCode));
//        jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_ORDER, 300, "1234");
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    // 手机快速登录时发送手机验证码
    @PostMapping("/loginCode")
    public Result send4Login(String telephone){
        // 使用工具类获取验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(6);
        // 给用户发送验证码
        try {
            SMSUtils.sendShortMessage(telephone, String.valueOf(validateCode));
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        // 把验证码保存到redis（5分钟）
        jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_LOGIN, 300, String.valueOf(validateCode));
//        jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_ORDER, 300, "1234");
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
