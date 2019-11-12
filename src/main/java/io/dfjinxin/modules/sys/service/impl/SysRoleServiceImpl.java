/**
 * 2019 东方金信
 *
 *
 *
 *
 */

package io.dfjinxin.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.exception.RRException;
import io.dfjinxin.common.utils.Constant;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.Query;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.modules.sys.dao.SysRoleDao;
import io.dfjinxin.modules.sys.entity.SysDepEntity;
import io.dfjinxin.modules.sys.entity.SysMenuEntity;
import io.dfjinxin.modules.sys.entity.SysRoleEntity;
import io.dfjinxin.modules.sys.entity.SysUserRoleEntity;
import io.dfjinxin.modules.sys.service.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

/**
 * 角色
 *
 * @author Mark sunlightcs@gmail.com
 */
@Service("sysRoleService")
@Transactional(rollbackFor = Exception.class)
public class SysRoleServiceImpl extends ServiceImpl<SysRoleDao, SysRoleEntity> implements SysRoleService {
	@Autowired
	private SysRoleMenuService sysRoleMenuService;

    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
	private SysRoleDao sysRoleDao;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		long no = params.containsKey("page") ? Long.valueOf(params.get("page").toString()) : 1;
		long limit = params.containsKey("limit") ? Long.valueOf(params.get("limit").toString()) : 10;
		IPage<SysRoleEntity> page = baseMapper.queryRole(new Page<>(no, limit), params);
		List<SysRoleEntity> list = page.getRecords();
		for(SysRoleEntity map:list){
			Integer st= map.getRoleState();
			if(st == 1){
				map.setStatus(true);
			}else {
				map.setStatus(false);
			}
		}

		return new PageUtils(page);
	}



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addOrUpdate(SysRoleEntity role) {
 		 int roleId = role.getRoleId();
 		 List<Integer> menus= role.getMenuIdList();
 		 if(menus.size()>0){
			 String ms= StringUtils.join(menus.toArray(),",");
			 role.setMenuIds(ms);
		 }
		if(roleId ==0 ){
        	veryRole(role,0);
			baseMapper.save(role);
		}else {
			veryRole(role,1);
			baseMapper.updateRole(role);
//			sysRoleMenuService.saveOrUpdate(role.getRoleId(),role.getMenuIdList());
		}
		//sysRoleMenuService.saveOrUpdate(roleId,role.getMenuIdList());
		//List<Integer> menus= role.getMenuIdList();
//		if(menus != null){
//			sysRoleMenuService.saveOrUpdate(roleId,role.getMenuIdList());
//		}
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(int[] roleIds) {
        //删除角色
		List<Integer> rids = new ArrayList<>();
		for(int data:roleIds){
			rids.add(data);
		}
		List<SysUserRoleEntity> list= sysUserRoleService.listByIds(rids);
		if(list !=null && list.size()>0){
			throw new RRException("当前指定的角色已经被使用，不能删除！");
		}
        this.removeByIds(rids);
		//删除角色与菜单关联
       sysRoleMenuService.deleteBatch(roleIds);

    }

	@Override
	public R rolePerm(String role_id) {
		Map<String,Object> fmap = new HashMap<>();
		List<Map<String,Object>> privilegeDef=  sysRoleMenuService.select(fmap);
		Map<String,Object> smap = new HashMap<>();
		fmap.put("role_id", role_id);
        List<SysMenuEntity> ps = new ArrayList<>();

		List<Map<String,Object>> privilegeOfRole= sysRoleMenuService.queryMenuList(fmap);
		Map<Integer, Map<String, Object>> priDefMap = new HashMap<>();
		privilegeDef.forEach(item -> priDefMap.put((Integer)item.get("menu_id"), item));

		Map<Integer, Map<String, Object>> pri4RoleMap = new HashMap<>();
		privilegeOfRole.forEach(item -> pri4RoleMap.put((Integer)item.get("menu_id"), item));

		List<Map<String, Object>> result = new ArrayList<>();
		priDefMap.forEach((k, v) -> result.add(pri4RoleMap.getOrDefault(k, v)));


		return R.ok().put("data",result);
	}

	@Override
	public void addRole(SysRoleEntity role) {

	}

	@Override
	public List<Map<String, Object>> getRole() {
		List<Map<String, Object>> roles=   sysRoleDao.getRole();
		return roles;
	}

	/**
	 * 检查角色
	 */
	private  void veryRole(SysRoleEntity r,int flag){
			String  roleName= r.getRoleName();
		    int rs = r.getRoleState();
		    int rt = r.getRoleTypeId();
			Map<String,Object> map = new HashMap<>();
		    map.put("role_name",roleName);
		    List<SysRoleEntity> re = re= baseMapper.selectByMap(map);;
			if(flag == 0 && re.size()>0){
				throw new RRException("角色名称重复");
			}else if(flag == 1 && re.size()>1){
				throw new RRException("角色名称已存在！");
			}

		    if(rt == 0){
		    	throw new RRException("请指定角色类型！");
		    }

	}


	/**
	 * 检查权限是否越权
	 */
	private void checkPrems(SysRoleEntity role){
//		//如果不是超级管理员，则需要判断角色的权限是否超过自己的权限
//		if(role.getCreateUserId() == Constant.SUPER_ADMIN){
//			return ;
//		}
//
//		//查询用户所拥有的菜单列表
//		List<Long> menuIdList = sysUserService.queryAllMenuId(role.getCreateUserId());
//
//		//判断是否越权
//		if(!menuIdList.containsAll(role.getMenuIdList())){
//			throw new RRException("新增角色的权限，已超出你的权限范围");
//		}
	}
}
