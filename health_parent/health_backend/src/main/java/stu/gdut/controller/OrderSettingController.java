package stu.gdut.controller;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import stu.gdut.constant.MessageConstant;
import stu.gdut.domain.OrderSetting;
import stu.gdut.entity.Result;
import stu.gdut.service.OrderSettingService;
import stu.gdut.utils.POIUtils;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {
    @DubboReference
    private OrderSettingService orderSettingService;

    // 文件上传，实现预约数据批量导入
    @PreAuthorize("hasAuthority('ORDERSETTING')")//权限校验
    @PostMapping("/file")
    public Result upload(MultipartFile excelFile) {
        try {
            // 使用POI解析表格数据
            var list = POIUtils.readExcel(excelFile);
            List<OrderSetting> data = new ArrayList<>();
            for (String[] strings : list) {
                String orderDate = strings[0];
                String number = strings[1];
                OrderSetting orderSetting = new OrderSetting(Date.valueOf(orderDate),
                        Integer.valueOf(number));
                data.add(orderSetting);
            }
            // 通过dubbo远程调用服务实现数据批量导入到数据库
            orderSettingService.add(data);
        } catch (Exception e) {
            e.printStackTrace();
            // 文件解析失败
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
        return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
    }

    // 查询预约数据
    @GetMapping("/orderSettings")
    public Result findByYearAndMonth(String date){
        try {
            List<Map> list = orderSettingService.findByYearAndMonth(date);
            return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS, list);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }

    // 查询预约数据
    @PostMapping("/orderSettings")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting){
        try {
            orderSettingService.editNumberByDate(orderSetting);
            return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDERSETTING_FAIL);
        }
    }
}
