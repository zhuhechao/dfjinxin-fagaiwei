/**
 * 2019 东方金信
 */

package io.dfjinxin.common.utils;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期处理
 *
 * @author Mark sunlightcs@gmail.com
 */
public class DateUtils {
    /**
     * 时间格式(yyyy-MM-dd)
     */
    public final static String DATE_PATTERN = "yyyy-MM-dd";
    /**
     * 时间格式(yyyy-MM-dd HH:mm:ss)
     */
    public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式化 日期格式为：yyyy-MM-dd
     *
     * @param date 日期
     * @return 返回yyyy-MM-dd格式日期
     */
    public static String format(Date date) {
        return format(date, DATE_PATTERN);
    }

    /**
     * 日期格式化 日期格式为：yyyy-MM-dd
     *
     * @param date    日期
     * @param pattern 格式，如：DateUtils.DATE_TIME_PATTERN
     * @return 返回yyyy-MM-dd格式日期
     */
    public static String format(Date date, String pattern) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        }
        return null;
    }

    public static Date parseDate(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date newDate = null;
        try {
            newDate = sdf.parse(dateToStr(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }

    public static String dateToStr(Date date) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(DATE_PATTERN);
            return df.format(date);
        }
        return null;
    }

    /**
     * 字符串转换成日期
     *
     * @param strDate 日期字符串
     * @param pattern 日期的格式，如：DateUtils.DATE_TIME_PATTERN
     */
    public static Date stringToDate(String strDate, String pattern) {
        if (StringUtils.isBlank(strDate)) {
            return null;
        }

        DateTimeFormatter fmt = DateTimeFormat.forPattern(pattern);
        return fmt.parseLocalDateTime(strDate).toDate();
    }

    /**
     * 根据周数，获取开始日期、结束日期
     *
     * @param week 周期  0本周，-1上周，-2上上周，1下周，2下下周
     * @return 返回date[0]开始日期、date[1]结束日期
     */
    public static Date[] getWeekStartAndEnd(int week) {
        DateTime dateTime = new DateTime();
        LocalDate date = new LocalDate(dateTime.plusWeeks(week));

        date = date.dayOfWeek().withMinimumValue();
        Date beginDate = date.toDate();
        Date endDate = date.plusDays(6).toDate();
        return new Date[]{beginDate, endDate};
    }

    /**
     * 对日期的【秒】进行加/减
     *
     * @param date    日期
     * @param seconds 秒数，负数为减
     * @return 加/减几秒后的日期
     */
    public static Date addDateSeconds(Date date, int seconds) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusSeconds(seconds).toDate();
    }

    /**
     * 对日期的【分钟】进行加/减
     *
     * @param date    日期
     * @param minutes 分钟数，负数为减
     * @return 加/减几分钟后的日期
     */
    public static Date addDateMinutes(Date date, int minutes) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusMinutes(minutes).toDate();
    }

    /**
     * 对日期的【小时】进行加/减
     *
     * @param date  日期
     * @param hours 小时数，负数为减
     * @return 加/减几小时后的日期
     */
    public static Date addDateHours(Date date, int hours) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusHours(hours).toDate();
    }

    /**
     * 对日期的【天】进行加/减
     *
     * @param date 日期
     * @param days 天数，负数为减
     * @return 加/减几天后的日期
     */
    public static Date addDateDays(Date date, int days) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusDays(days).toDate();
    }

    /**
     * 对日期的【周】进行加/减
     *
     * @param date  日期
     * @param weeks 周数，负数为减
     * @return 加/减几周后的日期
     */
    public static Date addDateWeeks(Date date, int weeks) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusWeeks(weeks).toDate();
    }

    /**
     * 对日期的【月】进行加/减
     *
     * @param date   日期
     * @param months 月数，负数为减
     * @return 加/减几月后的日期
     */
    public static Date addDateMonths(Date date, int months) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusMonths(months).toDate();
    }

    /**
     * 对日期的【年】进行加/减
     *
     * @param date  日期
     * @param years 年数，负数为减
     * @return 加/减几年后的日期
     */
    public static Date addDateYears(Date date, int years) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusYears(years).toDate();
    }

    /**
     * 获取当月第一天
     *
     * @return
     */
    public static String getMonthFirstDayStr() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
        c.set(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateUtils.DATE_PATTERN);
        return simpleDateFormat.format(c.getTime());
    }

    /**
     * 获取当前年第一天
     *
     * @return
     */
    public static String getYearFirstDayStr() {
        SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_YEAR, 1);
        Date time = cal.getTime();
        return format.format(time);
    }

    /**
     * 获取当月最后一天
     *
     * @return
     */
    public static String getMonthLastDayStr() {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateUtils.DATE_PATTERN);
        return simpleDateFormat.format(ca.getTime());
    }

    /**
     * 获取本周最后一天
     *
     * @return
     */
    public static String getWeekLastDayStr() {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_WEEK, ca.getActualMaximum(Calendar.DAY_OF_WEEK));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateUtils.DATE_PATTERN);
        return simpleDateFormat.format(ca.getTime());
    }

    /**
     * 获取当前天
     *
     * @return
     */
    public static String getCurrentDayStr() {
        Date currentDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateUtils.DATE_PATTERN);
        return simpleDateFormat.format(currentDate);
    }


    /**
     * 获取当月的 天数
     */
    public static int getCurrentMonthDay() {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 获取当月1号到当天相差天数
     *
     * @return
     */
    public static long getMonthDiffDay() {
        String date1 = getMonthFirstDayStr();
        String date2 = getCurrentDayStr();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = sdf.parse(date1);
            d2 = sdf.parse(date2);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        long daysBetween = (d2.getTime() - d1.getTime() + 1000000) / (60 * 60 * 24 * 1000);
        return daysBetween;
    }

    /**
     * 获取当年1月1号到当天相差天数
     *
     * @return
     */
    public static long getYearDiffDay() {
        //设置转换的日期格式
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
        //开始时间
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = sdf.parse(DateUtils.getYearFirstDayStr());
            endDate = sdf.parse(DateUtils.getCurrentDayStr());
        } catch (Exception e) {

        }
        //得到相差的天数 betweenDate
        long betweenDate = (endDate.getTime() - startDate.getTime()) / (60 * 60 * 24 * 1000);
        return betweenDate;
    }

    /**
     * 获取一年前的日期
     *
     * @return
     */
    public static String getLastYearByVal(int val) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -val);
        Date date = calendar.getTime();
        return format.format(date);
    }

    /**
     * 获取一个月前的日期
     *
     * @return
     */
    public static String getLastMonthByVal(int val) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -val);
        Date date = calendar.getTime();
        return format.format(date);
    }

    /**
     * 获取上一年的第一天
     *
     * @return
     */
    public static String getLastYearFirstDayStr() {
        SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        Date date = calendar.getTime();
        return format.format(date);
    }

    /**
     * 获取上一月的第一天
     *
     * @return
     */
    public static String getLastMonthFirstDayStr() {
        SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date date = calendar.getTime();
        return format.format(date);
    }

    /**
     * 获取上一年的最后一天
     *
     * @return
     */
    public static String getLastYearLastDayStr() {
        SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, 0);
        Date date = calendar.getTime();
        return format.format(date);
    }

    /**
     * 获取上一月的最后一天
     *
     * @return
     */
    public static String getLastMonthLastDayStr() {
        SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        Date date = calendar.getTime();
        return format.format(date);
    }

    //JAVA获取某段时间内的所有日期
    public static List<Date> getDates(Date dStart, Date dEnd) {
        Calendar cStart = Calendar.getInstance();
        cStart.setTime(dStart);
        List dateList = new ArrayList();
        //别忘了，把起始日期加上
        dateList.add(dStart);
        // 此日期是否在指定日期之后
        while (dEnd.after(cStart.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cStart.add(Calendar.DAY_OF_MONTH, 1);
            dateList.add(cStart.getTime());
        }
        return dateList;
    }

    public static void main(String[] args) {


        String start = "2019-11-01";
        String end = "2019-11-19";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dStart = null;
        Date dEnd = null;
        try {
            dStart = sdf.parse(start);
            dEnd = sdf.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<Date> dateList = getDates(dStart, dEnd);
        for (Date date : dateList) {
            System.out.println(sdf.format(date));
        }


//        System.out.println(DateUtils.getLastMonthFirstDayStr());
//        System.out.println(DateUtils.getLastMonthLastDayStr());
//        System.out.println("000000000000000000000000");
//        System.out.println(DateUtils.getLastYearFirstDayStr());
//        System.out.println(DateUtils.getLastYearLastDayStr());
//        System.out.println("000000000000000000000000");
//        System.out.println(DateUtils.getMonthFirstDayStr());
//        System.out.println(DateUtils.getMonthLastDayStr());
//        System.out.println("000000000000000000000000");
//        System.out.println(DateUtils.getYearFirstDayStr());
//        System.out.println("000000000000000000000000");
//        System.out.println(DateUtils.getLastMonthByVal(1));
//        System.out.println(DateUtils.getLastYearByVal(3));
//        System.out.println(DateUtils.getYearDiffDay());
//            System.out.println(getMonthFirstDayStr());
//        System.out.println(DateUtils.getCurrentDayStr());
    }
}
