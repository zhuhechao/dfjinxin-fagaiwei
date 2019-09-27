package io.dfjinxin.modules.sys.service.impl;

import org.junit.Test;

import java.io.*;

/**
 * 测试Python语言
 *
 * @author bourne
 * @email kuibobo@gmail.com
 * @date 2019-09-05 17:22:40
 */
public class TestPy {

    @Test
    public void demo1() throws IOException, InterruptedException {
        String file = new File("").getCanonicalPath() + "\\py\\demo.py";

        int a = 3;
        int b = 5;
        String[] args = new String[]{"python", file, String.valueOf(a), String.valueOf(b)};
        Process proc = Runtime.getRuntime().exec(args);// 执行py文件
        BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            System.out.println("***");
            System.out.println("the result:"+line);
        }
        in.close();
        proc.waitFor();
        System.out.println("end");
    }

    @Test
    public void demo2() throws IOException, InterruptedException {
        try {
            String file = new File("").getCanonicalPath() + "\\py\\groupdata.py";

            String[] args = new String[]{"python", file};
            Process proc = Runtime.getRuntime().exec(args);// 执行py文件

            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            proc.waitFor();
            System.out.println("end");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

