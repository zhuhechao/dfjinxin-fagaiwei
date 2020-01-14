package io.dfjinxin.modules.price.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.common.utils.python.PythonApiUtils;
import io.dfjinxin.modules.price.dao.PssAnalyReltDao;
import io.dfjinxin.modules.price.dto.PssAnalyInfoDto;
import io.dfjinxin.modules.price.entity.PssAnalyInfoEntity;
import io.dfjinxin.modules.price.entity.PssAnalyReltEntity;
import io.dfjinxin.modules.price.entity.PssDatasetInfoEntity;
import io.dfjinxin.modules.price.service.PssAnalyInfoService;
import io.dfjinxin.modules.price.service.PssAnalyReltService;
import io.dfjinxin.modules.price.service.PssDatasetInfoService;
import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Service("pssAnalyReltService")
public class PssAnalyReltServiceImpl extends ServiceImpl<PssAnalyReltDao, PssAnalyReltEntity> implements PssAnalyReltService {

    private final static Logger logger = LoggerFactory.getLogger(PssAnalyReltServiceImpl.class);

    @Value("${python.url}")
    private String url;

    @Autowired
    private PssAnalyReltDao pssAnalyReltDao;

    @Autowired
    private PssAnalyInfoService pssAnalyInfoService;

    @Autowired
    private PssDatasetInfoService pssDatasetInfoService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page page = new Page((Integer) params.get("pageIndex"), (Integer) params.get("pageSize"));
        page = super.baseMapper.queryPage(page, params);
        return new PageUtils(page);
    }

    @Override
    public List<PssAnalyReltEntity> getList(Map<String, Object> params) {
        return pssAnalyReltDao.getList(params);
    }

    @Override
    public PssAnalyReltEntity selectByAnalyId(Integer analyId) {
        return baseMapper.selectByAnalyId(analyId);
    }

    /**
     * @Desc: 分析
     * @Param: [dto]
     * @Return: java.util.Map
     * @Author: z.h.c
     * @Date: 2020/1/14 10:36
     */
    @Override
    public Map runGenera(PssAnalyInfoDto dto) {
        PssDatasetInfoEntity pssDatasetInfoEntity = pssDatasetInfoService.getPssDatasetInfoById(dto.getDataSetId());
        if (pssDatasetInfoEntity == null) return R.error("数据集," + dto.getDataSetId() + " 不存在!");

        String retStr = "";
        JSONObject jsonObject = new JSONObject();
        if ("偏相关性分析".equals(dto.getAnalyWay())) {
            jsonObject.put("table", pssDatasetInfoEntity.getDataSetEngName());
            jsonObject.put("indepVar", dto.getIndeVar());
            retStr = this.callPython(url + "pCorAna", jsonObject);
        } else if ("格兰杰".equals(dto.getAnalyWay())) {
            jsonObject.put("table", pssDatasetInfoEntity.getDataSetEngName());
            jsonObject.put("indepVar", dto.getIndeVar());
            jsonObject.put("depeVar", dto.getDepeVar());
            retStr = this.callPython(url + "grangerAna", jsonObject);
        } else if (dto.getAnalyWay().equals("路径分析")) {
            jsonObject.put("table", pssDatasetInfoEntity.getDataSetEngName());
            jsonObject.put("indepVar", dto.getIndeVar());
            //只能有一个值，要区分宏观、非宏观
            jsonObject.put("depeVar", dto.getDepeVar());
            retStr = this.callPython(url + "pathAna", jsonObject);
        } else {//一般相关性分析
            jsonObject.put("table", pssDatasetInfoEntity.getDataSetEngName());
            jsonObject.put("indepVar", dto.getIndeVar());
            retStr = this.callPython(url + "CorAna", jsonObject);
        }
        JSONObject jsonRet = JSONObject.parseObject(retStr);

        String code = jsonRet.containsKey("code") ? jsonRet.getString("code") : "";
        if (code == null || !"succ".equals(code)) {
            return null;
        }

        JSONObject jsonData = jsonRet.containsKey("data") ? jsonRet.getJSONObject("data") : null;

        pssAnalyInfoService.saveOrUpdate(dto);
        PssAnalyReltEntity relt = new PssAnalyReltEntity();
        relt.analyInfoToRelEnt(PssAnalyInfoEntity.toEntity(dto));
        if (null != jsonObject.get("pValue")) {
            Object jsonArray = JSONArray.parse(jsonObject.get("pValue").toString(), Feature.OrderedField);
            relt.setPvalue(jsonArray.toString());
        }
        if (null != jsonObject.get("coe")) {
            Object jsonArray = JSONArray.parse(jsonObject.get("coe").toString(), Feature.OrderedField);
            relt.setAnalyCoe(jsonArray.toString());
        }
        PssAnalyInfoEntity p = PssAnalyInfoEntity.toEntity(dto);
        Map map = new CaseInsensitiveMap();

        if (dto.getAnalyId() != null)
            map.put("analyId", dto.getAnalyId());
        else {
            map.put("analyName", relt.getReltName());
            map.put("analyWay", relt.getAnalyWay());
            map.put("datasetId", dto.getDataSetId());
            map.put("remarks", dto.getRemarks());
            map.put("indeVar", p.getIndeVar());
            map.put("depeVar", dto.getDepeVar());
        }
        List<PssAnalyReltEntity> list = this.getList(map);
        if (list != null && list.size() > 0)
            relt.setReltId(list.get(0).getReltId());

        relt.setRunTime(new Date());
        this.saveOrUpdate(relt);
        Map data = new LinkedHashMap();
        data.put("pva", relt.getPvalue());
        if (!StringUtils.isEmpty(relt.getAnalyCoe()))
            data.put("coe", relt.getAnalyCoe());


        return data;
    }

    /**
     * 调用相关分析py
     *
     * @param url
     * @param jsonObject
     * @return
     */
    private String callPython(String url, JSONObject jsonObject) {
        String retStr = null;
        logger.info("调用python分析接口-url:{}", url);
        logger.info("调用python分析接口-reqParams:{}", jsonObject.toJSONString());
        try {
            retStr = PythonApiUtils.doPost(url, jsonObject.toJSONString());
        } catch (Exception e) {
            logger.error(e.toString());
        } finally {
            return retStr;
        }
    }
}
