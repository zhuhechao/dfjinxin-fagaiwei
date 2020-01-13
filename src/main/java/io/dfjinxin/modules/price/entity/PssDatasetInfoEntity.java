package io.dfjinxin.modules.price.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.dfjinxin.modules.price.dto.PssDatasetInfoDto;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 *
 *
 * @author bourne
 * @email kuibobo@gmail.com
 * @date 2019-09-02 17:05:57
 */
@Data
@TableName("pss_dataset_info")
public class PssDatasetInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	private Integer dataSetId;

	private Integer dataSetType;
	/**
	 *
	 */
	private String dataSetName;

	/**
	 * 数据集英文名称，由python返回
	 */
	private String dataSetEngName;

	/**
	 * hive表数据集的行、列数
	 */
	private String shape;

	private String indeVar;

	private String userId;

	private String commIndevalPath;

	private String macroIndevalPath;
	/**
	 * 日期，创建数据集成功之后，预测后可能增加该值
	 */
	private String dateFeature;

	/**
	 *
	 */
	private Date dataTime;

	@TableField(exist = false)
	private String indeName;

	/**
	 * 创建数据集用户名
	 */
	@TableField(exist = false)
	private String userRealName;


	public static PssDatasetInfoEntity toEntity(PssDatasetInfoDto from) {
		if (null == from) {
			return null;
		}
		PssDatasetInfoEntity to = new PssDatasetInfoEntity();
		BeanUtils.copyProperties(from, to);

		to.dataTime = new Date();
		to.indeVar = JSON.toJSONString(from.getIndeVar());
		return to;
	}

	public static PssDatasetInfoDto toData(PssDatasetInfoEntity from){
		if(null == from){
			return null;
		}
		PssDatasetInfoDto to = new PssDatasetInfoDto();
		BeanUtils.copyProperties(from, to);
//		to.setIndeVar(JSONObject.parseObject(from.indeVar, new TypeReference<Map<String, Object>>(){}));
		return to;
	}
}
