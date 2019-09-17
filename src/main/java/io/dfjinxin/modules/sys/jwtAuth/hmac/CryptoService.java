package io.dfjinxin.modules.sys.jwtAuth.hmac;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by GaoPh on 2019/9/9.
 */
public class CryptoService {
      @Autowired
      private ShiroAuthPro shiroAuthPro;

    /**
     * 生成HMAC摘要
     *
     * @param plaintext 明文
     */
    public String hmacDigest(String plaintext) {
        return hmacDigest(plaintext,this.shiroAuthPro.getHmacSecretKey());
    }

    /**
     * 生成HMAC摘要
     *
     * @param plaintext 明文
     */
    public String hmacDigest(String plaintext,String appKey) {
        return CryptoUtil.hmacDigest(plaintext,appKey,this.shiroAuthPro.getHmacAlg());
    }
}
