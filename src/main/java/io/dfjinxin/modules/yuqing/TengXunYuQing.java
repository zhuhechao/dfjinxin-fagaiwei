package io.dfjinxin.modules.yuqing;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.dfjinxin.common.utils.MD5Utils;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.common.utils.echart.HttpUtil;
import io.dfjinxin.modules.price.service.PssPriceEwarnService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Desc: 腾讯-商品舆情分析
 * @Author: z.h.c
 * @Date: 2019/10/17 12:58
 * @Version: 1.0
 */

@RestController
@RequestMapping("tengxun/analyze/")
@Api(tags = "腾讯-商品舆情Api")
public class TengXunYuQing {

    private static final Logger logger = LoggerFactory.getLogger(TengXunYuQing.class);

    @Value("${tengxun.path}")
    private String path;
    @Value("${tengxun.appId}")
    private String appId;
    @Value("${tengxun.pwd}")
    private String pwd;

    @Autowired
    private PssPriceEwarnService pssPriceEwarnService;
//    接口文件地址 https://docs.qq.com/doc/DQmZ5eGpJTXZQT2d6

    /**
     * 商品舆情热度分布
     * 接口名：
     * <p>
     * <p>
     * 请求：
     * POST
     * Content-Type
     * <p>
     * unixTime：1540794339
     * appid："fagaiwei"
     * signid:：
     * programmeId：方案id long类型  即同步过去的商品id
     * appType：类型 int类型 0：定向监测舆情分析  1：话题分析  这里填0即可
     * dateType：日期类型 int类型 0：今天  1:7天内  2:30天内  这里填1即可
     *
     * @param commId
     * @return
     */
    @PostMapping("getHeatTrend/{commId}")
    @ApiOperation("商品舆情热度分布,参数为3类商品id")
    public R getHeatTrend(@PathVariable Integer commId) {

        logger.info("商品舆情热度分布,请求参数:{}", commId);
        long unixTime = new Date().getTime() / 1000;
        String signid = MD5Utils.getMD5(unixTime + MD5Utils.getMD5(appId + pwd));
        Map<String, Object> params = new HashMap<>();
        params.put("unixTime", unixTime);
        params.put("appid", appId);
        params.put("signid", signid);
        params.put("programmeId", converCommId(commId));
        params.put("appType", 0);
        params.put("dateType", 1);
        String jsonStr = JSON.toJSONString(params);
        logger.info("the getHeatTrend req params:{}", jsonStr);
        String apiUrl = "analyze/getHeatTrend";
        final String url = path + apiUrl;
        logger.info("the request url: {}", url);
        String res = null;
        try {
            res = HttpUtil.doPostJson(url, jsonStr);
        } catch (Exception e) {
            logger.error("call-商品舆情热度分布-异常:{}", e);
            return R.error("调用腾讯接口-getHeatTrend异常");
        }
        Object result = converResult(res);
        logger.info("the result:{}", result);
        return R.ok().put("data", result);
    }

    /**
     * 商品热点舆情
     * 接口名： analyze/getHeatTop
     * 请求：
     * POST
     * Content-Type: application/json
     * <p>
     * 请求参数：
     * unixTime：1540794339
     * appid："fagaiwei"
     * signid:：
     * programmeId：方案id long类型  即同步过去的商品id
     * appType：类型 int类型 0：定向监测舆情分析  1：话题分析  这里填0即可
     * dateType：日期类型 int类型 0：今天  1:7天内  2:30天内  这里填1即可
     *
     * @param commId
     * @return
     */
    @PostMapping("getHeatTop/{commId}")
    @ApiOperation("商品热点舆情,参数为3类商品id")
    public R getHeatTop(@PathVariable Integer commId) {

        logger.info("商品热点舆情,请求参数:{}", commId);
        long unixTime = new Date().getTime() / 1000;
        String signid = MD5Utils.getMD5(unixTime + MD5Utils.getMD5(appId + pwd));
        Map<String, Object> params = new HashMap<>();
        params.put("unixTime", unixTime);
        params.put("appid", appId);
        params.put("signid", signid);
        params.put("programmeId", converCommId(commId));
        params.put("appType", 0);
        params.put("dateType", 1);
        final String apiUrl = "analyze/getHeatTop";
        String jsonStr = JSON.toJSONString(params);
        logger.info("the getHeatTrend req params:{}", jsonStr);
        final String url = path + apiUrl;
        logger.info("the request url: {}", url);
        String res = null;
        try {
            res = HttpUtil.doPostJson(url, jsonStr);
        } catch (Exception e) {
            logger.error("call-商品热点舆情-异常:{}", e);
            return R.error("调用腾讯接口-getHeatTop异常");
        }
        Object result = converResult(res);
        logger.info("the result:{}", result);
        return R.ok().put("data", result);
    }

