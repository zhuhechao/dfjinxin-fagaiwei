package io.dfjinxin.modules.sys.controller;

import com.google.common.base.Strings;
import com.idsmanager.dingdang.jwt.DingdangUserRetriever;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.common.utils.ShiroUtils;
import io.dfjinxin.config.GovRedirectConfig;
import io.dfjinxin.modules.sys.entity.SysRoleEntity;
import io.dfjinxin.modules.sys.entity.SysUserEntity;
import io.dfjinxin.modules.sys.entity.SysUserTokenEntity;
import io.dfjinxin.modules.sys.jwtAuth.hmac.CryptoUtil;
import io.dfjinxin.modules.sys.jwtAuth.hmac.GenerateToken;
import io.dfjinxin.modules.sys.jwtAuth.jwt.SubjectInfo;
import io.dfjinxin.modules.sys.oauth2.OAuth2Token;
import io.dfjinxin.modules.sys.service.ShiroService;
import io.dfjinxin.modules.sys.service.SysRoleService;
import io.dfjinxin.modules.sys.service.SysUserService;
import io.dfjinxin.modules.sys.service.SysUserTokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("zhjg")
@Api(tags = "SmartPriceLoginController", description = "发改登录")
public class SmartPriceLoginController extends AbstractController {

    @Autowired
    private SysUserTokenService sysUserTokenService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private ShiroService shiroService;



    @GetMapping(value = {"/login", "/login/{userName}"})
    @ApiOperation("发改登陆接口")
    public R Login(@RequestParam(value = "id_token", required = false) String id_token, @PathVariable(value = "userName", required = false) String userName) {
        System.out.println(id_token);
        if (Strings.isNullOrEmpty(id_token)) {
            SysUserEntity sysUserEntity = sysUserService.queryByUserName(userName);
            if (sysUserEntity == null) {
                return R.error("该用户不存在！");
            } else {
                String userId = sysUserEntity.getUserId();
                return this.mockLogin(userId);
            }
        }
        logger.info("Validate ca form data==> {}", id_token);
        String publicKey = "{\"kty\":\"RSA\",\"kid\":\"7835635588759989719\",\"alg\":\"RS256\",\"n\":\"nDSjqFpp1JOt15SW7r12kOY0ah5-yay_q9JTIqEfBYT4hzuUTegQVaNri7SfprmMG66K_PFCAn1Sei7CQx6Q4kuDVmUr2aFW2_LFSUvg-_hxOmOGrACJvuQ_s1ElNlYlfRGDcc9ZvHhlhE0QSHytOKekqUfJuXz-rwhuzMMBLD0NbOkzIG3zjUgbNI0rUz421fKExhV_Jm4OzM7BkOxZ1TQTP4zC28wbt8kOCoJPOsr8VPZBPe0z9wQKz5iy6WzG0quOQrGfiTvVIVFdgVyu4_r33XtTDUqGRdRrllLP8W7_40RN8tCoJ4mKhkvuJPt28b8Zny9rAI7BHw6uS0mDuw\",\"e\":\"AQAB\"}";
        //验证签名及解密
        DingdangUserRetriever retriever = new DingdangUserRetriever(id_token, publicKey);
        //用户信息
        DingdangUserRetriever.User user = null;
        //处理票据信息
        if (retriever != null) {
            logger.info("FG id_token informations :{}", retriever);
            //用户姓名
            try {
                user = retriever.retrieve();
            } catch (Exception e) {
                logger.error("发改认证中心用户信息获取失败:{}", e);
                return R.error("发改认证中心用户信息获取失败");
            }
            //判断自己系统中是否存在该用户
            String uid = user.getSub();
            System.out.println("获取发改用户信息"+uid+"+++++++++"+ user.getName()+"+++"+user.getUsername()+"++++++++");
            SysUserEntity sysUserEntity = sysUserService.queryByUserName(uid);
            if (sysUserEntity != null) {
           List<Map<String,Object>> menus= sysUserService.getUserPerm(sysUserEntity.getUserId());
                R    createResult = sysUserTokenService.createToken(sysUserEntity.getUserId());
                Object token = createResult.get("token");
                SecurityUtils.getSubject().login(new OAuth2Token(String.valueOf(token), sysUserEntity));
                createResult.put("menu",menus);
                return createResult;
            } else {
                logger.error("智慧价格系统不存在该用户:{}", retriever);
                return R.error("智慧价格系统用户查找失败");
            }

        } else {
            logger.error("发改认证中心用户查找失败:{}", retriever);
            return R.error("发改认证中心用户查找失败");
        }
    }

    @Value("${ca.valid}")
    private boolean caVaid;

    @GetMapping("/ca/logout")
    @ApiOperation("发改登出接口")
    public Map<String, Object> logout() {

        if (caVaid) {
            sysUserTokenService.logout(ShiroUtils.getUserEntity().getUserId());
            SecurityUtils.getSubject().logout();
        }
        return R.ok();
    }

    @GetMapping("/goToService")
    @ApiOperation("集成系统跳转")
    public R goToService(@RequestParam(value = "urlParm") String urlParm,HttpServletRequest request,HttpServletResponse response) throws IOException {
        GenerateToken generateToken = new GenerateToken();
        String user = request.getHeader("token");

       // 如果header中不存在token，则从参数中获取token
        if(StringUtils.isBlank(user)){
            user = request.getParameter("token");
        }
       // String user= "79362e48e37283a7cdea0825e2614375";
        String token = null;
        String sep = null;
        if (!Strings.isNullOrEmpty(urlParm)) {
            try {
                sep = (new URL(urlParm)).getQuery() == null ? "?" : "&";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            token = (String) generateToken.applyToken(user).get("jwt");
        }

        String url = urlParm.concat(sep + "very_token=" + token);
        return R.ok().put("url",url);

    }

    @GetMapping(value = "/verifyToken")
    @ApiOperation("集成系统认证")
    public R sysVerify(@RequestParam(value = "very_token") String very_token) {

        SubjectInfo subjectInfo = CryptoUtil.parseJwt(very_token);
        if (subjectInfo == null || subjectInfo.getExpiration().getTime() < System.currentTimeMillis()) {
            return R.error("认证失败！");
        }
        String user = subjectInfo.getUserInfo();
        SysUserTokenEntity sysUserEntity = shiroService.queryByToken(user);
        Map map = new HashMap();
        if (sysUserEntity == null) {
            return R.error(1,"该用户不存在，认证失败！");
        }
        map.put("code", 0);
        return R.ok(map);

    }


    private R mockLogin(String mockUserId) {
        R createResult = sysUserTokenService.createToken(mockUserId);
        SysRoleEntity sysRoleEntity = sysRoleService.getById(mockUserId);
        String roleName = sysRoleEntity.getRoleName();
        createResult.put("roleName", roleName);
        SysUserEntity userEntity = sysUserService.getById(mockUserId);
        String userName = userEntity.getUserName();
        createResult.put("userName", userName);
        SysUserEntity sysUserEntity = new SysUserEntity();
        sysUserEntity.setUserId(mockUserId);
        sysUserEntity.setUserName("mockUser");
        ShiroUtils.setSessionAttribute("user", sysUserEntity);
        return createResult;
    }

}
