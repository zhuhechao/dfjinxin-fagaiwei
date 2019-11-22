package io.dfjinxin.modules.hive.service.impl;

import io.dfjinxin.common.exception.RRException;
import io.dfjinxin.config.SystemParams;
import io.dfjinxin.datasource.annotation.DataSource;
import io.dfjinxin.modules.hive.service.HiveService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@DataSource("hiveSource")
public class HiveServiceImpl implements HiveService {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    private final static String SQL_IMPALA = "impala";
    @Autowired
    private SystemParams systemParams;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Map<String, Object>> selectData(String sql) {
        logger.debug("Preparing: (hive) " + sql);
        List<Map<String, Object>> ms = jdbcTemplate.queryForList(sql);
        logger.debug(" Total: (hive) " + (ms != null ? ms.size() : "null"));
        return ms;
    }

    @Override
    public List<Map<String, Object>> pageData(String sql, String sortStr, Integer page, Integer limit) {
        String pageSql = getHivePageSql(sql, sortStr, page, limit);
//        if(SQL_IMPALA.equals(systemParams.getSqlDialect())){
//            pageSql = getImpalaPageSql(sql, rownoStr, sortStr, page, limit);
//        }else{
//            pageSql = getHivePageSql(sql, rownoStr, sortStr, page, limit);
//        }
        List<Map<String, Object>> list = selectData(pageSql);
        return list;
    }

    private String getImpalaPageSql(String sql, String sortStr, Integer page, Integer limit) {
        String pageSql = sql;
        if (StringUtils.isNotEmpty(sortStr))
            pageSql += " order by " + sortStr;
        if (page == 1) {
            pageSql += " limit " + limit;
        } else {
            Integer end = page * limit;
            Integer start = end - limit;
            pageSql += " limit " + end + " offset " + start;
        }
        return pageSql;
    }

    private String getHivePageSql(String sql, String sortStr, Integer page, Integer limit) {
        String pageSql = sql;
        if (page == 0) page = 1;
        Integer start = (page - 1) * limit + 1;
        Integer end = start + limit - 1;
        String orderBy = StringUtils.isNotEmpty(sortStr) ? "order by " + sortStr : "";
        if (page == 1) {
            pageSql = "select x.* from (" + pageSql + " " + orderBy + ") x limit " + limit.toString();
            return pageSql;
        }

        String rnStr = " row_number() over (" + orderBy + ") as rnum, ";
        pageSql = pageSql.replaceFirst("select ", "select" + rnStr);
        pageSql = "select x.* from (" + pageSql + ") x where rnum between " + start.toString() + " and " + end.toString();
        return pageSql;
    }

    public Object checkFunction(String funcName, String param, String type) {

        //type，0：Java UDF，1：Hive Regexp，2：Java Script
        try {
            String sql = "";
            if ("1".equals(type)) {
                sql = "select '" + param + "' regexp '" + funcName + "' result";
            } else {
                sql = "select default." + funcName + "('" + param + "') result";
            }
            List<Map<String, Object>> listMap = this.selectData(sql);
            if (!listMap.isEmpty() && listMap.size() > 0) {
                return listMap.get(0).get("result");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RRException("执行失败");
        }
        return null;
    }

    @Override
    public void dropTable(final String tableName) {
        String sql = "drop table " + tableName;
        logger.debug("Preparing: (hive) :" + sql);
        jdbcTemplate.execute(sql);
    }

}
