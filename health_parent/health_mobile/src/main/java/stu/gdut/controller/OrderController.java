package stu.gdut.controller;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.JedisPool;
import stu.gdut.constant.MessageConstant;
import stu.gdut.constant.RedisMessageConstant;
import stu.gdut.domain.Order;
import stu.gdut.entity.Result;
import stu.gdut.service.OrderService;
import stu.gdut.utils.SMSUtils;

import java.util.Map;

/**
 * 处理体检预约
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private JedisPool jedisPool;
    @DubboReference
    private OrderService orderService;
    // 在线体检预约
    @PostMapping("/order")
    public Result submit(@RequestBody Map map){
        String telephone = (String)map.get("telephone");
        // 从Redis中获取保存的验证码
        String orderCodeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        String validateCode = (String) map.get("validateCode");
        // 将用户输入的验证码和Redis中保存的验证码进行比对
        if(orderCodeInRedis != null && validateCode != null && orderCodeInRedis.equals(validateCode)){
            // 设置预约方式
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            // 如果比对成功，调用服务完成预约业务处理
            Result result = null;
            try{
                result = orderService.order(map);
            }catch (Exception e){
                e.printStackTrace();
                return result;
            }
            if(result.isFlag()){
                // 预约成功，为用户发送短信
                try {
                    SMSUtils.sendShortMessage(telephone, "1234");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return result;
        }else {
            // 如果比对不成功，返回结果给页面
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
    }

    @GetMapping("/order")
    public Result findById(Integer id){
        try {
            Map map = orderService.findById(id);
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, map);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}
