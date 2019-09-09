package io.dfjinxin.modules.price.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.dfjinxin.modules.price.dto.PssRptConfDto;
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
 * @date 2019-09-09 14:56:17
 */
@Data
@TableName("pss_rpt_conf")
public class PssRptConfEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.AUTO)
	private Integer rptId;
	/**
	 * 
	 */
	private Integer commId;
	/**
	 * 
	 */
	private Integer rptType;
	/**
	 * 
	 */
	private Integer rptFreq;
	/**
	 * 
	 */
	private String rschId;
	/**
	 * 
	 */
	private Date rptDate;
	/**
	 * 
	 */
	private String rptName;
	/**
	 * 
	 */
	private Integer statCode;
	/**
	 * 
	 */
	private String rptPath;
	/**
	 * 
	 */
	private Date crteDate;
	/**
	 * 
	 */
	private String crteName;
	/**
	 * 
	 */
	private Integer rptStatus;
	/**
	 * 
	 */
	private String rptRemarks;

	public static PssRptConfEntity toEntity(PssRptConfDto from) {
		if (null == from) {
			return null;
		}
		PssRptConfEntity to = new PssRptConfEntity();
		BeanUtils.copyProperties(from, to);
        to.rptDate = new Date();
        to.crteDate = new Date();
		return to;
	}

	public static PssRptConfDto toData(PssRptConfEntity from){
		if(null == from){
			return null;
		}
		PssRptConfDto to = new PssRptConfDto();
		BeanUtils.copyProperties(from, to);
		return to;
	}
}
