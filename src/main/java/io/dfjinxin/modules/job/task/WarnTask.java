/**
 * 2019 东方金信
 *
 *
 *
 *
 */

package io.dfjinxin.modules.job.task;


import io.dfjinxin.common.utils.DateTime;
import io.dfjinxin.common.utils.DateUtils;
import io.dfjinxin.modules.price.entity.*;
import io.dfjinxin.modules.price.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 *
 * 价格预警调度
 *
 * @author Mark sunlightcs@gmail.com
 */
@Component("warnTask")
public class WarnTask implements ITask {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private PssEwarnConfService pssEwarnConfService;
	@Autowired
	private PssCommConfService pssCommConfService;
	@Autowired
	private WpCommPriService wpCommPriService;
	@Autowired
	private PssPriceEwarnService pssPriceEwarnService;

	@Override
	public void run(String params) throws Exception {
		String[] ids=params.split("@");
		String ewarnId=ids[0].substring(ids[0].indexOf(":")+1,ids[0].length());
		String commConfId=ids[1].substring(ids[1].indexOf(":")+1,ids[1].length());

		//预警配置
		PssEwarnConfEntity pe=pssEwarnConfService.getById(ewarnId);

		//商品配置
		PssCommConfEntity pc=pssCommConfService.getById(commConfId);
		Integer indexId = pc.getIndexId();
		Map<String, Object> cc=new HashMap<String, Object>();

		cc.put("indexId",indexId);
		cc.put("endDate",new Date());
		//1：单日涨幅  2：连续涨幅
		cc.put("startDate",DateUtils.addDateDays(DateTime.getBeginOf(new Date()),
				"1".equals(pe.getEwarnTerm())?-2:pe.getEwarnDays()));

		List<WpCommPriEntity> wys=wpCommPriService.getData(cc);
		//价格预警结果
		PssPriceEwarnEntity pee=new PssPriceEwarnEntity();
		pee.setCommId(pc.getCommId());
		pee.setEwarnDate(new Date());
		//区域id
		WpCommPriEntity wy=wpCommPriService.getById(pc.getIndexId());
		//pee.setStatAreaCode(wy.getAreaId()+"");
		pee.setPricTypeId(pc.getIndexId()+"");
		pee.setEwarnTypeId(pe.getEwarnTypeId());

		if("1".equals(pe.getEwarnTerm())){
			BigDecimal price0=wys.get(0).getValue();
			BigDecimal price1=wys.get(1).getValue();
			BigDecimal b1=compareResult(price0,price1);
			pee.setPriRange(b1);
			//预警级别//R_W:红   O_W:橙     Y_W:黄     G_W:绿
			pee.setEwarnLevel(ewarnLevelResult(b1.abs(),pe));
		}
		pee.setPriValue(wys.get(0).getValue());
		if("2".equals(pe.getEwarnTerm())){
			BigDecimal price0=wys.get(0).getValue();
			BigDecimal price1=wys.get(1).getValue();
			BigDecimal price2=wys.get(2).getValue();
			BigDecimal price3=wys.get(3).getValue();
			BigDecimal b0=compareResult(price0,price1);
			BigDecimal b1=compareResult(price1,price2);
			BigDecimal b2=compareResult(price2,price3);

			pee.setPriRange(b0);
			//预警级别//R_W:红   O_W:橙     Y_W:黄     G_W:绿

			if(ewarnLevelResult(b0.abs(),pe).equals(ewarnLevelResult(b1,pe))&&
					ewarnLevelResult(b0.abs(),pe).equals(ewarnLevelResult(b2,pe))){
				pee.setEwarnLevel(ewarnLevelResult(b0.abs(),pe));
			}else{
				pee.setEwarnLevel("G_W");
			}
		}

		pssPriceEwarnService.saveOrUpdate(pee);

		logger.debug("定时任务正在执行，参数为：{}", params);
	}

	//价格涨跌幅百分比
	private BigDecimal compareResult(BigDecimal b1,BigDecimal b2){
		return b1.subtract(b2) .divide(b1,2, BigDecimal.ROUND_HALF_UP);
	}

	//价格涨跌幅所属级别判断
	private String ewarnLevelResult(BigDecimal b1,PssEwarnConfEntity pe){
		//红色预警
		if(b1.compareTo(pe.getEwarnLlmtRed())>=0&&b1.compareTo(pe.getEwarnUlmtRed())<=0) return "71";
		//橙色预警
		else if(b1.compareTo(pe.getEwarnLlmtOrange())>=0&&b1.compareTo(pe.getEwarnUlmtOrange())<=0) return"72";
		//黄色预警
		else if(b1.compareTo(pe.getEwarnLlmtYellow())>=0&&b1.compareTo(pe.getEwarnUlmtYellow())<=0) return "73";
		//绿色预警
		else if(b1.compareTo(pe.getEwarnLlmtGreen())>=0&&b1.compareTo(pe.getEwarnUlmtGreen())<=0) return "74";
		else return null;
	}
}
