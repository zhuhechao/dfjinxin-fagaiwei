
package io.dfjinxin.common.utils.excel;

import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.util.Base64Utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ExportExcel {

	XSSFWorkbook wb = null;
	// 创建第一个sheet
	static XSSFSheet sheet1 = null;
	static XSSFSheet sheet2 = null;
	// 生成第一行
	XSSFRow row = null;

	private XSSFCell cell = null;

	private XSSFCellStyle titleStyle = null;
	
	private XSSFCellStyle conditionStyle = null;

	private XSSFCellStyle headStyle = null;

	private XSSFCellStyle bodyStyle = null;

	private int num = 0;

    //生成一个sheet
	public static void download(HttpServletRequest request,HttpServletResponse response, List<Map<String, Object>> list,List<String> heads,String name,String condition) throws Exception {
			ExcelHeadXML xml=getExcelHeadXML(name +"_"+ System.currentTimeMillis(), heads, true, true);
			//实际调用
			toExport(list, xml,condition, request, response,"");
	}
	
	//生成两个sheet
	public static void doubledownload(HttpServletRequest request,HttpServletResponse response,List<Map<String, Object>> list,List<String> heads,List<Map<String, Object>> list1,List<String> heads1,String name1,String name2,String condition,String picPath1,String picPath2) throws Exception {
		//生成第一个sheet
		ExcelHeadXML xml=getExcelHeadXML(name1 +"_"+ System.currentTimeMillis(), heads, true, true);
		ExportExcel excel = new ExportExcel();
		XSSFWorkbook workBook = excel.createWorkBook(list, xml,sheet1,condition,picPath1);
		//生成第二个sheet
		ExcelHeadXML xml1=getExcelHeadXML(name2 +"_"+ System.currentTimeMillis(), heads1, true, true);
		workBook = excel.createWorkBook(list1, xml1,sheet2,condition,picPath2);
		doExport(workBook, xml.getTitle(), request, response);
   }
	
	public ExportExcel() {
		wb = new XSSFWorkbook();
		sheet1 = wb.createSheet("sheet1");
		sheet2 = wb.createSheet("sheet2");
		// sheet.setDefaultColumnWidth( 14 );
		// sheet.setDefaultRowHeight( (short)20 );
		// // 打印设置
		// // sheet.setHorizontallyCenter(true); // 设置打印页面为水平居中
		// // sheet.setVerticallyCenter(true); // 设置打印页面为垂直居中
		// // 冻结第一行和第二行
		sheet1.createFreezePane(0, 2, 0, 2);
		sheet2.createFreezePane(0, 2, 0, 2);
		init();
	}

	/**
	 * 初始化样式
	 */
	private void init() {
	/*	titleFont();
//		conditionFont();
//		headFont();
//		bodyFont();*/
	}

	/**
	 * 设置标题样式
	 */
	/*private void titleFont() {
		XSSFFont titleFont = wb.createFont();
		titleFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		titleFont.setFontName("宋体");
		titleFont.setFontHeightInPoints((short) 18);
		titleStyle = wb.createCellStyle();
		titleStyle.setFont(titleFont);
		titleStyle.setBorderBottom((short) 1);
		titleStyle.setBorderRight((short) 1);
		titleStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
	}*/
	
	/**
	 * 设置条件样式
	 */
	/*private void conditionFont() {
		XSSFFont conditionFont = wb.createFont();
		conditionFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		conditionFont.setFontName("宋体");
		conditionFont.setFontHeightInPoints((short) 15);
		conditionStyle = wb.createCellStyle();
		conditionStyle.setFont(conditionFont);
		conditionStyle.setBorderBottom((short) 1);
		conditionStyle.setBorderRight((short) 1);
		conditionStyle.setAlignment(XSSFCellStyle.ALIGN_LEFT);
	}*/

	/**
	 * 设置head样式
	 */
	/*private void headFont() {
		XSSFFont headFont = wb.createFont();
		headFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		headFont.setFontName("宋体");
		headFont.setFontHeightInPoints((short) 11);
		headStyle = wb.createCellStyle();
		headStyle.setFont(headFont);
		headStyle.setBorderTop((short) 1);
		headStyle.setBorderRight((short) 1);
		headStyle.setBorderBottom((short) 1);
		headStyle.setBorderLeft((short) 1);
		headStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
	}
*/
	/**
	 * 设置body样式
	 */
	/*private void bodyFont() {
		XSSFFont bodyFont = wb.createFont();
		bodyFont.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);
		bodyFont.setFontName("宋体");
		bodyFont.setFontHeightInPoints((short) 9);
		bodyStyle = wb.createCellStyle();
		bodyStyle.setFont(bodyFont);
		bodyStyle.setBorderTop((short) 1);
		bodyStyle.setBorderRight((short) 1);
		bodyStyle.setBorderBottom((short) 1);
		bodyStyle.setBorderLeft((short) 1);
		//bodyStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		bodyStyle.setAlignment(XSSFCellStyle.ALIGN_LEFT);
		bodyStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
	}*/

	public static void toExport(List<Map<String, Object>> list, ExcelHeadXML xml, String condition, HttpServletRequest request, HttpServletResponse response, String picPath)
			throws IOException, SQLException {
		ExportExcel excel = new ExportExcel();
		XSSFWorkbook workBook = excel.createWorkBook(list, xml,sheet1,condition,picPath);
		doExport(workBook, xml.getTitle(), request, response);
	}
	

	/**
	 * 生成Excel主方法
	 * @param sheet 
	 * 
	 * @param list：数据列表
//	 * @param title：标题
//	 * @param heads:头部列名
//	 * @param fit
//	 *            ：是否需要宽度自适应
//	 * @param isSeq：是否需要序号
	 * @return
	 * @throws SQLException
	 */
	public XSSFWorkbook createWorkBook(List<Map<String, Object>> list, ExcelHeadXML xml, XSSFSheet sheet, String condition, String picPath) throws SQLException {
		createTop(xml,sheet,condition);
		createBody(list, xml.getHead(), xml.isSeq(),sheet);
		if(picPath!=null&&picPath.length()>0){
			FileInputStream fis;
			try {
				fis = new FileInputStream(picPath);
				byte[] bytes = IOUtils.toByteArray(fis);
				int pictureIdx = wb.addPicture(bytes,Workbook.PICTURE_TYPE_PNG);
				fis.close();
				//创建一个顶级容器
				Drawing drawing = sheet.createDrawingPatriarch();
				CreationHelper helper = wb.getCreationHelper();
				ClientAnchor anchor = helper.createClientAnchor();
				anchor.setCol1(1);
				anchor.setRow1(num+2);
			    Picture pict = drawing.createPicture(anchor, pictureIdx);
			    pict.resize();//该方法只支持JPEG 和 PNG后缀文件
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  
		}
		// 宽度自适应
		if (xml.isFit()) {
			for (int i = 0; i < xml.getHeadLength(); i++) {
				sheet.autoSizeColumn(i);
			}
		}
		//sheet.setColumnWidth(headLength - 1, 1500);
		return wb;
	}

	/**
	 * 创建Excel的Head和title部分
	 * 
	 * @param xml
	 *            ExcelHeadXML
	 */
	private void createTop(final ExcelHeadXML xml, XSSFSheet sheet, String condition) {
		num = 0;
		// 创建Title部分
		if (null != xml.getTitle() && !"".equals(xml.getTitle())) {
			row = sheet.createRow(num++);
			cell = row.createCell(0);
			cell.setCellStyle(titleStyle);
			cell.setCellValue(xml.getTitle());
			sheet.addMergedRegion(new CellRangeAddress(xml.getUniteRowStart(), xml.getUniteRowEnd(), xml.getUniteColStart(), xml.getUniteColEnd()));

		}
		//创建条件标题
		if(condition!=null&&!(condition.equals(""))){
			row = sheet.createRow(num++);
			cell = row.createCell(0);
			cell.setCellStyle(conditionStyle);
			cell.setCellValue(condition);
			sheet.addMergedRegion(new CellRangeAddress(xml.getUniteRowStart()+1, xml.getUniteRowEnd()+1, xml.getUniteColStart(), xml.getUniteColEnd()));
		}
		// 创建Head部分
		List<String> head = xml.getHead();
		if (null != head && head.size() > 0) {
			row = sheet.createRow(num++);
			for (int i = 0; i < head.size(); i++) {
				cell = row.createCell(i);
				cell.setCellStyle(headStyle);
				cell.setCellValue(new XSSFRichTextString(head.get(i)));

			}
		}
	}
	

	/**
	 * 创建Excel的body部分
	 * 
	 * @param list
	 *            : body部分的数据
	 * @param isSeq
	 *            : body部分是否要序号
	 * @param sheet 
	 * @throws SQLException
	 */
	private void createBody(final List<Map<String, Object>> list, List<String> heads, boolean isSeq, XSSFSheet sheet) throws SQLException {
		if (null != list && list.size() > 0) {
			Map<String, Object> map;
			for (int i = 0; i < list.size(); i++) {
				row = sheet.createRow(num++);
				row.setHeightInPoints(30);
				map = list.get(i);
				for (int j = 0; j < heads.size(); j++) {
					cell = row.createCell(j);
					cell.setCellStyle(bodyStyle);
					if (isSeq && j==0) {// 序号
						cell.setCellValue((i + 1) + "");
						continue;
					}
					cell.setCellValue(new XSSFRichTextString(emptyToString(map.get(heads.get(j)))));
				}
			}
		}
	}

	private String emptyToString(Object obj) {
		if (obj != null) {
			return obj.toString();
		}
		return "";
	}

	/**
	 * 实际导出操作
	 * 
	 * @param workBook
	 * @param title
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private static void doExport(XSSFWorkbook workBook, String title, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			workBook.write(os);
		} catch (IOException e) {
			e.printStackTrace();
		}
		title = title + ".xlsx";
//		if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
//			//title = new String(title.getBytes("UTF-8"), "ISO8859-1");// firefox浏览器
//			title = "=?UTF-8?B?" + (new String(Base64Utils.encodeToString(title.getBytes("UTF-8")))) + "?=";
//		} else {
//			title = URLEncoder.encode(title, "UTF-8");
//		}
		title = URLEncoder.encode(title, "UTF-8");
		byte[] bytes = os.toByteArray();
		response.setContentType("application/octet-stream");
		response.setContentLength(bytes.length);
		response.setHeader("Content-Disposition", "attachment;filename=" + title);
		response.getOutputStream().write(bytes);
		response.flushBuffer();
	}

	/**
	 * 获取xml详细信息
	 * 
	 * @return
	 */
	public static ExcelHeadXML getExcelHeadXML(String title, List<String> heads, boolean isSeq, boolean fit) {
		ExcelHeadXML xml = new ExcelHeadXML();
		xml.setHead(heads);
		if (isSeq) {
			heads = xml.getHead();
			heads.add(0, "序号");
		}
		xml.setHeadLength(heads.size());
		xml.setUniteRowStart(0);
		xml.setUniteRowEnd(0);
		xml.setUniteColStart(0);
		xml.setUniteColEnd(heads.size()-1);
		xml.setSeq(isSeq);
		xml.setFit(fit);
		xml.setTitle(title);
		return xml;
	}
	

	/*public static void main(String[] args) throws SQLException {
		ExportExcel excel = new ExportExcel();
		OutputStream os;
		XSSFWorkbook wb = new XSSFWorkbook();
		try {
			os = new FileOutputStream("D:/" + System.currentTimeMillis() + ".xlsx");
			// 工作区
			// 如果循环超过10172次，则报内存溢出，有谁循环超过10万次不报错，麻烦请告诉我，这样是因为可以一次性导出大量数据
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			Map<String, Object> map = null;
			List<String> heads = new ArrayList<String>();
			for (int i = 0; i < 14; i++) {
				heads.add("列名" + i);
			}
			for (int j = 0; j < 15; j++) {//行
				map = new HashMap<String, Object>();
				for (int i = 0; i < 14; i++) {//列
					map.put("列名" + i, generateString(generateInteger(1,"3456789")));
				}
				list.add(map);
			}
			ExcelHeadXML xml=excel.getExcelHeadXML("Excel导出测试" + System.currentTimeMillis(), heads, true, true);
			//实际调用excel.toExport(list, xml, request, response);
			wb = excel.createWorkBook(list, xml);
			wb.write(os);
			// 关闭输出流
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 写文件
	}*/

	/**
	 * 返回一个定长的随机字符串(只包含大小写字母、数字)
	 * 
	 * @param length
	 *            随机字符串长度
	 * @return 随机字符串
	 */
	public static String generateString(int length) {
		String allChar = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(allChar.charAt(random.nextInt(allChar.length())));
		}
		return sb.toString();
	}
	/**
	 * 返回一个定长的随机字符串(数字)
	 * 
	 * @param length
	 *            随机字符串长度
	 * @return 随机字符串
	 */
	public static int generateInteger(int length,String allChar) {
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(allChar.charAt(random.nextInt(allChar.length())));
		}
		return (Integer.parseInt(sb.toString())-2)*3;
	}

}