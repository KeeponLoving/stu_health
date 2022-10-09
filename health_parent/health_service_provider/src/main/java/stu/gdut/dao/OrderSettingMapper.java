package stu.gdut.dao;

import stu.gdut.domain.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingMapper {
    public void add(OrderSetting orderSetting);

    public void editNumberByOrderDate(OrderSetting orderSetting);

    public int findCountByOrderDate(Date orderDate);

    public List<OrderSetting> findByYearAndMonth(Map<String, String> map);

    public OrderSetting findByOrderDate(Date parseString2Date);
    //更新已预约人数
    public void editReservationsByOrderDate(OrderSetting orderSetting);
}
