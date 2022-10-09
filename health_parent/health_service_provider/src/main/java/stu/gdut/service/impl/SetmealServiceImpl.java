package stu.gdut.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.JedisPool;
import stu.gdut.constant.RedisConstant;
import stu.gdut.dao.SetmealMapper;
import stu.gdut.domain.Setmeal;
import stu.gdut.entity.PageResult;
import stu.gdut.entity.QueryPageBean;
import stu.gdut.service.SetmealService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DubboService(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private JedisPool jedisPool;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Value("${out_put_path}")
    private String outPutPath;

    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        // 操作两张表
        setmealMapper.add(setmeal);
        Integer setmealId = setmeal.getId();
        this.setSetmealAndCheckGroup(setmealId, checkgroupIds);
        // 将图片名称保存到Redis集合中
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, setmeal.getImg());
        // 当添加套餐后需要重新生成静态页面（套餐列表页面，套餐详情页面）
        generateMobileStaticHtml();
    }

    // 用于生成静态页面
    public void generateHtml(String templateName, String htmlPageName, Map map) {
        // 获得配置对象
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        try (// 构造输出流
             Writer out = new FileWriter(new File(outPutPath + "/" + htmlPageName))) {
            // 获取模板对象
            Template template = configuration.getTemplate(templateName);
            template.process(map, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 生成当前方法所需的静态页面
    public void generateMobileStaticHtml(){
        // 在生成静态页面之前需要查询数据，感觉有点不好，这个每次都要查询全部并生成静态html
        List<Setmeal> list=setmealMapper.findAll();
        // 生成套餐列表静态页面
        generateMobileSetmealListHtml(list);
        // 生成套餐详情静态页面
        generateMobileSetmealDetailHtml(list);
    }

    //生成套餐列表静态页面
    public void generateMobileSetmealListHtml(List<Setmeal> list){
        Map map=new HashMap();
        // 为模板提供数据，用于生成静态页面
        map.put("setmealList", list);
        generateHtml("mobile_setmeal.ftl", "m_setmeal.html", map);
    }

    // 生成套餐详情页面，可能有多个
    public void generateMobileSetmealDetailHtml(List<Setmeal> list){
        for (Setmeal setmeal : list) {
            Map map = new HashMap();
            map.put("setmeal", setmealMapper.findById(setmeal.getId()));
            generateHtml("mobile_setmeal_detail.ftl", "setmeal_detail_"+setmeal.getId()+".html", map);
        }
    }

    // 实现分页查询
    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        // 当前页码
        Integer currentPage = queryPageBean.getCurrentPage();
        // 页面大小
        Integer pageSize = queryPageBean.getPageSize();
        // 查询条件
        String queryString = queryPageBean.getQueryString();
        // 使用插件
        PageHelper.startPage(currentPage, pageSize);
        Page<Setmeal> page = setmealMapper.findByCondition(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public List<Setmeal> findAll() {
        return setmealMapper.findAll();
    }

    // 根据套餐ID查询套餐详情（套餐基本信息、套餐对应的检查组信息、检查组对应的检查项信息）
    @Override
    public Setmeal findById(Integer id) {
        return setmealMapper.findById(id);
    }

    // 查询套餐预约占比数据
    @Override
    public List<Map<String, Object>> findSetmealCount() {
        return setmealMapper.findSetmealCount();
    }

    // 设置套餐和检查组多对多关系
    public void setSetmealAndCheckGroup(Integer setmealId, Integer[] checkgroupIds) {
        if (checkgroupIds != null && checkgroupIds.length > 0) {
            for (Integer checkgroupId : checkgroupIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("checkgroupId", checkgroupId);
                map.put("setmealId", setmealId);
                setmealMapper.setSetmealAndChechGroup(map);
            }
        }
    }
}
