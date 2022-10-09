package stu.gdut.controller;

import org.apache.dubbo.config.annotation.DubboReference;
import org.aspectj.bridge.Message;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import stu.gdut.constant.MessageConstant;
import stu.gdut.domain.CheckGroup;
import stu.gdut.entity.PageResult;
import stu.gdut.entity.QueryPageBean;
import stu.gdut.entity.Result;
import stu.gdut.service.CheckGroupService;

import java.util.List;

/**
 * 检查组管理
 */
@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {
    @DubboReference
    private CheckGroupService checkGroupService;

    // 新增检查组
    @PreAuthorize("hasAuthority('CHECKGROUP_ADD')")//权限校验
    @PostMapping("/checkGroup")
    public Result add(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds) {
        try {
            checkGroupService.add(checkGroup, checkitemIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    // 分页查询
    @PreAuthorize("hasAuthority('CHECKGROUP_QUERY')")//权限校验
    @PostMapping("/checkGroups")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        return checkGroupService.pageQuery(queryPageBean);
    }

    // 根据id查询检查组
    @PreAuthorize("hasAuthority('CHECKGROUP_QUERY')")//权限校验
    @GetMapping("/checkGroup")
    public Result findById(Integer id){
        try {
            CheckGroup checkGroup = checkGroupService.findById(id);
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, checkGroup);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    // 根据检查组id查询检查组包含的多个检查项id
    @PreAuthorize("hasAuthority('CHECKGROUP_QUERY')")//权限校验
    @GetMapping("/checkItems")
    public Result findCheckItemIdsByCheckGroupId(Integer id){
        try {
            List<Integer> checkItemIds = checkGroupService.findCheckItemIdsByCheckGroupId(id);
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkItemIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    // 编辑检查组
    @PreAuthorize("hasAuthority('CHECKGROUP_EDIT')")//权限校验
    @PutMapping("/checkGroup")
    public Result edit(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds) {
        try {
            checkGroupService.edit(checkGroup, checkitemIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }

    // 删除检查组
    @PreAuthorize("hasAuthority('CHECKGROUP_DELETE')")//权限校验
    @DeleteMapping("/checkGroup")
    public Result deleteGroup(Integer id){
        try {
            checkGroupService.deleteGroup(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }

    // 查询所有的检查组
    @PreAuthorize("hasAuthority('CHECKGROUP_QUERY')")//权限校验
    @GetMapping("/checkGroups")
    public Result findAllCheckGroup(){
        try {
            List<CheckGroup> checkGroups = checkGroupService.findAll();
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, checkGroups);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }
}