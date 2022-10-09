package stu.gdut.service;

import stu.gdut.entity.Result;

import java.util.Map;

public interface OrderService {
    public Result order(Map map) throws Exception;

    public Map findById(Integer id) throws Exception;
}
