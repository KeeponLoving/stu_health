package stu.gdut.service.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import stu.gdut.dao.OrderSettingMapper;
import stu.gdut.domain.OrderSetting;
import stu.gdut.service.OrderSettingService;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DubboService(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingMapper orderSettingMapper;

    // 批量导入预约设置数据
    @Override
    public void add(List<OrderSetting> list) {
        if (list != null) {
            for (OrderSetting orderSetting : list) {
                // 判断当前日期是否已经进行了预约设置
                int count = orderSettingMapper.findCountByOrderDate(orderSetting.getOrderDate());
                if (count > 0) {
                    // 已经进行了预约设置，执行更新操作
                    orderSettingMapper.editNumberByOrderDate(orderSetting);
                } else {
                    // 没有进行预约设置，执行插入操作
                    orderSettingMapper.add(orderSetting);
                }
            }
        }
    }

    // 根据月份查询预约设置数据
    @Override
    public List<Map> findByYearAndMonth(String date) {
        // date 格式YYYY-MM
        String begin = date + "-1";
        String end = date + "-31";
        Map<String, String> map = new HashMap<>();
        map.put("begin", begin);
        map.put("end", end);
        List<OrderSetting> list = orderSettingMapper.findByYearAndMonth(map);
        List<Map> result=new ArrayList<>();
        if (list != null) {
            for (OrderSetting orderSetting : list) {
                Map<String, Object> m = new HashMap<>();
                // 获取日期
                LocalDate localDate = orderSetting.getOrderDate().toLocalDate();
                m.put("date", localDate.getDayOfMonth());
                m.put("number", orderSetting.getNumber());
                m.put("reservations", orderSetting.getReservations());
                result.add(m);
            }
        }
        return result;
    }

    // 根据日期设置对应的预约设置数据
    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        // 判断当前日期是否已进行预约设置
        Date date = orderSetting.getOrderDate();
        int count = orderSettingMapper.findCountByOrderDate(date);
        if(count>0){
            // 已经进行了预约设置，进行更新操作
            orderSettingMapper.editNumberByOrderDate(orderSetting);
        }else{
            // 当前日期没有进行预约设置，进行插入操作
            orderSettingMapper.add(orderSetting);
        }
    }
}
