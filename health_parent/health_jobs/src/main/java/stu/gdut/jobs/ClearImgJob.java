package stu.gdut.jobs;

import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import stu.gdut.constant.RedisConstant;
import stu.gdut.utils.QiniuUtils;

import java.util.Set;

public class ClearImgJob {
    @Autowired
    private JedisPool jedisPool;

    public void clearImg(){
        // 从两个Set集合中计算差值，然后根据差值删除七牛云服务器上多余的图片，并维护Redis集合中的Set集合
        Set<String> set=jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if(set != null){
            for (String picName : set) {
                // 删除七牛云服务器上的图片
                QiniuUtils.deleteFileFromQiniu(picName);
                // 从Redis集合中删除图片名称
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES, picName);
                System.out.println("清除图片："+picName);
            }
        }
    }
}
