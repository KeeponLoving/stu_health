package stu.gdut.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import stu.gdut.entity.PageResult;
import stu.gdut.entity.QueryPageBean;
import stu.gdut.service.CheckItemService;
import stu.gdut.constant.MessageConstant;
import stu.gdut.domain.CheckItem;
import stu.gdut.entity.Result;

import java.util.List;

@RestController
@RequestMapping("/checkitem")
public class CheckItemController {
    // @Reference跟@autowired类似，完成自动注入，是Dubbo框架提供的注解
    @DubboReference
    private CheckItemService checkItemService;

    // 新增检查项
    @PreAuthorize("hasAuthority('CHECKITEM_ADD')")//权限校验
    @PostMapping("/checkItem")
    public Result add(@RequestBody CheckItem checkItem) {
        try {
            System.out.println(checkItem);
            checkItemService.add(checkItem);
        }catch (Exception e){
            e.printStackTrace();
            // 服务调用失败
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    // 分页查询
    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")//权限校验
    @PostMapping("/checkItems")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return checkItemService.pageQuery(queryPageBean);
    }

    // 删除检查项
    @PreAuthorize("hasAuthority('CHECKITEM_DELETE')")//权限校验
    @DeleteMapping("/checkItem")
    public Result Delete(Integer id){
        try {
            checkItemService.deleteById(id);
        }catch (Exception e){
            e.printStackTrace();
            // 服务调用失败
            return new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }

    // 编辑检查项
    @PreAuthorize("hasAuthority('CHECKITEM_EDIT')")//权限校验
    @PutMapping("/checkItem")
    public Result edit(@RequestBody CheckItem checkItem){
        try {
            checkItemService.edit(checkItem);
        }catch (Exception e){
            e.printStackTrace();
            // 服务调用失败
            return new Result(false, MessageConstant.EDIT_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }

    // 查找检查项
    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")//权限校验
    @GetMapping("/checkItem")
    public Result findById(Integer id){
        try {
            CheckItem checkItem = checkItemService.findById(id);
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkItem);
        }catch (Exception e){
            e.printStackTrace();
            // 服务调用失败
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    // 查找检查项
    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")//权限校验
    @GetMapping("/checkItems")
    public Result findAll(){
        try {
            List<CheckItem> list = checkItemService.findAll();
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, list);
        }catch (Exception e){
            e.printStackTrace();
            // 服务调用失败
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }
}
