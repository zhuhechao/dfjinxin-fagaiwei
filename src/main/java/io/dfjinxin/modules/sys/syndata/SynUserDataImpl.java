//package io.dfjinxin.modules.sys.syndata;
//
//import com.google.common.base.Strings;
//import io.dfjinxin.common.utils.R;
//import io.dfjinxin.modules.sys.entity.*;
//import io.dfjinxin.modules.sys.service.SysDepService;
//import io.dfjinxin.modules.sys.service.SysUserDepService;
//import io.dfjinxin.modules.sys.service.SysUserService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.Map;
//
///**
// * Created by GaoPh on 2019/9/4.
// */
//@Transactional
//@RestController
//@Api(tags = "SynUserDataImpl", description ="发改认证中心进行数据同步")
//public class SynUserDataImpl {
//    @Autowired
//    private SysUserService sysUserService;
//
//    @Autowired
//    private SysDepService sysDepService;
//
//    @Autowired
//    private SysUserDepService sysUserDepService;
//
//    @PostMapping(value = {"/zhjg/synch/syscnUserInfo"})
//    @ApiOperation("同步发改用户数据接口")
//    public R syscnUserInfo(@RequestBody FgUserEntity form,HttpServletRequest request) throws IOException {
//        String userId = form.getId();
//        if(!Strings.isNullOrEmpty(userId)){
//            synuser(form);
//        }else {
//            return R.error("用户Id为空！请重新同步该数据");
//        }
//
//        ArrayList<Map<String,Object>> deps= form.getBelongs();
//        if(deps.size()>0){
//           synUserDep(form);
//       }
//
//        return R.ok();
//    }
//
//    @PostMapping(value = {"/zhjg/synch/syscnDepInfo"})
//    @ApiOperation("同步发改组织机构数据接口")
//    public R syscnDepInfo(@RequestBody FgDepEntity form, HttpServletRequest request) throws IOException {
//
//        String depId = form.getOrganizationUuid();
//        SysDepEntity sysDepEntity = null;
//        if(!Strings.isNullOrEmpty(depId)){
//            sysDepEntity=  sysDepService.getById(depId);
//        }else {
//            return R.error("用户Id为空！请重新同步该数据");
//        }
//
//        if(sysDepEntity !=null){
//            sysDepService.removeById(depId);
//        }
//        sychDep(form);
//
//
//        return R.ok();
//    }
//
//    private void synuser(FgUserEntity form){
//        Timestamp timestamp = getTime();
//        SysUserEntity sysUserEntity = new SysUserEntity();
//        String requestType = form.getRequestType();
//        String userId = form.getId();
//        String userName = form.getUserName();
//        String userRelName = form.getDisplayName();
//        String password = form.getPassword();
//        ArrayList<Map<String, Object>> emails = form.getEmails();
//        String email = null;
//        for(Map<String, Object> map:emails){
//            String value= (String) map.get("value");
//            email+= value;
//        }
//        ArrayList<Map<String, Object>> phones = form.getPhoneNumbers();
//        String phone = null;
//        for(Map<String, Object> map:phones){
//            String value= (String) map.get("value");
//            phone= value+";"+phone;
//        }
//        if(!Strings.isNullOrEmpty(password)){
//            sysUserEntity.setUserPass(password);
//        }else {
//            sysUserEntity.setUserPass("123456");
//        }
//
//        if(!Strings.isNullOrEmpty(email)){
//            sysUserEntity.setEmail(email);
//        }else {
//            sysUserEntity.setEmail("12345678901@163.com");
//        }
//
//        if(!Strings.isNullOrEmpty(userId)){
//            sysUserEntity.setUserId(userId);
//        }else {
//            sysUserEntity.setUserId(null);
//        }
//        if(!Strings.isNullOrEmpty(phone)){
//            sysUserEntity.setMblPhoneNo(phone);
//        }else {
//            sysUserEntity.setMblPhoneNo(null);
//        }
//
//        if(!Strings.isNullOrEmpty(userRelName)){
//            sysUserEntity.setUserRealName(userRelName);
//        }else {
//            sysUserEntity.setUserRealName(null);
//        }
//
//        if(!Strings.isNullOrEmpty(userName)){
//            sysUserEntity.setUserName(userName);
//        }else {
//            sysUserEntity.setUserName(null);
//        }
//
//        sysUserEntity.setUserStatus(1);
//
//        if(requestType.equals("UPDATE")){
//            sysUserEntity.setUpdDate(timestamp);
//            sysUserService.update(sysUserEntity);
//
//        }else if(requestType.equals("DELETE")){
//            deleteUserInfo(form);
//        }else {
//            sysUserService.removeById(userId);
//            sysUserService.saveUser(sysUserEntity);
//        }
//
//    }
//
//    private void sychDep(FgDepEntity form){
//        ArrayList<SysDepEntity> list = new ArrayList<>();
//        ArrayList<String> depIds = new ArrayList<>();
//        Timestamp timestamp = getTime();
//        SysDepEntity sysDepEntity = null;
//        ArrayList<String> children= form.getChildrenOuUuid();
//        String id= form.getOrganizationUuid() ;
//        String pid = form.getParentUuid();
//        String depName = form.getOrganization();
//        String type = form.getRequestType();
//        int depState = 1;
//        sysDepEntity = new SysDepEntity();
//        sysDepEntity.setDepId(id);
//        sysDepEntity.setSuperDepId(pid);
//        sysDepEntity.setDepName(depName);
//        sysDepEntity.setDepState(depState);
//        sysDepEntity.setCreDate(timestamp);
//        sysDepEntity.setUpdDate(timestamp);
//        list.add(sysDepEntity);
//        depIds.add(id);
////        if(children.size() >0) {
////            for (String data : children) {
////                sysDepEntity = new SysDepEntity();
////                String depId = data;
////                sysDepEntity.setDepId(depId);
////                sysDepEntity.setSuperDepId(id);
////                sysDepEntity.setDepState(depState);
////                sysDepEntity.setCreDate(timestamp);
////                sysDepEntity.setUpdDate(timestamp);
////                list.add(sysDepEntity);
////                depIds.add(depId);
////            }
////        }
//        if(type.equals("UPDATE")){
//            sysDepService.updateBatchById(list);
//        }else if(type.equals("DELETE")){
//            sysDepService.removeByIds(depIds);
//        }else {
//            sysDepService.addDeps(list);
//        }
//
//    }
//
//    private void synUserDep(FgUserEntity form){
//        SysUserDepEntity sysUserDepEntity = null;
//        ArrayList<SysUserDepEntity> list = new ArrayList<>();
//        String userId = form.getId();
//        String type= form.getRequestType();
//       ArrayList<Map<String,Object>> belongs= form.getBelongs();
//        sysUserDepService.deleteAll(userId);
//       if(!type.equals("DELETE")){
//           for(Map<String,Object> map:belongs){
//               sysUserDepEntity = new SysUserDepEntity();
//               String depId= (String) map.get("belongOuUuid");
//               sysUserDepEntity.setUserId(userId);
//               sysUserDepEntity.setDepId(depId);
//               list.add(sysUserDepEntity);
//           }
//
//           sysUserDepService.saveDep(list);
//       }
//    }
//
//     private void deleteUserInfo(FgUserEntity form){
//        String userId = form.getId();
//        sysUserService.removeById(userId);
//     }
//
//
//    private Timestamp getTime(){
//     Date now = new Date();
//     Timestamp timestamp = new Timestamp(now.getTime());
//     return timestamp;
//    }
//
//
//}
//
