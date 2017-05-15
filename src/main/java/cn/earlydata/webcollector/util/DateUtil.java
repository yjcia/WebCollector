package cn.earlydata.webcollector.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Yanjun on 2017/04/27.
 */
public class DateUtil {
    private final static SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
    private final static SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
    private final static SimpleDateFormat sdfDays = new SimpleDateFormat("yyyyMMdd");
    private final static SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final static SimpleDateFormat sdfTimes = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * 获取YYYY格式
     * @return
     */
    public static String getSdfTimes() {
        return sdfTimes.format(new Date());
    }

    /**
     * 获取YYYY格式
     * @return
     */
    public static String getYear() {
        return sdfYear.format(new Date());
    }

    /**
     * 获取YYYY-MM-DD格式
     * @return
     */
    public static String getDay() {
        return sdfDay.format(new Date());
    }

    /**
     * 获取YYYYMMDD格式
     * @return
     */
    public static String getDays(){
        return sdfDays.format(new Date());
    }

    /**
     * 获取YYYY-MM-DD HH:mm:ss格式
     * @return
     */
    public static String getTime() {
        return sdfTime.format(new Date());
    }

    /**
     * @Title: compareDate
     * @Description: TODO(日期比较，如果s>=e 返回true 否则返回false)
     * @param s
     * @param e
     * @return boolean
     * @throws
     * @author fh
     */
    public static boolean compareDateEquals(String s, String e) {
        if(fomatDate(s)==null||fomatDate(e)==null){
            return false;
        }
        return fomatDate(s).getTime() == fomatDate(e).getTime();
    }

    public static boolean compareDateAfter(String s, String e) {
        if(fomatDate(s)==null||fomatDate(e)==null){
            return false;
        }
        return fomatDate(s).getTime() > fomatDate(e).getTime();
    }

    public static boolean compareDateEarlier(String s, String e) {
        if(fomatDate(s)==null||fomatDate(e)==null){
            return false;
        }
        return fomatDate(s).getTime() < fomatDate(e).getTime();
    }

    /**
     * 格式化日期
     * @return
     */
    public static Date fomatDate(String date) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return fmt.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date formatDate(String date,String pattern) {
        DateFormat fmt = new SimpleDateFormat(pattern);
        try {
            return fmt.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getFormatDay(Date day){
        return sdfDay.format(day);
    }

    /**
     * 校验日期是否合法
     * @return
     */
    public static boolean isValidDate(String s) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            fmt.parse(s);
            return true;
        } catch (Exception e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            return false;
        }
    }

    /**
     * @param startTime
     * @param endTime
     * @return
     */
    public static int getDiffYear(String startTime,String endTime) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            //long aa=0;
            int years=(int) (((fmt.parse(endTime).getTime()-fmt.parse(startTime).getTime())/ (1000 * 60 * 60 * 24))/365);
            return years;
        } catch (Exception e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            return 0;
        }
    }

    /**
     * <li>功能描述：时间相减得到天数
     * @param beginDateStr
     * @param endDateStr
     * @return
     * long
     * @author Administrator
     */
    public static long getDaySub(String beginDateStr,String endDateStr){
        long day=0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = null;
        Date endDate = null;

        try {
            beginDate = format.parse(beginDateStr);
            endDate= format.parse(endDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        day=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000);
        //System.out.println("相隔的天数="+day);

        return day;
    }

    /**
     * <li>功能描述：时间相减得到相差小时数
     * @param beginDateStr
     * @param endDateStr
     * @return
     * long
     * @author Administrator
     */
    public static long getHourSub(String beginDateStr,String endDateStr){
        long hour = 0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date beginDate = null;
        Date endDate = null;

        try {
            beginDate = format.parse(beginDateStr);
            endDate= format.parse(endDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        hour = (endDate.getTime()-beginDate.getTime())/(60*60*1000);

        return hour;
    }

    /**
     * 得到n天之后的日期
     * @param days
     * @return
     */
    public static String getAfterDayDate(String days) {
        int daysInt = Integer.parseInt(days);

        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();

        SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdfd.format(date);

        return dateStr;
    }

    public static String getAfterDayDate(String days,String pattern) {
        int daysInt = Integer.parseInt(days);

        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();

        SimpleDateFormat sdfd = new SimpleDateFormat(pattern);
        String dateStr = sdfd.format(date);

        return dateStr;
    }

    /**
     * 得到n天之后是周几
     * @param days
     * @return
     */
    public static String getAfterDayWeek(String days) {
        int daysInt = Integer.parseInt(days);
        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("E");
        String dateStr = sdf.format(date);
        return dateStr;
    }

    /**
     * 获得某一年的所有天数
     * @param year
     * @return
     */
    public static int getYearDateCount(int year){
        return new GregorianCalendar().isLeapYear(year) ? 366 : 365;
    }

    public static int getMaxDayOfMonth(int year,int month){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR,year);
        cal.set(Calendar.MONTH, month - 1);
        int maxDate = cal.getActualMaximum(Calendar.DATE);
        return maxDate;
    }

    public static int getMinDayOfMonth(int year,int month){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR,year);
        cal.set(Calendar.MONTH, month - 1);
        int minDate = cal.getActualMinimum(Calendar.DATE);
        return minDate;
    }

    /**
     *
     * @param dateStr yyyy-MM-dd
     * @return
     */
    public static int getDayPart(String dateStr){
        return Integer.parseInt(dateStr.substring(dateStr.lastIndexOf("-")+1));
    }

    /**
     * 得到指定日期前一天的日期
     * @param specifiedDay
     * @return
     */
    public static String getSpecifiedDayBefore(String specifiedDay){
        Calendar c = Calendar.getInstance();
        Date date=null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day=c.get(Calendar.DATE);
        c.set(Calendar.DATE,day-1);

        String dayBefore=new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        return dayBefore;
    }

    /**
     * 获得某年某周的第一天
     * @param year
     * @param week
     * @return
     */
    public static String getFirstDayOfWeek(String year,String week)
    {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR,Integer.parseInt(year));
        //设置周
        cal.set(Calendar.WEEK_OF_YEAR, Integer.parseInt(week));
        //设置该周第一天为周日
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String firstDayOfWeek = sdf.format(cal.getTime());

        return firstDayOfWeek;
    }
}