    /**
     * 商品舆情预警
     * 接口名： yuqingcgi/queryStatisticsWarningContent
     * <p>
     * 请求参数：
     * unixTime：1540794339
     * appid："fagaiwei"
     * signid:：
     * node_userid：用户id string类型 这里填1即可
     * programmeId：方案id long类型  即同步过去的商品id
     * time_type：日期类型 int类型 0:今天,1:昨天,2:近七天,3:近30天  这里填2即可
     * order_type : 排序方式,  int类型  0:按时间降序,1:按时间升序 这里填0即可
     * state_type: 状态 int类型 0:全部,1:已处理,2:未处理 这里填0即可
     * page_num: 页数  int类型  这里填1即可
     * page_size:每页信息条数  int类型   这里填10即可
     *
     * @param commId
     * @return
     */
    @PostMapping("queryStatisticsWarningContent/{commId}")
    @ApiOperation("商品舆情预警,参数为3类商品id")
    public R queryStatisticsWarningContent(@PathVariable Integer commId) {

        logger.info("商品舆情预警,请求参数:{}", commId);
        long unixTime = new Date().getTime() / 1000;
        String signid = MD5Utils.getMD5(unixTime + MD5Utils.getMD5(appId + pwd));
        Map<String, Object> params = new HashMap<>();
        params.put("unixTime", unixTime);
        params.put("appid", appId);
        params.put("signid", signid);
        params.put("node_userid", "1");
        params.put("programmeId", converCommId(commId));
        params.put("time_type", 2);
        params.put("order_type", 0);
        params.put("state_type", 0);
        params.put("page_num", 1);
        params.put("page_size", 10);
        final String apiUrl = "yuqingcgi/queryStatisticsWarningContent";
        String jsonStr = JSON.toJSONString(params);
        logger.info("the getHeatTrend req params:{}", jsonStr);
        final String url = path + apiUrl;
        logger.info("the request url: {}", url);
        String res = null;
        try {
            res = HttpUtil.doPostJson(url, jsonStr);
        } catch (Exception e) {
            logger.error("call-商品舆情预警-异常:{}", e);
            return R.error("调用腾讯接口-queryStatisticsWarningContent异常");
        }
        Object result = converResult(res);
        logger.info("the result:{}", result);
        return R.ok().put("data", result);
    }

    /**
     * topurl信息
     * 接口名： /analyze/getTopUrlInfo
     * 请求：
     * POST
     * Content-Type: application/json
     * <p>
     * 请求参数：
     * unixTime：1540794339
     * appid："fagaiwei"
     * signid:：
     * userId：用户id string类型 这里填1即可
     * dateType：日期类型 int类型 0：今天  1:7天内  2:30天内  这里填1即可
     *
     * @return
     */
    @PostMapping("getTopUrlInfo/")
    @ApiOperation("topurl信息")
    public R getTopUrlInfo() {
        logger.info("topurl信息,开始--");
        long unixTime = new Date().getTime() / 1000;
        String signid = MD5Utils.getMD5(unixTime + MD5Utils.getMD5(appId + pwd));
        Map<String, Object> params = new HashMap<>();
        params.put("unixTime", unixTime);
        params.put("appid", appId);
        params.put("signid", signid);
        params.put("userId", "1");
        params.put("dateType", 1);
        final String apiUrl = "analyze/getTopUrlInfo";
        String jsonStr = JSON.toJSONString(params);
        logger.info("the getTopUrlInfo req params:{}", jsonStr);
        final String url = path + apiUrl;
        logger.info("the request url: {}", url);
        String res = null;
        try {
            res = HttpUtil.doPostJson(url, jsonStr);
            logger.info("res:{}", res);
        } catch (Exception e) {
            logger.error("call-topurl信息-异常:{}", e);
            return R.error("调用腾讯接口-getTopUrlInfo异常");
        }
        Object result = converResult(res);
        logger.info("the result:{}", result);
        return R.ok().put("data", result);
    }

