package io.dfjinxin.modules.sys.service.impl;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RConnection;

/**
 * @Desc:
 * @Author: z.h.c
 * @Date: 2019/9/19 14:47
 * @Version: 1.0
 */
public class R0919{
    public static void main(String[] args) {
        RConnection connection = null;
        int commId = 1;
        try {
            connection = new RConnection();
            System.out.println("clll R start-----------");
            connection.eval("source('D:/Rdemo0918.R')");
            System.out.println("load R file end----");
//            int ret = connection.eval("write_remove_seasonal_price(" + commId + ")").asInteger();
            int ret = connection.eval("fun3(" + commId + ")").asInteger();
            System.out.println("the call R return: "+ ret);
            System.out.println("clll R end-----------");
        } catch (REngineException e) {
            e.printStackTrace();
        } catch (REXPMismatchException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) connection.close();
        }
    }
}
