package io.dfjinxin.modules.model.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import io.dfjinxin.common.utils.httpClient.HttpClientSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by SongCQ on 2018/10/18.
 */
public abstract class AbstractClientController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    protected HttpClientSupport httpClientSupport() {
        return null;
    }

    public String clientRequest(HttpServletRequest request, boolean useJson) throws IOException, URISyntaxException, ServletException {
        String serviceUrl = checkServiceUrl(request);
        String response = null;
        String sendMethod = request.getMethod();
        logger.info("python req url:{}", serviceUrl);
        logger.info("python req method:{}", sendMethod);
        logger.info("python req context-type:{}", request.getContentType());
        Map<String, Object> reqParams = getPostJsonParameter(request);
        logger.info("python req params:{}", JSONObject.toJSONString(reqParams));
        if (!Strings.isNullOrEmpty(sendMethod)) {
            if (request instanceof MultipartHttpServletRequest) {//有文件上传
                MultipartHttpServletRequest muliRequest = (MultipartHttpServletRequest) request;
                Iterator<String> fileNames = muliRequest.getFileNames();
                URL classPath = this.getClass().getClassLoader().getResource("/");
                String classPathStr = classPath.toString();

                logger.debug("classPathStr : {}", classPathStr);
//                classPathStr = classPathStr.replaceFirst("file:/","");
                classPathStr = classPathStr.replaceFirst("file:", "");
                logger.debug("classPathStr : {}", classPathStr);

                Map<String, File> fileMap = new HashMap<>();
                while (fileNames.hasNext()) {
                    MultipartFile multiUploadFile = muliRequest.getFile(fileNames.next());
                    String fileName = multiUploadFile.getOriginalFilename();
                    File uploadFile = new File(classPathStr + "/" + fileName);
                    uploadFile.deleteOnExit();
                    multiUploadFile.transferTo(uploadFile);
                    fileMap.put(multiUploadFile.getName(), uploadFile);
                }

                Map parameters = getParameter(request);
                response = httpClientSupport().sendPostWithFile(serviceUrl, parameters, fileMap);

            } else {
                if ("get".equalsIgnoreCase(sendMethod)) {
                    response = httpClientSupport().sendRequest(serviceUrl, getParameter(request), RequestMethod.GET, useJson);
                } else if ("post".equalsIgnoreCase(sendMethod)) {//目前只用了post请求
                    response = httpClientSupport().sendRequest(serviceUrl, reqParams, RequestMethod.POST, useJson);
                } else if ("put".equalsIgnoreCase(sendMethod)) {
                    response = httpClientSupport().sendRequest(serviceUrl, getParamesWithPutDelete(request), RequestMethod.PUT, useJson);
                } else if ("delete".equalsIgnoreCase(sendMethod)) {
                    response = httpClientSupport().sendRequest(serviceUrl, getParamesWithPutDelete(request), RequestMethod.DELETE, useJson);
                }

            }
            logger.debug("python res json is ======>{}", response);

        }
        return response;
    }

    public String checkServiceUrl(HttpServletRequest request) {
        String url = request.getRequestURI();
        String[] urlCharts = url.split("/");

        StringBuilder builder = new StringBuilder();
        int count = 0;
        for (String chart : urlCharts) {
            if (count > 1) {
                builder.append("/");
                builder.append(chart);
            }
            count++;
        }

        return builder.toString();
    }

    protected Map<String, Object> getParameter(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>();
        Enumeration<String> allNames = request.getParameterNames();
        while (allNames.hasMoreElements()) {
            String paramName = allNames.nextElement();
            params.put(paramName, request.getParameter(paramName));
        }
        return params;
    }

    /**
     * @Desc: 获取post方式json格式请求参数
     * @Param: [request]
     * @Return: java.util.Map<java.lang.String, java.lang.Object>
     * @Author: z.h.c
     * @Date: 2019/12/24 16:54
     */
    protected Map<String, Object> getPostJsonParameter(HttpServletRequest request) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String line = null;
        StringBuilder sb = new StringBuilder();
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JSONObject.parseObject(sb.toString());
    }

    private Map getParamesWithPutDelete(HttpServletRequest request) {
        BufferedReader br = null;
        Map<String, String> dataMap = null;
        try {
            br = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String data = URLDecoder.decode(br.readLine(), "utf-8");

            dataMap = Splitter.on('&')
                    .trimResults()
                    .withKeyValueSeparator(
                            Splitter.on('=')
                                    .limit(2)
                                    .trimResults())
                    .split(data);

            return dataMap;
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        return dataMap;

    }

}
