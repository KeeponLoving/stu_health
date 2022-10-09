package stu.gdut.service;

import stu.gdut.domain.CheckItem;
import stu.gdut.entity.PageResult;
import stu.gdut.entity.QueryPageBean;

import java.util.List;

public interface CheckItemService {
    public void add(CheckItem checkItem);

    public PageResult pageQuery(QueryPageBean queryPageBean);

    public void deleteById(Integer id);

    public void edit(CheckItem checkItem);

    public CheckItem findById(Integer id);

    public List<CheckItem> findAll();
}
