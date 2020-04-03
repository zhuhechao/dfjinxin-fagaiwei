package io.dfjinxin.modules.report.controller;

import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import io.dfjinxin.config.propertie.AppProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.dfjinxin.modules.report.entity.PssRptInfoEntity;
import io.dfjinxin.modules.report.service.PssRptInfoService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.R;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;


/**
 * @author mxq
 * @email 397968061@qq.com
 * @date 2019-09-20 11:07:08
 */
@RestController
@RequestMapping("report/pssrptinfo")
@Api(tags = "报告信息-配置、管理")

public class PssRptInfoController {
    @Autowired
    private PssRptInfoService pssRptInfoService;
    @Autowired
    private AppProperties appProperties;
    @Autowired
    private HttpServletResponse response;

    /**
     * 分页
     */
    @GetMapping("/list")
    @ApiOperation(value = "报告配置-查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "页码", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "返回数据集", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "rptType", value = "报告类型 0 自动 1 手工 ", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "rptFreq", value = "报告频度", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "rptStatus", value = "报告状态，1是有效，0是删除", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "minCrteTime", value = "生成报告最小日期", required = false, dataType = "Date", paramType = "query"),
            @ApiImplicitParam(name = "maxCrteTime", value = "生成报告最大日期", required = false, dataType = "Date", paramType = "query"),
    })
    public R list(
            @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
            @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
            @RequestParam(value = "rptType", required = false) String rptType,
            @RequestParam(value = "rptFreq", required = false) String rptFreq,
            @RequestParam(value = "rptStatus", required = false) String rptStatus,
            @RequestParam(value = "minCrteTime", required = false) Date minCrteTime,
            @RequestParam(value = "maxCrteTime", required = false) Date maxCrteTime
    ) {
        Map<String, Object> params = new HashMap() {{
            put("pageIndex", pageIndex);
            put("pageSize", pageSize);

            put("rptType", rptType);
            put("rptFreq", rptFreq);
            put("rptStatus", rptStatus);
            put("minCrteTime", minCrteTime);
            put("maxCrteTime", maxCrteTime);
        }};
        PageUtils pageOne = pssRptInfoService.queryPage(params);
        return R.ok().put("page", pageOne);
    }


    //所有分析报告单独接口
    @GetMapping("/queryRptName")
    @ApiOperation("所有分析报告单独接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "commId", value = "传参商品报告和所有的商品报告文件", required = false, dataType = "Integer", paramType = "query"),
    })
    public R queryRptName(
            @RequestParam(value = "commId", required = false) Integer commId
    ) {
        Map<String, Object> params = new HashMap() {{
            put("commId", commId);
        }};
        List<PssRptInfoEntity> page = pssRptInfoService.queryRptName(params);
        for (int i = 0; i < page.size(); i++) {
            String rptName = page.get(i).getRptName().substring(0, page.get(i).getRptName().lastIndexOf("."));
            page.get(i).setRptName(rptName);
        }

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{rptId}")
    @RequiresPermissions("report:pssrptinfo:info")
    @ApiOperation("报告运行信息获取")
    public R info(@PathVariable("rptId") Long rptId) {
        PssRptInfoEntity pssRptInfo = pssRptInfoService.getById(rptId);

        return R.ok().put("pssRptInfo", pssRptInfo);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("report:pssrptinfo:save")
    @ApiOperation("报告运行信息保存")
    public R save(@RequestBody PssRptInfoEntity pssRptInfo) {
        pssRptInfoService.save(pssRptInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("report:pssrptinfo:update")
    @ApiOperation("报告运行信息更新")
    public R update(@RequestBody PssRptInfoEntity pssRptInfo) {
        pssRptInfoService.updateById(pssRptInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @RequiresPermissions("report:pssrptinfo:delete")
    @ApiOperation("报告运行信息删除")
    public R delete(@RequestBody Long[] rptIds) {
        pssRptInfoService.removeByIds(Arrays.asList(rptIds));

        return R.ok();
    }

    @PostMapping("/preview/image/{infoId}/word/media/{imageName}")
    public void previewImage(@PathVariable("infoId") String infoId,
                             @PathVariable(value = "imageName") String imageName) throws Exception {
        PssRptInfoEntity prie = pssRptInfoService.getById(infoId);
        InputStream input = null;
        OutputStream out = null;
        Path imageP = Paths.get(new StringBuilder()
                //.append(appProperties.getPath().getUserDir())
                .append(prie.getRptPath(), 0, prie.getRptPath().lastIndexOf("\\"))
                .append("/image/").append(infoId).append("/word/media/").append(imageName).toString());
        input = new FileInputStream(imageP.toString());
        response.setContentType("image/png");

        out = response.getOutputStream();
        byte[] b = new byte[512];
        if (out != null) {
            if (input != null) {
                while ((input.read(b)) != -1) {
                    out.write(b);
                }
            } else {
                System.out.println("InputStream为空。。。");
            }
        } else {
            System.out.println("OutputStream为空。。。");
        }
        out.flush();
        input.close();
        out.close();
    }


    @GetMapping("/preview")
    public void preview(@RequestParam(value = "fileType") String fileType,
                        @RequestParam(value = "infoId") String infoId) throws Exception {
        PssRptInfoEntity prie = pssRptInfoService.getById(infoId);
        Path p = Paths.get(//appProperties.getPath().getUserDir(),
                prie.getRptPath());
        InputStream input = null;
        OutputStream out = null;
        if ("html".equals(fileType)) {
            input = new FileInputStream(p.toString().replace("docx", "html"));
            response.setContentType("text/html;charset=UTF-8");//解决页面显示乱码 
        } else if ("pdf".equals(fileType)) {
            input = new FileInputStream(p.toString().replace("docx", "pdf"));
            response.setContentType("application/pdf");
        } else {
            File file = new File(p.toString());
            input = new FileInputStream(p.toString());
            response.setContentType("application/x-msdownload");
            response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
            response.setHeader("Content-Length", String.valueOf(file.length()));
        }
        out = response.getOutputStream();
        byte[] b = new byte[512];
        if (out != null) {
            if (input != null) {
                while ((input.read(b)) != -1) {
                    out.write(b);
                }
            } else {
                System.out.println("InputStream为空。。。");
            }
        } else {
            System.out.println("OutputStream为空。。。");
        }
        out.flush();
        input.close();
        out.close();
    }
}