    /**
     * 接口名： analyze/getProgrammeDistribution
     * 请求：获取配置方案结果分布 ,取total_content_cnt值
     * POST
     * Content-Type: application/json
     * <p>
     * unixTime：1540794339
     * appid："fagaiwei"
     * signid:：
     * node_userid：用户id string类型 0
     *
     * @return
     */
    @PostMapping("getProgrammeDistribution/")
    @ApiOperation("获取配置方案结果分布")
    public R getProgrammeDistribution() {
        return R.ok().put("data", pssPriceEwarnService.getProgrammeDistribution());

    }

    public static Object converResult(String jsonStr) {
        logger.info("call tengxun api,the respose is:{}", jsonStr);
        JSONObject jsonObj = JSON.parseObject(jsonStr);
        Integer code = jsonObj.getInteger("code");
        if (code == 0) {
            return jsonObj.get("data");
        } else {
            return jsonObj.get("message");
        }
    }


    private int converCommId(int commId) {
        HashMap<Integer, Integer> map = new HashMap();
        map.put(29, 409);
        map.put(30, 410);
        map.put(31, 411);
        map.put(32, 412);
        map.put(33, 413);
        map.put(34, 414);
        map.put(35, 415);
        map.put(36, 416);
        map.put(37, 417);
        map.put(38, 418);
        map.put(39, 419);
        map.put(40, 420);
        map.put(41, 421);
        map.put(42, 422);
        map.put(43, 423);
        map.put(44, 424);
        map.put(45, 425);
        map.put(46, 426);
        map.put(47, 427);
        map.put(48, 428);
        map.put(49, 429);
        map.put(50, 430);
        map.put(51, 431);
        map.put(52, 432);
        map.put(53, 433);
        map.put(54, 434);
        map.put(55, 435);
        map.put(56, 436);
        map.put(57, 437);
        map.put(58, 438);
        map.put(59, 439);
        map.put(60, 440);
        map.put(61, 441);
        map.put(62, 442);
        map.put(63, 443);
        map.put(64, 444);
        map.put(65, 445);
        map.put(66, 446);
        map.put(67, 447);
        map.put(68, 448);
        map.put(69, 449);
        map.put(70, 450);
        map.put(71, 451);
        map.put(72, 450);
        map.put(73, 452);
        map.put(74, 453);
        map.put(75, 454);
        map.put(76, 455);
        map.put(77, 456);
        map.put(78, 457);
        map.put(79, 458);
        map.put(80, 459);
        map.put(81, 460);
        map.put(82, 461);
        map.put(83, 462);
        map.put(84, 463);
        map.put(85, 464);
        map.put(86, 465);
        map.put(87, 466);
        map.put(88, 467);
        map.put(89, 468);
        map.put(90, 469);
        map.put(91, 470);
        map.put(92, 471);
        map.put(93, 472);
        map.put(94, 473);
        map.put(95, 474);
        map.put(96, 475);
        map.put(97, 476);
        map.put(98, 477);
        map.put(99, 478);
        map.put(100, 479);
        map.put(101, 480);
        map.put(102, 481);
        map.put(103, 482);
        map.put(104, 483);
        map.put(105, 484);
        map.put(106, 485);
        map.put(107, 486);
        map.put(108, 487);
        map.put(109, 488);
        map.put(110, 489);
        map.put(111, 490);
        map.put(112, 491);
        map.put(113, 492);
        map.put(295, 493);
        return map.get(commId);
    }


}
