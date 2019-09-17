package io.dfjinxin.modules.sys.jwtAuth.hmac;

import io.dfjinxin.modules.sys.jwtAuth.jwt.SubjectInfo;
import io.jsonwebtoken.*;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.util.Date;
import java.util.UUID;

/**
 * Created by GaoPh on 2019/9/9.
 */
public class CryptoUtil {


    /**
     * 生成HMAC摘要
     *
     * @param plaintext 明文
     * @param secretKey 安全秘钥
     * @param algName 算法名称
     * @return 摘要
     */
    public static String hmacDigest(String plaintext,String secretKey,String algName) {
        try {
            Mac mac = Mac.getInstance(algName);
            byte[] secretByte = secretKey.getBytes();
            byte[] dataBytes = plaintext.getBytes();
            SecretKey secret = new SecretKeySpec(secretByte,algName);
            mac.init(secret);
            byte[] doFinal = mac.doFinal(dataBytes);
            return byte2HexStr(doFinal);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 字节数组转字符串
     *
     * @param bytes 字节数组
     * @return 字符串
     */
    private static String byte2HexStr(byte[] bytes) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; bytes!=null && n < bytes.length; n++) {
            stmp = Integer.toHexString(bytes[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toUpperCase();
    }

    /**
     * 验签JWT
     *
     * @param jwt json web token
     */
    public static SubjectInfo parseJwt(String jwt) {
        return parseJwt(jwt,"*)=+5e7kluahjsxrzrb03819qvgyf");
    }

    /**
     * 验签JWT
     *
     * @param jwt json web token
     */
    public static SubjectInfo parseJwt(String jwt, String appKey) {
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(appKey))
                .parseClaimsJws(jwt)
                .getBody();
        SubjectInfo statelessAccount = new SubjectInfo();
        statelessAccount.setTokenId(claims.getId());// 令牌ID
        statelessAccount.setIssuser(claims.getIssuer());// 签发者
        statelessAccount.setIssuedAt(claims.getIssuedAt());// 签发时间
        statelessAccount.setExpiration(claims.getExpiration()); //过期时间
        statelessAccount.setAudience(claims.getAudience());// 接收方
        statelessAccount.setUserInfo(claims.get("userId", String.class));// 用户信息
        return statelessAccount;
    }
}
