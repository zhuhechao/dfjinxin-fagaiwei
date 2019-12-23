package io.dfjinxin.modules.model;

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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.HashMap;
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
@Api(tags = "模型管理 Api")
public class ModelController extends AbstractClientController {

    private static final Logger LOG = LoggerFactory.getLogger(ModelController.class);

    //测试环境
//    private static String pythonHost = "http://10.1.3.239:8092";
    //生产环境
    private static String pythonHost = "http://172.20.10.207:8092";
    private HttpClientSupport httpClientSupport = HttpClientSupport.getInstance(pythonHost);

//    @RequestMapping(path = "/nlp/test", method = {RequestMethod.GET})
//    @ResponseBody
//    @CrossOrigin(allowCredentials = "true")
//    public String nlpTestRequest() throws IOException {
//        LOG.debug("get is runnning......");
//        return JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "YYY", null, null);
//    }

    @ApiOperation(value = "模型管理-调用python接口适配", notes = "调用方式:域名/model/python接口地址;eg:www.localhosst:8081/fagaiwei_api/model/x")
//    @RequestMapping(path = "/model/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
    @PostMapping("/model/**")
    @CrossOrigin(allowCredentials = "true")
    public R nlpRequest(HttpServletRequest request) throws IOException, URISyntaxException, ServletException {
        LOG.info("---模型管理调用python  开始---");
        LOG.info("模型管理-调用python 请求信息*****");
        LOG.info("模型管理 python host:{}", pythonHost);
        String response = clientRequest(request, true);
        LOG.info("-模型管理调用python返回结果:{}", response);
        if (StringUtils.isEmpty(response)) {
            return R.error("模型管理-调用python失败!");
        }
        JSONObject jsonObj = JSON.parseObject(response);
        if (jsonObj.getInteger("code") == 0) {
//            Object data = jsonObj.containsKey("data") ? jsonObj.get("data") : null;
            Object data = jsonObj.containsKey("algorithm_list") ? jsonObj.get("algorithm_list") : null;
            return R.ok().put("data", data);
        }
        return R.error().put("msg", jsonObj.containsKey("msg") ? jsonObj.getString("msg") : "");
    }

    @PostMapping("/download/**")
    public void download(HttpServletRequest request, HttpServletResponse response) throws IOException, URISyntaxException {
        String serviceUrl = checkServiceUrl(request).replace("download", "model");

        URIBuilder uriBuilder = new URIBuilder(new StringBuilder()
                .append(pythonHost).append(serviceUrl).toString());

        Map<String, Object> paramMap = getParameter(request);

        for (String paramKey : paramMap.keySet()) {
            uriBuilder.addParameter(paramKey, String.valueOf(paramMap.get(paramKey)));
        }

        CloseableHttpResponse serviceResponse = this.httpClientSupport.sendGet(uriBuilder.toString());

        InputStream serviceResponseStream = serviceResponse.getEntity().getContent();

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

    private Map<String, Object> getParameter(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>();
        Enumeration<String> allNames = request.getParameterNames();
        while (allNames.hasMoreElements()) {
            String paramName = allNames.nextElement();
            params.put(paramName, request.getParameter(paramName));
        }
        return params;
    }

    @Override
    public HttpClientSupport httpClientSupport() {
        return this.httpClientSupport;
    }
}
