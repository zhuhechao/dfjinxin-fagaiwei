package io.dfjinxin.modules.sys.controller;

import io.dfjinxin.common.utils.DateTime;
import io.dfjinxin.common.utils.FileUtil;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.common.utils.ShiroUtils;
import io.dfjinxin.config.propertie.AppProperties;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 上传文件
 *
 * @author bourne kuibobo@gmail.com
 */
@Controller
@RequestMapping("/file")
@Api(tags = "文件处理")
public class FileController {

    private static Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private AppProperties appProperties;

    @PostMapping("/upload")
    @ResponseBody
    @ApiOperation("上传")
    /**
     * @param moduleName 0 分析报告 report
     */
    public R upload(@RequestParam("file") MultipartFile file,
                    @RequestParam(value = "saveOriName", defaultValue = "false") Boolean saveOriName,
                    @RequestParam(value = "saveSubPath", defaultValue = "true") Boolean saveSubPath,
                    @RequestParam(value = "moduleName", defaultValue = "") String moduleName

                    ) throws IOException {
        if (file.isEmpty()) {
            return R.error("上传失败，请选择文件");
        }

        // Build directory
        String subPath = saveSubPath ? new StringBuilder(32).append("/")
                                        .append(DateTime.now().getYear()).append('/')
                                        .append(DateTime.now().getMonth() + 1).append('/')
                                        .append(DateTime.now().getDay())
                                        .append('/').toString() : "";

        String originalBasename = file.getOriginalFilename();

        // Get basename
        String basename = saveOriName ? originalBasename.substring(0, originalBasename.lastIndexOf(".")) : ShiroUtils.md5(originalBasename + System.currentTimeMillis());

        // Get extension
        String extension = originalBasename.substring(originalBasename.lastIndexOf(".") + 1);

        logger.debug("Base name: [{}], extension: [{}] of original filename: [{}]", basename, extension, file.getOriginalFilename());

        // Build sub file path
        String subFilePath = subPath + basename + '.' + extension;

        // Get upload path 根据module判断
        String modulePath=appProperties.getPath().getUpload();
        if(moduleName.equals("0"))
          modulePath=appProperties.getPath().getModule().getReport();
        Path uploadPath = Paths.get(modulePath , subFilePath);
        if (saveOriName == true && saveSubPath == false && FileUtil.exists(uploadPath.toString()))
            return R.error("文件已存在");

        logger.debug("Uploading to directory: [{}]", uploadPath.toString());
        FileUtil.transferTo(file, uploadPath);

        Path absPath = Paths.get(modulePath, subFilePath);
        return R.ok().put("path", absPath.toString()).put("originalName",originalBasename);
    }
}
