package io.dfjinxin.modules.price.controller;

import io.dfjinxin.common.utils.R;
import io.dfjinxin.modules.price.dto.PssBigdataFormInfoDto;
import io.dfjinxin.modules.price.service.PssBigdataFormInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @author bourne
 * @email kuibobo@gmail.com
 * @date 2019-09-03 17:15:22
 */
@RestController
@RequestMapping("price/pssbigdataforminfo")
@Api(tags = "数据结构表")
public class PssBigdataFormInfoController {
    @Autowired
    private PssBigdataFormInfoService pssBigdataFormInfoService;

    /**
     * 列表
     */
    @GetMapping("/listAll")
    @RequiresPermissions("price:pssbigdataforminfo:listall")
    @ApiOperation("返回所有表")
    public R listAll() {
        List<PssBigdataFormInfoDto> list = pssBigdataFormInfoService.listAll();

        return R.ok().put("list", list);
    }
}
