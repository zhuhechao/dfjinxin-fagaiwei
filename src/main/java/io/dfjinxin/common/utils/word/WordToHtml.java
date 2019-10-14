package io.dfjinxin.common.utils.word;

import java.io.*;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.xwpf.converter.core.BasicURIResolver;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.w3c.dom.Document;

/**
 * <p>Title:Word2007ToHtml </p>
 * <p>Company: </p>
 * @author
 * @date 2018年4月11日下午2:21:30
 * Description:
 */
public class WordToHtml {

    public static String word2007ToHtml(String sourceFileName,String reportResultPath,String rptId) throws Exception {
        //String filepath = "E:\\ideaWorkspace\\dfjinxin-fagaiwei\\fagaiwei\\2019\\9\\29\\";
        //String sourceFileName =filepath+"模板名称.docx";

        String targetFileName =sourceFileName.replace("docx","html");
        String imagePathStr = reportResultPath+"/image/"+rptId+"/" ;
        OutputStreamWriter outputStreamWriter = null;
        try {
          XWPFDocument document = new XWPFDocument(new FileInputStream(sourceFileName));
          XHTMLOptions options = XHTMLOptions.create();
          // 存放图片的文件夹
          options.setExtractor(new FileImageExtractor(new File(imagePathStr)));
          // html中图片的路径
          options.URIResolver(new BasicURIResolver("image/"+rptId));
          outputStreamWriter = new OutputStreamWriter(new FileOutputStream(targetFileName), "utf-8");
          XHTMLConverter xhtmlConverter = (XHTMLConverter) XHTMLConverter.getInstance();
          xhtmlConverter.convert(document, outputStreamWriter, options);
        } finally {
          if (outputStreamWriter != null) {
            outputStreamWriter.close();
          }
        }
        return targetFileName;
      }

    public static void main(String[] args) throws Throwable {

        //WordToHtml.word2007ToHtml();
    }
}
