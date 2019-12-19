package io.dfjinxin.modules.analyse.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 *
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-12-18 10:56:59
 */
@Data
@TableName("wp_pub_om")
public class WpPubOmEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	private Integer commId;
	/**
	 * 总
	 */
	private Long heatTrend;
	/**
	 * 负面
	 */
	private Long sentimentTrend;
	/**
	 * 中立
	 */
	private Long neuTrend;
	/**
	 * 正面
	 */
	private Long posTrend;
	/**
	 *
	 */
	private Date dataDate;

}
