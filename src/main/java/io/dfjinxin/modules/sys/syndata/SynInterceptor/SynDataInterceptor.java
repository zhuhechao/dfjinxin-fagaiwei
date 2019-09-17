package io.dfjinxin.modules.sys.syndata.SynInterceptor;

import com.google.common.base.Strings;
import io.dfjinxin.modules.sys.dao.AuthorizeInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Created by GaoPh on 2019/9/5.
 */
@Component
public class SynDataInterceptor implements HandlerInterceptor {
    @Autowired
    private AuthorizeInfoDao authorizeInfoDao;

    @Override
    public boolean preHandle(HttpServletRequest request,HttpServletResponse response,Object hanler){
        String name=  request.getHeader("Authorization");
        String param = name.substring(6);
        BASE64Decoder decoder = new BASE64Decoder();
        String up = null;
        try {
            up = new String(decoder.decodeBuffer(param));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String user = up.split(":")[0];
        String result=  authorizeInfoDao.getUserInfo(user);
        if(Strings.isNullOrEmpty(result)){
            return  false;
        }
        return true;
    }

}
