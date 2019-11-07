package io.dfjinxin.modules.sys.service.impl;

import com.alibaba.fastjson.JSON;
import io.dfjinxin.common.utils.MD5Utils;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.common.utils.echart.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Desc:
 * @Author: z.h.c
 * @Date: 2019/10/17 9:50
 * @Version: 1.0
 */
public class YuQingTest {

    public static final String PATH = "https://jianguan.urlsec.qq.com/";
    public static final String APPID = "fagaiwei";
    public static final String PWD = "fgwprice";
    private static Logger logger = LoggerFactory.getLogger(YuQingTest.class);

    public static void main(String[] args) throws Exception {
        /**
         * unixTime：1540794339
         * appid："fagaiwei"
         * signid:：
         * programmeId：方案id long类型  即同步过去的商品id
         * appType：类型 int类型 0：定向监测舆情分析  1：话题分析  这里填0即可
         * dateType：日期类型 int类型 0：今天  1:7天内  2:30天内  这里填1即可
         */

       /* String baseUrl = "https://jianguan.urlsec.qq.com/";
        Long dateTime = new Date().getTime() / 1000;
        int unixTime = dateTime.intValue();
        String appid = "fagaiwei";
        String pwd = "fgwprice";
        int appType = 0;
        int dateType = 1;
        System.out.println(unixTime);
        String signid = MD5Utils.getMD5(unixTime + MD5Utils.getMD5(appid + pwd));
        Map<String, Object> params = new HashMap<>();
        params.put("unixTime", unixTime);
        params.put("appid", appid);
        params.put("signid", signid);
        params.put("programmeId", 409);
        params.put("appType", appType);
        params.put("dateType", dateType);
        JSON.toJSONString(params);
        String apiUrl = "analyze/getHeatTrend";
        String jsonStr = JSON.toJSONString(params);
        System.out.println("params:" + jsonStr);
        String res = HttpUtil.doPostJson(baseUrl + apiUrl, jsonStr);
        System.out.println(res);*/

        long unixTime = new Date().getTime() / 1000;
        String signid = MD5Utils.getMD5(unixTime + MD5Utils.getMD5(APPID + PWD));
        Map<String, Object> params = new HashMap<>();
        params.put("unixTime", unixTime);
        params.put("appid", "fagaiwei");
        params.put("signid", signid);
        params.put("node_userid", "0");
        final String apiUrl = "analyze/getProgrammeDistribution";
//        https://jianguan.urlsec.qq.com/analyze/getTopUrlInfo
        String jsonStr = JSON.toJSONString(params);
        System.out.println("getProgrammeDistribution url:" + PATH + apiUrl);
        String res = null;
        try {
            res = HttpUtil.doPostJson(PATH + apiUrl, jsonStr);
            logger.info("res:{}", res);
            System.out.println("getProgrammeDistribution url:" + PATH + apiUrl);
            System.out.println("the getProgrammeDistribution req params:" + jsonStr);

        } catch (Exception e) {
            logger.error("call-topurl信息-异常:{}", e);
//            return R.error("调用腾讯接口-getTopUrlInfo异常");
        }
//        Object result = converResult(res);
//        logger.info("the result:{}", result);
//        return R.ok().put("data", result);*/
    }
}
