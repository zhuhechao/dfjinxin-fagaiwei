package io.dfjinxin.modules.price.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.modules.price.dto.PssAnalyInfoDto;
import io.dfjinxin.modules.price.entity.PssAnalyInfoEntity;
import io.dfjinxin.modules.price.entity.PssAnalyReltEntity;
import io.dfjinxin.modules.price.entity.PssDatasetInfoEntity;
import io.dfjinxin.modules.price.service.PssAnalyInfoService;
import io.dfjinxin.modules.price.service.PssAnalyReltService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.zookeeper.server.DataNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author bourne
 * @email kuibobo@gmail.com
 * @date 2019-09-02 17:03:24
 */
@RestController
@RequestMapping("price/pssanalyinfo")
@Api(tags = "分析信息")
public class PssAnalyInfoController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PssAnalyInfoService pssAnalyInfoService;

    @Autowired
    private PssAnalyReltService pssAnalyReltService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("price:pssanalyinfo:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = pssAnalyInfoService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{analyId}")
    @RequiresPermissions("price:pssanalyinfo:info")
    public R info(@PathVariable("analyId") Integer analyId) {
        PssAnalyInfoEntity pssAnalyInfo = pssAnalyInfoService.getById(analyId);

        return R.ok().put("pssAnalyInfo", pssAnalyInfo);
    }

    /**
     * 运行一般相关性分析
     */
    @PostMapping("/runGeneral ")
    @ApiOperation("运行")
    public R runGeneral (@RequestBody PssAnalyInfoDto dto) {

//        R r = testCallPy();
        R r = callRet();
        JSONObject jsonObject = (JSONObject)r.get("data");
        if(null!=r && r.get("code").toString().equals("0")){
            pssAnalyInfoService.saveOrUpdate(dto);
            PssAnalyReltEntity relt = new PssAnalyReltEntity();
            relt.analyInfoToRelEnt(PssAnalyInfoEntity.toEntity(dto));
            if(null!=jsonObject.get("pValue")){
                JSONArray jsonArray = JSONArray.parseArray(jsonObject.get("pValue").toString());
                relt.setPvalue(jsonArray.toJSONString());
            }
            if(null!=jsonObject.get("coe")) {
                JSONArray jsonArray = JSONArray.parseArray(jsonObject.get("coe").toString());
                relt.setAnalyCoe(jsonArray.toJSONString());
            }
            pssAnalyReltService.saveOrUpdate(relt);
        }

        return R.ok();
    }

    public R callRet() {
        String strRet= "{\n" +
                "  \"msg\": \"success\",\n" +
                "  \"code\": 0,\n" +
                "  \"data\": [\n" +
                "    \"[[1]]\",\n" +
                "    \"[{\"猪生产价格指数\":0,\"粮食产量\":0,\"粮食价格指数\":0,\"人均纯收入\":0,\"人均猪肉消费量\":0.0043,\"存栏数\":0,\"猪肉产量\":0,\"_row\":\"猪生产价格指数\"},{\"猪生产价格指数\":0,\"粮食产量\":0,\"粮食价格指数\":0,\"人均纯收入\":0,\"人均猪肉消费量\":0.0001,\"存栏数\":0,\"猪肉产量\":0,\"_row\":\"粮食产量\"},{\"猪生产价格指数\":0,\"粮食产量\":0,\"粮食价格指数\":0,\"人均纯收入\":0,\"人均猪肉消费量\":0.0043,\"存栏数\":0,\"猪肉产量\":0,\"_row\":\"粮食价格指数\"},{\"猪生产价格指数\":0,\"粮食产量\":0,\"粮食价格指数\":0,\"人均纯收入\":0,\"人均猪肉消费量\":0.0012,\"存栏数\":0,\"猪肉产量\":0,\"_row\":\"人均纯收入\"},{\"猪生产价格指数\":0.0022,\"粮食产量\":0,\"粮食价格指数\":0.0029,\"人均纯收入\":0.0004,\"人均猪肉消费量\":0,\"存栏数\":0.0003,\"猪肉产量\":0.0001,\"_row\":\"人均猪肉消费量\"},{\"猪生产价格指数\":0,\"粮食产量\":0,\"粮食价格指数\":0,\"人均纯收入\":0,\"人均猪肉消费量\":0.0001,\"存栏数\":0,\"猪肉产量\":0,\"_row\":\"存栏数\"},{\"猪生产价格指数\":0,\"粮食产量\":0,\"粮食价格指数\":0,\"人均纯收入\":0,\"人均猪肉消费量\":0,\"存栏数\":0,\"猪肉产量\":0,\"_row\":\"猪肉产量\"}] \",\n" +
                "    \"\",\n" +
                "    \"[[2]]\",\n" +
                "    \"[\",\n" +
                "    \"  {\",\n" +
                "    \"    \"猪生产价格指数\": 1,\",\n" +
                "    \"    \"粮食产量\": 0.8805,\",\n" +
                "    \"    \"粮食价格指数\": 0.9633,\",\n" +
                "    \"    \"人均纯收入\": 0.8756,\",\n" +
                "    \"    \"人均猪肉消费量\": 0.5379,\",\n" +
                "    \"    \"存栏数\": 0.9057,\",\n" +
                "    \"    \"猪肉产量\": 0.9156,\",\n" +
                "    \"    \"_row\": \"猪生产价格指数\"\",\n" +
                "    \"  },\",\n" +
                "    \"  {\",\n" +
                "    \"    \"猪生产价格指数\": 0.8805,\",\n" +
                "    \"    \"粮食产量\": 1,\",\n" +
                "    \"    \"粮食价格指数\": 0.8613,\",\n" +
                "    \"    \"人均纯收入\": 0.7915,\",\n" +
                "    \"    \"人均猪肉消费量\": 0.6921,\",\n" +
                "    \"    \"存栏数\": 0.8642,\",\n" +
                "    \"    \"猪肉产量\": 0.878,\",\n" +
                "    \"    \"_row\": \"粮食产量\"\",\n" +
                "    \"  },\",\n" +
                "    \"  {\",\n" +
                "    \"    \"猪生产价格指数\": 0.9633,\",\n" +
                "    \"    \"粮食产量\": 0.8613,\",\n" +
                "    \"    \"粮食价格指数\": 1,\",\n" +
                "    \"    \"人均纯收入\": 0.939,\",\n" +
                "    \"    \"人均猪肉消费量\": 0.5256,\",\n" +
                "    \"    \"存栏数\": 0.8972,\",\n" +
                "    \"    \"猪肉产量\": 0.9466,\",\n" +
                "    \"    \"_row\": \"粮食价格指数\"\",\n" +
                "    \"  },\",\n" +
                "    \"  {\",\n" +
                "    \"    \"猪生产价格指数\": 0.8756,\",\n" +
                "    \"    \"粮食产量\": 0.7915,\",\n" +
                "    \"    \"粮食价格指数\": 0.939,\",\n" +
                "    \"    \"人均纯收入\": 1,\",\n" +
                "    \"    \"人均猪肉消费量\": 0.6036,\",\n" +
                "    \"    \"存栏数\": 0.8545,\",\n" +
                "    \"    \"猪肉产量\": 0.9409,\",\n" +
                "    \"    \"_row\": \"人均纯收入\"\",\n" +
                "    \"  },\",\n" +
                "    \"  {\",\n" +
                "    \"    \"猪生产价格指数\": 0.5379,\",\n" +
                "    \"    \"粮食产量\": 0.6921,\",\n" +
                "    \"    \"粮食价格指数\": 0.5256,\",\n" +
                "    \"    \"人均纯收入\": 0.6036,\",\n" +
                "    \"    \"人均猪肉消费量\": 1,\",\n" +
                "    \"    \"存栏数\": 0.6599,\",\n" +
                "    \"    \"猪肉产量\": 0.7011,\",\n" +
                "    \"    \"_row\": \"人均猪肉消费量\"\",\n" +
                "    \"  },\",\n" +
                "    \"  {\",\n" +
                "    \"    \"猪生产价格指数\": 0.9057,\",\n" +
                "    \"    \"粮食产量\": 0.8642,\",\n" +
                "    \"    \"粮食价格指数\": 0.8972,\",\n" +
                "    \"    \"人均纯收入\": 0.8545,\",\n" +
                "    \"    \"人均猪肉消费量\": 0.6599,\",\n" +
                "    \"    \"存栏数\": 1,\",\n" +
                "    \"    \"猪肉产量\": 0.958,\",\n" +
                "    \"    \"_row\": \"存栏数\"\",\n" +
                "    \"  },\",\n" +
                "    \"  {\",\n" +
                "    \"    \"猪生产价格指数\": 0.9156,\",\n" +
                "    \"    \"粮食产量\": 0.878,\",\n" +
                "    \"    \"粮食价格指数\": 0.9466,\",\n" +
                "    \"    \"人均纯收入\": 0.9409,\",\n" +
                "    \"    \"人均猪肉消费量\": 0.7011,\",\n" +
                "    \"    \"存栏数\": 0.958,\",\n" +
                "    \"    \"猪肉产量\": 1,\",\n" +
                "    \"    \"_row\": \"猪肉产量\"\",\n" +
                "    \"  }\",\n" +
                "    \"] \",\n" +
                "    \"\",\n" +
                "    \"\"\n" +
                "  ]\n" +
                "}";
        strRet = testCallPy().get("data").toString();
        JSONObject jsonObject = transStrToJsonObject(strRet);
        return R.ok().put("data",jsonObject
        );
    }

    private JSONObject transStrToJsonObject(String retStr){
        if(StringUtils.isEmpty(retStr))
            return null;
        String str = retStr;
        str = retStr.replace("\\","").replace("\"    \"","\"").replace(",\",",",").replace("\"\"","\"").replace("\"  {\",","{").replace("\"  }","}").replace("\n","").replace(",    }","}");
        final  String dataOne = "[[1]]";
        final  String dataSec = "[[2]]";
        str = str.replace(" ","");
        String header = str.substring(0,str.indexOf(",\"data\""))+"}";
        JSONObject jsonObject = null;
        try {
            String data = str.substring(str.indexOf(dataOne) + dataOne.length() + 4, str.indexOf("}]") + 1);
            str = str.substring(str.indexOf(dataSec) + dataSec.length() + 1);
            str = str.substring(str.indexOf("\"[\",") + 4, str.indexOf(",\"]") - 1);
            jsonObject = JSONObject.parseObject(header);
            try {
                JSONArray jsonArrayOne = JSON.parseArray("[" + data + "]");
                jsonObject.put("pValue",jsonArrayOne);
            }catch(Exception eo){

            }
            try {
                JSONArray jsonArraySec = JSONArray.parseArray("[" + str + "]");
                jsonObject.put("coe",jsonArraySec);
            }catch(Exception es){

            }


        }catch (Exception e){

        }
        return jsonObject;
    }

    @PostMapping("/testPy")
    @ApiOperation("测试调用python")
    public R testCallPy() {
//        List<String> list = new ArrayList();
        StringBuffer stringBuffer = new StringBuffer();
        logger.debug("调用 python start");
        String file = "/zhjg/pyjiaoben/corr_ana.py";
        logger.debug("file path:" + file);
        String[] args = new String[]{"python", file, "/zhjg/", "猪生产价格指数&粮食产量&粮食价格指数&人均纯收入&人均猪肉消费量&存栏数&猪肉产量"};
        for (String str : args) {
            logger.debug("call py arg:" + str);
        }
        try {
            Process proc = Runtime.getRuntime().exec(args);// 执行py文件
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                logger.debug("call result the result:" + line);
//                list.add(line);
                stringBuffer.append(line);
            }
            in.close();
            proc.waitFor();
            logger.debug("调用 end");
        } catch (Exception e1) {
            logger.debug("调用 py异常");
            logger.debug(e1.getMessage());
        }
//        return R.ok().put("data", list);
        return R.ok().put("data",stringBuffer.toString());
    }


    @GetMapping("/bussType/{bussType}")
    @ApiOperation(value = "根据业务类型查询分析类型", notes = "1:相关性分析;2:因果分析")
    public R getAnalyWayByBussType(@PathVariable("bussType") Integer bussType) {
        List<PssAnalyReltEntity> analyWayList = pssAnalyInfoService.getAnalyWayByBussType(bussType);
        return R.ok().put("data", analyWayList);
    }

    @GetMapping("/analyWay/{AnalyWay}")
    @ApiOperation(value = "根据分析类型查询该类型的结果集")
    public R getDataSetByAnalyWay(@PathVariable("AnalyWay") String AnalyWay) {
        List<PssDatasetInfoEntity> dataSetList = pssAnalyInfoService.getDataSetByAnalyWay(AnalyWay);
        return R.ok().put("data", dataSetList);
    }

}
