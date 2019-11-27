package io.dfjinxin.modules.sys.jwtAuth.hmac;

import io.dfjinxin.common.utils.MD5Utils;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.modules.sys.entity.SysUserTokenEntity;
import io.dfjinxin.modules.sys.service.ShiroService;
import io.dfjinxin.modules.sys.service.SysUserTokenService;
import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.bind.DatatypeConverter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by GaoPh on 2019/9/9.
 */
public class GenerateToken {

    private final String SECRET_KEY = "*)=+5e7kluahjsxrzrb03819qvgyf";
    private final long ept = 600000l;

    @Autowired
    private CryptoService cryptoService;

    public Map<String, Object> applyToken(String client,long expira) {
        String jwt = genJwt(UUID.randomUUID().toString(), client, "gold-price", expira);
        Map<String, Object> res = new HashMap<>();
        res.put("jwt", jwt);
        return res;
    }

    /**
     * @param id      令牌ID
     * @param subject 用户ID
     * @param iuser   签发人
     * @param pt      有效时间(毫秒)
     * @return json web token
     */

    private String genJwt(String id, String subject, String iuser, long pt) {
        long currentTm = System.currentTimeMillis();
        SignatureAlgorithm algorithm = SignatureAlgorithm.HS256;
        byte[] secretKey = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Map<String, Object> claims = new HashMap<>();
        String subject2 = MD5Utils.getMD5(subject);
        claims.put("userId", subject2);
        JwtBuilder jwt = Jwts.builder();
        if (Strings.isNotBlank(id)) jwt.setId(id);
        if (Strings.isNotBlank(iuser)) jwt.setIssuer(iuser);
        jwt.setClaims(claims);
        jwt.setIssuedAt(new Date(currentTm));
        Date expira = new Date(pt);
        jwt.setExpiration(expira);
        jwt.compressWith(CompressionCodecs.DEFLATE);
        jwt.signWith(algorithm, secretKey);
        return jwt.compact();
    }

}
