package io.dfjinxin.modules.sys.service.impl;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

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

        try (BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream(), "gbk"))) {
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
        }
        proc.waitFor();
    }

    @Test
    public void demo2() throws IOException, InterruptedException {
        String file = new File("").getCanonicalPath() + "\\py\\groupdata.py";

        String[] args = new String[]{"python", file};
        Process proc = Runtime.getRuntime().exec(args);// 执行py文件

        try (BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream(), "gbk"))) {
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
        }
        proc.waitFor();
    }
}
