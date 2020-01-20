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
		long no = params.containsKey("pageIndex") ? Long.valueOf(params.get("pageIndex").toString()) : 1;
		long limit = params.containsKey("pageSize") ? Long.valueOf(params.get("pageSize").toString()) : 10;
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
    public R addOrUpdate(SysRoleEntity role) {
		 R r=checkPerm(role);
		Integer ft= (Integer) r.get("code");
		String msg= (String) r.get("msg");
		if(ft == 1){
			return R.error(1,msg);
		}else {
 		 int roleId = role.getRoleId();
 		 Date date = new Date();
 		 Timestamp ts = new Timestamp(date.getTime());
 		 List<Integer> menus= role.getMenuIdList();
 		 if(menus !=null && menus.size()>0){
			 String ms= StringUtils.join(menus.toArray(),",");
			 role.setMenuIds(ms);
		 }
		if(roleId ==0 ){
 		 	role.setCreDate(ts);
 		 	role.setUpdDate(ts);
			baseMapper.save(role);
		}else {
			role.setUpdDate(ts);
			baseMapper.updateRole(role);
		}
		return R.ok();
    }
	}

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R deleteBatch(ArrayList<Integer> roleIds) {
		 R r= checkPermInfo(roleIds);
		 Integer ft= (Integer) r.get("code");
		 String ms= (String) r.get("msg");
		 if(ft == 1){
		 	return R.error(1,ms);
		 }else {
			 //删除角色
			 List<Integer> rids = new ArrayList<>();
			 for(int data:roleIds){
				 rids.add(data);
			 }
			 this.removeByIds(rids);
			 //删除角色与菜单关联
			 sysRoleMenuService.deleteBatch(roleIds);
			 return R.ok();
		 }


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

		for(Map<String,Object> map : result){
			 Object rid= map.get("role_id");
		     if(rid == null || rid == ""){
		     	map.put("role_id","");
			 }
		}



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
	 * flg 0表示新增角色，1表示修改角色
	 */
	@Override
	public  R checkPerm(SysRoleEntity r){
			String  roleName= r.getRoleName();
			int roleId = r.getRoleId();
		    List<Integer> rs = r.getMenuIdList();
		    int rt = r.getRoleTypeId();
		    if(roleName !=null && !roleName.equals("")){
				Map<String,Object> map = new HashMap<>();
				map.put("role_name",roleName);
				List<SysRoleEntity> re =  baseMapper.selectByMap(map);
				int sid = 0;
				if(re.size()>0){
					sid = re.get(0).getRoleId();
				}
				if(roleId == 0 && re.size()>0){
					return R.error(1,"角色名称重复");
				}else if(roleId != 0 && sid!=0 &&  sid != roleId){
					R.error(1,"角色名称已存在！");
				}
			}

			if(roleId !=0 && rs!=null){
				if(rt == 1 && !rs.contains(2)){
					return R.error(1,"该角色未分配系统管理员权限！");
				}else if(rt !=1 && rs.contains(2)){
					return  R.error(1,"该角色不应分配系统管理员角色！");
				}
			}

          return R.ok();
	}

	@Override
	public R checkPermInfo(ArrayList<Integer> roles) {
		List<SysUserRoleEntity> list= sysUserRoleService.listByIds(roles);
		if(list !=null && list.size()>0){
			return R.error(1,"当前指定的角色已经被使用，不能删除！");
		}
		return R.ok();
	}

	@Override
	public SysRoleEntity getRoleById(String roleId) {
		Map<String,Object> map = new HashMap<>();
		map.put("roleId",roleId);
		return baseMapper.queryRole(map);
	}

}
