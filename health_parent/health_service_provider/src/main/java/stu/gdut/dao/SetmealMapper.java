package stu.gdut.dao;

import com.github.pagehelper.Page;
import stu.gdut.domain.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealMapper {

    public void add(Setmeal setmeal);

    public void setSetmealAndChechGroup(Map map);

    public Page<Setmeal> findByCondition(String queryString);

    public List<Setmeal> findAll();

    public Setmeal findById(Integer id);

    public List<Map<String, Object>> findSetmealCount();
}
