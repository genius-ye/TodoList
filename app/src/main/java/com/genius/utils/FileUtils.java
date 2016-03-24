package com.genius.utils;

import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by 112 on 2016/3/16.
 */
public class FileUtils {

    /**
     * 读取文件
     *
     * @param context
     * @param filePath
     * @return
     * @throws IOException
     */
    public static String readFile(Context context, String filePath){
        String content = null;
        File file = new File(filePath);
        if (file.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                int length = fileInputStream.available();
                byte[] bytes = new byte[length];
                fileInputStream.read(bytes);
                content = new String(bytes);
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, "文件不存在", Toast.LENGTH_SHORT).show();
        }

        return content;
    }

    /**
     * 写文件
     *
     * @param context
     * @param filePath
     * @param content
     * @throws IOException
     */
    public static void writeFile(Context context, String filePath, String content) {
        File file = new File(filePath);
        if (file.isDirectory()) {
            return;
        } else {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(content.getBytes());
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
