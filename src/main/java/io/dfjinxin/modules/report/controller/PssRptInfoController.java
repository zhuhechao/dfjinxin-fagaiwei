package io.dfjinxin.modules.report.controller;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;

import io.dfjinxin.config.propertie.AppProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.dfjinxin.modules.report.entity.PssRptInfoEntity;
import io.dfjinxin.modules.report.service.PssRptInfoService;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.R;

import javax.servlet.http.HttpServletResponse;


/**
 * 
 *
 * @author mxq
 * @email 397968061@qq.com
 * @date 2019-09-20 11:07:08
 */
@RestController
@RequestMapping("report/pssrptinfo")
@Api(tags = "PssRptInfoController", description = "报告信息表")

public class PssRptInfoController {
    @Autowired
    private PssRptInfoService pssRptInfoService;
    @Autowired
    private AppProperties appProperties;
    @Autowired
    private HttpServletResponse response;
    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("report:pssrptinfo:list")
    @ApiOperation("报告运行信息列表分页")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = pssRptInfoService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{rptId}")
    @RequiresPermissions("report:pssrptinfo:info")
    @ApiOperation("报告运行信息获取")
    public R info(@PathVariable("rptId") Long rptId){
		PssRptInfoEntity pssRptInfo = pssRptInfoService.getById(rptId);

        return R.ok().put("pssRptInfo", pssRptInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("report:pssrptinfo:save")
    @ApiOperation("报告运行信息保存")
    public R save(@RequestBody PssRptInfoEntity pssRptInfo){
		pssRptInfoService.save(pssRptInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("report:pssrptinfo:update")
    @ApiOperation("报告运行信息更新")
    public R update(@RequestBody PssRptInfoEntity pssRptInfo){
		pssRptInfoService.updateById(pssRptInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("report:pssrptinfo:delete")
    @ApiOperation("报告运行信息删除")
    public R delete(@RequestBody Long[] rptIds){
		pssRptInfoService.removeByIds(Arrays.asList(rptIds));

        return R.ok();
    }
    @RequestMapping("/image/{infoId}/word/media/{imageName}")
    public void previewImage(@PathVariable("infoId" ) String infoId,
                        @PathVariable(value = "imageName" ) String imageName ) throws Exception {
        PssRptInfoEntity prie = pssRptInfoService.getById(infoId);
        InputStream input = null;
        OutputStream out = null;
        Path imageP=Paths.get(new StringBuilder()
                .append(appProperties.getPath().getUserDir())
                .append(prie.getRptPath().substring(0,prie.getRptPath().lastIndexOf("\\")))
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


    @RequestMapping("/preview")
    public void preview(@RequestParam(value = "fileType" ) String fileType,
                        @RequestParam(value = "infoId" ) String infoId ) throws Exception {
        PssRptInfoEntity prie = pssRptInfoService.getById(infoId);
        Path p = Paths.get(appProperties.getPath().getUserDir(), prie.getRptPath());
        InputStream input = null;
        OutputStream out = null;
        if ("docx".equals(fileType)) {
            input = new FileInputStream(p.toString().replace("docx", "html"));
            response.setContentType("text/html;charset=UTF-8");//解决页面显示乱码 
        } else {
            input = new FileInputStream(p.toString().replace("docx", "pdf"));
            response.setContentType("application/pdf");
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
