package stu.gdut.dao;

import com.github.pagehelper.Page;
import stu.gdut.domain.CheckGroup;

import java.util.List;
import java.util.Map;

public interface CheckGroupMapper {
    public void add(CheckGroup checkGroup);

    public void setCheckGroupAndCheckItem(Map map);

    public Page<CheckGroup> findByCondition(String queryString);

    public CheckGroup findById(Integer id);

    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    public void edit(CheckGroup checkGroup);

    public void deleteAssociation(Integer id);

    public long findCountByGroupId(Integer id);

    public void deleteGroupFromItemAndGroup(Integer id);

    public void deleteGroup(Integer id);

    public List<CheckGroup> findAll();
}
