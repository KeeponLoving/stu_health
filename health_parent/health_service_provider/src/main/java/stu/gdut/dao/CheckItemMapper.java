package stu.gdut.dao;

import com.github.pagehelper.Page;
import stu.gdut.domain.CheckItem;

import java.util.List;

public interface CheckItemMapper {
    public void add(CheckItem checkItem);

    public Page<CheckItem> selectByCondition(String queryString);

    public long findCountByCheckItemId(Integer id);

    public void deleteById(Integer id);

    public void edit(CheckItem checkItem);

    public CheckItem findById(Integer id);

    public List<CheckItem> findAll();
}
