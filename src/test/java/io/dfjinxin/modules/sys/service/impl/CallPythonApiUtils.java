package io.dfjinxin.modules.sys.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.*;

/**
 * @Desc: 调用远程python pai 方法
 * @Param:
 * @Return:
 * @Author: z.h.c
 * @Date: 2019/11/1 17:29
 */
public class CallPythonApiUtils {

    private static Logger logger = LoggerFactory.getLogger(CallPythonApiUtils.class);

    private static String URL = "http://10.1.1.123:7777/yiBanXingFengXi";
    private static String DATASET = "http://10.1.1.123:7777/createDataSet";

    private static HttpURLConnection getConn(final String urlStr) throws IOException {
        //创建连接
        URL url = new URL(urlStr);// 创建连接
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setUseCaches(false);
        connection.setInstanceFollowRedirects(true);
        try {
            connection.setRequestMethod("POST");                                // 设置请求方式
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        connection.setRequestProperty("Accept", "application/json");        // 设置接收数据的格式
        connection.setRequestProperty("Content-Type", "application/json");  // 设置发送数据的格式
        return connection;
    }

    public static String doPost(String urlStr, Map<String, Object> params) {

        HttpURLConnection conn = null;
        String result = null;
        try {
            conn = getConn(urlStr);
            conn.connect();
            int code = conn.getResponseCode();
            InputStream is;
            if (code == 200) {
                logger.info("连接成功");
                is = conn.getInputStream();
            } else {
                logger.info("连接失败：" + code);
                return null;
            }
            //发送数据
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8"); // utf-8编码
            out.append(JSON.toJSONString(params));
            out.flush();
            out.close();

            int length = conn.getContentLength();// 获取长度
            if (length != -1) {
                byte[] data = new byte[length];
                byte[] temp = new byte[1025];
                int readLen = 0;
                int destPos = 0;
                while ((readLen = is.read(temp)) > 0) {
                    System.arraycopy(temp, 0, data, destPos, readLen);
                    destPos += readLen;
                }
                result = new String(data, "UTF-8"); // utf-8编码
                Map<String, Object> ret = JSON.parseObject(result, new TypeReference<Map<String, Object>>() {
                });
                logger.info("the resutl:{}", result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return result;
    }


    public static void main(String[] args) {

        Map<String, Object> params = new HashMap<>();
        int[] val = new int[]{39, 13};
        params.put("indeVar", val);
        doPost(DATASET, params);

//        String[] val = new String[]{"b_1", "b_2", "m_3", "m_2"};
//        params.put("table", "pig_data");
//        params.put("variable", val);
//
//        int[] val = new int[]{39, 13};
//        params.put("indeVar", val);
//        logger.info(System.currentTimeMillis());
//        try {
//            //创建连接
//            URL url = new URL(URL);// 创建连接
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setDoOutput(true);
//            connection.setDoInput(true);
//            connection.setUseCaches(false);
//            connection.setInstanceFollowRedirects(true);
//            connection.setRequestMethod("POST");                                // 设置请求方式
//            connection.setRequestProperty("Accept", "application/json");        // 设置接收数据的格式
//            connection.setRequestProperty("Content-Type", "application/json");  // 设置发送数据的格式
//            connection.connect();
//
//
//            //发送数据
//            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8"); // utf-8编码
//            out.append(JSON.toJSONString(params));
//            out.flush();
//            out.close();
//
//            int code = connection.getResponseCode();
//            InputStream is;
//            if (code == 200) {
//                logger.info("连接成功");
//                is = connection.getInputStream();
//            } else {
//                logger.info("连接失败：" + code);
//                is = connection.getErrorStream();
//                return;
//            }
//
//            logger.info(System.currentTimeMillis());
//
//            // 读取响应
//            int length = connection.getContentLength();// 获取长度
//            if (length != -1) {
//                byte[] data = new byte[length];
//                byte[] temp = new byte[512];
//                int readLen = 0;
//                int destPos = 0;
//                while ((readLen = is.read(temp)) > 0) {
//                    System.arraycopy(temp, 0, data, destPos, readLen);
//                    destPos += readLen;
//                }
//                String result = new String(data, "UTF-8"); // utf-8编码
//                Map<String, Object> ret = JSON.parseObject(result, new TypeReference<Map<String, Object>>() {
//                });
//                logger.info(ret);
////                if (ret.get("SVM_TextSort") == null) {
////                    logger.info("11111111111111111");
////                } else {
////                    logger.info("2122222222222222222");
////                }
////                logger.info(ret.get("SVM_TextSort"));
//
//                //SVM_CHANNEL相关
//                List<String> svmChannelList = new ArrayList<String>();
//                if (svmChannelList.isEmpty()) {
//                    logger.info("222222222222222222");
//                }
//                //返回SVM_CHANNEL值不为空时执行
//                if (ret.get("SVM_TextSort") != null) {
//                    String str = ret.get("SVM_TextSort").toString().trim();
//                    if (str.contains(",")) {
//                        //对多标签进行转换
//                        svmChannelList = Arrays.asList(str.split(","));
//                    } else {
//                        //只有一个标签或没有标签
//                        svmChannelList = Collections.singletonList(str);
//                    }
//                    if (!svmChannelList.isEmpty()) {
//                        logger.info("11111111111111111111111");
//                    }
//                }
//            }
//            logger.info(System.currentTimeMillis());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
