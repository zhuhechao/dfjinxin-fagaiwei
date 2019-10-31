package io.dfjinxin.modules.price.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.modules.analyse.entity.WpBaseIndexInfoEntity;
import io.dfjinxin.modules.analyse.service.WpBaseIndexInfoService;
import io.dfjinxin.modules.price.dao.PssDatasetInfoDao;
import io.dfjinxin.modules.price.entity.PssDatasetInfoEntity;
import io.dfjinxin.modules.price.service.PssDatasetInfoService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("pssDatasetInfoService")
public class PssDatasetInfoServiceImpl extends ServiceImpl<PssDatasetInfoDao, PssDatasetInfoEntity> implements PssDatasetInfoService {

    @Override
    public List<PssDatasetInfoEntity> listAll() {
        return baseMapper.selectList(new QueryWrapper());
    }

    @Override
    public PssDatasetInfoEntity getPssDatasetInfoById(int id) {
        PssDatasetInfoEntity pssDatasetInfoEntity = new PssDatasetInfoEntity();
        pssDatasetInfoEntity.setDataSetId(id);
        return baseMapper.selectById(pssDatasetInfoEntity);
    }

    @Autowired
    WpBaseIndexInfoService wpBaseIndexInfoService;

    @Override
    public void setPssDatasetInfoIndeName(PssDatasetInfoEntity pssDatasetInfoEntity) {
        try {
            Object jsonObject = JSON.parse(pssDatasetInfoEntity.getIndeVar().toString(), Feature.OrderedField);
            String jsonStr = jsonObject.toString();
            jsonStr = jsonStr.substring(jsonStr.indexOf("[")+1, jsonStr.indexOf("]"));
            String []idsOrder = jsonStr.split(",");
            List<WpBaseIndexInfoEntity> wpAsciiInfoEntityList = wpBaseIndexInfoService.getIndexTreeByIds(idsOrder);
            final String[] names = new String[wpAsciiInfoEntityList.size()];

            for(int i=0;i<idsOrder.length;i++){
                int t = i;
                wpAsciiInfoEntityList.forEach(f->{
                    if(f.getIndexId().toString().equals(idsOrder[t])) {
                        names[t] = f.getIndexName();
                    }
                });
            }
            Map indeNames = new HashedMap();
            indeNames.put("indeNames",names);
            pssDatasetInfoEntity.setIndeName(JSONObject.toJSONString(indeNames));
        }catch (Exception e){

        }
    }
}
