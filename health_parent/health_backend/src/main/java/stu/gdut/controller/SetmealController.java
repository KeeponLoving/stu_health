package stu.gdut.controller;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import stu.gdut.constant.MessageConstant;
import stu.gdut.constant.RedisConstant;
import stu.gdut.domain.Setmeal;
import stu.gdut.entity.PageResult;
import stu.gdut.entity.QueryPageBean;
import stu.gdut.entity.Result;
import stu.gdut.service.SetmealService;
import stu.gdut.utils.QiniuUtils;

import java.io.IOException;
import java.util.UUID;

/**
 * 体检套餐管理
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @DubboReference
    private SetmealService setmealService;

    @Autowired
    private JedisPool jedisPool;

    @PostMapping("/img")
    public Result upload(MultipartFile imgFile){
        // 原始文件名
        String originalFilename = imgFile.getOriginalFilename();
        // 获取后缀
        int index = originalFilename.lastIndexOf(".");
        String extention = originalFilename.substring(index);
        String fileName = UUID.randomUUID().toString() + extention;
        try {
            // 上传文件到七牛云
            QiniuUtils.upload2Qiniu(imgFile.getBytes(), fileName);
            // 向redis集合中增加图片名称
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES, fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
        return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, fileName);
    }

    @PreAuthorize("hasAuthority('SETMEAL_ADD')")//权限校验
    @PostMapping("/setmeal")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds){
        try {
            setmealService.add(setmeal, checkgroupIds);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    // 分页查询
    @PreAuthorize("hasAuthority('SETMEAL_QUERY')")//权限校验
    @PostMapping("/setmeals")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        return setmealService.pageQuery(queryPageBean);
    }
}
