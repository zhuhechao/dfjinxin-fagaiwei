package io.dfjinxin.modules.sys.controller;

import io.dfjinxin.common.annotation.RequiresPermissions;
import io.dfjinxin.common.annotation.SysLog;
import io.dfjinxin.common.exception.RRException;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.common.validator.ValidatorUtils;
import io.dfjinxin.common.validator.group.UpdateGroup;
import io.dfjinxin.modules.sys.entity.DepParams;
import io.dfjinxin.modules.sys.entity.MenuParams;
import io.dfjinxin.modules.sys.entity.SysDepEntity;
import io.dfjinxin.modules.sys.entity.SysUserEntity;
import io.dfjinxin.modules.sys.service.SysDepService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by GaoPh on 2019/9/12.
 */
@RestController
@RequestMapping("/sys/dep")
@Api(tags = "部门管理")
public class SysDepController  extends AbstractController{
    @Autowired
    private SysDepService sysDepService;

    /**
     * 所有部门列表
     */
    @ApiOperation("获取所有部门列表")
    @GetMapping("/list")
    @RequiresPermissions("sys:dep:list")
    public R list(@RequestParam Map<String, Object> params){
        //只有超级管理员，才能查看所有管理员列表
        PageUtils page = sysDepService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 部门信息保存
     */
    @ApiOperation("保存部门信息")
    @PostMapping("/save")
    @RequiresPermissions("sys:dep:save")
    public R saveDep(@RequestBody SysDepEntity dep){
        if(!dep.getDepId().equals("")&& dep.getDepId() != null){
            sysDepService.updateById(dep);
        }else {
            List<SysDepEntity> list = new ArrayList<>();
            String depId= Long.toString(new Date().getTime());
            dep.setDepId(depId);
            list.add(dep);
            sysDepService.addDeps(list);
        }
        return R.ok() ;
    }

    /**
     * 部门信息
     */
    @ApiOperation("获取指定部门信息")
    @GetMapping("/info")
    @RequiresPermissions("sys:dep:info")
    public R info(@RequestParam("depId") String depId){
        SysDepEntity dep = sysDepService.getDepId(depId);
        return R.ok().put("dep", dep);
    }

    /**
     * 用户管理部门信息下拉框
     */
    @GetMapping("/userDepInfo")
    @RequiresPermissions("sys:dep:userDepInfo")
    public R userDepInfo( ){
        List<Map<String,Object>> dep = sysDepService.serDepInfo();
        return R.ok().put("dep", dep);
    }



    /**
     * 批量删除部门
     */
    @ApiOperation("批量删除部门")
    @PostMapping("/delete")
    @RequiresPermissions("sys:dep:delete")
    public R delete(@RequestBody DepParams depIds){

         List<String> list=  depIds.getIds();
        sysDepService.removeByIds(list);

        return R.ok();
    }

    /*
	 * 部门信息验证
	 */
    @ApiOperation("验证部门信息")
    @PostMapping("/checkDep")
    @RequiresPermissions("sys:dep:checkDep")
    public R checkDep(@RequestBody SysDepEntity dep){
        return verifyForm(dep);
    }


    private R verifyForm(SysDepEntity dep) {
        if (StringUtils.isBlank(dep.getDepName())) {
            return R.error(1,"菜单名称不能为空");
        }
        return R.ok();
    }

}
