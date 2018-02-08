package com.mingnong.scanappnew.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zhyzh on 2016/10/27.
 */
public class DateUtil {

    /**
     * 验证输入日期的合法性
     *
     * @param str
     * @return
     */
    public static boolean isValidDate(String str) {
        boolean convertSuccess = false;
        int flag = 0;
        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        try {
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            format.setLenient(false);
            format.parse(str);
        } catch (ParseException e) {
            // e.printStackTrace();
// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            convertSuccess = true;
        }
        return convertSuccess;
    }

    /**
     * 验证两个日期的大小
     *
     * @param DATE1
     * @param DATE2
     * @return
     */
    public static boolean compareDate(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                System.out.println("dt1 在dt2前");
                return true;
            }
//            else if (dt1.getTime() < dt2.getTime()) {
//                System.out.println("dt1在dt2后");
//                return false;
//            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    /**
     * 验证传进来的日期与当前日期的大小
     *
     * @param DATE
     * @return
     */
    public static boolean compareNewDate(String DATE) {
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Date dt1 = df.parse(DATE);
            Date dt2 = new Date();
            if (dt1.getTime() > dt2.getTime()) {
                System.out.println("dt1 在dt2前");
                return true;
            }
//            else if (dt1.getTime() < dt2.getTime()) {
//                System.out.println("dt1在dt2后");
//                return false;
//            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public static boolean isValidDates(String str) {
        int year = Integer.valueOf(str.substring(0, 4));
        int moth = Integer.valueOf(str.substring(4, 6));
        int day = Integer.valueOf(str.substring(6, 8));
        switch (moth) {
            case 1:
                if (day > 31 || day < 1) {
                    return true;
                }
                break;
            case 2:
                if (isLeapYear(year)) {
                    if (day > 29 || day < 1) {
                        return true;
                    }
                }else{
                    if (day > 28 || day < 1) {
                        return true;
                    }
                }
                break;
            case 3:
                if (day > 31 || day < 1) {
                    return true;
                }
                break;
            case 4:
                if (day > 30 || day < 1) {
                    return true;
                }
                break;
            case 5:
                if (day > 31 || day < 1) {
                    return true;
                }
                break;
            case 6:
                if (day > 30 || day < 1) {
                    return true;
                }
                break;
            case 7:
                if (day > 31 || day < 1) {
                    return true;
                }
                break;
            case 8:
                if (day > 31 || day < 1) {
                    return true;
                }
                break;
            case 9:
                if (day > 30 || day < 1) {
                    return true;
                }
                break;
            case 10:
                if (day > 31 || day < 1) {
                    return true;
                }
                break;
            case 11:
                if (day > 30 || day < 1) {
                    return true;
                }
                break;
            case 12:
                if (day > 31 || day < 1) {
                    return true;
                }
                break;
        }
        return false;
    }


    public static boolean isLeapYear(int years){
        Calendar cal= Calendar.getInstance();
        cal.set(years, Calendar.DECEMBER,31);
        if(cal.get(Calendar.DAY_OF_YEAR)==366){
            //闰年
         return true;
        }else{
            //平年
            return false;
        }

    }
}
