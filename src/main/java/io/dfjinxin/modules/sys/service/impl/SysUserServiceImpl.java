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
import io.dfjinxin.modules.sys.entity.MenuHiddenEnum;
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
		long no = params.containsKey("pageIndex") ? Long.valueOf(params.get("pageIndex").toString()) : 1;
		long limit = params.containsKey("pageSize") ? Long.valueOf(params.get("pageSize").toString()) : 10;
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
	public SysUserEntity queryByUserName(SysUserEntity sysUserEntity) {

		return sysUserDao.queryByUserName(sysUserEntity);
	}

	@Override
	public void saveUser(SysUserEntity user) {
		Date date = new Date();
		//sha256加密,暂不需要
//		String salt = RandomStringUtils.randomAlphanumeric(20);
//		user.setUserPass(new Sha256Hash(user.getUserPass(), salt).toHex());
//		user.setSalt(salt);
		List<Integer> list = user.getRoles();
		String role= StringUtils.join(list.toArray(),",");
		user.setRoleId(role);
		user.setError_no(0);
		user.setUserPass("99ec41f1dc48f4c6a018b688411b456d");
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
		List<Map<String, Object>> m6 = new ArrayList<>();
		List<Map<String, Object>> m7 = new ArrayList<>();
		List<Map<String, Object>> m8 = new ArrayList<>();
		List<Map<String, Object>> m9 = new ArrayList<>();
		List<Map<String, Object>> md = new ArrayList<>();
		List<Map<String, Object>> tmp = new ArrayList<>();
	  if(list.size()<=0 || list == null){
	  	return new ArrayList<>();
	  }
	  tmp.addAll(list);
	  menusMap(list,tmp);

		for(Map<String, Object> data:list){
       	if((int)data.get("pare_menu_id")==0){
       		break;
		}

		if(Integer.parseInt((String) data.get("parent"))==0){
       		data.put("depth",0);
		}
		if(Integer.parseInt((String) data.get("parent"))==1){
			data.put("depth",1);
			buildChildren(data,list);
		}
	   }
        for(Map<String, Object> data:list){
			if((int)data.get("depth") == 3) {
				levelOneMenu(m6,data);
			}else if((int)data.get("depth") == 2){
				List<Map<String, Object>> lt = new ArrayList<>();
				Map<String, Object> map = new HashMap<>();
				String pr = (String) data.get("menu_router");
				int i= pr.lastIndexOf("/");
				if(i==0){
					map.put("path",pr.substring(1));
				}else {
					map.put("path",pr.substring(1,i));
				}
				if(i==0){
					map.put("component",pr.substring(1));
				}else {
					map.put("component",pr.substring(1,i));
				}

				map.put("redirect",pr);
				Map<String, Object>  m4 = new HashMap<>();
				m4.put("title",data.get("menu_name"));
				map.put("meta",m4);
				map.put("children",lt);
				map.put("pare_menu_id",data.get("pare_menu_id"));
				map.put("menu_id",data.get("menu_id"));
				map.put("menu_order",data.get("menu_order"));
				m3.add(map);
			}else if((int) data.get("depth") == 1 && (int) data.get("pare_menu_id")!= 1) {
				Map<String, Object> map = new HashMap<>();
				List<Map<String, Object>> lt = new ArrayList<>();
				String pr = (String) data.get("menu_router");
				int i= pr.lastIndexOf("/");
				map.put("path",pr.substring(i+1));
				map.put("component",pr.substring(1));
				Map<String, Object>  m4 = new HashMap<>();
				m4.put("title",data.get("menu_name"));
				map.put("meta",m4);
				map.put("children",lt);
				map.put("pare_menu_id",data.get("pare_menu_id"));
				map.put("menu_id",data.get("menu_id"));
				map.put("menu_order",data.get("menu_order"));
				m2.add(map);
			}else if((int) data.get("depth") == 1 && (int) data.get("pare_menu_id")== 1) {
				levelOneMenu(m1,data);
			}else if((int) data.get("depth") == 0 && (int) data.get("pare_menu_id")!= 1) {
				Map<String, Object> map = new HashMap<>();
				String pr = (String) data.get("menu_router");
				int i= pr.lastIndexOf("/");
				map.put("path",pr.substring(i+1));
				map.put("name",upperCase(pr.substring(i+1)));
				map.put("component",pr.substring(1));
				Map<String, Object>  m4 = new HashMap<>();
				m4.put("title",data.get("menu_name"));
				map.put("meta",m4);
				map.put("pare_menu_id",data.get("pare_menu_id"));
				map.put("menu_id",data.get("menu_id"));
				map.put("menu_order",data.get("menu_order"));
				m7.add(map);
			}else if((int) data.get("depth") == 0 && (int) data.get("pare_menu_id")== 1) {
				Map<String, Object> map = new HashMap<>();
				List<Map<String, Object>> lt = new ArrayList<>();
				String pr = (String) data.get("menu_router");
				int i= pr.lastIndexOf("/");
				if(i==0){
					map.put("path",pr);
				}else {
					map.put("path",pr.substring(0,i));
				}
				map.put("component","Layout");
				map.put("redirect",pr);
				Map<String, Object>  m4 = new HashMap<>();
				Map<String, Object>  m5 = new HashMap<>();
				m5.put("path",pr.substring(i+1));
				if(i==0){
					m5.put("component",pr.substring(1));
				}else {
					m5.put("component",pr.substring(1,i));
				}
				m4.put("title",data.get("menu_name"));
				MenuHiddenEnum menuHiddenEnum = MenuHiddenEnum.getbyType(pr);
				if(menuHiddenEnum !=null ){
					m4.put("icon",menuHiddenEnum.getValue());
				}else {
					m4.put("icon","");
				}
				m5.put("meta",m4);
				lt.add(m5);
				map.put("children",lt);
				map.put("pare_menu_id",data.get("pare_menu_id"));
				map.put("menu_id",data.get("menu_id"));
				map.put("menu_order",data.get("menu_order"));
				m8.add(map);
			}
		}
       //m6 3级 m3 2级 m2深度为1不是1级菜单 m1深度为1并且是1级菜单 m7深度为0不是1级菜单   m8 深度为0且1级菜单
		CopyMaps(m9,m2);
		CopyMaps(m9,m7);
		CopyMaps(md,m7);
		CopyMaps(md,m3);
		Collections.sort(m7,new MapComparatorDesc());
		updateMap(m2,m7);
		Collections.sort(m2,new MapComparatorDesc());
		Collections.sort(m9,new MapComparatorDesc());
		updateMap(m3,m9);
		Collections.sort(m3,new MapComparatorDesc());
		Collections.sort(md,new MapComparatorDesc());
		updateMap(m6,md);
		Collections.sort(m6,new MapComparatorDesc());
        updateMap(m1,m7);
        m1.addAll(m6);
        m1.addAll(m8);
		Collections.sort(m1,new MapComparatorDesc());
		for(Map<String,Object> dt: m1){
			int flg=(int)dt.get("pare_menu_id");
			if(flg==1){
				dt.remove("pare_menu_id");
				dt.remove("menu_id");
				dt.remove("menu_order");
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
						dt.remove("menu_order");
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

	private void buildChildren(Map<String,Object> mbean,List<Map<String,Object>> menus){
		int mid= (int) mbean.get("menu_id");
		int dth = (int) mbean.get("depth");
		for(Map<String,Object> dt : menus){
			if((int)dt.get("pare_menu_id")== mid && Integer.parseInt((String) dt.get("parent"))==1){
             mbean.put("depth",dth+1);
            for(Map<String,Object> data : menus){
            	 if((int)dt.get("menu_id")== (int)data.get("pare_menu_id") && Integer.parseInt((String) data.get("parent"))== 1){
					int dthh = (int) mbean.get("depth");
					mbean.put("depth",dthh+1);
				}
			}
			}
		}
	}

	private void menusMap(List<Map<String,Object>> list,List<Map<String,Object>> tmap){
		int fl = 1;
		for(int j=0;j<list.size();j++){
			int pid= (int) list.get(j).get("pare_menu_id");
			int mmid= (int) list.get(j).get("menu_id");
			Object m = list.get(j);
			for(Map<String,Object> dt:tmap){
				int mid = (int) dt.get("menu_id");
				if(pid ==1 ){
					fl = 0;
					break;
				}
				if(mmid == mid){
					continue;
				}
				if( pid == mid){
					fl = 0;
					break;
				}
			}
			if(fl != 0){
				list.remove(m);
				return;
			}
		}

		if(fl != 0){
			List<Map<String, Object>> tmp = new ArrayList<>();
			tmp.addAll(list);
			menusMap(list,tmp);
		}
	}

	private void levelOneMenu(List<Map<String, Object>> m,Map<String,Object> data){
		Map<String, Object> map = new HashMap<>();
		List<Map<String, Object>> lt = new ArrayList<>();
		String pr = (String) data.get("menu_router");
		int i= pr.lastIndexOf("/");
		map.put("path",pr.substring(0,i));
		map.put("component","Layout");
		map.put("redirect",pr);
		Map<String, Object>  m4 = new HashMap<>();
		m4.put("title",data.get("menu_name"));
		MenuHiddenEnum menuHiddenEnum = MenuHiddenEnum.getbyType(pr);
		if(menuHiddenEnum !=null ){
			m4.put("icon",menuHiddenEnum.getValue());
		}else {
			m4.put("icon","");
		}
		map.put("meta",m4);
		map.put("children",lt);
		map.put("pare_menu_id",data.get("pare_menu_id"));
		map.put("menu_id",data.get("menu_id"));
		map.put("menu_order",data.get("menu_order"));
		m.add(map);
	}

	private static class MapComparatorDesc implements Comparator<Map<String,Object>>{

		@Override
		public int compare(Map<String, Object> o1, Map<String, Object> o2) {
			Integer v1 = Integer.valueOf(o1.get("menu_order").toString()) ;
			Integer v2 = Integer.valueOf(o2.get("menu_order").toString()) ;
			if(v2 !=null){
				return v2.compareTo(v1);
			}
			return 0;
		}
	}

	private void  CopyMaps(List<Map<String,Object>> m1,List<Map<String,Object>> m2){
		if(m2.size()>0) {
			for (Map<String, Object> map : m2) {
				Map<String, Object> pm = new HashMap<>();
				Iterator it= map.entrySet().iterator();
				while (it.hasNext()){
					Map.Entry entry = (Map.Entry) it.next();
					String key = (String) entry.getKey();
					pm.put( key,map.get(key) != null ? map.get(key):"");
				}
				m1.add(pm);
			}
		}
	}

}