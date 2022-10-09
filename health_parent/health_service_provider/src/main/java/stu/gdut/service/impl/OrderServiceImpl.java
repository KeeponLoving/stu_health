package stu.gdut.service.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import stu.gdut.constant.MessageConstant;
import stu.gdut.dao.MemberMapper;
import stu.gdut.dao.OrderMapper;
import stu.gdut.dao.OrderSettingMapper;
import stu.gdut.domain.Member;
import stu.gdut.domain.Order;
import stu.gdut.domain.OrderSetting;
import stu.gdut.entity.Result;
import stu.gdut.service.OrderService;
import stu.gdut.utils.DateUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 体检预约服务
 */
@DubboService(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderSettingMapper orderSettingMapper;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private OrderMapper orderMapper;

    // 体检预约
    @Override
    public Result order(Map map) throws Exception {
        // 1、检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有设置则无法进行预约
        String orderDate = (String) map.get("orderDate");
        OrderSetting orderSetting = orderSettingMapper.findByOrderDate(DateUtils.parseString2Date(orderDate));
        if (orderSetting == null) {
            // 指定日期没有进行预约设置，无法完成体检预约
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        // 2、检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约
        int number = orderSetting.getNumber();  // 可预约人数
        int reservation = orderSetting.getReservations();   // 已预约人数
        if (reservation > number) {
            // 预约人数已满，无法进行预约
            return new Result(false, MessageConstant.ORDER_FULL);
        }

        // 3、检查用户是否重复预约（同一个用户在同一天预约了同一个套餐），如果是重复预约则无法完成再次预约
        String telephone = (String) map.get("telephone");
        Member member = memberMapper.findByTelephone(telephone);

        if (member != null) {
            // 判断是否在重复预约，需要用户id、预约日期、套餐id
            Integer memberId = member.getId();
            Date order_Date = DateUtils.parseString2Date(orderDate);
            String setmealId = (String) map.get("setmealId");
            Order order = new Order(memberId, order_Date, Integer.valueOf(setmealId));
            // 根据条件进行查询
            List<Order> list = orderMapper.findByCondition(order);
            if (list != null && list.size() > 0) {
                // 用户在重复预约，无法完成再次预约
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        } else {
            // 4、检查当前用户是否为会员，如果是会员则直接完成预约，如果不是会员则自动完成注册并进行预约
            member = new Member();
            member.setName((String) map.get("name"));
            member.setPhoneNumber((String) map.get("telephone"));
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));
            member.setRegTime(new Date());
            memberMapper.add(member);   // 完成会员注册
        }
        // 5、预约成功，更新当日的已预约人数
        Order order = new Order();
        // 设置会员id
        order.setMemberId(member.getId());
        // 预约日期
        order.setOrderDate(DateUtils.parseString2Date(orderDate));
        order.setOrderType((String) map.get("orderType"));
        // 到诊状态
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        order.setSetmealId(Integer.valueOf((String) map.get("setmealId")));
        orderMapper.add(order);
        orderSetting.setReservations(orderSetting.getReservations()+1); //设置已预约人数
        orderSettingMapper.editReservationsByOrderDate(orderSetting);
        return new Result(true, MessageConstant.ORDER_SUCCESS, order.getId());
    }

    // 根据预约ID查询预约相关信息（体检人姓名、预约日期、套餐名称、预约类型）
    @Override
    public Map findById(Integer id) throws Exception{
        Map map = orderMapper.findById4Detail(id);
        if(map != null){
            //处理日期格式
            Date orderDate = (Date) map.get("orderDate");
            map.put("orderDate",DateUtils.parseDate2String(orderDate));
        }
        return map;
    }
}
