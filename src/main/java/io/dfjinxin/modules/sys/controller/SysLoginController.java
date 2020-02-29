/**
 * 2019 东方金信
 *
 *
 *
 *
 */

package io.dfjinxin.modules.sys.controller;

import io.dfjinxin.common.utils.R;
import io.dfjinxin.modules.sys.form.SysLoginForm;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 登录相关
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
public class SysLoginController {

    /**
     * 登录
     */
    @PostMapping("/sys/login")
    public Map<String, Object> login(@RequestBody SysLoginForm form, HttpServletRequest request) {

        request.getSession().invalidate();
        request.getSession().setAttribute("UserId", form.getUsername());
        request.getSession().setAttribute("TenementId", form.getPassword());

        return R.ok();
    }

}
