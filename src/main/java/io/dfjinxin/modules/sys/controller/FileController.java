package io.dfjinxin.modules.sys.controller;

import io.dfjinxin.common.utils.DateTime;
import io.dfjinxin.common.utils.FileUtil;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.common.utils.ShiroUtils;
import io.dfjinxin.config.propertie.AppProperties;

import io.swagger.annotations.Api;
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


import java.io.IOException;
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
    public R upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return R.error("上传失败，请选择文件");
        }

        // Build directory
        Integer year = DateTime.now().getYear();
        Integer month = DateTime.now().getMonth() + 1;
        String subPath = null;
        subPath = new StringBuilder(32).append("/").append(year).append('/').append(month).append('/').toString();

        String originalBasename = file.getOriginalFilename();

        // Get basename
        String basename = ShiroUtils.md5(originalBasename + System.currentTimeMillis());

        // Get extension
        String extension = originalBasename.substring(originalBasename.lastIndexOf(".") + 1);

        logger.debug("Base name: [{}], extension: [{}] of original filename: [{}]", basename, extension, file.getOriginalFilename());

        // Build sub file path
        String subFilePath = subPath + basename + '.' + extension;

        // Get upload path
        Path uploadPath = Paths.get(appProperties.getPath().getWorkDir(), appProperties.getPath().getUpload(), subFilePath);
        logger.debug("Uploading to directory: [{}]", uploadPath.toString());
        FileUtil.transferTo(file, uploadPath);

        Path absPath = Paths.get(appProperties.getPath().getUpload(), subFilePath);
        return R.ok().put("path", absPath.toString());
    }
}
