package io.dfjinxin.modules.price.controller;

import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.modules.price.service.WpCommPriOrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @ClassName WpCommPriOrgController
 * @Author：lym 863968235@qq.com
 * @Date： 2019/12/10 16:50
 * 修改备注：
 */
@RestController
@RequestMapping("wp/commpri/org")
public class WpCommPriOrgController {


    @Autowired
    private WpCommPriOrgService wpCommPriOrgService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wpCommPriOrgService.queryPage(params);

        return R.ok().put("page", page);
    }


}
