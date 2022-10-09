package stu.gdut.controller;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import stu.gdut.constant.MessageConstant;
import stu.gdut.domain.Setmeal;
import stu.gdut.entity.Result;
import stu.gdut.service.SetmealService;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @DubboReference
    private SetmealService setmealService;

    @GetMapping("/setmeals")
    public Result getAllSetmeal() {
        try {
            List<Setmeal> list = setmealService.findAll();
            return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS, list);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_LIST_FAIL);
        }
    }

    // 根据套餐ID查询套餐详情（套餐基本信息、套餐对应的检查组信息、检查组对应的检查项信息）
    @GetMapping("/setmeal")
    public Result findById(Integer id) {
        try {
            Setmeal setmeal = setmealService.findById(id);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }
}
