package io.dfjinxin.modules.model;

import io.dfjinxin.common.utils.R;
import io.dfjinxin.common.utils.httpClient.HttpClientSupport;
import io.dfjinxin.common.utils.json.JsonSupport;
import io.dfjinxin.common.utils.jsonp.JsonResult;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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

@Controller
@RequestMapping()
@MultipartConfig
public class ModelController extends AbstractClientController {

    Logger logger = LoggerFactory.getLogger(ModelController.class);

    private final static String pythonHost = "http://10.1.3.239:8082";//预生产环境
    private HttpClientSupport httpClientSupport = HttpClientSupport.getInstance(pythonHost);

    @RequestMapping(path = "/nlp/test", method = {RequestMethod.GET})
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public String nlpTestRequest() throws IOException {
        logger.debug("get is runnning......");
        return JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "YYY", null, null);
    }

    @RequestMapping(path = "/model/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public R nlpRequest(HttpServletRequest request) throws IOException, URISyntaxException, ServletException {
        String response = clientRequest(request, true);
        HashMap map = JsonSupport.jsonToMap(response);
//        JsonResult jsonpResult   = JsonSupport.makeJsonpResult(JsonResult.RESULT.SUCCESS, "YYY", null, JsonSupport.jsonToMap(response));
        return R.ok().put("data", JsonSupport.jsonToMap(response));
    }

    @RequestMapping("/download/**")
    public void nlpDownload(HttpServletRequest request, HttpServletResponse response) throws IOException, URISyntaxException {
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
