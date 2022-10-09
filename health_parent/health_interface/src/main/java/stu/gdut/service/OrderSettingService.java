package stu.gdut.service;

import stu.gdut.domain.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {
    public void add(List<OrderSetting> data);

    public List<Map> findByYearAndMonth(String date);

    public void editNumberByDate(OrderSetting orderSetting);
}
