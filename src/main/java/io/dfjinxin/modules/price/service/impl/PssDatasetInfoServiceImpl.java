package io.dfjinxin.modules.price.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.modules.analyse.dao.WpBaseIndexInfoDao;
import io.dfjinxin.modules.analyse.dao.WpMcroIndexInfoDao;
import io.dfjinxin.modules.analyse.entity.WpBaseIndexInfoEntity;
import io.dfjinxin.modules.analyse.entity.WpMcroIndexInfoEntity;
import io.dfjinxin.modules.analyse.service.WpBaseIndexInfoService;
import io.dfjinxin.modules.analyse.service.WpMcroIndexInfoService;
import io.dfjinxin.modules.price.dao.PssDatasetInfoDao;
import io.dfjinxin.modules.price.dto.DataSetIndexInfoDto;
import io.dfjinxin.modules.price.entity.PssDatasetInfoEntity;
import io.dfjinxin.modules.price.service.PssDatasetInfoService;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("pssDatasetInfoService")
public class PssDatasetInfoServiceImpl extends ServiceImpl<PssDatasetInfoDao, PssDatasetInfoEntity> implements PssDatasetInfoService {

    @Autowired
    WpBaseIndexInfoService wpBaseIndexInfoService;
    @Autowired
    WpMcroIndexInfoService wpMcroIndexInfoService;

    @Autowired
    WpBaseIndexInfoDao wpBaseIndexInfoDao;
    @Autowired
    WpMcroIndexInfoDao wpMcroIndexInfoDao;

    @Override
    public List<PssDatasetInfoEntity> listAll() {
//        return baseMapper.selectList(new QueryWrapper());
        return baseMapper.getDataSetList();
    }

    @Override
    public PssDatasetInfoEntity getPssDatasetInfoById(int id) {
        PssDatasetInfoEntity pssDatasetInfoEntity = new PssDatasetInfoEntity();
        pssDatasetInfoEntity.setDataSetId(id);
        return baseMapper.selectById(pssDatasetInfoEntity);
    }

    @Override
    public void setPssDatasetInfoIndeName(PssDatasetInfoEntity pssDatasetInfoEntity) {
        try {
            Object jsonObject = JSON.parse(pssDatasetInfoEntity.getIndeVar(), Feature.OrderedField);
            Map indeNames = new HashedMap();
            if (((JSONObject) jsonObject).get("comm_table") != null) {
                String jsonStr = ((JSONObject) jsonObject).get("comm_table").toString();
                jsonStr = jsonStr.substring(jsonStr.indexOf("[") + 1, jsonStr.indexOf("]"));
                String[] idsOrder = jsonStr.split(",");
                List<WpBaseIndexInfoEntity> wpAsciiInfoEntityList = wpBaseIndexInfoService.getIndexTreeByIds(idsOrder);
                final String[] names = new String[wpAsciiInfoEntityList.size()];

                for (int i = 0; i < idsOrder.length; i++) {
                    int t = i;
                    wpAsciiInfoEntityList.forEach(f -> {
                        if (f.getIndexId().toString().equals(idsOrder[t])) {
                            names[t] = f.getIndexName();
                        }
                    });
                }
                indeNames.put("commIndexNames", names);
            }
            if (((JSONObject) jsonObject).get("macro_table") != null) {
                String jsonStr = ((JSONObject) jsonObject).get("macro_table").toString();
                jsonStr = jsonStr.substring(jsonStr.indexOf("[") + 1, jsonStr.indexOf("]"));
                String[] idsOrder = jsonStr.split(",");
                List<WpMcroIndexInfoEntity> wpAsciiInfoEntityList = wpMcroIndexInfoService.getIndexTreeByIds(idsOrder);
                final String[] names = new String[wpAsciiInfoEntityList.size()];

                for (int j = 0; j < idsOrder.length; j++) {
                    int t = j;
                    wpAsciiInfoEntityList.forEach(f -> {
                        if (f.getIndexId().toString().equals(idsOrder[t])) {
                            names[t] = f.getIndexName();
                        }
                    });
                }

                indeNames.put("macroIndexNames", names);
            }

            pssDatasetInfoEntity.setIndeName(JSONObject.toJSONString(indeNames));
        } catch (Exception e) {

        }
    }

