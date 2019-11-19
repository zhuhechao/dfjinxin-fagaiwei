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
import io.dfjinxin.modules.sys.dao.SysUserDao;
import io.dfjinxin.modules.sys.dao.SysUserRoleDao;
import io.dfjinxin.modules.sys.entity.SysUserEntity;
import io.dfjinxin.modules.sys.service.SysRoleService;
import io.dfjinxin.modules.sys.service.SysUserRoleService;
import io.dfjinxin.modules.sys.service.SysUserService;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
@Service("sysUserService")
@Transactional(rollbackFor = Exception.class)
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUserEntity> implements SysUserService {
    @Autowired
	private SysUserDao sysUserDao;
    @Autowired
	private SysUserRoleService sysUserRoleService;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		IPage<SysUserEntity> page = this.page(
			new Query<SysUserEntity>().getPage(params),
				new QueryWrapper<SysUserEntity>()
		);
		return new PageUtils(page);
	}

   @Override
	public PageUtils queryUserList(Map<String, Object> params) {
		long no = params.containsKey("page") ? Long.valueOf(params.get("page").toString()) : 1;
		long limit = params.containsKey("limit") ? Long.valueOf(params.get("limit").toString()) : 10;
		IPage<SysUserEntity> page = baseMapper.queryUserList(new Page<>(no, limit), params);
		List<SysUserEntity> list = page.getRecords();
		for(SysUserEntity map:list){
		  Integer st= map.getUserStatus();
			setRolesInfo(map);
		  if(st == 1){
		  	map.setStatus(true);
		  }else {
			  map.setStatus(false);
		  }
		}
		return new PageUtils(page);
	}


	@Override
	public List<String> queryAllPerms(String userId) {
		return baseMapper.queryAllPerms(userId);
	}

	@Override
	public List<Integer> queryAllMenuId(String userId) {
		return baseMapper.queryAllMenuId(userId);
	}

	@Override
	public SysUserEntity queryByUserName(String username) {

		return sysUserDao.queryByUserName(username);
	}

	@Override
	@Transactional
	public void saveUser(SysUserEntity user) {
		Date date = new Date();
		//sha256加密,暂不需要
//		String salt = RandomStringUtils.randomAlphanumeric(20);
//		user.setUserPass(new Sha256Hash(user.getUserPass(), salt).toHex());
//		user.setSalt(salt);
		sysUserDao.insertUserData(user);

	}

	@Override
	public void update(SysUserEntity user) {
		List<Integer> list = user.getRoles();
		String role= StringUtils.join(list.toArray(),",");
		user.setRoleId(role);
		user.setError_no(0);
//		String userId = user.getUserId();
//		sysUserRoleService.saveOrUpdate(userId,list);
//		this.updateById(user);
		baseMapper.updateUserData(user);

	}


	@Override
	public void deleteBatch(String[] userId) {
		this.removeByIds(Arrays.asList(userId));
	}

	@Override
	public void deleteUser(String userId) {
		this.removeById(userId);
	}

	@Override
	public SysUserEntity getUserById(String userId) {
		Map<String,Object> map = new HashMap<>();
		map.put("userId",userId);
		SysUserEntity sysUserEntity = baseMapper.queryUserList(map);
		setRolesInfo(sysUserEntity);
		return  sysUserEntity;
	}


	@Override
	public List<Map<String, Object>> getUserPerm(String userId) {
		List<Map<String, Object>> list= baseMapper.getUserPerm(userId);
		List<Map<String, Object>> m1 = new ArrayList<>();
		List<Map<String, Object>> m2 = new ArrayList<>();
		List<Map<String, Object>> m3 = new ArrayList<>();
        for(Map<String, Object> data:list){
			if((int)data.get("pare_menu_id") != 1 && (int)data.get("menu_type")==2) {
				Map<String, Object> map = new HashMap<>();

				String pr = (String) data.get("menu_router");
				int i= pr.lastIndexOf("/");
				map.put("path",pr.substring(i+1));
				map.put("name",upperCase(pr.substring(i+1)));
				Map<String, Object>  m4 = new HashMap<>();
				m4.put("title",data.get("menu_name"));
				map.put("meta",m4);
				map.put("component","() => import('@/views"+pr+"')");
				map.put("pare_menu_id",data.get("pare_menu_id"));
				map.put("menu_id",data.get("menu_id"));
				m3.add(map);
			}else if( (int)data.get("pare_menu_id") != 1 && (int)data.get("menu_type")==1){
				List<Map<String, Object>> lt = new ArrayList<>();
				Map<String, Object> map = new HashMap<>();
				String pr = (String) data.get("menu_router");
				int i= pr.lastIndexOf("/");
				map.put("path",pr.substring(i+1));
				Map<String, Object>  m4 = new HashMap<>();
				m4.put("title",data.get("menu_name"));
				map.put("meta",m4);
				map.put("component","() => import('@/views"+pr+"')");
				map.put("pare_menu_id",data.get("pare_menu_id"));
				map.put("menu_id",data.get("menu_id"));
				map.put("children",lt);
				m2.add(map);
			}else {
				Map<String, Object> map = new HashMap<>();
				List<Map<String, Object>> lt = new ArrayList<>();
				String pr = (String) data.get("menu_router");
				int i= pr.lastIndexOf("/");
				Map<String, Object>  m4 = new HashMap<>();
				m4.put("title",data.get("menu_name"));
				m4.put("icon","example");
				map.put("meta",m4);
				map.put("path",pr.substring(0,i));
				map.put("component", "Layout");
				map.put("redirect",pr);
				map.put("pare_menu_id",data.get("pare_menu_id"));
				map.put("menu_id",data.get("menu_id"));
				map.put("children",lt);
				m1.add(map);
			}
		}

		updateMap(m2,m3);
		updateMap(m1,m3);
		updateMap(m1,m2);

		for(Map<String,Object> dt: m1){
			int flg=(int)dt.get("pare_menu_id");
			List<Map<String,Object>> lt= (List<Map<String, Object>>) dt.get("children");
			String pr = (String) dt.get("redirect");
			int i= pr.lastIndexOf("/");
			if(lt.size()==0 && flg==0){
				Map<String, Object> map = new HashMap<>();
				map.put("path",pr.substring(i+1));
				map.put("component","() => import('@/views"+pr.substring(0,i)+"')");
				Map<String,Object> mt= (Map<String, Object>) dt.get("meta");
				dt.remove("meta");
				map.put("meta",mt);
				lt.add(map);
			}
			if(flg==0){
				dt.remove("pare_menu_id");
				dt.remove("menu_id");
			}
		}
		return  m1;
	}

	private static  String upperCase(String str){
		char[] ch = str.toCharArray();
		if (ch[0] >= 'a' && ch[0] <= 'z') {
			ch[0] = (char) (ch[0] - 32);
		}
		return new String(ch);
	}

	private static void  updateMap(List<Map<String, Object>> m1,List<Map<String, Object>> m2){
		for(Map<String,Object> dt:m2){
			Object mid =  dt.get("pare_menu_id");
			if(mid != null) {
				for (Map<String, Object> d : m1) {
					if ( (int) d.get("menu_id") == (int) mid){
						List<Map<String, Object>> lt = (List<Map<String, Object>>) d.get("children");
						dt.remove("pare_menu_id");
						dt.remove("menu_id");
						lt.add(dt);
						d.put("children", lt);
					}
				}
			}
		}
	}

	private static void  setRolesInfo(SysUserEntity sysUserEntity){
		String roleId = sysUserEntity.getRoleId();
		if(roleId !=null && !roleId.equals("")){
			String[] roleIds = roleId.split(",");
			ArrayList<Integer> role = new ArrayList<>();
			for(String rid : roleIds){
				role.add(Integer.parseInt(rid));
			}
			sysUserEntity.setRoles(role);
		}
	}

}