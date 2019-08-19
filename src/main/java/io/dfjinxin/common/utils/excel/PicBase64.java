package io.dfjinxin.common.utils.excel;

import com.alibaba.druid.util.StringUtils;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.UUID;

public class PicBase64 {
	
	public static String getPic(){
		URL url = PicBase64.class.getClassLoader()
				.getResource(File.separator);
		String url1 =  url.getPath();
		return url.getPath();
	}
	/** 
	 * 解析base64，返回图片所在路径 
	 * @param base64Info 
	 * @return 
	 */  
	public static String decodeBase64(String base64Info, String filePath){  
	    if(StringUtils.isEmpty(base64Info)){  
	        return null;  
	    }  
	    BASE64Decoder decoder = new BASE64Decoder();  
	    String[] arr = base64Info.split("base64,");           
	    // 数据中：data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAABI4AAAEsCAYAAAClh/jbAAA ...  在"base64,"之后的才是图片信息  
	    String picPath = filePath+ "/"+ UUID.randomUUID().toString() +".png";  
	    try {  
	        byte[] buffer = decoder.decodeBuffer(arr[1]);  
	        OutputStream os = new FileOutputStream(picPath);  
	        os.write(buffer);  
	        os.close();  
	    } catch (IOException e) {  
	        throw new RuntimeException();  
	    }  
	      
	    return picPath;  
	}
}
