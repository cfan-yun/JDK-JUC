package com.cyfan.my.test.file;

import java.io.File;
import java.io.IOException;

public class FileTest {

    public static void main(String[] args) throws IOException {
        String userDir = System.getProperty("user.dir");
        File f = new File(userDir, ".."); // 获取用户文件夹的上级目录，即与user.dir同级的所有目录
        String path = f.getPath();
        String absolutePath = f.getAbsolutePath();
        String canonicalPath = f.getCanonicalPath();
    }
}
