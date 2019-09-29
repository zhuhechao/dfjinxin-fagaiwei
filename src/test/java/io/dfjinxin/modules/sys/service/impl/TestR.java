package io.dfjinxin.modules.sys.service.impl;

import io.dfjinxin.common.utils.FileUtil;
import io.dfjinxin.modules.price.entity.PssPriceEwarnEntity;
import org.junit.Test;
import org.rosuda.REngine.*;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

/**
 * 测试R语言
 *
 * @author bourne
 * @email kuibobo@gmail.com
 * @date 2019-09-05 17:22:40
 */
public class TestR {

    /**
     * 简单获取 R 语言版本
     *
     * @throws RserveException
     * @throws REXPMismatchException
     */
    @Test
    public void hello() throws RserveException, REXPMismatchException {
        RConnection c = new RConnection();

        REXP x = c.eval("R.version.string");

        System.out.println(x.asString());
    }

    /**
     * 执行简单的函数
     *
     * @throws RserveException
     * @throws REXPMismatchException
     */
    @Test
    public void demo() throws RserveException, REXPMismatchException {
        RConnection connection = new RConnection();
        String vetor = "c(1,2,3,4)";
        double mean = 0;

        connection.eval("meanVal<-mean(" + vetor + ")");
        mean = connection.eval("meanVal").asDouble();
        System.out.println("the mean of given vector is=" + mean);
        connection.close();
    }

    /**
     * 执行来自R脚本的简单函数
     *
     * @throws IOException
     * @throws REXPMismatchException
     */
    @Test
    public void demo2() throws IOException, REXPMismatchException {
        String file = new File("").getCanonicalPath() + "\\r\\sum.r";
        RConnection connection = null;

        file = file.replace("\\", "/");
        try {
            connection = new RConnection();
            connection.eval("source('" + file + "')");
            int num1 = 20;
            int num2 = 10;
            int sum = connection.eval("sum(" + num1 + "," + num2 + ")").asInteger();
            System.out.println("the sum=" + sum);
        } catch (RserveException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) connection.close();
        }
    }

    /**
     * Rserve onnection.eval不支持中文 调用一般相关性分析.R
     *
     * @throws IOException
     * @throws REXPMismatchException
     */
    @Test
    public void demo3() throws IOException, REXPMismatchException {
        String file = new File("").getCanonicalPath() + "\\r\\demo3.R";
        String cvsFile = new File("").getCanonicalPath() + "\\r\\cor-data.csv";
        RConnection connection = null;
        RList rList = null;

        file = file.replace("\\", "/");
        cvsFile = cvsFile.replace("\\", "/");
        try {
            connection = new RConnection();
            connection.eval("source('" + file + "')");
            rList = connection.eval("fun5('" + cvsFile + "')").asList();
            REXPString pfile = (REXPString) rList.get(0);
            REXPString rfile = (REXPString) rList.get(1);

            System.out.println(FileUtil.getFileContent(pfile.asString(), "gbk"));
            System.out.println(FileUtil.getFileContent(rfile.asString(), "gbk"));
        } catch (REngineException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) connection.close();
        }
    }

    @Test
    public void demo4() throws IOException, REXPMismatchException {
        String file = new File("").getCanonicalPath() + "\\r\\Rdemo0918.R";
//        String cvsFile = new File("").getCanonicalPath() + "\\r\\cor-data.csv";
        RConnection connection = null;

//        RList rList = null;
        int commId = 1;

        file = file.replace("\\", "/");
//        cvsFile = cvsFile.replace("\\", "/");
        try {
            connection = new RConnection();
            connection.eval("source('" + file + "')");
//            "myAdd(" + num1 + "," + num2 + ")"
            connection.eval("period(" + new Date() + "," + new Date() + "," + commId + ")");
//            REXPString pfile = (REXPString) rList.get(0);
//            REXPString rfile = (REXPString) rList.get(1);

//            System.out.println(FileUtil.getFileContent(pfile.asString(), "gbk"));
//            System.out.println(FileUtil.getFileContent(rfile.asString(), "gbk"));
        } catch (REngineException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) connection.close();
        }
    }

    @Test
    public void demo5() throws IOException, REXPMismatchException {
        String file = new File("").getCanonicalPath() + "\\r\\demo3.R";
        RConnection connection = null;
        int a =1;

        file = file.replace("\\", "/");
        try {
            connection = new RConnection();
            connection.eval("source('" + file + "')");
            int retu = connection.eval("fun3(" + a + ")").asInteger();
            System.out.println("the call R return: "+retu);
        } catch (REngineException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) connection.close();
        }
    }

    @Test
    public void contionRateVal() {

        int num1 = 4;

        int num2 = 6;

        // 创建一个数值格式化对象

        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);

        String result = numberFormat.format((float) num1 / (float) num2 * 100);

        System.out.println("num1和num2的百分比为:" + result + "%");

        DecimalFormat df = new DecimalFormat("#.00");
        System.out.println("占比是" + df.format(Float.valueOf((num1/num2))*100)+"%");

    }

}
