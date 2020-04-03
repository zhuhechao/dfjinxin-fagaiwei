/**
 * 2019 东方金信
 *
 *
 *
 *
 */

package io.dfjinxin.modules.job.task;

import com.alibaba.fastjson.JSON;
import freemarker.template.TemplateException;
import io.dfjinxin.common.utils.DateTime;
import io.dfjinxin.common.utils.DateUtils;
import io.dfjinxin.common.utils.echart.EchartsUtil;
import io.dfjinxin.common.utils.echart.FreemarkerUtil;
import io.dfjinxin.common.utils.word.CustomXWPFDocument;
import io.dfjinxin.common.utils.word.WordToHtml;
import io.dfjinxin.common.utils.word.WordToPDF;
import io.dfjinxin.common.utils.word.WordUtil;
import io.dfjinxin.config.propertie.AppProperties;
import io.dfjinxin.modules.price.entity.PssPriceEwarnEntity;
import io.dfjinxin.modules.price.service.PssCommTotalService;
import io.dfjinxin.modules.price.service.PssPriceEwarnService;
import io.dfjinxin.modules.price.service.WpCommPriService;
import io.dfjinxin.modules.report.entity.PssRptConfEntity;
import io.dfjinxin.modules.report.entity.PssRptInfoEntity;
import io.dfjinxin.modules.report.service.PssRptConfService;
import io.dfjinxin.modules.report.service.PssRptInfoService;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试定时任务(演示Demo，可删除)
 *
 * testTask为spring bean的名称
 *
 * @author Mark sunlightcs@gmail.com
 */
@Component("testTask2")
public class TestTask2 implements ITask {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private PssRptConfService pssRptConfService;
	@Autowired
	private PssRptInfoService pssRptInfoService;
	@Autowired
	private PssCommTotalService pssCommTotalService;
	@Autowired
	private AppProperties appProperties;

	@Autowired
	private PssPriceEwarnService pssPriceEwarnService;
	//涨跌幅数据
	private List<PssPriceEwarnEntity> ppees;
	//下属规格涨跌幅数据
	List<Map<String,Object>> ppeesImg;
	//商品名称
	String commName;
	@Override
	public void run(String params) throws Exception {
		String[] ps=params.split("@");
		String rptId=ps[0];
		String jobId=ps[1].substring(6,ps[1].length());
		PssRptConfEntity pe=pssRptConfService.getById(rptId);
		String temPath=pe.getRptPath();
		String ymd=new StringBuilder().append(DateTime.now().getYear()).append('-')
				.append(DateTime.now().getMonth() + 1).append('-')
				.append(DateTime.now().getDay()).toString();
		//以模板的名称为底层子文件夹
		String reportResultPath=new StringBuilder()
				.append(appProperties.getPath().getModule().getReportresult())
				//linux 路径
				.append(temPath.substring(temPath.lastIndexOf("/"),temPath.indexOf("."))).append("/")
				//win 路径   .append(temPath.substring(temPath.lastIndexOf("\\")+1,temPath.indexOf("."))).append("\\")

				.append(ymd)
				//linux 路径
				.append("/").toString();
				//win 路径    .append("\\").toString();
		//linux 路径
		reportResultPath= Paths.get(reportResultPath)+"/";
		//win 路径 reportResultPath= Paths.get(reportResultPath)+"\\";
		new File(reportResultPath).mkdirs();


		//商品名称
		 commName=pssCommTotalService.getById(pe.getCommId()).getCommName();
		//生成报告图片
		String[] reportImagePath=generateReportImage(pe,reportResultPath);
		//替换模板生成报告
		String reportResult=generateReportDocx(pe,reportResultPath,reportImagePath,ymd);

		String rptResultName=new StringBuilder()
				//去除userdir
				//.append(pe.getRptName().substring(0,pe.getRptName().indexOf("."))).append("-")
				.append(pe.getRptName()).append("-")

				.append(ymd).append(".docx").toString();
		//记录运行报告信息
		PssRptInfoEntity prie=new PssRptInfoEntity();
		prie.setRptName(rptResultName);

		prie.setCommId(pe.getCommId());
		prie.setCommName(commName);
		prie.setRptType("0");
		prie.setRptFreq(pe.getRptFreq());
		prie.setCrteTime(new Date());
		prie.setRptFile("");
		//去除userdir
		//prie.setRptPath(reportResult.substring(reportResult.lastIndexOf("/reportResult")));
		//prie.setRptPath(reportResult.substring(reportResult.lastIndexOf("\\reportResult")));
		prie.setRptPath(reportResult);
		prie.setRptStatus("1");
		pssRptInfoService.saveOrUpdate(prie);


		//doc转html生成报告
		String reportResultHtml=WordToHtml.word2007ToHtml(reportResult,reportResultPath,prie.getRptId()+"");
        //doc转pdf生成报告
		String reportResultPdf=generateReportPdf(reportResult);



		logger.debug("TestTask定时任务正在执行，参数为：{}", params);
	}

