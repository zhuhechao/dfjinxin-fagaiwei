package io.dfjinxin.modules.price.controller;

import io.dfjinxin.common.dto.PssEwarnConfDto;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.common.validator.ValidatorUtils;
import io.dfjinxin.modules.price.entity.PssEwarnConfEntity;
import io.dfjinxin.modules.price.service.PssEwarnConfService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


/**
 * 价格监测子系统-价格预警-价格预警配置-预警配置Controller
 *
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-08-22 14:55:38
 */

@RestController
@RequestMapping("/price/ewarn/conf")
@Api(tags = "价格监测子系统-预警配置")
public class PssEwarnConfController {

    @Autowired
    private PssEwarnConfService pssEwarnConfService;


    @PostMapping("/queryPageList")
    @ApiOperation("预警配置-查询")
    public R queryPageList(@RequestBody PssEwarnConfDto params) {

        if (params == null) {
            R.error("请求参数为空!");
        }
        params.startNumber();
        PageUtils page = pssEwarnConfService.queryPageList(params);
        return R.ok().put("data", page);
    }


    /**
     * 根据代码简称查询代码类型
     */
    @GetMapping("/getEwarnBycodeSimple/{codeSimple}")
    @ApiOperation("根据代码简称类型,查询预警类型&预警名称")
    public R getEwarnBycodeSimple(@PathVariable("codeSimple") String codeSimple) {

        if (StringUtils.isBlank(codeSimple)) {
            R.error("请求参数为空!");
        }
        List<PssEwarnConfEntity> list = pssEwarnConfService.getWarnTypeAndName(codeSimple);
        return R.ok().put("data", list);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation("预警配置-新增")
    public R save(@RequestBody PssEwarnConfEntity pssEwarnConf) {
        ValidatorUtils.validateEntity(pssEwarnConf);
        pssEwarnConf.setDelFlag("0");
        pssEwarnConf.setCrteDate(new Date());
        pssEwarnConfService.saveOrUpdate(pssEwarnConf);
        return R.ok();
    }

    @PostMapping("/update")
    @ApiOperation("预警配置-修改")
    public R update(@RequestBody PssEwarnConfEntity pssEwarnConf) {
        ValidatorUtils.validateEntity(pssEwarnConf);
        pssEwarnConf.setCrteDate(new Date());
        pssEwarnConfService.updateById(pssEwarnConf);
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @ApiOperation("预警配置-删除")
    public R delete(@RequestBody String ewarnId) {

        if (StringUtils.isBlank(ewarnId)) {
            return R.error("预警ID为空!");
        }
        PssEwarnConfEntity pssEwarnConfEntity = pssEwarnConfService.queryEntityByEwarnId(ewarnId);
        if (pssEwarnConfEntity == null) {
            return R.error("预警ID:" + ewarnId + ",不存在！");
        }
        pssEwarnConfEntity.setDelFlag("1");
        pssEwarnConfService.updateById(pssEwarnConfEntity);
        return R.ok();
    }
}
