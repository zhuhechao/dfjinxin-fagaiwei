package io.dfjinxin.modules.report.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author mxq
 * @email 397968061@qq.com
 * @date 2019-09-20 11:07:08
 */
@Data
@TableName("wp_crawler_data")
public class WpCarwlerDataEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId
	private Integer id;
	/**
	 * 报告时间
	 */
	private String data_dt;
	/**
	 * 报告名称
	 */
	private String title;
	/**
	 * 报告链接
	 */
	private String link;
	/**
	 * 来源
	 */
	private String web;
	/**
	 * 商品名称
	 */
	private String comm_name;
	/**
	 * 报告类型，默认为日
	 */
	private String report_type;
	/**
	 * 报告状态，0有效，1无效
	 */
	private String report_flag;


}
