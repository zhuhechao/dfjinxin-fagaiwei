package io.dfjinxin.modules.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 模型基本信息
 *
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-12-25 13:48:01
 */
@Data
@TableName("model_info")
public class ModelInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 模型ID
	 */
	@TableId
	private String modelId;
	/**
	 * 模型名称
	 */
	private String modelName;
	/**
	 * 模型文件路径
	 */
	private String filePath;
	/**
	 * 操作者
	 */
	private String operator;
	/**
	 * 管理实体代码
	 */
	private String orgId;
	/**
	 * 算法
	 */
	private String algorithm;
	/**
	 * 预测类型
	 */
	private Integer predictType;
	/**
	 *
	 */
	private Integer predictTimeType;
	/**
	 * 是否展示 0：不显示 1：显示
	 */
	private Integer isShow;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新时间
	 */
	private Date updateTime;

}
