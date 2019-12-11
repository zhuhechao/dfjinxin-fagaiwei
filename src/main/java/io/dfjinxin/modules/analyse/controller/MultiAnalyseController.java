package io.dfjinxin.modules.analyse.controller;

import io.dfjinxin.common.utils.R;
import io.dfjinxin.modules.analyse.service.WpBaseIndexValService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


/**
 * 价格监测子系统-价格分析-多维分析
 *
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-09-10 15:28:19
 */
@RestController
@RequestMapping("analyse/multidimensional")
@Api(tags = "多维分析")
public class MultiAnalyseController {

    @Autowired
    private WpBaseIndexValService wpBaseIndexValService;

    /**
     * @Desc: 新的多维分析 4类商品指标信息分析
     * 1、统计该3类商品下所有4类商品的指标类型为价格的所有最新价格及上一天的价格作涨辐
     * 计算条件：指标类型为价格、区域为全国
     * 2、统计该3类商品下所有4类商品的指标类型为价格最近一个月的价格值作动态图
     * 计算条件：指标类型为价格、区域为全国、最近一月
     * 3、计算地图
     * 计算条件：指标类型为价格、区域为省的
     * 4、统计该3类商品下所有4类商品的指标类型为非价格的所有最新价格及上一天的价格作涨辐
     * @Param: [commId]
     * @Return: io.dfjinxin.common.utils.R
     * @Author: z.h.c
     * @Date: 2019/10/16 13:30
     */
    @GetMapping("/detailV2/{commId}")
    @ApiOperation("多维分析详情页-根据3级商品id 获取相应该商品所有4级商品 指标信息")
    public R detailNew(@PathVariable("commId") Integer commId) {

        Map<String, Object> map = wpBaseIndexValService.analyseType4CommIndexs(commId);
        return R.ok().put("data", map);
    }


}
