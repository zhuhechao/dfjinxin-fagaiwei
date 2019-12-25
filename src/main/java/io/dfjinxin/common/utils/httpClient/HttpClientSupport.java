package io.dfjinxin.common.utils.httpClient;

import com.alibaba.fastjson.JSONObject;
import io.dfjinxin.common.utils.json.JsonSupport;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by SongCQ on 2018/10/11.
 */
public class HttpClientSupport {

    private static Logger logger = LoggerFactory.getLogger(HttpSendMessage.class);

    private static ThreadLocal<HttpClientSupport> simpleSupport = new ThreadLocal<>();

    private String serviceUrl;

    private CloseableHttpClient httpClient;

    /**
     * 单例模式
     *
     * @param serviceUrl
     * @return
     */
    public static HttpClientSupport getSingleInstance(String serviceUrl) {
        if (simpleSupport.get() != null) {
            return simpleSupport.get();
        } else {
            simpleSupport.set(new HttpClientSupport(serviceUrl));
        }
        return simpleSupport.get();
    }

    /**
     * 非单例模式
     *
     * @param serviceUrl
     * @return
     */
    public static HttpClientSupport getInstance(String serviceUrl) {
        return new HttpClientSupport(serviceUrl);
    }

    private HttpClientSupport(String serviceUrl) {
        this.serviceUrl = serviceUrl;
        this.httpClient = HttpClients.createDefault();
    }

    public CloseableHttpResponse sendGet(String url) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        return response;
    }

    public CloseableHttpResponse sendDelete(String url) throws IOException {
        HttpDelete httpDelte = new HttpDelete(url);
        CloseableHttpResponse response = httpClient.execute(httpDelte);
        return response;
    }

    public CloseableHttpResponse sendPostByJson(String url, Map<String, Object> params) throws IOException {
        logger.info("the model python url:{}", url);
        logger.info("the reqparams:{}", JSONObject.toJSONString(params));
        HttpPost httpPost = new HttpPost(url);
        return sendPutOrPostByJson(httpPost, params);
    }

    public CloseableHttpResponse sendPutByJson(String url, Map<String, Object> params) throws IOException {
        HttpPut httpPut = new HttpPut(url);
        return sendPutOrPostByJson(httpPut, params);
    }

    public CloseableHttpResponse sendPost(String url, Map<String, Object> params) throws IOException, URISyntaxException {
        HttpPost httpPost = new HttpPost(url);
        return sendPutOrPostByForm(httpPost, params);
    }

    public CloseableHttpResponse sendPut(String url, Map<String, Object> params) throws IOException, URISyntaxException {
        HttpPut httpPut = new HttpPut(url);
        return sendPutOrPostByForm(httpPut, params);
    }

    private CloseableHttpResponse sendPutOrPostByJson(HttpEntityEnclosingRequestBase putOrPost, Map<String, Object> params) throws IOException {
        String sendJson = JsonSupport.objectToJsonWithoutFormatter(params);
//        String sendJson = params.toString();
        String targetJson = JsonSupport.formatString(sendJson);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(targetJson.getBytes(StandardCharsets.UTF_8));
        InputStreamEntity entity = new InputStreamEntity(inputStream, -1);

        putOrPost.setEntity(entity);

        CloseableHttpResponse response = httpClient.execute(putOrPost);

        return response;
    }

    public CloseableHttpResponse sendPutOrPostByForm(HttpEntityEnclosingRequestBase putOrPost, Map<String, Object> params) throws IOException, URISyntaxException {

        List<NameValuePair> parameters = new ArrayList<NameValuePair>(0);
        for (String key : params.keySet()) {
            parameters.add(new BasicNameValuePair(key, String.valueOf(params.get(key))));
        }
        // 构造一个form表单式的实体
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters, HTTP.UTF_8);

        putOrPost.setEntity(formEntity);

        CloseableHttpResponse response = httpClient.execute(putOrPost);
        return response;
    }

    public String sendPostWithFile(String url, Map<String, String> stringParams, Map<String, File> files) throws URISyntaxException, IOException {
        Map<String, Object> paramMap = parseParams(stringParams);
        URIBuilder uriBuilder = new URIBuilder(new StringBuilder()
                .append(this.serviceUrl != null ? this.serviceUrl.trim() : "")
                .append("/").append(url).toString());
        HttpPost httpPost = new HttpPost(uriBuilder.build().toString());

        ContentType strContent = ContentType.create("text/plain", Charset.forName("UTF-8"));

        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();

//        multipartEntityBuilder.setCharset(Charset.forName("utf-8"));
        multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);//设置浏览器兼容模式

        if (files != null && files.size() > 0) {
            for (String fileName : files.keySet()) {
                multipartEntityBuilder.addBinaryBody(fileName, files.get(fileName), strContent, files.get(fileName).getName());
            }
        }

        if (stringParams != null && stringParams.size() > 0) {
            for (String stringParamName : stringParams.keySet()) {
                multipartEntityBuilder.addTextBody(stringParamName, stringParams.get(stringParamName), strContent);
            }
        }

        httpPost.setEntity(multipartEntityBuilder.build());
        CloseableHttpResponse response = httpClient.execute(httpPost);

        String responseStr = EntityUtils.toString(response.getEntity(), "UTF-8");
        return responseStr;
    }

    public String sendRequest(String url, Map<String, Object> params, RequestMethod sendMethod, boolean useJson) throws IOException, URISyntaxException {
        Map<String, Object> paramMap = parseParams(params);
        URIBuilder uriBuilder = new URIBuilder(new StringBuilder()
                .append(this.serviceUrl != null ? this.serviceUrl.trim() : "").append(url).toString());

        CloseableHttpResponse response = null;
        if (sendMethod.equals(RequestMethod.GET)) {
            for (String paramKey : paramMap.keySet()) {
                uriBuilder.addParameter(paramKey, String.valueOf(paramMap.get(paramKey)));
            }
            response = this.sendGet(uriBuilder.toString());
        } else if (sendMethod.equals(RequestMethod.PUT)) {
            if (useJson) {
                response = this.sendPutByJson(uriBuilder.toString(), params);
            } else {
                response = this.sendPut(uriBuilder.toString(), params);
            }
        } else if (sendMethod.equals(RequestMethod.POST)) {
            if (useJson) {
                response = this.sendPostByJson(uriBuilder.toString(), params);
            } else {
                response = this.sendPost(uriBuilder.toString(), params);
            }
        } else if (sendMethod.equals(RequestMethod.DELETE)) {
            for (String paramKey : paramMap.keySet()) {
                uriBuilder.addParameter(paramKey, String.valueOf(paramMap.get(paramKey)));
            }
            response = this.sendDelete(uriBuilder.toString());
        }
        if (response != null) {
            String responseStr = EntityUtils.toString(response.getEntity(), "UTF-8");
            logger.debug("{},{}", uriBuilder.toString(), responseStr);
            return responseStr;
        } else {
            return null;
        }

    }


    private Map<String, Object> parseParams(Object params) {
        if (params != null) {
            if (params instanceof Map) {
                return (Map<String, Object>) params;
            } else {
                return null;
            }

        } else {
            return new HashMap<String, Object>();
        }
    }

}
