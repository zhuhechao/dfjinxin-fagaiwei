package io.dfjinxin.modules.price.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 *
 *
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-08-26 14:00:37
 */
@Data
@TableName("wp_ascii_info")
public class WpAsciiInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	private Integer codeId;
	/**
	 *
	 */
	private String codeName;
	/**
	 *
	 */
//	private String codeSimple;
	/**
	 *
	 */
	private Integer pCodeVal;
	/**
	 *
	 */
	private Date dataTime;
	/**
	 *
	 */
	private Integer codeStatus;
	/**
	 *
	 */
	private String crteName;

	@TableField(exist = false)
	private List<PssEwarnConfEntity> ewarnNamelist;
}
