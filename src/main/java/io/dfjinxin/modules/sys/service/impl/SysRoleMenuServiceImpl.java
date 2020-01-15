/**
 * 2019 东方金信
 *
 *
 *
 *
 */

package io.dfjinxin.modules.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.modules.sys.dao.SysRoleMenuDao;
import io.dfjinxin.modules.sys.entity.SysRoleEntity;
import io.dfjinxin.modules.sys.entity.SysRoleMenuEntity;
import io.dfjinxin.modules.sys.service.SysRoleMenuService;
import io.dfjinxin.modules.sys.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 角色与菜单对应关系
 *
 * @author Mark sunlightcs@gmail.com
 */
@Service("sysRoleMenuService")
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuDao, SysRoleMenuEntity> implements SysRoleMenuService {
    @Autowired
	private SysRoleMenuDao sysRoleMenuDao;
    @Autowired
	private SysRoleService sysRoleService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R saveOrUpdate(int roleId,int roleTypeId, List<Integer> menuIdList) {
		//先删除角色与菜单关系
		SysRoleEntity sysRoleEntity = new SysRoleEntity();
		sysRoleEntity.setRoleId(roleId);
		sysRoleEntity.setRoleTypeId(roleTypeId);
		sysRoleEntity.setMenuIdList(menuIdList);
		R r=  sysRoleService.checkPerm(sysRoleEntity);
		Integer ft= (Integer) r.get("code");
		String msg= (String) r.get("msg");
		if(ft == 1){
			return R.error(1,msg);
		}else {
			ArrayList<Integer> list = new ArrayList<>();
			list.add(roleId);
			deleteBatch(list);
			Date now = new Date();
			if (menuIdList.size() == 0) {
				return R.ok();
			}

			//保存角色与菜单关系
			for (int menuId : menuIdList) {
				SysRoleMenuEntity sysRoleMenuEntity = new SysRoleMenuEntity();
				sysRoleMenuEntity.setMenuId(menuId);
				sysRoleMenuEntity.setRoleId(roleId);
				sysRoleMenuEntity.setCreDate(now);
				sysRoleMenuEntity.setUpdDate(now);

				this.save(sysRoleMenuEntity);
			}
		}
		return R.ok();
	}

	@Override
	public List<Map<String,Object>> queryMenuList(Map<String,Object> roleId) {

		return   baseMapper.queryMenuList(roleId);
	}

	@Override
	public int deleteBatch(ArrayList<Integer> roleIds){
		return baseMapper.deleteBatch(roleIds);
	}

	@Override
	public List<Map<String, Object>> select(Map<String, Object> map) {
		return sysRoleMenuDao.select(map);
	}

}
