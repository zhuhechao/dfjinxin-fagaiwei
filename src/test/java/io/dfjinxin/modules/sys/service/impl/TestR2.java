package io.dfjinxin.modules.sys.service.impl;

import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RConnection;

import java.io.File;
import java.io.IOException;


/**
 * @Desc:
 * @Author: z.h.c
 * @Date: 2019/9/18 18:07
 * @Version: 1.0
 */
public class TestR2 {
    public static void main(String[] args)throws IOException {
        String file = new File("").getCanonicalPath() + "\\r\\Rdemo0918.R";
        RConnection connection = null;
        int commId = 1;
        try {
            connection = new RConnection();
            System.out.println("clll R start-----------");
            connection.eval("source('D:/Rdemo0918.R')");
            connection.eval("write_remove_seasonal_price(" + commId + ")");
            System.out.println("clll R end-----------");
        } catch (REngineException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) connection.close();
        }
    }
}
