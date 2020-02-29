package io.dfjinxin.modules.price.controller;

import io.dfjinxin.common.utils.R;
import io.dfjinxin.modules.price.entity.WpAsciiInfoEntity;
import io.dfjinxin.modules.price.service.WpAsciiInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 智慧价格码值信息表Controller
 *
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-08-26 14:00:37
 */
@RestController
@RequestMapping("price/ascii/info")
@Api(tags = "智慧价格码值信息表")
public class WpAsciiInfoController {

    @Autowired
    private WpAsciiInfoService wpAsciiInfoService;

    /**
     * @Desc: 根据码表codeId
     * @Param: [CodeId]
     * @Return: io.dfjinxin.common.utils.R
     * @Author: z.h.c
     * @Date: 2019/11/14 9:31
     */
    @GetMapping("/getAsciiByCode/{CodeId}")
    @ApiOperation("根据码表codeId,查询类型信息")
    public R getInfoByCodeId(@PathVariable("CodeId") String codeId) {

        List<WpAsciiInfoEntity> list = wpAsciiInfoService.getInfoByCodeId(codeId);
        return R.ok().put("data", list);
    }

    /**
     * 查询码表所有数据
     */
    @GetMapping("/getAsciiAll")
    @ApiOperation("查询码表所有数据")
    public R getAscii() {

        List<WpAsciiInfoEntity> list = wpAsciiInfoService.getInfoAll();
        return R.ok().put("data", list);
    }

}
