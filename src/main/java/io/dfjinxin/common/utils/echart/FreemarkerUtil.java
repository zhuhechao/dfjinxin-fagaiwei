package io.dfjinxin.common.utils.echart;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreemarkerUtil {
    private static final String path = FreemarkerUtil.class.getClassLoader().getResource("").getPath();

    public static String generateString(String templateFileName, String templateDirectory, Map<String, Object> datas)
            throws IOException, TemplateException {
        Configuration configuration = new Configuration();

        // 设置默认编码
        configuration.setDefaultEncoding("UTF-8");

        // 设置模板所在文件夹
        configuration.setDirectoryForTemplateLoading(new File(path + templateDirectory));

        // 生成模板对象
        Template template = configuration.getTemplate(templateFileName);

        // 将datas写入模板并返回
        try (StringWriter stringWriter = new StringWriter()) {
            template.process(datas, stringWriter);
            stringWriter.flush();
            return stringWriter.getBuffer().toString();
        }
    }
}