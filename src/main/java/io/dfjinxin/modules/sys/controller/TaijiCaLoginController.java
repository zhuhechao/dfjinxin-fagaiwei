//package io.dfjinxin.modules.sys.controller;
//
//import com.google.common.base.Strings;
//import com.google.gson.Gson;
//import io.dfjinxin.common.utils.GovCaTicketManager;
//import io.dfjinxin.common.utils.R;
//import io.dfjinxin.common.utils.ShiroUtils;
//import io.dfjinxin.config.GovRedirectConfig;
//import io.dfjinxin.modules.meta.entity.TnmtEntity;
//import io.dfjinxin.modules.meta.service.TnmtService;
//import io.dfjinxin.modules.sys.dao.TaijiCaLoginDao;
//import io.dfjinxin.modules.sys.entity.GovCaUserForm;
//import io.dfjinxin.modules.sys.entity.SysUserEntity;
//import io.dfjinxin.modules.sys.oauth2.OAuth2Token;
//import io.dfjinxin.modules.sys.service.SysUserTokenService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.apache.shiro.SecurityUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.*;
//import java.nio.charset.Charset;
//import java.util.Map;
//
//import com.bjca.sso.bean.*;
//
//@RestController
//@Api("太极登陆CONTROLLER")
//public class TaijiCaLoginController extends AbstractController{
//
//    @Autowired
//    private SysUserTokenService sysUserTokenService;
//
//    @Autowired
//    private TnmtService tnmtService;
//
//    @Autowired
//    private GovRedirectConfig govRedirectConfig;
//
//    @Autowired
//    private TaijiCaLoginDao taijiCaLoginDao;
//
//    /**
//     * CA认证中心跳转过来的接口
//     * @throws IOException
//     */
//    @PostMapping("cleanse")
//    @ApiOperation("太极登陆接口-数据清洗")
//    public void cleanseRedirect(HttpServletRequest request,HttpServletResponse response) throws IOException {
//        this.doLogin(request,response,"cleanse",govRedirectConfig.getCleanseWebUrl());
//    }
//
//    /**
//     * CA认证中心跳转过来的接口
//     * @throws IOException
//     */
//    @PostMapping("desensitize")
//    @ApiOperation("太极登陆接口-数据脱敏")
//    public void desensitizeRedirect(HttpServletRequest request,HttpServletResponse response) throws IOException {
//        this.doLogin(request,response,"desensitize",govRedirectConfig.getDesensitizeWebUrl());
//    }
//
//    /**
//     * CA认证中心跳转过来的接口
//     * @throws IOException
//     */
//    @PostMapping("tagManager")
//    @ApiOperation("太极登陆接口-标签管理")
//    public void tagManagerRedirect(HttpServletRequest request,HttpServletResponse response) throws IOException {
//        this.doLogin(request,response,"tagManager",govRedirectConfig.getTagManagerWebUrl());
//    }
//
//
//    private void doLogin(HttpServletRequest request,HttpServletResponse response,String redirectType,String redirectBaseUrl) throws IOException {
//        request.setCharacterEncoding("gbk");
//
//        logger.info("{}",request.getParameter("BJCA_TICKET"));
//
//        GovCaUserForm form = new GovCaUserForm();
//        form.setBjcaTicket(request.getParameter("BJCA_TICKET"));
//        form.setBjcaTicketType(request.getParameter("BJCA_TICKET_TYPE"));
//        form.setBjcaTargetUrl(request.getParameter("BJCA_TARGET_URL"));
//        form.setBjcaServerCert(request.getParameter("BJCA_SERVER_CERT"));
//
//        R loginResult = this.login(form, null,request);
//
//        if(loginResult.containsKey("code")&&!"0".equals(String.valueOf(loginResult.get("code")))){
//            logger.info("login failed :{}",loginResult);
//        }else{
//            logger.info("login success :{}",loginResult);
//
//            Object token = loginResult.get("token");
//            String redirectUrl = new StringBuilder(redirectBaseUrl)
//                    .append("?token=").append(token).append("&type=").append(redirectType).toString();
//
//            logger.info("Redirect url ====> {}",redirectUrl);
//            this.doRedirect(redirectUrl,response);
//
//        }
//    }
//
//    private void doRedirect(String redirectUrl,HttpServletResponse response) throws IOException {
//        response.sendRedirect(redirectUrl);
//    }
//
//    @PostMapping(value = {"/ca/login/{mockUserId}","/ca/login"})
//    @ApiOperation("太极ajax登陆接口")
//    public R login(@RequestBody GovCaUserForm form,
//                   @PathVariable(value = "mockUserId",required = false) String mockUserId, HttpServletRequest request)  {
//        if(!Strings.isNullOrEmpty(mockUserId)){
//            return this.mockLogin(mockUserId);
//        }
//
//        logger.info("Validate ca form data==> {}",new Gson().toJson(form));
//
//        GovCaTicketManager ticketmag = new GovCaTicketManager();
//
//        String BJCA_TICKET = form.getBjcaTicket();
//        String BJCA_TICKET_TYPE = form.getBjcaTicketType();
//        String BJCA_SERVER_CERT = form.getBjcaServerCert();
//
//        //验证签名及解密
//        UserTicket userticket = ticketmag.getTicket(BJCA_TICKET, BJCA_TICKET_TYPE, BJCA_SERVER_CERT);
//        //处理票据信息
//        if(userticket != null) {
//            logger.info("CA Ticket informations :{}", new Gson().toJson(userticket));
//
//            //用户姓名
//            String username = userticket.getUserName();//这个是由“BJCA公司” 配置的选项，如果没有值需要告知“BJCA公司”。
//            //用户3	2位唯一标识码
//            String userid = userticket.getUserUniqueID();
//            //用户所在部门的编码
//            String departcd = userticket.getUserDepartCode();
//            //用户所在部门的名称
//            String departname = userticket.getUserDepartName();
//
//            TnmtEntity tnmtEntity = tnmtService.getTnmtByDepart(departcd);
//
//            SysUserEntity sysUserEntity = new SysUserEntity();
//            sysUserEntity.setUserId(userid);
//            sysUserEntity.setUsername(username);
//            sysUserEntity.setTenantId(new Long(tnmtEntity.getTnmtid()));
//
////            taijiCaLoginDao.removeOldUser(userid);
////            taijiCaLoginDao.saveUser(userid,username,sysUserEntity.getTenantId());
//
//            R createResult = sysUserTokenService.createToken(sysUserEntity.getUserId());
//            Object token = createResult.get("token");
//            SecurityUtils.getSubject().login(new OAuth2Token(String.valueOf(token),sysUserEntity));
//            return createResult;
//
//        }else{
//            logger.error("CA认证中心用户查找失败:{}",new Gson().toJson(form));
////            response.sendRedirect("sso_errors.jsp");//这是测试的错误页面。
//            return R.error("CA认证中心用户查找失败");
//        }
//    }
//
//    @Value("${ca.valid}")
//    private boolean caVaid;
//
//    @GetMapping("/ca/logout")
//    @ApiOperation("太极登出接口")
//    public Map<String, Object> logout() {
//
//        if(caVaid){
//            sysUserTokenService.logout(ShiroUtils.getUserEntity().getUserId());
//            SecurityUtils.getSubject().logout();
//        }
//
//        return R.ok();
//    }
//
//    @GetMapping("/ca/getLoginUserInfo")
//    @ApiOperation("太极登陆用户信息")
//    public R getLoginUserInfo(){
//        if(caVaid){
//            String userId = ShiroUtils.getUserEntity().getUserId();
//            String username = ShiroUtils.getUserEntity().getUsername();
//            return R.ok().put("userId",userId).put("username",username);
//        }else{
//            return R.ok().put("userId",1).put("username","mockUser");
//        }
//
//    }
//
//    private R mockLogin(String mockUserId){
//        R createResult = sysUserTokenService.createToken(mockUserId);
//        SysUserEntity sysUserEntity = new SysUserEntity();
//        sysUserEntity.setUserId(mockUserId);
//        sysUserEntity.setUsername("mockUser");
//        sysUserEntity.setTenantId(new Long(mockUserId));
//
//        ShiroUtils.setSessionAttribute("user",sysUserEntity);
//
//        return createResult;
//    }
//
//}
