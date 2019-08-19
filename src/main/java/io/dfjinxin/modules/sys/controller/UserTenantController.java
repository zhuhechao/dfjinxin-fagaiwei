//package io.dfjinxin.modules.sys.controller;
//
//import java.util.Arrays;
//import java.util.Map;
//
//import io.dfjinxin.common.utils.PageUtils;
//import io.dfjinxin.common.utils.R;
//import io.dfjinxin.modules.sys.entity.UserTenantEntity;
//import io.dfjinxin.modules.sys.service.UserTenantService;
//import io.dfjinxin.common.annotation.RequiresPermissions;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//
///**
// * 用户租户信息表
// *
// * @author chenshun
// * @email sunlightcs@gmail.com
// * @date 2019-06-12 17:06:08
// */
//@RestController
//@RequestMapping("/sys/usertenant")
//public class UserTenantController extends AbstractController{
//    @Autowired
//    private UserTenantService userTenantService;
//
//    /**
//     * 列表
//     */
//    @RequestMapping("/list")
//    public R list(@RequestParam Map<String, Object> params){
//        PageUtils page = userTenantService.queryPage(params);
//
//        return R.ok().put("page", page);
//    }
//
//
//    /**
//     * 信息
//     */
//    @RequestMapping("/info/{id}")
//    public R info(@PathVariable("id") Long id){
//		UserTenantEntity userTenant = userTenantService.getById(id);
//
//        return R.ok().put("userTenant", userTenant);
//    }
//
//    /**
//     * 保存
//     */
//    @RequestMapping("/save")
//    public R save(@RequestBody UserTenantEntity userTenant){
//        userTenant.setUserId(getUserId());
//		userTenantService.save(userTenant);
//
//        return R.ok();
//    }
//
//    /**
//     * 修改
//     */
//    @RequestMapping("/update")
//    public R update(@RequestBody UserTenantEntity userTenant){
//        userTenant.setUserId(getUserId());
//		userTenantService.updateById(userTenant);
//
//        return R.ok();
//    }
//
//    /**
//     * 删除
//     */
//    @RequestMapping("/delete")
//    public R delete(@RequestBody Long[] ids){
//		userTenantService.removeByIds(Arrays.asList(ids));
//
//        return R.ok();
//    }
//
//    @RequestMapping("/queryAll")
//    public R queryAll(){
//        return R.ok().put("data", userTenantService.queryAll());
//    }
//}
