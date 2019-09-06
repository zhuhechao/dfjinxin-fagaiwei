package io.dfjinxin.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileUtil {

    public static String getFileContent(String path) throws IOException {
        return getFileContent(path, "utf-8");
    }

    public static String getFileContent(String path, String charsetName) throws IOException {
        byte[] datas = null;
        String content = null;

        datas = getFileData(path);
        content = new String(datas, charsetName);
        return content;
    }

    public static byte[] getFileData(String fullpath) throws IOException {
        return getFileData(new File(fullpath));
    }

    public static byte[] getFileData(File file) throws IOException {
        byte[] data = new byte[1024];
        int size = 0;

        try (FileInputStream fis = new FileInputStream(file); ByteArrayOutputStream baos = new ByteArrayOutputStream(1024)) {
            while ((size = fis.read(data)) != -1)
                baos.write(data, 0, size);

            data = baos.toByteArray();
        }

        return data;
    }
}