    /**
     * @Desc: 数据集-查询指标(商品指标信息&宏观指标信息)信息
     * @Param: []
     * @Return: java.util.List<io.dfjinxin.modules.price.entity.PssDatasetInfoEntity>
     * @Author: z.h.c
     * @Date: 2019/11/28 17:57
     */
    @Override
    public List<DataSetIndexInfoDto> getIndexInfoByDataSetIndeVal(String indeVal) {
        if (StringUtils.isEmpty(indeVal)) return new ArrayList<>();

        JSONObject jsonObj = JSONObject.parseObject(indeVal);
        List<DataSetIndexInfoDto> res = new ArrayList<>();

        //商品信息指标ids
        if (jsonObj.containsKey("comm_table")) {
            JSONArray commIndexIdArr = jsonObj.getJSONArray("comm_table");
            commIndexIdArr.forEach(indexId -> {
                DataSetIndexInfoDto dto = wpBaseIndexInfoDao.getIndexInfoByIndexId(indexId);
                res.add(dto);
            });
        }

        if (jsonObj.containsKey("macro_table")) {
            JSONArray macroIndexIdArr = jsonObj.getJSONArray("macro_table");
            macroIndexIdArr.forEach(indexId -> {
                DataSetIndexInfoDto dto = wpMcroIndexInfoDao.getIndexInfoByIndexId(indexId);
                res.add(dto);
            });
        }
        return res;
    }

    @Override
    public PageUtils queryByPage(Map<String, Object> params) {
        Page page = new Page((Integer) params.get("pageIndex"), (Integer) params.get("pageSize"));
        page = super.baseMapper.queryByPage(page, params);
        return new PageUtils(page);
    }

    @Override
    public List<PssDatasetInfoEntity> getDataSetList() {
        return baseMapper.getDataSetList();
    }

    /**
     * @Desc: 转换数据集的val&name值
     * @Param: [pssDatasetInfoEntity]
     * @Return: void
     * @Author: z.h.c
     * @Date: 2020/1/13 16:51
     */
    @Override
    public void converValAndName(PssDatasetInfoEntity entity) {

        String dateFeature = entity.getDateFeature();
        List<String> dateFeatureList = new ArrayList<>();
        if(StringUtils.isNotEmpty(dateFeature)){
            String[] strArr = dateFeature.split(",");
            dateFeatureList = Arrays.asList(strArr);
        }

        String indeVar = entity.getIndeVar();
        JSONObject indeNameObj = JSON.parseObject(indeVar);
        Map<String, Object> map = new HashMap<>();
        map.put("comm_table", indeNameObj.containsKey("comm_table") ? getIndexNameById("comm", indeNameObj.getString("comm_table")) : new ArrayList<String>());
        map.put("macro_table", indeNameObj.containsKey("macro_table") ? getIndexNameById("macro", indeNameObj.getString("macro_table")) : new ArrayList<String>());
        map.put("date_feature", dateFeatureList);
        entity.setIndeName(JSONObject.toJSONString(map));
        map.clear();

        JSONObject indeVarObj = JSON.parseObject(indeVar);
        map.put("comm_table", indeVarObj.containsKey("comm_table") ? indeVarObj.get("comm_table") : new ArrayList<String>());
        map.put("macro_table", indeVarObj.containsKey("macro_table") ? indeVarObj.get("macro_table") : new ArrayList<String>());
        map.put("date_feature", dateFeatureList);
        entity.setIndeVar(JSONObject.toJSONString(map));
    }

    private List<String> getIndexNameById(String type, String indexList) {

        List<String> names = new ArrayList<>();
        indexList = indexList.replace("[", "");
        indexList = indexList.replace("]", "");

        if ("comm".equals(type)) {
            QueryWrapper<WpBaseIndexInfoEntity> where = new QueryWrapper<>();
            where.inSql("index_id", indexList);
            List<WpBaseIndexInfoEntity> entities = wpBaseIndexInfoDao.selectList(where);
            entities.forEach(en -> names.add(en.getIndexName()));
        } else {
            QueryWrapper<WpMcroIndexInfoEntity> where2 = new QueryWrapper<>();
            where2.inSql("index_id", indexList);
            List<WpMcroIndexInfoEntity> entities = wpMcroIndexInfoDao.selectList(where2);
            entities.forEach(en -> names.add(en.getIndexName()));
        }
        return names;
    }
}
