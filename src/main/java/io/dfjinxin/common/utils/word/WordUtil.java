package io.dfjinxin.common.utils.word;

import io.dfjinxin.common.utils.DateTime;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.*;
import java.util.*;

public class WordUtil {
	/*
	 * 
	 * @Author:Rick
	 * filepath 模板文件路径
	 * tofilepath 要生成的文件路径
	 * items word xml模板文件中的占位符
	 * data  word xml文件要替换的数据
	 */
	  public static boolean changeFileText(String filepath, String tofilepath, Map<String,String > replaceContent){
		  File file = new File(filepath);
		  String line=null;
		  InputStream is=null;
		  FileOutputStream fos=null;
		  try{
		   is = new FileInputStream(file);
		  StringBuffer  sb=new StringBuffer ();
	      BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
	      while((line=reader.readLine()) != null){
	    	  sb.append(line);
	      }
	      String result= String.valueOf(sb);
	      
	      for( String key :replaceContent.keySet()){
	    	  /*
	    	   * 
	    	   * 正则替换文件中的占位符
	    	   */
	    	  result=result.replaceAll(key, replaceContent.get(key));
	      }
	      System.out.println(result);
	      //tofilepath=tofilepath.substring(0,tofilepath.indexOf("."))+".doc";
		  File out=new File(tofilepath);
		  out.createNewFile();
		  fos=new FileOutputStream(out);
		  fos.write(result.getBytes());
		  
		  
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}finally{
			try {
				is.close();
				fos.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
		  return true;
	  }


	/**
	 *
	 * @param srcPath word模板数据源路径
	 * @param destPath word导出路径
	 * @param map 关键字键值对映射
	 * @throws Exception
	 */
	public static void replaceWord(String srcPath, String destPath,Map<String, Object> map) throws Exception {
		FileOutputStream out = null;
		FileInputStream input = null;
		try {
			if("doc".equals(srcPath.split("\\.")[1])) {
				input = new FileInputStream(new File(srcPath));
				HWPFDocument document = new HWPFDocument(input);
				Range range = document.getRange();
				for(Map.Entry<String, Object> entry : map.entrySet()) {
					if (entry.getValue() == null) {
						entry.setValue("");
					}
					range.replaceText(entry.getKey(), entry.getValue().toString());
				}
				ByteArrayOutputStream ostream = new ByteArrayOutputStream();
				out = new FileOutputStream(new File(destPath));
				document.write(out);
				out.write(ostream.toByteArray());
				out.flush();
			}else {
				CustomXWPFDocument document = new CustomXWPFDocument(POIXMLDocument.openPackage(srcPath));
				// 替换段落中的指定文字
				List<XWPFParagraph> itPara = document.getParagraphs();
				for (int i=0;i<itPara.size(); i++){
					XWPFParagraph paragraph = itPara.get(i);
					List<XWPFRun> runs = paragraph.getRuns();

						for (int j=0;j<runs.size(); j++){
							XWPFRun run=runs.get(j);
						Object ob = changeValue(run.toString(), map);
						if (ob instanceof String&&ob.toString()!=""){
							run.setText((String)ob,0);
						}else if (ob instanceof Map){
							run.setText("",0);
							Map pic = (Map)ob;
							int width = Integer.parseInt(pic.get("width").toString());
							int height = Integer.parseInt(pic.get("height").toString());
							int picType = getPictureType(pic.get("type").toString());
							byte[] byteArray = (byte[]) pic.get("content");
							ByteArrayInputStream byteInputStream = new ByteArrayInputStream(byteArray);
							try {
								String ind = document.addPictureData(byteInputStream,picType);
								document.createPicture(ind, 33,width, height,paragraph);
							} catch (Exception e) {
								e.printStackTrace();
							}
				}

//						String oneparaString = run.getText(run.getTextPosition());
//						if (StringUtils.isBlank(oneparaString)){
//							continue;
//						}
//						for (Map.Entry<String, Object> entry :
//								map.entrySet()) {
//							oneparaString = oneparaString.replace(entry.getKey(), entry.getValue().toString());
//						}
//						run.setText(oneparaString, 0);
					}

				}
				ByteArrayOutputStream ostream = new ByteArrayOutputStream();
				out = new FileOutputStream(new File(destPath));
				document.write(out);
				out.write(ostream.toByteArray());
				out.flush();

			}



		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(out != null) {
				out.close();
			}
			if(input != null) {
				input.close();
			}
		}
	}

    /**
     * 根据图片类型，取得对应的图片类型代码
     * @param picType
     * @return int
     */
    private static int getPictureType(String picType){
        int res = CustomXWPFDocument.PICTURE_TYPE_PICT;
        if(picType != null){
            if(picType.equalsIgnoreCase("png")){
                res = CustomXWPFDocument.PICTURE_TYPE_PNG;
            }else if(picType.equalsIgnoreCase("dib")){
                res = CustomXWPFDocument.PICTURE_TYPE_DIB;
            }else if(picType.equalsIgnoreCase("emf")){
                res = CustomXWPFDocument.PICTURE_TYPE_EMF;
            }else if(picType.equalsIgnoreCase("jpg") || picType.equalsIgnoreCase("jpeg")){
                res = CustomXWPFDocument.PICTURE_TYPE_JPEG;
            }else if(picType.equalsIgnoreCase("wmf")){
                res = CustomXWPFDocument.PICTURE_TYPE_WMF;
            }
        }
        return res;
    }
    /**
     * 匹配传入信息集合与模板
     * @param value 模板需要替换的区域
     * @param textMap 传入信息集合
     * @return 模板需要替换区域信息集合对应值
     */
    public static Object changeValue(String value, Map<String, Object> textMap){
        Set<Map.Entry<String, Object>> textSets = textMap.entrySet();
        Object valu = "";
        for (Map.Entry<String, Object> textSet : textSets) {
            //匹配模板与替换值 格式${key}
            String key = textSet.getKey();
            if(value.indexOf(key)!= -1){
                valu = textSet.getValue();
            }
        }
        return valu;
    }
	public static void main(String[] args) throws Exception {
		String sourcePath="E:/ideaWorkspace/dfjinxin-fagaiwei/fagaiwei/upload/2019/9/25/96e7df9f28a154b6c3af82a99a36bdf0.docx";
		String fileExtension=sourcePath.substring(sourcePath.lastIndexOf(".")+1,sourcePath.length());
		String subPath =  new StringBuilder(32).append("/")
				.append(DateTime.now().getYear()).append('/')
				.append(DateTime.now().getMonth() + 1).append('/')
				.append(DateTime.now().getDay() + 1)
				.append('/').toString() ;
		// Build sub file path
		String subFilePath = "E:/ideaWorkspace/dfjinxin-fagaiwei/fagaiwei"+subPath + "模板名称." + fileExtension;
		new File(subFilePath.substring(0,subFilePath.lastIndexOf("/"))).mkdirs();
		Map map=new HashMap<String,Object>();
		map.put("${1}",DateTime.toString(new Date(),"yyyy-MM-dd"));
		map.put("${2}",DateTime.toString(new Date(),"测试2"));

        Map<String,Object> picture1 = new HashMap<String, Object>();
        picture1.put("width", 100);
        picture1.put("height", 150);
        picture1.put("type", "jpg");
        picture1.put("content", new CustomXWPFDocument().inputStream2ByteArray(new FileInputStream("E:\\UserDirectory\\test.png"), true));
        map.put("${picture1}",picture1);
		WordUtil.replaceWord(sourcePath,subFilePath,map);
	}



}
