package io.dfjinxin.common.utils.httpClient;

import io.dfjinxin.common.utils.json.JsonSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Created by SongCQ on 2017/8/29.
 */
public class HttpSendMessage {

    private static Logger logger = LoggerFactory.getLogger(HttpSendMessage.class);

    public static String postHttpRequest4Str(String url,Map<String,Object> paramMap){
        return sendHttpRequest4Str(url,paramMap, RequestMethod.POST);
    }

    public static String getHttpRequest4Str(String url,Map<String,Object> paramMap){
        return sendHttpRequest4Str(url,paramMap, RequestMethod.GET);
    }

    private static String sendHttpRequest4Str(String url, Map<String,Object> paramMap, RequestMethod sendType){
        try (InputStream inputStream = sendHttpRequest(url,paramMap,sendType) ){
            if(inputStream==null)
                return null;
            String line = null;
            StringBuilder result = new StringBuilder();
            try( BufferedReader in = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8))){
                while ((line = in.readLine()) != null) {
                    result.append(line);
                }
                return result.toString();
            }catch (IOException e){
                e.printStackTrace();
                throw e;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public static InputStream sendHttpRequest(String url, Map<String,Object> paramMap, RequestMethod sendType){
        try {
            URLConnection conn = makeConnection(url);
            StringBuilder builder = null;

            if(sendType== RequestMethod.POST){
                conn.setDoOutput(true);
                conn.setDoInput(true);
                builder = new StringBuilder();

                builder.append(JsonSupport.objectToJson(paramMap) );
            }else{
                if(paramMap!=null){
                    builder = new StringBuilder();
                    for(String paramKey : paramMap.keySet()){
                        Object paramVal = paramMap.get(paramKey);
                        if(builder.length()>0){
                            builder.append("&");
                        }
                        builder.append(paramKey);
                        builder.append("=");
                        if(paramVal instanceof String){
                            builder.append(URLEncoder.encode((String) paramVal, "UTF-8") );
                        }else{
                            builder.append(paramVal );
                        }
                    }

                    logger.info("\n request http msg to -->{}<--,\n params:-->{}<--, \n finally send to server param string is -->{}<--",url,paramMap,builder.toString());

                }
            }


            if(builder!=null){

                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8));
                bw.write(builder.toString());
                bw.flush();
                bw.close();

//                try(PrintWriter out = new PrintWriter(new OutputStreamWriter(
//                        conn.getOutputStream(), StandardCharsets.UTF_8), true)){
//                    out.print(builder.toString());
//                    out.flush();
//                }catch (IOException e) {
//                    e.printStackTrace();
//                    throw e;
//                }
            }



            return conn.getInputStream();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static InputStream postHttpRequest(String url,Map<String,Object> paramMap){
        return HttpSendMessage.sendHttpRequest(url,paramMap, RequestMethod.POST);
    }

    public static InputStream getHttpRequest(String url,Map<String,Object> paramMap){
        return HttpSendMessage.sendHttpRequest(url,paramMap, RequestMethod.GET);
    }

    public static URLConnection makeConnection(String url) throws IOException {
        URL urlObj = new URL(url);
        URLConnection conn = urlObj.openConnection();
        conn.setRequestProperty("accept", "*/*");
        conn.setRequestProperty("connection", "Keep-Alive");
        conn.setRequestProperty("user-agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        return conn;
    }
}
