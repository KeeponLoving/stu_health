package stu.gdut.service;

import stu.gdut.domain.CheckGroup;
import stu.gdut.entity.PageResult;
import stu.gdut.entity.QueryPageBean;

import java.util.List;

public interface CheckGroupService {
    public void add(CheckGroup checkGroup, Integer[] checkitemIds);

    public PageResult pageQuery(QueryPageBean queryPageBean);

    public CheckGroup findById(Integer id);

    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    public void edit(CheckGroup checkGroup, Integer[] checkitemIds);

    public void deleteGroup(Integer id);

    public List<CheckGroup> findAll();
}
