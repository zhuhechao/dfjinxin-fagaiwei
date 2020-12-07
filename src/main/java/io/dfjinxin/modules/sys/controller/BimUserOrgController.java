package io.dfjinxin.modules.sys.controller;

import com.alibaba.fastjson.JSON;
import com.banboocloud.Codec.BamboocloudFacade;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.modules.sys.bimUtils.BamboocloudUtils;
import io.dfjinxin.modules.sys.entity.SysDepEntity;
import io.dfjinxin.modules.sys.entity.SysRoleEntity;
import io.dfjinxin.modules.sys.entity.SysUserDepEntity;
import io.dfjinxin.modules.sys.entity.SysUserEntity;
import io.dfjinxin.modules.sys.service.SysDepService;
import io.dfjinxin.modules.sys.service.SysUserDepService;
import io.dfjinxin.modules.sys.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.*;

/**
 * Copyright (C) 2020 THL A29 Limited, a dfjx company. All rights reserved.
 *
 * @author
 * @since 2020/12/7 14:42
 */
@RestController
@RequestMapping("/sys/bim")
@Api(tags = "数据同步")
public class BimUserOrgController extends AbstractController {
    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysDepService sysDepService;
    @Resource
    private SysUserDepService sysUserDepService;
    @Value("${bim.key}")
    private String key;
    @Value("${bim.type}")
    private String type;

    private String authType = "MD5";

