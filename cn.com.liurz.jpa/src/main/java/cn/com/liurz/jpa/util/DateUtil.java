package cn.com.liurz.jpa.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static String format(Date date) {
        SimpleDateFormat formatTool = new SimpleDateFormat();
        formatTool.applyPattern("yyyy-MM-dd HH:mm:ss");
        return formatTool.format(date);
    }
}
