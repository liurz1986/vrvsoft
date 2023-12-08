package cn.com.liurz.es.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtil {
    private static Logger log = LoggerFactory.getLogger(DateUtil.class);
    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String Year_Mouth = "yyyy-MM";
    public static final String Date_Format = "MM-dd";
    public static final String HH_MM = "HH:mm";
    public static final int YEAR_RETURN = 0;
    public static final int MONTH_RETURN = 1;
    public static final int DAY_RETURN = 2;
    public static final int HOUR_RETURN = 3;
    public static final int MINUTE_RETURN = 4;
    public static final int SECOND_RETURN = 5;
    public static final String FIRST_TIME = "0000-01-01 00:00:00";
    public static final String LAST_TIME = "9999-12-31 23:59:59";

    private DateUtil() {
    }

    public static Date stringToDate(String dateTime, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;

        try {
            date = sdf.parse(dateTime);
        } catch (ParseException var5) {
            var5.printStackTrace();
        }

        return date;
    }

    public static boolean compareTime(Date startTime, Date endTime) {
        return startTime.getTime() > endTime.getTime();
    }

    public static String format(Date date) {
        SimpleDateFormat formatTool = new SimpleDateFormat();
        formatTool.applyPattern("yyyy-MM-dd HH:mm:ss");
        return formatTool.format(date);
    }

    public static long getBetween(String beginTime, String endTime, String formatPattern, int returnPattern) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatPattern);
        Date beginDate = simpleDateFormat.parse(beginTime);
        Date endDate = simpleDateFormat.parse(endTime);
        Calendar beginCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        beginCalendar.setTime(beginDate);
        endCalendar.setTime(endDate);
        switch(returnPattern) {
            case 0:
                return getByField(beginCalendar, endCalendar, 1);
            case 1:
                return getByField(beginCalendar, endCalendar, 1) * 12L + getByField(beginCalendar, endCalendar, 2);
            case 2:
                return getTime(beginDate, endDate) / 86400000L;
            case 3:
                return getTime(beginDate, endDate) / 3600000L;
            case 4:
                return getTime(beginDate, endDate) / 60000L;
            case 5:
                return getTime(beginDate, endDate) / 1000L;
            default:
                return 0L;
        }
    }

    private static long getByField(Calendar beginCalendar, Calendar endCalendar, int calendarField) {
        return (long)(endCalendar.get(calendarField) - beginCalendar.get(calendarField));
    }

    private static long getTime(Date beginDate, Date endDate) {
        return endDate.getTime() - beginDate.getTime();
    }

    public static Date parseDate(String str, String patten) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(patten);
        Calendar cd = Calendar.getInstance();
        cd.setTime(sdf.parse(str));
        return cd.getTime();
    }

    public static String format(Date date, String pattern) {
        SimpleDateFormat formatTool = new SimpleDateFormat();
        formatTool.applyPattern(pattern);
        return formatTool.format(date);
    }

    public static String getBeforeMouth() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(2, -1);
        String dateString = format(calendar.getTime());
        return dateString;
    }

    public static List<String> getDatesBetweenDays(String beginDay, String endDay, String format) {
        ArrayList list = new ArrayList();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date dBegin = sdf.parse(beginDay);
            Date dEnd = sdf.parse(endDay);
            List<Date> date = getDatesBetweenTwoDate(dBegin, dEnd);

            for(int i = 0; i < date.size(); ++i) {
                list.add(sdf.format((Date)date.get(i)));
            }
        } catch (ParseException var9) {
            var9.printStackTrace();
        }

        return list;
    }

    public static List<Date> getDatesBetweenTwoDate(Date beginDate, Date endDate) {
        List<Date> lDate = new ArrayList();
        lDate.add(beginDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(beginDate);
        boolean bContinue = true;

        while(bContinue) {
            cal.add(5, 1);
            if (!endDate.after(cal.getTime())) {
                break;
            }

            lDate.add(cal.getTime());
        }

        lDate.add(endDate);
        return lDate;
    }

    public static String getBeforeYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(1, -1);
        String dateString = format(calendar.getTime());
        return dateString;
    }

    public static Date getNextWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(5, -7);
        date = calendar.getTime();
        return date;
    }

    public static List<String> getBetweenMounths(String beginTime, String endTime, String format) {
        List<String> list = new ArrayList();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date d1 = null;
        Date d2 = null;

        try {
            d1 = sdf.parse(beginTime);
            d2 = sdf.parse(endTime);
        } catch (ParseException var9) {
            var9.printStackTrace();
        }

        Calendar dd = Calendar.getInstance();
        dd.setTime(d1);

        while(dd.getTime().before(d2)) {
            String str = sdf.format(dd.getTime());
            list.add(str);
            dd.add(2, 1);
        }

        return list;
    }

    public static String getBeforeMouthByNum(int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(2, -num);
        String dateString = format(calendar.getTime());
        return dateString;
    }

    public static Date addMinutes(Date date, int n) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        cd.add(12, n);
        return cd.getTime();
    }

    public static Date addDay(Date date, int n) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        cd.add(5, n);
        return cd.getTime();
    }

    public static Date addHours(Date date, int n) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        cd.add(10, n);
        return cd.getTime();
    }

    public static Date addSeconds(Date date, int n) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        cd.add(13, n);
        return cd.getTime();
    }


    public static Map<String, Object> getTimeFullMapForMonth(String timeBegin, String timeEnd, String timeFormat, Map<String, Object> maps) {
        LinkedHashMap result = new LinkedHashMap();

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
            Date sdate = format.parse(timeBegin);
            Calendar sCal = Calendar.getInstance();
            sCal.setTime(sdate);
            Date edate = format.parse(timeEnd);
            Calendar eCel = Calendar.getInstance();
            eCel.setTime(edate);

            for(Calendar c = sCal; c.before(eCel) || c.equals(eCel); c.add(2, 1)) {
                String str = format(c.getTime(), timeFormat);
                if (maps.containsKey(str)) {
                    result.put(str, maps.get(str));
                } else {
                    result.put(str, 0);
                }
            }

            return result;
        } catch (Exception var12) {
            log.error("安月时间补全错误：", var12);
            return null;
        }
    }

    public static String timeStamp2Date(String seconds, String format) {
        if (seconds != null && !seconds.isEmpty() && !seconds.equals("null")) {
            if (format == null || format.isEmpty()) {
                format = "yyyy-MM-dd HH:mm:ss";
            }

            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(new Date(Long.valueOf(seconds + "000")));
        } else {
            return "";
        }
    }
}
