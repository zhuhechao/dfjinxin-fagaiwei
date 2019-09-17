package io.dfjinxin.modules.sys.jwtAuth.hmac;

import lombok.Data;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by GaoPh on 2019/9/9.
 */
@Data
public class ShiroAuthPro {
    // 默认SESSION超时时间：1小时=3600000毫秒(ms)
    protected static final Integer DEFAULT_SESSION_TIMEOUT = 3600000;
    // 默认SESSION清扫时间：1小时=3600000毫秒(ms)
    protected static final Integer DEFAULT_SESSION_VALIDATION_INTERVAL = 3600000;
    // 记住我默认时间：1天=86400000毫秒(ms)
    protected static final Integer DEFAULT_REMEMBERME_MAX_AGE = 86400000 * 7;
    // 默认HMAC签名有效期：1分钟=60000毫秒(ms)
    protected static final Integer DEFAULT_HMAC_PERIOD = 60000;
    // 默认HASH加密算法
    protected static final String DEFAULT_HASH_ALGORITHM_NAME = "MD5";
    // 默认HASH加密盐
    protected static final String DEFAULT_HASH_SALT = "A1B2C3D4efg.5679g8e7d6c5b4a_-=_)(8.";
    // 默认HASH加密迭代次数
    protected static final Integer DEFAULT_HASH_ITERATIONS = 2;
    // 默认记住我cookie加密秘钥
    protected static final String DEFAULT_REMEMBERME_SECRETKEY = "1a2b5c8e6c9e5g2s";
    // 默认JWT加密算法
    protected static final String DEFAULT_HMAC_ALGORITHM_NAME = "HmacMD5";
    // HASH加密算法
    public static final String HASH_ALGORITHM_NAME_MD5 = "MD5";
    public static final String HASH_ALGORITHM_NAME_SHA1 = "SHA-1";
    public static final String HASH_ALGORITHM_NAME_SHA256 = "SHA-256";
    public static final String HASH_ALGORITHM_NAME_SHA512 = "SHA-512";
    // HMACA签名算法
    public static final String HMAC_ALGORITHM_NAME_MD5 = "HmacMD5";// 128位
    public static final String HMAC_ALGORITHM_NAME_SHA1 = "HmacSHA1";// 126
    public static final String HMAC_ALGORITHM_NAME_SHA256 = "HmacSHA256";// 256
    public static final String HMAC_ALGORITHM_NAME_SHA512 = "HmacSHA512";// 512
    // 缓存名称
    public static final String CACHE_NAME_PASSWORD_RETRY = "shiro-passwordRetryCache";
    public static final String CACHE_NAME_KEEP_ONE_USER = "shiro-keepOneUserCache";
    public static final String CACHE_NAME_AUTHENTICATION = "shiro-authenticationCache";
    public static final String CACHE_NAME_AUTHORIZATION = "shiro-authorizationCache";
    public static final String CACHE_NAME_TOKEN_BURNERS = "shiro-tokenBurnersCache";

    // ATTRIBUTE名称
    public static final String ATTRIBUTE_SESSION_CURRENT_USER = "shiro_current_user";
    public static final String ATTRIBUTE_SESSION_CURRENT_USER_ACCOUNT = "shiro_current_user_account";
    public static final String ATTRIBUTE_SESSION_KICKOUT = "shiro_kickout_attribute";
    public static final String ATTRIBUTE_SESSION_FORCE_LOGOUT = "shiro_force_logout_attribute";
    public static final String ATTRIBUTE_REQUEST_AUTH_MESSAGE = "shiro_auth_message";
    //  PARAM名称
    public static final String PARAM_JCAPTCHA = "jcaptcha";
    public static final String PARAM_HMAC_APP_ID = "hmac_app_id";
    public static final String PARAM_HMAC_TIMESTAMP = "hmac_timestamp";
    public static final String PARAM_HMAC_DIGEST = "hmac_digest";
    public static final String PARAM_JWT = "jwt";



    public static final List<String> DEFAULT_IGNORED = Arrays.asList(
            "/**/favicon.ico"
            ,"/css/**"
            ,"/js/**"
            ,"/images/**"
            ,"/webjars/**"
            ,"/jcaptcha.jpg");

    private boolean jcaptchaEnable = Boolean.FALSE; // 是否启用验证码
    private boolean keepOneEnabled = Boolean.FALSE; // 是否启用账号唯一用户登陆
    private boolean forceLogoutEnable = Boolean.FALSE; // 是否启用强制用户下线
    private boolean authCacheEnabled = Boolean.FALSE;// 是否启用认证授权缓存
    private boolean hmacEnabled = Boolean.FALSE; // 是否启用HMAC鉴权
    private boolean hmacBurnEnabled = Boolean.FALSE; // 是否启用HMAC签名即时失效
    private boolean jwtEnabled = Boolean.FALSE; // 是否启用JWT鉴权
    private boolean jwtBurnEnabled = Boolean.FALSE; // 是否启用JWT令牌即时失效

    private String loginUrl;// 登陆地址
    private String loginSuccessUrl;// 登陆成功地址
    private String unauthorizedUrl;// 无访问权限地址
    private String kickoutUrl;// 被踢出地址
    private String forceLogoutUrl;// 强制退出地址

    private Integer passwdMaxRetries = 0;// 登陆最大重试次数
    private Integer sessionTimeout = DEFAULT_SESSION_TIMEOUT;// session超时时间
    private Integer sessionValidationInterval = DEFAULT_SESSION_VALIDATION_INTERVAL;// session清扫时间

    private Integer remembermeMaxAge = DEFAULT_REMEMBERME_MAX_AGE;// rememberMe时间
    private String remembermeSecretKey = DEFAULT_REMEMBERME_SECRETKEY;// rememberMe秘钥
    private String passwdAlg = DEFAULT_HASH_ALGORITHM_NAME;// 密码算法
    private String passwdSalt = DEFAULT_HASH_SALT;// 密码HASH盐
    private Integer passwdIterations = DEFAULT_HASH_ITERATIONS;// 密码HASH次数

    private String ehcacheConfigFile;// ehcache配置文件

    private String hmacAlg = DEFAULT_HMAC_ALGORITHM_NAME;// HMAC算法
    private String hmacSecretKey;// HMAC秘钥
    private Integer hmacPeriod = DEFAULT_HMAC_PERIOD;// HMAC签名有效时间
    private String jwtSecretKey;// JWT秘钥
    private List<String> filteRules = new LinkedList<String>();// 过滤规则
}
