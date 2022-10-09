package stu.gdut.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import stu.gdut.dao.CheckGroupMapper;
import stu.gdut.domain.CheckGroup;
import stu.gdut.domain.CheckItem;
import stu.gdut.entity.PageResult;
import stu.gdut.entity.QueryPageBean;
import stu.gdut.service.CheckGroupService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 检查组服务
 */
@DubboService(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {
    @Autowired
    private CheckGroupMapper checkGroupMapper;
    // 新增检查组，同时需要让检查组关联检查项
    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        // 新增检查组，操作t_checkgroup
        checkGroupMapper.add(checkGroup);
        // 设置检查组和检查项的多对多关联关系，操作t_checkgroup_checkitem
        Integer checkGroupId=checkGroup.getId();
        setCheckGroupAndCheckItem(checkGroupId, checkitemIds);
    }

    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        // 完成分页查询，基于mybatis框架提供的分页助手插件完成
        PageHelper.startPage(currentPage, pageSize);
        Page<CheckGroup> page=checkGroupMapper.findByCondition(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupMapper.findById(id);
    }

    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return checkGroupMapper.findCheckItemIdsByCheckGroupId(id);
    }

    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {
        // 修改检查组基本信息
        checkGroupMapper.edit(checkGroup);
        // 先清除关联关系
        checkGroupMapper.deleteAssociation(checkGroup.getId());
        // 再建立关联关系
        Integer checkGroupId=checkGroup.getId();
        this.setCheckGroupAndCheckItem(checkGroup.getId(), checkitemIds);
    }

    // 删除检查项
    @Override
    public void deleteGroup(Integer id) {
        // 不能直接删除的情况：当前检查组在检查套餐内
        long count = checkGroupMapper.findCountByGroupId(id);
        if(count>0){
            new RuntimeException();
        }
        // 删除检查组需要删除检查组表和检查项-检查表中的内容
        checkGroupMapper.deleteGroupFromItemAndGroup(id);
        checkGroupMapper.deleteGroup(id);
    }

    @Override
    public List<CheckGroup> findAll() {
        return checkGroupMapper.findAll();
    }

    // 设置检查组和检查项的对应关系
    public void setCheckGroupAndCheckItem(Integer checkGroupId, Integer[] checkitemIds){
        if(checkitemIds!=null && checkitemIds.length>0){
            for(Integer checkitemId : checkitemIds){
                Map<String, Integer> map=new HashMap<>();
                map.put("checkgroupId", checkGroupId);
                map.put("checkitemId", checkitemId);
                System.out.println("项目组ID："+checkGroupId);
                checkGroupMapper.setCheckGroupAndCheckItem(map);
            }
        }
    }
}
