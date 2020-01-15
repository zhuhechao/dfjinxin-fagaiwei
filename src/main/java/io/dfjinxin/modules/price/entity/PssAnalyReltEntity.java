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
 * @date 2019-09-02 17:03:24
 */
@Data
@TableName("pss_analy_relt")
public class PssAnalyReltEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	private Integer reltId;
	/**
	 *
	 */
	private Integer analyId;
	/**
	 *
	 */
	private Date analyTime;
	/**
	 *
	 */
	private String reltName;
	/**
	 *
	 */
	private String analyWay;
	/**
	 *
	 */
	private String basVar;
	/**
	 *
	 */
	private String tarVar;

	private Date runTime;

	private Integer runStatus;
	/**
	 *
	 */
	private String analyCoe;
	/**
	 *
	 */
	private String pvalue;

	public void analyInfoToRelEnt(PssAnalyInfoEntity pssAnalyInfoEntity){
		if(pssAnalyInfoEntity==null)
			return;
		if(pssAnalyInfoEntity.getAnalyId()!=null)
			this.setAnalyId(pssAnalyInfoEntity.getAnalyId());
		this.setAnalyWay(pssAnalyInfoEntity.getAnalyWay());
		this.setAnalyTime(new Date());
		this.setReltName(pssAnalyInfoEntity.getAnalyName());
		this.setRunStatus(0);//只插入正常 -0：正常 1：失败
		this.setBasVar(pssAnalyInfoEntity.getIndeVar());
		this.setTarVar(pssAnalyInfoEntity.getDepeVar());
		//		pssAnalyReltEntity.setAnalyCoe("1");//分析系数
//		pssAnalyReltEntity.setPvalue("");// Pvalue
	}

}
