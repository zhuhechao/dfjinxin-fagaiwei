package io.dfjinxin.modules.sys.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.banboocloud.Codec.BamboocloudFacade;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.common.utils.httpClient.HttpSendMessage;
import io.dfjinxin.modules.sys.bimUtils.BamboocloudUtils;
import io.dfjinxin.modules.sys.entity.*;
import io.dfjinxin.modules.sys.service.SysDepService;
import io.dfjinxin.modules.sys.service.SysUserDepService;
import io.dfjinxin.modules.sys.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static Logger log = LoggerFactory.getLogger(BimUserOrgController.class);

    @PostMapping("/UserCreateService")
    @ApiOperation("新增用户")
    public void createUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String bodyParam = BamboocloudUtils.getRequestBody(request);
        log.info("接收到新增用户原始参数:{}", bodyParam);
        bodyParam = BamboocloudUtils.getPlaintext(bodyParam, key, type);
        Map<String, Object> reqmap = (Map<String, Object>) JSON.parse(bodyParam);
        if (BamboocloudUtils.verify(reqmap, authType).booleanValue()) {
            log.info("接收到新增用户参数:{}", reqmap);
            String orgId = (String) reqmap.get("depId");
            SysUserEntity user = new SysUserEntity();
            user.setCrteDate(new Date());
            user.setDepId(orgId);
            SysDepEntity sysDepEntity = sysDepService.getDepId(orgId);
            user.setDepName(Objects.isNull(sysDepEntity) ? "" : sysDepEntity.getDepName());
            String userId1 = (String) reqmap.get("userId");
            String userId = StringUtils.isNotBlank(userId1) ? userId1 : Long.toString(System.currentTimeMillis());
            user.setUserId(userId);
            user.setError_no(0);
            user.setUserPass((String) reqmap.get("userPass"));
            user.setUserName((String) reqmap.get("userName"));
            user.setUserRealName((String) reqmap.get("fullName"));
            user.setUserStatus(1);
            user.setStatus((Boolean) reqmap.get("userStatus"));
            boolean saveUser = sysUserService.saveOrUpdate(user);
            log.info("保存库表中的用户参数:{},结果:{}", user, saveUser);
            SysUserDepEntity depEntity = new SysUserDepEntity();
            depEntity.setDepId(orgId);
            depEntity.setUserId(userId);
            boolean saveDept = sysUserDepService.save(depEntity);
            log.info("保存用户机构中间表中的参数:{},结果:{}", depEntity, saveDept);
            PrintWriter out = response.getWriter();
            out.write(backJson(saveUser, userId, (String) reqmap.get("bimRequestId"), "账号创建失败，报错信息自定义"));
            out.close();
        }
    }

    @PostMapping("/UserUpdateService")
    @ApiOperation("修改用户")
    public void updateUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String bodyParam = BamboocloudUtils.getRequestBody(request);
        log.info("接收到修改用户原始参数:{}", bodyParam);
        bodyParam = BamboocloudUtils.getPlaintext(bodyParam, key, type);
        Map<String, Object> reqmap = (Map<String, Object>) JSON.parse(bodyParam);
        if (BamboocloudUtils.verify(reqmap, authType).booleanValue()) {
            log.info("接收到修改用户参数:{}", reqmap);
            String userId = (String) reqmap.get("bimUid");
            String orgId = (String) reqmap.get("depId");
            SysUserEntity user = sysUserService.lambdaQuery().eq(SysUserEntity::getUserId, userId).one();
            user.setUserPass((String) reqmap.get("userPass"));
            user.setUserName((String) reqmap.get("userName"));
            user.setUserRealName((String) reqmap.get("fullName"));
            if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(orgId)) {
                sysUserDepService.deleteAll(userId);
                SysUserDepEntity depEntity = new SysUserDepEntity();
                depEntity.setDepId(orgId);
                depEntity.setUserId(userId);
                boolean saveDept = sysUserDepService.save(depEntity);
                log.info("保存用户机构中间表中的参数:{},结果:{}", depEntity, saveDept);
            }
            Boolean enable = (Boolean) reqmap.get("__ENABLE__");
            if (!Objects.isNull(enable) && enable) {
                boolean status = (boolean) reqmap.get("__ENABLE__");
                if (status) {
                    user.setUserStatus(1);
                } else {
                    user.setUserStatus(0);
                }
            }
            boolean updateUser = sysUserService.updateById(user);
            log.info("修改库表中的用户参数:{},结果:{}", user, updateUser);
            PrintWriter out = response.getWriter();
            out.write(backJson(updateUser, null, (String) reqmap.get("bimRequestId"), "账号更新失败，报错信息自定义"));
            out.close();
        }
    }

    @PostMapping("/UserDeleteService")
    @ApiOperation("删除用户")
    public void deleteUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String bodyParam = BamboocloudUtils.getRequestBody(request);
        log.info("接收到删除用户原始参数:{}", bodyParam);
        bodyParam = BamboocloudUtils.getPlaintext(bodyParam, key, type);
        Map<String, Object> reqmap = (Map<String, Object>) JSON.parse(bodyParam);
        if (BamboocloudUtils.verify(reqmap, authType).booleanValue()) {
            log.info("接收到删除用户参数:{}", reqmap);
            String userId = (String) reqmap.get("bimUid");
            SysUserEntity user = sysUserService.lambdaQuery().eq(SysUserEntity::getUserId, userId).one();
            user.setUserStatus(0);
            boolean deleteUser = sysUserService.updateById(user);
            log.info("删除库表中的用户参数:{},结果:{}", user, deleteUser);
            if (StringUtils.isNotBlank(userId)) {
                sysUserDepService.deleteAll(userId);
            }
            PrintWriter out = response.getWriter();
            out.write(backJson(deleteUser, null, (String) reqmap.get("bimRequestId"), "账号删除失败，报错信息自定义"));
            out.close();
        }
    }

    @PostMapping("/OrgCreateService")
    @ApiOperation("新增组织机构")
    public void createOrg(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String bodyParam = BamboocloudUtils.getRequestBody(request);
        log.info("接收到新增组织原始参数:{}", bodyParam);
        bodyParam = BamboocloudUtils.getPlaintext(bodyParam, key, type);
        Map<String, Object> reqmap = (Map<String, Object>) JSON.parse(bodyParam);
        if (BamboocloudUtils.verify(reqmap, authType).booleanValue()) {
            log.info("接收到新增组织机构参数:{}", reqmap);
            String parOrgId = (String) reqmap.get("superDepId");
            String depName = (String) reqmap.get("depName");
            String depId = (String) reqmap.get("depId");
            boolean status = (boolean) reqmap.get("status");
            SysDepEntity org = new SysDepEntity();
            org.setDepId(StringUtils.isNotBlank(depId) ? depId : Long.toString(System.currentTimeMillis()));
            org.setDepName(depName);
            if (StringUtils.isNotBlank(parOrgId)) {
                org.setSuperDepId(parOrgId);
            }
            org.setStatus(status);
            boolean saveOrg = sysDepService.saveOrUpdate(org);
            log.info("保存机构中的机构参数:{},结果:{}", org, saveOrg);
            PrintWriter out = response.getWriter();
            out.write(backJson(saveOrg, org.getDepId(), (String) reqmap.get("bimRequestId"), "机构创建失败"));
            out.close();
        }
    }

    @PostMapping("/OrgUpdateService")
    @ApiOperation("修改组织机构")
    public void updateOrg(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String bodyParam = BamboocloudUtils.getRequestBody(request);
        log.info("接收到修改组织原始参数:{}", bodyParam);
        bodyParam = BamboocloudUtils.getPlaintext(bodyParam, key, type);
        Map<String, Object> reqmap = (Map<String, Object>) JSON.parse(bodyParam);

        if (BamboocloudUtils.verify(reqmap, authType).booleanValue()) {
            log.info("接收到修改组织机构参数:{}", reqmap);
            SysDepEntity org = sysDepService.getDepId((String) reqmap.get("bimOrgId"));
            String superDepId = (String) reqmap.get("superDepId");
            org.setSuperDepId(superDepId);
            if (StringUtils.isNotBlank(superDepId)) {
                SysDepEntity parOrg = sysDepService.getDepId((String) reqmap.get("superDepId"));
                org.setSuperDepName(Objects.isNull(parOrg) ? "" : parOrg.getDepName());
            }
            org.setDepName((String) reqmap.get("depName"));
            Boolean enable = (Boolean) reqmap.get("__ENABLE__");
            if (!Objects.isNull(enable) && enable) {
                boolean status = (boolean) reqmap.get("__ENABLE__");
                if (status) {
                    org.setStatus(true);
                } else {
                    org.setStatus(false);
                }
            }
            boolean updateUser = sysDepService.updateById(org);
            log.info("修改机构中的机构参数:{},结果:{}", org, updateUser);
            PrintWriter out = response.getWriter();
            out.write(backJson(updateUser, null, (String) reqmap.get("bimRequestId"), "组织更新失败，报错信息自定义"));
            out.close();
        }
    }

    @PostMapping("/OrgDeleteService")
    @ApiOperation("删除组织机构")
    public void deleteOrg(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String bodyParam = BamboocloudUtils.getRequestBody(request);
        log.info("接收到删除组织原始参数:{}", bodyParam);
        bodyParam = BamboocloudUtils.getPlaintext(bodyParam, key, type);
        Map<String, Object> reqmap = (Map<String, Object>) JSON.parse(bodyParam);
        if (BamboocloudUtils.verify(reqmap, authType).booleanValue()) {
            log.info("接收到删除组织机构参数:{}", reqmap);
            SysDepEntity sysDepEntity = sysDepService.getDepId((String) reqmap.get("bimOrgId"));
            sysDepEntity.setStatus(false);
            boolean deleteDept = sysDepService.updateById(sysDepEntity);
            log.info("修改机构中的机构参数:{},结果:{}", sysDepEntity, deleteDept);
            PrintWriter out = response.getWriter();
            out.write(backJson(deleteDept, null, (String) reqmap.get("bimRequestId"), "组织删除失败，报错信息自定义"));
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

    @PostMapping("/SchemaService")
    @ApiOperation("schemaService")
    public void schemaService(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String bodyparam = BamboocloudUtils.getRequestBody(request);
        log.info("request---原始数据-->:{}", bodyparam);
        //报文解密
        bodyparam = BamboocloudUtils.getPlaintext(bodyparam, key, type);
        Map<String, Object> reqmap = (Map<String, Object>) JSON.parse(bodyparam);
        if (BamboocloudUtils.verify(reqmap, authType).booleanValue()) {
            log.info("response---param-->:{}", reqmap);
            Map<String, Object> schema = new HashMap<>();
            List<AttributeEntity> attrList = new ArrayList<>();
            AttributeEntity attr1 = new AttributeEntity();
            attr1.setType("String");
            attr1.setName("userId");
            attr1.setRequired(true);
            attr1.setMultivalued(false);
            attrList.add(attr1);
            AttributeEntity attr2 = new AttributeEntity();
            attr2.setType("String");
            attr2.setName("depId");
            attr2.setRequired(true);
            attr2.setMultivalued(false);
            attrList.add(attr2);
            AttributeEntity attr3 = new AttributeEntity();
            attr3.setType("String");
            attr3.setName("userName");
            attr3.setRequired(true);
            attr3.setMultivalued(false);
            attrList.add(attr3);
            AttributeEntity attr4 = new AttributeEntity();
            attr4.setType("boolean");
            attr4.setName("userStatus");
            attr4.setRequired(true);
            attr4.setMultivalued(false);
            attrList.add(attr4);
            AttributeEntity attr5 = new AttributeEntity();
            attr5.setType("String");
            attr5.setName("userPass");
            attr5.setRequired(true);
            attr5.setMultivalued(false);
            attrList.add(attr5);
            List<AttributeEntity> orgattrlist = new ArrayList<AttributeEntity>();
            AttributeEntity org1 = new AttributeEntity();
            org1.setType("String");
            org1.setName("depId");
            org1.setRequired(true);
            org1.setMultivalued(false);
            orgattrlist.add(org1);
            AttributeEntity org2 = new AttributeEntity();
            org2.setType("String");
            org2.setName("superDepId");
            org2.setRequired(true);
            org2.setMultivalued(false);
            orgattrlist.add(org2);
            AttributeEntity org3 = new AttributeEntity();
            org3.setType("String");
            org3.setName("depName");
            org3.setRequired(true);
            org3.setMultivalued(false);
            orgattrlist.add(org3);
            AttributeEntity org4 = new AttributeEntity();
            org4.setType("boolean");
            org4.setName("status");
            org4.setRequired(true);
            org4.setMultivalued(false);
            orgattrlist.add(org4);

            schema.put("account", attrList);
            schema.put("organization", orgattrlist);
            schema.put("bimRequestId", reqmap.get("bimRequestId"));
            String mapJson = JSON.toJSONString(schema);
            log.info("response---json-->" + mapJson);
            mapJson = BamboocloudFacade.encrypt(mapJson, key, type);
            PrintWriter out = response.getWriter();
            out.write(mapJson);
            out.close();
        }
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
