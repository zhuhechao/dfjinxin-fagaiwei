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
import io.dfjinxin.common.utils.echart.EchartsUtil;
import io.dfjinxin.common.utils.echart.FreemarkerUtil;
import io.dfjinxin.common.utils.word.CustomXWPFDocument;
import io.dfjinxin.common.utils.word.WordToHtml;
import io.dfjinxin.common.utils.word.WordToPDF;
import io.dfjinxin.common.utils.word.WordUtil;
import io.dfjinxin.config.propertie.AppProperties;
import io.dfjinxin.modules.report.entity.PssRptConfEntity;
import io.dfjinxin.modules.report.service.PssRptConfService;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
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
	private AppProperties appProperties;
	@Override
	public void run(String params) throws Exception {
		String rptId=params;
		PssRptConfEntity pe=pssRptConfService.getById(params);
		String temPath=pe.getRptPath();
		String ymd=new StringBuilder().append(DateTime.now().getYear()).append('-')
				.append(DateTime.now().getMonth() + 1).append('-')
				.append(DateTime.now().getDay()).toString();
		//以模板的名称为底层子文件夹
		String reportResultPath=new StringBuilder()
				.append(appProperties.getPath().getModule().getReportresult())
				.append(pe.getRptPath().substring(temPath.lastIndexOf("\\"),temPath.indexOf("."))).append("/")
				.append(ymd)
				.append("/").toString();
		new File(reportResultPath).mkdirs();

		//生成报告图片
		String reportImagePath=generateReportImage(pe,reportResultPath);
		//替换模板生成报告
		String reportResult=generateReportDocx(pe,reportResultPath,reportImagePath,ymd);
		//doc转html生成报告
		String reportResultHtml=WordToHtml.word2007ToHtml(reportResult,reportResultPath);
        //doc转pdf生成报告
		String reportResultPdf=generateReportPdf(reportResult);

		logger.debug("TestTask定时任务正在执行，参数为：{}", params);
	}



	//docx报告转为pdf文档
    private String generateReportPdf(String filepath){
        {
            //String filepath = "E:\\ideaWorkspace\\dfjinxin-fagaiwei\\fagaiwei\\2019\\9\\29\\模板名称.docx";
            //String outpath = "E:\\ideaWorkspace\\dfjinxin-fagaiwei\\fagaiwei\\2019\\9\\29\\模板名称.pdf";
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
	private String generateReportDocx( PssRptConfEntity pe,String reportResultPath,String reportImagePath,String ymd) throws Exception {
		String sourcePath=appProperties.getPath().getUserDir()+pe.getRptPath();

		String subFilePath=new StringBuilder()
				.append(reportResultPath)
				.append(pe.getRptName()).append("-")
				.append(ymd).append(".docx").toString();
		Map map=new HashMap<String,Object>();
		map.put("${1}",DateTime.toString(new Date(),"yyyy-MM-dd"));
		map.put("${2}",DateTime.toString(new Date(),"测试2"));
		Map<String,Object> picture1 = new HashMap<String, Object>();
		picture1.put("width", 580);
		picture1.put("height", 240);
		picture1.put("type", "png");
		picture1.put("content", new CustomXWPFDocument().inputStream2ByteArray(new FileInputStream(reportImagePath), true));
		map.put("${picture1}",picture1);
		WordUtil.replaceWord(sourcePath,subFilePath,map);
		return subFilePath;
	}

	//生成报告图片
	private String generateReportImage(PssRptConfEntity pe,String reportResultPath) throws IOException, TemplateException {
		// 变量
		String title = "水果";
		String[] categories = new String[] { "苹果", "香蕉", "西瓜" };
		int[] values = new int[] { 3, 2, 1 };

		// 模板参数
		HashMap<String, Object> datas = new HashMap<>();
		datas.put("categories", JSON.toJSONString(categories));
		datas.put("values", JSON.toJSONString(values));
		datas.put("title", title);

		// 生成option字符串
		String option = FreemarkerUtil.generateString("option2.ftl", "/template", datas);

		// 根据option参数
		String base64 = EchartsUtil.generateEchartsBase64(option);

		System.out.println("BASE64:" + base64);
		String reportImagePath=reportResultPath+"report.png";
		EchartsUtil.generateImage(base64, reportImagePath);
		return reportImagePath;
	}
}
