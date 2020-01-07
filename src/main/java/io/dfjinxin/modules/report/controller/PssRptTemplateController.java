package io.dfjinxin.modules.report.controller;

import io.dfjinxin.common.utils.R;
import io.dfjinxin.modules.report.entity.PssRptTemplateEntity;
import io.dfjinxin.modules.report.service.PssRptTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName PssRptTemplateController
 * @Author：lym 863968235@qq.com
 * @Date： 2019/12/27 17:09
 * 修改备注：
 */
@RestController
@RequestMapping("report/rptTemplate")
@Api(tags = "报告模板")
public class PssRptTemplateController {


    @Autowired
    private PssRptTemplateService pssRptTemplateService;


    //文件路径和文件名称的接口
    @GetMapping("/queryRptTemplate")
    @ApiOperation("所有文件和路径列表")
    public R queryRptTemplate() {
        List<PssRptTemplateEntity> page = pssRptTemplateService.list();

        return R.ok().put("page", page);
    }




}
