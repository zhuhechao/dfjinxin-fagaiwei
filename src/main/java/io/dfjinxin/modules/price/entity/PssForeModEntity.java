package io.dfjinxin.modules.price.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author bourne
 * @email kuibobo@gmail.com
 * @date 2019-09-04 09:33:51
 */
@Data
@TableName("pss_fore_mod")
public class PssForeModEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer modId;
	/**
	 * 
	 */
	private String modName;
	/**
	 * 
	 */
	private Integer commId;
	/**
	 * 
	 */
	private String modType;
	/**
	 * 
	 */
	private String modDetail;
	/**
	 * 
	 */
	private Integer modStatus;
	/**
	 * 
	 */
	private Integer bestFlag;
	/**
	 * 
	 */
	private String crteName;
	/**
	 * 
	 */
	private Date crteTime;

}
