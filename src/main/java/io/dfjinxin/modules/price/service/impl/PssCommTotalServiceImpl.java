package io.dfjinxin.modules.price.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.dto.PssCommTotalDto;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.Query;
import io.dfjinxin.modules.price.dao.PssCommTotalDao;
import io.dfjinxin.modules.price.entity.PssCommTotalEntity;
import io.dfjinxin.modules.price.service.PssCommTotalService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("pssCommTotalService")
public class PssCommTotalServiceImpl extends ServiceImpl<PssCommTotalDao, PssCommTotalEntity> implements PssCommTotalService {

    @Autowired
    private PssCommTotalDao pssCommTotalDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PssCommTotalEntity> page = this.page(
                new Query<PssCommTotalEntity>().getPage(params),
                new QueryWrapper<PssCommTotalEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public Map<String, List<PssCommTotalEntity>> queryCommType() {

        QueryWrapper where1 = new QueryWrapper();
        where1.eq("level_code", "0");
        where1.eq("data_flag", "0");

        List<PssCommTotalEntity> commType1 = baseMapper.selectList(where1);
        List<PssCommTotalEntity> commType2 = new ArrayList<>();
        List<PssCommTotalEntity> commType3 = new ArrayList<>();
        List<PssCommTotalEntity> commType4 = new ArrayList<>();
        Map<String, List<PssCommTotalEntity>> resultMap = new HashMap<>();
        for (PssCommTotalEntity entity : commType1) {
            QueryWrapper where2 = new QueryWrapper();
            where2.in("parent_code", entity.getCommId());
            where2.eq("data_flag", "0");
            where2.eq("level_code", "1");
            List<PssCommTotalEntity> subType = baseMapper.selectList(where2);
            commType2.addAll(subType);
        }

        for (PssCommTotalEntity entity : commType2) {
            QueryWrapper where3 = new QueryWrapper();
            where3.in("parent_code", entity.getCommId());
            where3.eq("data_flag", "0");
            where3.eq("level_code", "2");
            List<PssCommTotalEntity> subType3 = baseMapper.selectList(where3);
            commType3.addAll(subType3);
        }

        for (PssCommTotalEntity entity : commType3) {
            QueryWrapper where4 = new QueryWrapper();
            where4.in("parent_code", entity.getCommId());
            where4.eq("data_flag", "0");
            where4.eq("level_code", "3");
            List<PssCommTotalEntity> subType4 = baseMapper.selectList(where4);
            commType4.addAll(subType4);
        }

        //商品类型-0类 大宗，民生
        resultMap.put("commLevelCode_0", commType1);
        //商品大类-1类
        resultMap.put("commLevelCode_1", commType2);
        //商品名称-2类
        resultMap.put("commLevelCode_2", commType3);
        resultMap.put("commLevelCode_3", commType4);
        return resultMap;
    }

    @Override
    public PageUtils queryPageList(PssCommTotalDto pssCommTotalDto) {

        String levelCode_0 = pssCommTotalDto.getCommLevelCode_0();
        String levelCode_1 = pssCommTotalDto.getCommLevelCode_1();
        String levelCode_2 = pssCommTotalDto.getCommLevelCode_2();
        //商品类型-0类 为空 查询所有
        if (StringUtils.isBlank(levelCode_0)) {
            QueryWrapper where1 = new QueryWrapper();
            where1.eq("level_code", "0");
            where1.eq("data_flag", "0");
            List<PssCommTotalEntity> commType0 = baseMapper.selectList(where1);

            List<Map<String, PssCommTotalEntity>> resultList = new ArrayList();
            Map<String, PssCommTotalEntity> map = new HashMap<>();
            Integer totalCount = 0;
            for (PssCommTotalEntity entity : commType0) {
                Map<String, Object> temp = selectSubCommByLevelCode0(entity, pssCommTotalDto);
                PssCommTotalEntity result = (PssCommTotalEntity) temp.get("result");
                totalCount = (Integer) temp.get("totalCount");
                if (1 == entity.getCommId()) {
                    map.put("dazong", result);
                } else {
                    map.put("minsheng", result);
                }
            }
            resultList.add(map);
            return new PageUtils(resultList, totalCount, pssCommTotalDto.getPageSize(), pssCommTotalDto.getPageIndex());

        }
        //商品类型-0类 不为空，商品大类-1类 为空，查询指定0类
        if (StringUtils.isNotBlank(levelCode_0) && StringUtils.isBlank(levelCode_1)) {
            PssCommTotalEntity commType0 = selectCommByLevelCode0(Integer.valueOf(levelCode_0));
            int countLevelCode0 = pssCommTotalDao.queryPageListCountByLevelCode0(pssCommTotalDto);
            List<PssCommTotalEntity> listLevelCode0 = pssCommTotalDao.queryPageLisByLevelCode0(pssCommTotalDto);
            commType0.setSubCommList(listLevelCode0);
            List<PssCommTotalEntity> returnList0 = new ArrayList<>();
            returnList0.add(commType0);
            return new PageUtils(returnList0, countLevelCode0, pssCommTotalDto.getPageSize(), pssCommTotalDto.getPageIndex());
        }

        //商品类型-0类 不为空，商品大类-1类 不为空，商品名称为空，查询指定1类
        if (StringUtils.isNotBlank(levelCode_0) && StringUtils.isNotBlank(levelCode_1)
                && StringUtils.isBlank(levelCode_2)) {
            PssCommTotalEntity commType0 = selectCommByLevelCode0(Integer.valueOf(levelCode_0));
            int countLevelCode1 = pssCommTotalDao.queryPageListCountByLevelCode1(pssCommTotalDto);
            List<PssCommTotalEntity> listLevelCode1 = pssCommTotalDao.queryPageLisByLevelCode1(pssCommTotalDto);
            commType0.setSubCommList(listLevelCode1);
            List<PssCommTotalEntity> returnList1 = new ArrayList<>();
            returnList1.add(commType0);
            return new PageUtils(returnList1, countLevelCode1, pssCommTotalDto.getPageSize(), pssCommTotalDto.getPageIndex());
        }

        //商品类型-0类 不为空，商品大类-1类 不为空，商品名称-2类 不为空，查询指定2类
        if (StringUtils.isNotBlank(levelCode_0) && StringUtils.isNotBlank(levelCode_1)
                && StringUtils.isNotBlank(levelCode_2)) {
            PssCommTotalEntity commType0 = selectCommByLevelCode0(Integer.valueOf(levelCode_0));
            int countLevelCode2 = pssCommTotalDao.queryPageListCountByLevelCode2(pssCommTotalDto);
            List<PssCommTotalEntity> listLevelCode2 = pssCommTotalDao.queryPageLisByLevelCode2(pssCommTotalDto);
            commType0.setSubCommList(listLevelCode2);
            List<PssCommTotalEntity> returnList2 = new ArrayList<>();
            returnList2.add(commType0);
            return new PageUtils(returnList2, countLevelCode2, pssCommTotalDto.getPageSize(), pssCommTotalDto.getPageIndex());
        }

        return null;
    }

    @Override
    public List<PssCommTotalEntity> getAll() {
        QueryWrapper where2 = new QueryWrapper();
        where2.eq("data_flag", "0");
        return baseMapper.selectList(where2);
    }

    @Override
    public PageUtils queryCommInfoPageList(PssCommTotalDto params) {
        String levelCode_0 = params.getCommLevelCode_0();
        String levelCode_1 = params.getCommLevelCode_1();
        String levelCode_2 = params.getCommLevelCode_2();
        //商品类型-0类 为空 查询所有
        if (StringUtils.isBlank(levelCode_0)) {
            QueryWrapper where1 = new QueryWrapper();
            where1.eq("level_code", "0");
            where1.eq("data_flag", "0");
            List<PssCommTotalEntity> commType0 = baseMapper.selectList(where1);

            List<Map<String, PssCommTotalEntity>> resultList = new ArrayList();
            Map<String, PssCommTotalEntity> map = new HashMap<>();
            Integer totalCount = 0;
            for (PssCommTotalEntity entity : commType0) {
                Map<String, Object> temp = selectSubCommByLevelCode0(entity, params);
                PssCommTotalEntity result = (PssCommTotalEntity) temp.get("result");
                totalCount = (Integer) temp.get("totalCount");
                if (1 == entity.getCommId()) {
                    map.put("dazong", result);
                } else {
                    map.put("minsheng", result);
                }
            }
            resultList.add(map);
            return new PageUtils(resultList, totalCount, params.getPageSize(), params.getPageIndex());

        }
        //商品类型-0类 不为空，商品大类-1类 为空，查询指定0类
        if (StringUtils.isNotBlank(levelCode_0) && StringUtils.isBlank(levelCode_1)) {
            PssCommTotalEntity commType0 = selectCommByLevelCode0(Integer.valueOf(levelCode_0));
            int countLevelCode0 = pssCommTotalDao.queryPageListCountByLevelCode0(params);
            List<PssCommTotalEntity> listLevelCode0 = pssCommTotalDao.queryPageLisByLevelCode0(params);
            commType0.setSubCommList(listLevelCode0);
            List<PssCommTotalEntity> returnList0 = new ArrayList<>();
            returnList0.add(commType0);
            return new PageUtils(returnList0, countLevelCode0, params.getPageSize(), params.getPageIndex());
        }

        //商品类型-0类 不为空，商品大类-1类 不为空，商品名称为空，查询指定1类
        if (StringUtils.isNotBlank(levelCode_0) && StringUtils.isNotBlank(levelCode_1)
                && StringUtils.isBlank(levelCode_2)) {
            PssCommTotalEntity commType0 = selectCommByLevelCode0(Integer.valueOf(levelCode_0));
            int countLevelCode1 = pssCommTotalDao.queryPageListCountByLevelCode1(params);
            List<PssCommTotalEntity> listLevelCode1 = pssCommTotalDao.queryPageLisByLevelCode1(params);
            commType0.setSubCommList(listLevelCode1);
            List<PssCommTotalEntity> returnList1 = new ArrayList<>();
            returnList1.add(commType0);
            return new PageUtils(returnList1, countLevelCode1, params.getPageSize(), params.getPageIndex());
        }

        //商品类型-0类 不为空，商品大类-1类 不为空，商品名称-2类 不为空，查询指定2类
        if (StringUtils.isNotBlank(levelCode_0) && StringUtils.isNotBlank(levelCode_1)
                && StringUtils.isNotBlank(levelCode_2)) {
            PssCommTotalEntity commType0 = selectCommByLevelCode0(Integer.valueOf(levelCode_0));
//            int countLevelCode2 = pssCommTotalDao.queryPageListCountByLevelCode2(params);
            int countLevelCode2 = pssCommTotalDao.queryCommInfoCountLevelCode2(params);
//            List<PssCommTotalEntity> listLevelCode2 = pssCommTotalDao.queryPageLisByLevelCode2(params);
            List<PssCommTotalEntity> listLevelCode2 = pssCommTotalDao.queryCommInfoLevelCode2(params);
            commType0.setSubCommList(listLevelCode2);
            List<PssCommTotalEntity> returnList2 = new ArrayList<>();
            returnList2.add(commType0);
            return new PageUtils(returnList2, countLevelCode2, params.getPageSize(), params.getPageIndex());
        }

        return null;
    }

    private Map<String, Object> selectSubCommByLevelCode0(PssCommTotalEntity levelCode0, PssCommTotalDto dto) {
        if (levelCode0 == null || levelCode0.getCommId() == null) {
            return null;
        }
        //根据0类查询1类
        QueryWrapper where2 = new QueryWrapper();
        where2.in("parent_code", levelCode0.getCommId());
        where2.eq("data_flag", "0");
        where2.eq("level_code", "1");
        // 获取一类商品
        List<PssCommTotalEntity> commLevelCode1 = baseMapper.selectList(where2);
        for (PssCommTotalEntity entity1 : commLevelCode1) {
            List<PssCommTotalEntity> commLevelCode2 = pssCommTotalDao.selectSubCommByLevelCode2(entity1.getCommId(), dto);
            entity1.setSubCommList(commLevelCode2);
        }
        levelCode0.setSubCommList(commLevelCode1);

        //查询所有levelCode2的总数
        QueryWrapper where3 = new QueryWrapper();
//        where3.in("parent_code", levelCode1Ids);
        where3.eq("data_flag", "0");
        where3.eq("level_code", "2");
        int totalCount = baseMapper.selectCount(where3);

        Map<String, Object> map = new HashMap<>();
        map.put("result", levelCode0);
        map.put("totalCount", totalCount);
        return map;
    }

    private PssCommTotalEntity selectCommByLevelCode0(Integer commId) {

        if (commId == null) {
            return null;
        }
        QueryWrapper where1 = new QueryWrapper();
        where1.eq("level_code", "0");
        where1.eq("data_flag", "0");
        where1.eq("comm_id", commId);
        return baseMapper.selectOne(where1);
    }

}
