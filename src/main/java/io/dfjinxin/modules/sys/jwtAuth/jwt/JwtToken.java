package io.dfjinxin.modules.sys.jwtAuth.jwt;

import lombok.Data;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * Created by GaoPh on 2019/9/9.
 */
@Data
public class JwtToken implements AuthenticationToken {
    private static final long serialVersionUID = -7904916553001235366L;

    private String jwt;
    private String host;

    public JwtToken(String jwt,String host){
        this.jwt = jwt;
        this.host = host;
    }
    @Override
    public Object getPrincipal() {
        return this.jwt;
    }

    @Override
    public Object getCredentials() {
        return Boolean.TRUE;
    }
}
