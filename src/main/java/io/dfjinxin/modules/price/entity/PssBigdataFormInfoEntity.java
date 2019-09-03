package io.dfjinxin.modules.price.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.dfjinxin.modules.price.dto.PssBigdataFormInfoDto;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.*;

/**
 * @author bourne
 * @email kuibobo@gmail.com
 * @date 2019-09-03 17:15:22
 */
@Data
@TableName("pss_bigdata_form_info")
public class PssBigdataFormInfoEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private Integer formId;
    /**
     *
     */
    private String formName;
    /**
     *
     */
    private String formNameC;
    /**
     *
     */
    private String indexType;

    public static PssBigdataFormInfoEntity toEntity(PssBigdataFormInfoDto from) {
        if (null == from) {
            return null;
        }
        PssBigdataFormInfoEntity to = new PssBigdataFormInfoEntity();
        BeanUtils.copyProperties(from, to);
        return to;
    }

    public static PssBigdataFormInfoDto toData(PssBigdataFormInfoEntity from) {
        if (null == from) {
            return null;
        }
        PssBigdataFormInfoDto to = new PssBigdataFormInfoDto();
        BeanUtils.copyProperties(from, to);

        JSONObject json = JSONObject.parseObject(from.indexType);
        String[] descs = json.getString("fields_c").split(",");
        String[] names = json.getString("fields_e").split(",");
        List<Map<String, String>> list = new ArrayList();
        for (int i = 0; i < names.length; i++) {
            Map<String, String> map = new HashMap();
            map.put(names[i], i >= descs.length ? "" : descs[i]);
            list.add(map);
        }
        to.setFields(list);
        return to;
    }
}
