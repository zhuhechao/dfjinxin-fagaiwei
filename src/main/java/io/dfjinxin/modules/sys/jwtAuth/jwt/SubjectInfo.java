package io.dfjinxin.modules.sys.jwtAuth.jwt;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by GaoPh on 2019/9/9.
 */
@Data
public class SubjectInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String tokenId;
    private String appId;
    private String issuser;
    private Date issuedAt;
    private String audience;
    private String userInfo;
    private String host;
    private Date expiration;
}
