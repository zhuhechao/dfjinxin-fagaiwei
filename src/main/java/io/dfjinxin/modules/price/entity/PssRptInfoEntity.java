package io.dfjinxin.modules.price.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.dfjinxin.modules.price.dto.PssRptInfoDto;
import lombok.Data;
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
@TableName("pss_rpt_info")
public class PssRptInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId(type = IdType.INPUT)
	private Integer rptId;
	/**
	 * 
	 */
	private String rptName;
	/**
	 * 
	 */
	private Integer commId;
	/**
	 * 
	 */
	private Integer rptFreq;
	/**
	 * 
	 */
	private Integer rptType;
	/**
	 * 
	 */
	private Integer statCode;
	/**
	 * 
	 */
	@TableId(type = IdType.INPUT)
	private Date rptDate;
	/**
	 * 
	 */
	private Date crteTime;
	/**
	 * 
	 */
	private String rptPath;
	/**
	 * 
	 */
	private Integer rptStatus;
	/**
	 * 
	 */
	private byte[] rptFile;
	/**
	 * 
	 */
	private Date delTime;

	public static PssRptInfoEntity toEntity(PssRptInfoDto from) {
		if (null == from) {
			return null;
		}
		PssRptInfoEntity to = new PssRptInfoEntity();
		BeanUtils.copyProperties(from, to);
		to.rptStatus = 1;
		return to;
	}

	public static PssRptInfoDto toData(PssRptInfoEntity from){
		if(null == from){
			return null;
		}
		PssRptInfoDto to = new PssRptInfoDto();
		BeanUtils.copyProperties(from, to);
		return to;
	}
}
