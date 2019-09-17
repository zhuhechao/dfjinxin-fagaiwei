package io.dfjinxin.modules.sys.controller;

import io.dfjinxin.common.annotation.RequiresPermissions;
import io.dfjinxin.common.annotation.SysLog;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.common.validator.ValidatorUtils;
import io.dfjinxin.common.validator.group.UpdateGroup;
import io.dfjinxin.modules.sys.entity.SysDepEntity;
import io.dfjinxin.modules.sys.entity.SysUserEntity;
import io.dfjinxin.modules.sys.service.SysDepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by GaoPh on 2019/9/12.
 */
@RestController
@RequestMapping("/sys/dep")
public class SysDepController  extends AbstractController{
    @Autowired
    private SysDepService sysDepService;

    /**
     * 所有部门列表
     */
    @GetMapping("/list")
    @RequiresPermissions("sys:dep:list")
    public R list(@RequestParam Map<String, Object> params){
        //只有超级管理员，才能查看所有管理员列表
        PageUtils page = sysDepService.queryPage(params);
        List<SysDepEntity> list = (List<SysDepEntity>)page.getList();
        page.setList(list);

        return R.ok().put("page", page);
    }



    /**
     * 部门信息
     */
    @GetMapping("/info/{depId}")
    @RequiresPermissions("sys:dep:info")
    public R info(@PathVariable("depId") String userId){
        SysDepEntity dep = sysDepService.getById(userId);
        return R.ok().put("dep", dep);
    }


    /**
     * 修改部门
     */
    @SysLog("修改部门")
    @PostMapping("/update")
    @RequiresPermissions("sys:dep:update")
    public R update(@RequestBody SysDepEntity user){
        ValidatorUtils.validateEntity(user, UpdateGroup.class);
        sysDepService.updateById(user);
        return R.ok();
    }

    /**
     * 批量删除部门
     */
    @SysLog("批量删除部门")
    @PostMapping("/delete")
    @RequiresPermissions("sys:dep:delete")
    public R delete(@RequestBody String[] depIds){

//		if(ArrayUtils.contains(userIds, getUserId())){
//			return R.error("当前用户不能删除");
//		}
        List<String> list = new ArrayList<>();
       for(String dep : depIds){
           list.add(dep);
       }
        sysDepService.removeByIds(list);

        return R.ok();
    }

}
