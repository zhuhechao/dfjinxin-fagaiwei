package io.dfjinxin.modules.price.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.common.utils.python.PythonApiUtils;
import io.dfjinxin.modules.price.dao.PssAnalyInfoDao;
import io.dfjinxin.modules.price.dao.PssAnalyReltDao;
import io.dfjinxin.modules.price.dto.AnalyReqDto;
import io.dfjinxin.modules.price.entity.PssAnalyInfoEntity;
import io.dfjinxin.modules.price.entity.PssAnalyReltEntity;
import io.dfjinxin.modules.price.entity.PssDatasetInfoEntity;
import io.dfjinxin.modules.price.service.PssAnalyReltService;
import io.dfjinxin.modules.price.service.PssDatasetInfoService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
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
    private PssAnalyInfoDao pssAnalyInfoDao;

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
    public Map runGenera(AnalyReqDto dto) {

        logger.info("分析-开始***");
        logger.info("分析-req params:{}", dto);
        PssDatasetInfoEntity pssDatasetInfoEntity = pssDatasetInfoService.getPssDatasetInfoById(dto.getDataSetId());
        if (pssDatasetInfoEntity == null) return R.error("数据集," + dto.getDataSetId() + " 不存在!");

        JSONObject indeValJsonObj = JSONObject.parseObject(dto.getIndeVar());
        JSONObject depValJsonObj = JSONObject.parseObject(dto.getDepeVar());
        final String engName = pssDatasetInfoEntity.getDataSetEngName();
        String retStr = "";
        JSONObject jsonObject = new JSONObject();
        Map<String, Object> result = new HashMap<>();
        final String analyWag = dto.getAnalyWay();
        PssAnalyInfoEntity infoEntity = new PssAnalyInfoEntity();
        if ("偏相关性分析".equals(analyWag)) {
            jsonObject.put("table", engName);
            jsonObject.put("indepVar", indeValJsonObj);
            retStr = this.callPython(url + "pCorAna", jsonObject);
            result = this.converPythonResult(analyWag, retStr);
            infoEntity.setBussType(1);
        } else if ("一般相关性分析".equals(analyWag)) {//一般相关性分析
            jsonObject.put("table", engName);
            jsonObject.put("indepVar", indeValJsonObj);
            retStr = this.callPython(url + "CorAna", jsonObject);
            result = this.converPythonResult(analyWag, retStr);
            infoEntity.setBussType(1);
        } else if ("路径分析".equals(analyWag)) {
            jsonObject.put("table", engName);
            jsonObject.put("indepVar", indeValJsonObj);
            //depeVar,只能有一个值，要区分宏观、非宏观
            jsonObject.put("depeVar", depValJsonObj);
            retStr = this.callPython(url + "pathAna", jsonObject);
            result = this.converPythonResult(analyWag, retStr);
            infoEntity.setBussType(2);
        } else if ("格兰杰".equals(dto.getAnalyWay())) {
            jsonObject.put("table", engName);
            jsonObject.put("indepVar", indeValJsonObj);
            jsonObject.put("depeVar", depValJsonObj);
            retStr = this.callPython(url + "grangerAna", jsonObject);
            result = this.converPythonResult(analyWag, retStr);
            infoEntity.setBussType(2);
        }

        String code = (String) result.getOrDefault("code", "");
        if (!"succ".equals(code)) return null;

        String indeVal = jsonObject.getString("indepVar");
        String depeVar = jsonObject.getString("depeVar");
        infoEntity.setAnalyName(dto.getAnalyName());
        infoEntity.setAnalyWay(dto.getAnalyWay());
        infoEntity.setDataSetId(pssDatasetInfoEntity.getDataSetId());
        infoEntity.setIndeVar(indeVal);
        infoEntity.setDepeVar(depeVar);
        infoEntity.setCrteTime(new Date());
        infoEntity.setRemarks(dto.getRemarks());
        pssAnalyInfoDao.insert(infoEntity);
        int keyId = infoEntity.getAnalyId();

        PssAnalyReltEntity relt = new PssAnalyReltEntity();
        relt.setReltName(dto.getAnalyName());
        relt.setAnalyWay(dto.getAnalyWay());
        relt.setBasVar(indeVal);
        relt.setTarVar(depeVar);
        relt.setAnalyId(keyId);
        relt.setAnalyCoe(result.getOrDefault("coe", "").toString());
        relt.setPvalue(result.getOrDefault("pva", "").toString());
        relt.setAnalyTime(new Date());
        relt.setRunTime(new Date());
        relt.setRunStatus(0);
        this.save(relt);
        logger.info("分析-结束***");
        result.remove("code");
        return result;
    }

    private Map<String, Object> converPythonResult(String analyWay, String retStr) {
        Map<String, Object> result = new HashMap<>();
        if (StringUtils.isEmpty(retStr)) {
            result.put("code", "");
            return result;
        }

        JSONObject jsonObj = JSONObject.parseObject(retStr);
        String code = jsonObj.containsKey("code") ? jsonObj.getString("code") : "";
        result.put("code", code);
        if (!"succ".equals(code)) {
            return result;
        }

        if ("格兰杰".equals(analyWay)) {
            JSONArray jsonArray = jsonObj.containsKey("data") ? jsonObj.getJSONArray("data") : null;
            result.put("pva", jsonArray);
        } else {
            JSONObject dataObj = jsonObj.containsKey("data") ? jsonObj.getJSONObject("data") : null;
            result.put("coe", dataObj.containsKey("coe") ? dataObj.get("coe") : null);
            result.put("pva", dataObj.containsKey("pva") ? dataObj.get("pva") : null);
        }
        return result;
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
