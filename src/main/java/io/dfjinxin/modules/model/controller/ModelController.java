package io.dfjinxin.modules.model.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.common.utils.httpClient.HttpClientSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * @Desc:
 * @Param: 模型管理
 * @Return:
 * @Author: z.h.c
 * @Date: 2019/12/9 14:32
 */

//@RequestMapping()
@MultipartConfig
@RestController
@Api(tags = "模型管理-调用python Api")
public class ModelController extends AbstractClientController {

    private static final Logger LOG = LoggerFactory.getLogger(ModelController.class);

    @Value("${python.url2}")
    private String pythonHost;

    @Autowired
    private HttpServletRequest request;

//    @Autowired
//    private HttpServletResponse response;

    private HttpClientSupport httpClientSupport = HttpClientSupport.getInstance(pythonHost);

    public HttpClientSupport httpClientSupport() {
        return HttpClientSupport.getInstance(pythonHost);
    }

    @ApiOperation(value = "模型管理-调用python接口适配", notes = "调用方式:域名/model/python接口地址;eg:www.localhosst:8081/fagaiwei_api/model/x")
//    @RequestMapping(path = "/model/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
    @PostMapping("/model/**")
    @CrossOrigin(allowCredentials = "true")
    public R nlpRequest() {
        LOG.info("---模型管理调用python  开始---");
        LOG.info("模型管理-调用python 请求信息*****");
        LOG.info("模型管理 python host:{}", pythonHost);
        String response = null;
        try {
            response = super.clientRequest(request, true);
        } catch (Exception e) {
            e.printStackTrace();
            String pyApi = super.checkServiceUrl(request);
            LOG.error("模型管理-调用py-{【】}接口异常:{}", pyApi, e.getMessage());
            R.error("模型管理-调用py-[" + pyApi + "]接口失败!");
        }

        LOG.info("-模型管理调用python返回结果:{}", response);
        if (StringUtils.isEmpty(response)) {
            return R.error("模型管理-调用python失败!");
        }
        JSONObject jsonObj = JSON.parseObject(response);
        if (jsonObj.getInteger("code") == 0) {
            Object data = jsonObj.containsKey("data") ? jsonObj.get("data") : null;
            return R.ok().put("data", data);
        }
        return R.error().put("msg", jsonObj.containsKey("msg") ? jsonObj.getString("msg") : "");
    }

    @PostMapping("/download/**")
    public void download() throws IOException, URISyntaxException {
        String serviceUrl = checkServiceUrl(request).replace("download", "model");

        URIBuilder uriBuilder = new URIBuilder(new StringBuilder()
                .append(pythonHost).append(serviceUrl).toString());

//        Map<String, Object> paramMap = getParameter(request);
        Map<String, Object> paramMap = getPostJsonParameter(request);

        for (String paramKey : paramMap.keySet()) {
            uriBuilder.addParameter(paramKey, String.valueOf(paramMap.get(paramKey)));
        }

        CloseableHttpResponse serviceResponse = this.httpClientSupport.sendGet(uriBuilder.toString());

        InputStream serviceResponseStream = serviceResponse.getEntity().getContent();

        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletResponse response = servletRequestAttributes.getResponse();

        for (Header header : serviceResponse.getAllHeaders()) {
            response.setHeader(header.getName(), header.getValue());
        }
        OutputStream outputStream = response.getOutputStream();

        byte[] buf = new byte[8192];
        int n;
        while ((n = serviceResponseStream.read(buf)) > 0) {
            outputStream.write(buf, 0, n);
        }
        outputStream.flush();
    }
}
