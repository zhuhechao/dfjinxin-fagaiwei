package io.dfjinxin.modules.price.controller;

import io.dfjinxin.common.utils.R;
import io.dfjinxin.modules.price.entity.PssAnalyReltEntity;
import io.dfjinxin.modules.price.service.PssAnalyInfoService;
import io.dfjinxin.modules.price.service.PssAnalyReltService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author bourne
 * @email kuibobo@gmail.com
 * @date 2019-09-02 17:03:24
 */
@RestController
@RequestMapping("price/pssanalyrelt")
@Api(tags = "分析结果")
public class PssAnalyReltController {

    @Autowired
    private PssAnalyReltService pssAnalyReltService;

    @Autowired
    private PssAnalyInfoService pssAnalyInfoService;

    /**
     * @Desc: 相关性分析&因果分析-获取详情
     * @Param: [analyId]
     * @Return: io.dfjinxin.common.utils.R
     * @Author: z.h.c
     * @Date: 2019/12/20 15:25
     */
    @GetMapping("detail/{analyId}")
    @ApiOperation("相关性分析&因果分析-获取详情")
    public R detail(@PathVariable("analyId") Integer analyId) {
        PssAnalyReltEntity pssAnalyRelt = pssAnalyReltService.selectByAnalyId(analyId);

        return R.ok().put("data", pssAnalyRelt);
    }

    /**
     * @Desc: 相关性分析&因果分析-删除info&relt表数据
     * @Param: [analyId]
     * @Return: io.dfjinxin.common.utils.R
     * @Author: z.h.c
     * @Date: 2019/12/20 15:25
     */
    @GetMapping("delete/{analyId}")
    @ApiOperation("相关性分析&因果分析-删除info&relt表数据")
    public R delete(@PathVariable("analyId") Integer analyId) {
        PssAnalyReltEntity pssAnalyRelt = pssAnalyReltService.selectByAnalyId(analyId);
        if (pssAnalyRelt == null) {
            return R.error("数据不存在!");
        }
        pssAnalyInfoService.removeById(analyId);
        pssAnalyReltService.removeById(pssAnalyRelt.getReltId());

        return R.ok();
    }
}
