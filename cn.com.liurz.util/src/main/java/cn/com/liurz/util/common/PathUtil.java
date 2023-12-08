package cn.com.liurz.util.common;

import java.io.File;

public class PathUtil {
    public PathUtil() {
    }

    public static String combine(String path1, String path2) {
        if (!path1.endsWith(File.separator)) {
            path1 = path1 + File.separator;
        }

        if (path2.startsWith(File.separator)) {
            path2 = path2.substring(1);
        }

        return path1 + path2;
    }

    public static String combine(String path1, String path2, String separator) {
        if (!path1.endsWith(separator)) {
            path1 = path1 + separator;
        }

        if (path2.startsWith(separator)) {
            path2 = path2.substring(1);
        }

        return path1 + path2;
    }
}
