/**
 * 2019 东方金信
 */

package io.dfjinxin.common.utils;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
        // 获取截止当前天数
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        Date nowDay = null;
        try {
            nowDay = format.parse(year + "");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String time = format.format(nowDay);
        String firstDay = time + "-01-01";
        return firstDay;
    }

//    /**
//     * 获取某年第一天日期
//     *
//     * @return Date
//     */
//    public static Date getYearFirst() {
//        Calendar calendar = Calendar.getInstance();
//        int year = calendar.get(Calendar.YEAR);
//        calendar.set(Calendar.YEAR, year);
//        Date currYearFirst = calendar.getTime();
//        return currYearFirst;
//    }

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
     * 获取当前系统时间前指定年的时间
     *
     * @return
     */
    public static String getLastYearByVal(int val) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN);
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, -val);
        date = calendar.getTime();
        return format.format(date);
    }

    /**
     * 获取当前系统时间前指定年的时间
     *
     * @return
     */
    public static String getLastMonthByVal(int val) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN);
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -val);
        date = calendar.getTime();
        return format.format(date);
    }


    public static void main(String[] args) {
        System.out.println(DateUtils.getLastMonthByVal(1));
        System.out.println(DateUtils.getLastYearByVal(1));
        System.out.println(DateUtils.dateToStr(DateUtils.addDateDays(new Date(),-1)));
//        System.out.println(DateUtils.getCurrentDayStr());
    }
}
