package io.dfjinxin.modules.sys.entity;

/**
 * @创建人 PanHu.Gao
 * @创建时间 2020/10/19
 * @描述
 */
public enum UrlEnum {
    GET_TOKEN_URL("/oauth2/getToken"),
    CHECK_TOKEN_URL("/oauth2/checkTokenValid"),
    REFRESH_TOKEN_URL("/oauth2/refreshToken"),
    GET_USER_INFO("/oauth2/getUserInfo"),
    LOGOUT_TOKEN_URL("/profile/OAUTH2/Redirect/GLO");
    private String url;

    private UrlEnum(String url) {
        this.url = url;
    }

    public String getUrl() {
        return this.url;
    }
}