	//docx报告转为pdf文档
    private String generateReportPdf(String filepath){
        {
            String outpath=filepath.replace("docx","pdf");
            InputStream source;
            OutputStream target;
            try {
                source = new FileInputStream(filepath);
                target = new FileOutputStream(outpath);
                Map<String, String> params = new HashMap<String, String>();
                PdfOptions options = PdfOptions.create();
                WordToPDF.wordConverterToPdf(source, target, options, params);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return outpath;
        }
    }

	//根据模板替换变量生成报告
	private String generateReportDocx( PssRptConfEntity pe,String reportResultPath,String[] reportImagePath,String ymd) throws Exception {
		String sourcePath=//appProperties.getPath().getUserDir()+
				pe.getRptPath();

		String subFilePath=new StringBuilder()
				.append(reportResultPath)
				//去除userdir
				//.append(pe.getRptName().substring(0,pe.getRptName().indexOf("."))).append("-")
				.append(pe.getRptName()).append("-")
				.append(ymd).append(".docx").toString();


        Map map=new HashMap<String,Object>();
        StringBuilder sb=new StringBuilder();
        if(null==ppees||ppees.size()<=0){
        	return null;//无数据
		}
		PssPriceEwarnEntity pee=ppees.get(ppees.size()-1);
		//报告日期
		map.put("${reportDate}",DateUtils.format(new Date(),"yyyy年MM月dd日"));

		//指标名称
//		String indexName=pee.getPricTypeId();
		//涨跌幅度
		BigDecimal zdfd=pee.getPriRange();
		sb.append(commName)
				.append("市场价格同比")
				.append(zdfd.compareTo(new BigDecimal(0))>0?"上涨":"下跌")
				.append(zdfd.toString())
				.append("%");
		//设置涨跌幅
		map.put("${dayPriceLimit}",sb.toString());

		sb.setLength(0);//清空stringbuilder
		for(Map<String,Object> da:ppeesImg){
			sb.append(da.get("comm_name"))
					.append("市场价格同比")
					.append(((BigDecimal)da.get("pri_range")).compareTo(new BigDecimal(0))>0?"上涨":"下跌")
					.append(da.get("pri_range").toString())
					.append("%").append(",");
		}
		sb.deleteCharAt(sb.length()-1);//去除逗号

		map.put("${dayPriceDetail}",sb.toString());//猪肉上涨2.8%，二元仔猪上涨3.6%

		Map<String,Object> picture1 = new HashMap<String, Object>();
		picture1.put("width", 580);
		picture1.put("height", 240);
		picture1.put("type", "png");
		picture1.put("content", new CustomXWPFDocument().inputStream2ByteArray(new FileInputStream(reportImagePath[0]), true));
		map.put("${picture1}",picture1);

		Map<String,Object> picture2 = new HashMap<String, Object>();
		picture2.put("width", 600);
		picture2.put("height", 240);
		picture2.put("type", "png");
		picture2.put("content", new CustomXWPFDocument().inputStream2ByteArray(new FileInputStream(reportImagePath[1]), true));
		map.put("${picture2}",picture2);


		WordUtil.replaceWord(sourcePath,subFilePath,map);
		return subFilePath;
	}

	//生成报告图片
	private String[] generateReportImage(PssRptConfEntity pe,String reportResultPath) throws IOException, TemplateException {

		Map<String,Object> params=new HashMap<String,Object>();
		//设置商品id  查询最近7天的预警数据按照 预警日期正序排列
		params.put("commId",pe.getCommId());//params.put("commId",58);
		params.put("endDate",DateTime.getBeginOf(new Date()));
		params.put("startDate", DateUtils.addDateDays(DateTime.getBeginOf(new Date()),-7));
		//涨跌幅数据
		ppees=pssPriceEwarnService.getDayReportData(params);

		// 变量
		String title = "生猪的市场最近一周价格涨跌幅";
		//String[] categories = new String[] { "苹果", "香蕉", "西瓜" };
		//int[] values = new int[] { 3, 2, 1 };
		String[] categories=new String[ppees.size()];
		String[] values = new String[ppees.size()];
		for(int i=0;i<ppees.size();i++){
			PssPriceEwarnEntity map=ppees.get(i);
			categories[i]=DateUtils.format(map.getEwarnDate());//日期
			values[i]=map.getPriRange()+"";
		}
		// 模板参数
		HashMap<String, Object> datas = new HashMap<>();
		datas.put("categories", JSON.toJSONString(categories));
		datas.put("values", JSON.toJSONString(values));
		datas.put("title", title);

		// 生成option字符串
		String option = FreemarkerUtil.generateString("report1.ftl", "/template", datas);
		logger.error("Echart服务地址----------"+EchartsUtil.url);
		// 根据option参数
		String base64 = EchartsUtil.generateEchartsBase64(option);

		System.out.println("BASE64:" + base64);
		String reportImagePath1=reportResultPath+"report1.png";
		EchartsUtil.generateImage(base64, reportImagePath1);

		//生成第二张图片

		//下属规格涨跌幅 图形所需数据
		params.clear();params.put("commId",pe.getCommId());
		params.put("beginYestarday", DateUtils.format(DateUtils.addDateDays(DateTime.getBeginOf(new Date()),-1)));
		params.put("endYestarday", DateUtils.format(DateUtils.addDateDays(DateTime.getBeginOf(new Date()),-1))+" 23:59:59 ");
		//下属规格涨跌幅数据
		ppeesImg=pssPriceEwarnService.getDayReportDataForBarImage(params);

		//拼装下属规格图形数据
		 categories=new String[ppeesImg.size()];
		 values = new String[ppeesImg.size()];
		for(int i=0;i<ppeesImg.size();i++){
			Map<String,Object> map=ppeesImg.get(i);
			categories[i]=map.get("comm_name").toString();//商品名称
			values[i]=map.get("pri_range")+"";
		}

		// 模板参数
		datas.clear();
		datas.put("categories", JSON.toJSONString(categories));
		datas.put("values", JSON.toJSONString(values));
		datas.put("title", title);

		// 生成option字符串
		 option = FreemarkerUtil.generateString("report2.ftl", "/template", datas);

		// 根据option参数
		 base64 = EchartsUtil.generateEchartsBase64(option);

		System.out.println("BASE64:" + base64);
		String reportImagePath2=reportResultPath+"report2.png";
		EchartsUtil.generateImage(base64, reportImagePath2);

		String[] reportImagePaths=new String[]{reportImagePath1,reportImagePath2};
		return reportImagePaths;
	}
}