    @PostMapping("/user-create")
    @ApiOperation("新增用户")
    public void createUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String bodyParam = BamboocloudUtils.getRequestBody(request);
        bodyParam = BamboocloudUtils.getPlaintext(bodyParam, key, type);
        Map<String, Object> reqmap = (Map<String, Object>) JSON.parse(bodyParam);
        if (BamboocloudUtils.verify(reqmap, authType).booleanValue()) {
            String orgId = (String) reqmap.get("orgId");
            SysUserEntity user = new SysUserEntity();
            user.setCrteDate(new Date());
            user.setDepId(orgId);
            SysDepEntity sysDepEntity = sysDepService.getDepId(orgId);
            user.setDepName(Objects.isNull(sysDepEntity) ? "" : sysDepEntity.getDepName());
            String userId = Long.toString(System.currentTimeMillis());
            user.setUserId(userId);
            user.setError_no(0);
            user.setUserPass((String) reqmap.get("bimRemotePwd"));
            user.setUserName((String) reqmap.get("bimRemoteUser"));
            user.setUserRealName((String) reqmap.get("fullName"));
            user.setUserStatus(1);
            user.setStatus(true);
            boolean saveUser = sysUserService.save(user);
            SysUserDepEntity depEntity = new SysUserDepEntity();
            depEntity.setDepId(orgId);
            depEntity.setUserId(userId);
            sysUserDepService.save(depEntity);
            PrintWriter out = response.getWriter();
            out.write(backJson(saveUser, userId, (String) reqmap.get("bimRequestId"), "账号创建失败，报错信息自定义"));
            out.close();
        }
    }

    @PostMapping("/user-update")
    @ApiOperation("修改用户")
    public void updateUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String bodyParam = BamboocloudUtils.getRequestBody(request);
        bodyParam = BamboocloudUtils.getPlaintext(bodyParam, key, type);
        Map<String, Object> reqmap = (Map<String, Object>) JSON.parse(bodyParam);
        if (BamboocloudUtils.verify(reqmap, authType).booleanValue()) {
            SysUserEntity user = sysUserService.getUserById((String) reqmap.get("bimUid"));
            user.setUserPass((String) reqmap.get("bimRemotePwd"));
            user.setUserName((String) reqmap.get("bimRemoteUser"));
            user.setUserRealName((String) reqmap.get("fullName"));
            if (StringUtils.isNotBlank((String) reqmap.get("__ENABLE__"))) {
                String status = (String) reqmap.get("__ENABLE__");
                if ("true".equals(status)) {
                    user.setUserStatus(1);
                } else if ("false".equals(status)) {
                    user.setUserStatus(0);
                }
            }
            boolean saveUser = sysUserService.updateById(user);
            PrintWriter out = response.getWriter();
            out.write(backJson(saveUser, null, (String) reqmap.get("bimRequestId"), "账号更新失败，报错信息自定义"));
            out.close();
        }
    }

    @PostMapping("/user-delete")
    @ApiOperation("删除用户")
    public void deleteUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String bodyParam = BamboocloudUtils.getRequestBody(request);
        bodyParam = BamboocloudUtils.getPlaintext(bodyParam, key, type);
        Map<String, Object> reqmap = (Map<String, Object>) JSON.parse(bodyParam);
        if (BamboocloudUtils.verify(reqmap, authType).booleanValue()) {
            SysUserEntity user = sysUserService.getUserById((String) reqmap.get("bimUid"));
            user.setUserStatus(0);
            boolean saveUser = sysUserService.updateById(user);
            PrintWriter out = response.getWriter();
            out.write(backJson(saveUser, null, (String) reqmap.get("bimRequestId"), "账号删除失败，报错信息自定义"));
            out.close();
        }
    }

    @PostMapping("/org-create")
    @ApiOperation("新增组织机构")
    public void createOrg(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String bodyParam = BamboocloudUtils.getRequestBody(request);
        bodyParam = BamboocloudUtils.getPlaintext(bodyParam, key, type);
        Map<String, Object> reqmap = (Map<String, Object>) JSON.parse(bodyParam);
        if (BamboocloudUtils.verify(reqmap, authType).booleanValue()) {
            String parOrgId = (String) reqmap.get("parOrgId");
            String orgName = (String) reqmap.get("orgName");
            SysDepEntity org = new SysDepEntity();
            org.setCreDate((Timestamp) new Date());
            org.setDepId(Long.toString(System.currentTimeMillis()));
            org.setDepName(orgName);
            org.setSuperDepId(parOrgId);
            org.setStatus(true);
            boolean saveOrg = sysDepService.save(org);
            PrintWriter out = response.getWriter();
            out.write(backJson(saveOrg, org.getDepId(), (String) reqmap.get("bimRequestId"), "机构创建失败"));
            out.close();
        }
    }

    @PostMapping("/org-update")
    @ApiOperation("修改组织机构")
    public void updateOrg(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String bodyParam = BamboocloudUtils.getRequestBody(request);
        bodyParam = BamboocloudUtils.getPlaintext(bodyParam, key, type);
        Map<String, Object> reqmap = (Map<String, Object>) JSON.parse(bodyParam);

        if (BamboocloudUtils.verify(reqmap, authType).booleanValue()) {
            SysDepEntity org = sysDepService.getDepId((String) reqmap.get("bimOrgId"));
            org.setSuperDepId((String) reqmap.get("parOrgId"));
            SysDepEntity parOrg = sysDepService.getDepId((String) reqmap.get("parOrgId"));
            org.setSuperDepName(Objects.isNull(parOrg) ? "" : parOrg.getDepName());
            org.setDepName((String) reqmap.get("orgName"));
            if (StringUtils.isNotBlank((String) reqmap.get("__ENABLE__"))) {
                String status = (String) reqmap.get("__ENABLE__");
                if ("true".equals(status)) {
                    org.setStatus(true);
                } else if ("false".equals(status)) {
                    org.setStatus(false);
                }
            }
            org.setUpdDate((Timestamp) new Date());
            boolean saveUser = sysDepService.updateById(org);
            PrintWriter out = response.getWriter();
            out.write(backJson(saveUser, null, (String) reqmap.get("bimRequestId"), "组织更新失败，报错信息自定义"));
            out.close();
        }
    }

    @PostMapping("/org-delete")
    @ApiOperation("删除用户")
    public void deleteOrg(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String bodyParam = BamboocloudUtils.getRequestBody(request);
        bodyParam = BamboocloudUtils.getPlaintext(bodyParam, key, type);
        Map<String, Object> reqmap = (Map<String, Object>) JSON.parse(bodyParam);
        if (BamboocloudUtils.verify(reqmap, authType).booleanValue()) {
            SysDepEntity user = sysDepService.getDepId((String) reqmap.get("bimOrgId"));
            user.setStatus(false);
            boolean saveUser = sysDepService.updateById(user);
            PrintWriter out = response.getWriter();
            out.write(backJson(saveUser, null, (String) reqmap.get("bimRequestId"), "组织删除失败，报错信息自定义"));
            out.close();
        }
    }

    @PostMapping("/role-create")
    @ApiOperation("新增角色")
    public void createRole(HttpServletRequest request, HttpServletResponse response) throws IOException {
       // not need todo
    }

    @PostMapping("/role-update")
    @ApiOperation("修改组织机构")
    public void updateRole(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // not need todo
    }

    @PostMapping("/role-delete")
    @ApiOperation("删除用户")
    public void deleteRole(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // not need todo
    }

    private String backJson(boolean flag, String user, String bimRequestId, String message) {
        Map<String, Object> schema = new HashMap<String, Object>();
        String mapJson;
        if (flag) {
            if (StringUtils.isNotBlank(user)) {
                schema.put("uid", user);
            }
            schema.put("bimRequestId", bimRequestId);
            schema.put("resultCode", "0");
            schema.put("message", "success");
            mapJson = JSON.toJSONString(schema);

        } else {
            schema.put("bimRequestId", bimRequestId);
            schema.put("resultCode", "500");
            schema.put("message", message);
            mapJson = JSON.toJSONString(schema);
        }
        mapJson = BamboocloudFacade.encrypt(mapJson, key, type);
        return mapJson;
    }
}
