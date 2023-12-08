package cn.com.liurz.util.common;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil {
    public static final String Date_Format = "MM-dd";
    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String MMDD = "MMdd";
    public static final String DATE_PATTERN_HOUR = "yyyyMMddHH";
    public static final String DATE_PATTERN_DAY = "yyyyMMdd";
    public static final String Year_Mouth = "yyyy-MM";
    public static final String Mouth = "MM";
    public static final String Year_Mouth_Day = "yyyy-MM-dd";
    public static final String FIRST_TIME = "0000-01-01 00:00:00";
    public static final String LAST_TIME = "9999-12-31 23:59:59";
    public static final String UTC_TIME = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    private DateUtil() {

    }

    public static String format(Date date) {
        SimpleDateFormat formatTool = new SimpleDateFormat();
        formatTool.applyPattern("yyyy-MM-dd HH:mm:ss");
        return formatTool.format(date);
    }

    public static String format(Date date, String pattern) {
        SimpleDateFormat formatTool = new SimpleDateFormat();
        formatTool.applyPattern(pattern);
        return formatTool.format(date);
    }

    public static Date addMillSeconds(Date date, long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int span;
        if (time > 0L) {
            for(; time > 0L; calendar.add(14, span)) {
                if (time >= 2147483647L) {
                    span = 2147483647;
                    time -= 2147483647L;
                } else {
                    span = (int)time + 0;
                    time = 0L;
                }
            }
        } else if (time < 0L) {
            for(; time < 0L; calendar.add(14, span)) {
                if (time <= -2147483648L) {
                    span = -2147483648;
                    time -= -2147483648L;
                } else {
                    span = (int)time + 0;
                    time = 0L;
                }
            }
        }

        date = calendar.getTime();
        return date;
    }

    public static Date addMinutes(Date date, int n) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        cd.add(12, n);
        return cd.getTime();
    }

    public static Date addHours(Date date, int n) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        cd.add(10, n);
        return cd.getTime();
    }

    public static Date addDay(Date date, int n) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        cd.add(5, n);
        return cd.getTime();
    }

    public static Date addMouth(Date date, int n) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        cd.add(2, n);
        return cd.getTime();
    }

    public static String addNMouth(int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(2, n);
        String dateString = format(calendar.getTime());
        return dateString;
    }

    public static String addNYear(int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(1, n);
        String dateString = format(calendar.getTime());
        return dateString;
    }

    public static List<String> getDatesBetweenDays(String beginDay, String endDay, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date dBegin = sdf.parse(beginDay);
            Date dEnd = sdf.parse(endDay);
            List<Date> date = getDatesBetweenTwoDate(dBegin, dEnd);
            List<String> list = new ArrayList();

            for(int i = 0; i < date.size(); ++i) {
                list.add(sdf.format((Date)date.get(i)));
            }

            return list;
        } catch (ParseException var9) {
            throw new RuntimeException("时间解析出现错误");
        }
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

    public static Date parseDate(String str, String patten) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(patten);
        Calendar cd = Calendar.getInstance();
        cd.setTime(sdf.parse(str));
        return cd.getTime();
    }

    public static int getCurrentYear() {
        Calendar date = Calendar.getInstance();
        int year = date.get(1);
        return year;
    }

    public static int getCurrentMonth() {
        Calendar date = Calendar.getInstance();
        int month = date.get(2) + 1;
        return month;
    }

    public static int getDays(int year, int month) {
        int days = 0;
        if (month != 2) {
            switch(month) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    days = 31;
                case 2:
                default:
                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    days = 30;
            }
        } else if ((year % 4 != 0 || year % 100 == 0) && year % 400 != 0) {
            days = 28;
        } else {
            days = 29;
        }

        return days;
    }

    public static Date randomDate(String beginDate, String endDate, String dateFormat) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(dateFormat);
            Date start = format.parse(beginDate);
            Date end = format.parse(endDate);
            if (start.getTime() >= end.getTime()) {
                return null;
            } else {
                long date = random(start.getTime(), end.getTime());
                return new Date(date);
            }
        } catch (Exception var8) {
            var8.printStackTrace();
            return null;
        }
    }

    private static long random(long begin, long end) {
        long rtn = begin + (long)(Math.random() * (double)(end - begin));
        return rtn != begin && rtn != end ? rtn : random(begin, end);
    }

    public static long getBetweenDays(String beginTime, String endTime, String format) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date beginDate = sdf.parse(beginTime);
        Date endDate = sdf.parse(endTime);
        long betweenDate = (endDate.getTime() - beginDate.getTime()) / 86400000L;
        return betweenDate;
    }

    public static String parseUTC(String utcDate) {
        SimpleDateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date date = utcFormat.parse(utcDate);
            return sdf.format(date);
        } catch (ParseException var4) {
            var4.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        String utcTime = "2020-12-02T08:09:07.000Z";
        String parseUTC = parseUTC(utcTime);
        System.out.println(parseUTC);
        String currentDate = format(new Date());
        String lastMounth = addNMouth(-5);
        Date date = randomDate(lastMounth, currentDate, "yyyy-MM-dd HH:mm:ss");
        System.out.println(date);
    }
}

