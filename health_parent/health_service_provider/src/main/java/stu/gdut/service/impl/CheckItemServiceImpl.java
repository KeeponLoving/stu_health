package stu.gdut.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import stu.gdut.dao.CheckItemMapper;
import stu.gdut.domain.CheckItem;
import stu.gdut.entity.PageResult;
import stu.gdut.entity.QueryPageBean;
import stu.gdut.service.CheckItemService;

import java.util.List;

@DubboService(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {
    // 注入CheckItemDao
    @Autowired
    private CheckItemMapper checkItemMapper;
    @Override
    public void add(CheckItem checkItem) {
        checkItemMapper.add(checkItem);
    }

    // 检查项分页查询
    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        // 完成分页查询，基于mybatis框架提供的分页助手插件完成
        PageHelper.startPage(currentPage, pageSize);
        Page<CheckItem> page=checkItemMapper.selectByCondition(queryString);
        long total=page.getTotal();
        List<CheckItem> rows = page.getResult();
        return new PageResult(total, rows);
    }

    // 根据id删除检查项
    @Override
    public void deleteById(Integer id) {
        // 不能直接删除，还需要查其是否已被归进某些检查组内
        long count = checkItemMapper.findCountByCheckItemId(id);
        if(id > 0){
            new RuntimeException();
        }
        checkItemMapper.deleteById(id);
    }

    @Override
    public void edit(CheckItem checkItem) {
        checkItemMapper.edit(checkItem);
    }

    @Override
    public CheckItem findById(Integer id) {
        return checkItemMapper.findById(id);
    }

    @Override
    public List<CheckItem> findAll() {
        return checkItemMapper.findAll();
    }
}
