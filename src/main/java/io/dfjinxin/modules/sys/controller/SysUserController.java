/**
 * 2019 东方金信
 *
 *
 *
 *
 */

package io.dfjinxin.modules.sys.controller;

import io.dfjinxin.common.annotation.SysLog;
import io.dfjinxin.common.utils.Constant;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.common.validator.ValidatorUtils;
import io.dfjinxin.common.validator.group.AddGroup;
import io.dfjinxin.common.validator.group.UpdateGroup;
import io.dfjinxin.modules.sys.entity.DepParams;
import io.dfjinxin.modules.sys.entity.MenuParams;
import io.dfjinxin.modules.sys.entity.SysUserEntity;
import io.dfjinxin.modules.sys.service.SysUserRoleService;
import io.dfjinxin.modules.sys.service.SysUserService;
import io.dfjinxin.modules.sys.service.UserTenantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.ArrayUtils;
import io.dfjinxin.common.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/sys/user")
@Api(tags = "用户管理")
public class SysUserController extends AbstractController {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserRoleService sysUserRoleService;



	/**
	 * 所有用户列表
	 */
	@GetMapping("/list")
	@RequiresPermissions("sys:user:list")
	@ApiOperation("用户数据查询")
	public R list(@RequestParam Map<String, Object> params){
		//只有超级管理员，才能查看所有管理员列表
		PageUtils page = sysUserService.queryUserList(params);

		return R.ok().put("page", page);
	}



	/**
	 * 用户信息
	 */
	@GetMapping("/userInfo/{userId}")
	@RequiresPermissions("sys:user:info")
	public R info(@PathVariable("userId") String userId){
		SysUserEntity user = sysUserService.getUserById(userId);
		return R.ok().put("user", user);
	}


	/**
	 * 修改用户
	 */
	@SysLog("修改用户")
	@PostMapping("/update")
	@RequiresPermissions("sys:user:update")
	public R update(@RequestBody  SysUserEntity user){
      if(user.getUserId() == null || user.getUserId().equals("")){
	  String userId= Long.toString(new Date().getTime());
	  user.setUserId(userId);
	  sysUserService.saveUser(user);
      }else {
      sysUserService.update(user);
	  }
		return R.ok();
	}

	/**
	 * 批量删除用户
	 */
	@SysLog("删除用户")
	@PostMapping("/delete")
	@RequiresPermissions("sys:user:delete")
	public R delete(@RequestBody DepParams depIds){
		ArrayList<String> ids = depIds.getIds();
         String[] userIds = ids.toArray(new String[ids.size()]);
//		if(ArrayUtils.contains(userIds, getUserId())){
//			return R.error("当前用户不能删除");
//		}

		sysUserService.deleteBatch(userIds);

		return R.ok();
	}

}
