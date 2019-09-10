package io.dfjinxin.modules.price.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.dfjinxin.modules.price.dto.PssRschConfDto;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author bourne
 * @email kuibobo@gmail.com
 * @date 2019-09-10 09:22:42
 */
@Data
@TableName("pss_rsch_conf")
public class PssRschConfEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer rschId;
	/**
	 * 
	 */
	private String rschName;
	/**
	 * 
	 */
	private Integer rschType;
	/**
	 * 
	 */
	private Integer rschFreq;
	/**
	 * 
	 */
	private char execType;
	/**
	 * 
	 */
	private Date execTime;

	private Date startTime;
	/**
	 * 
	 */
	private Date endTime;
	/**
	 * 
	 */
	private String execCdt;
	/**
	 * 
	 */
	private String rschRemarks;
	/**
	 * 
	 */
	private Date crteTime;

	public static PssRschConfEntity toEntity(PssRschConfDto from) {
		if (null == from) {
			return null;
		}
		PssRschConfEntity to = new PssRschConfEntity();
		BeanUtils.copyProperties(from, to);

		return to;
	}

	public static PssRschConfDto toData(PssRschConfEntity from){
		if(null == from){
			return null;
		}
		PssRschConfDto to = new PssRschConfDto();
		BeanUtils.copyProperties(from, to);

		return to;
	}
}
