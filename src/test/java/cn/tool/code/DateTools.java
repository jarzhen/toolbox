package cn.tool.code;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author jiazhen
 * @ClassName DateTools
 * @description: TODO
 * @email jarzhen@163.com
 * @datetime 2022年 09月 20日 11:53
 * @version: 1.0
 */
public class DateTools {
    @Test
    public void test_day() throws ParseException {
        String json = "";
        json = generateJson("2022-02-02","2022-05-02","day");
        System.out.println(json);
    }
    @Test
    public void test_week() throws ParseException {
        String json = "";
        json = generateJson("2022-02-02","2022-05-02","week");
        System.out.println(json);
    }
    @Test
    public void test_month() throws ParseException {
        String json = "";
        json = generateJson("2022-02-02","2022-05-02","month");
        System.out.println(json);
    }
    @Test
    public void test_quarter() throws ParseException {
        String json = "";
        json = generateJson("2022-02-02","2022-05-02","quarter");
        System.out.println(json);
    }

    public String generateJson(String dateStart, String dateEnd, String unit) throws ParseException {
        Calendar calendarStart = convert2Calendar(dateStart);
        Calendar calendarEnd = convert2Calendar(dateEnd);
        List<Calendar> calendarList = new ArrayList<>();
        JSONObject jsonObj = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        switch (unit) {
            case "day":
                while (!calendarStart.equals(calendarEnd)) {
                    calendarList.add((Calendar) calendarStart.clone());
                    calendarStart.add(Calendar.DATE, 1);
                }
                for (Calendar calendar : calendarList) {
                    JSONObject dayObj = new JSONObject();
                    dayObj.put("start", convert2StringStart(calendar));
                    dayObj.put("end", convert2StringEnd(calendar));
                    jsonArray.add(dayObj);
                }
                break;
            case "week":
                while (calendarStart.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
                    calendarStart.add(Calendar.DATE, -1);
                }
                while (calendarEnd.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                    calendarEnd.add(Calendar.DATE, 1);
                }
                Calendar weekStart = (Calendar) calendarStart.clone();
                Calendar weekEnd = (Calendar) calendarStart.clone();
                weekEnd.add(Calendar.DATE, 6);
                while (!weekEnd.equals(calendarEnd)) {
                    JSONObject dayObj = new JSONObject();
                    dayObj.put("start", convert2StringStart(weekStart));
                    dayObj.put("end", convert2StringEnd(weekEnd));
                    jsonArray.add(dayObj);
                    weekStart.add(Calendar.DATE, 7);
                    weekEnd.add(Calendar.DATE, 7);
                }
                break;
            case "month":
                calendarStart.set(Calendar.DAY_OF_MONTH, 1);
                calendarEnd.set(Calendar.DAY_OF_MONTH, calendarEnd.getActualMaximum(Calendar.DAY_OF_MONTH));
                Calendar tempMonthEnd = (Calendar) calendarEnd.clone();
                tempMonthEnd.set(Calendar.DAY_OF_MONTH, 1);
                while (!calendarStart.after(tempMonthEnd)) {
                    Calendar monthStart = (Calendar) calendarStart.clone();
                    Calendar monthEnd = (Calendar) calendarStart.clone();
                    monthEnd.set(Calendar.DAY_OF_MONTH, calendarStart.getActualMaximum(Calendar.DAY_OF_MONTH));
                    JSONObject dayObj = new JSONObject();
                    dayObj.put("start", convert2StringStart(monthStart));
                    dayObj.put("end", convert2StringEnd(monthEnd));
                    jsonArray.add(dayObj);
                    calendarStart.add(Calendar.MONTH, 1);
                    calendarStart.set(Calendar.DAY_OF_MONTH, 1);
                }
                break;
            case "quarter":
                int start = calendarStart.get(Calendar.MONTH);
                calendarStart.set(Calendar.MONTH, (start / 3) * 3);
                calendarStart.set(Calendar.DAY_OF_MONTH, 1);
                int end = calendarEnd.get(Calendar.MONTH);
                calendarEnd.set(Calendar.MONTH, (end / 3) * 3 + 3);
                calendarEnd.set(Calendar.DAY_OF_MONTH, calendarEnd.getActualMaximum(Calendar.DAY_OF_MONTH));
                int tempEnd = calendarStart.get(Calendar.MONTH);
                Calendar tempQuarterEnd = (Calendar)calendarStart.clone();
                tempQuarterEnd.set(Calendar.MONTH, (tempEnd / 3) + 2);
                tempQuarterEnd.set(Calendar.DAY_OF_MONTH, calendarStart.getActualMaximum(Calendar.DAY_OF_MONTH));
                while (!tempQuarterEnd.after(calendarEnd)) {
                    JSONObject dayObj = new JSONObject();
                    dayObj.put("start", convert2StringStart(calendarStart));
                    dayObj.put("end", convert2StringEnd(tempQuarterEnd));
                    jsonArray.add(dayObj);
                    calendarStart.add(Calendar.MONTH, 3);
                    calendarStart.set(Calendar.DAY_OF_MONTH, 1);
                    tempQuarterEnd.add(Calendar.MONTH, 3);
                    tempQuarterEnd.set(Calendar.DAY_OF_MONTH, 1);
                }
                break;
            default:
                System.out.println(String.format("时间单位[%s]非法参数！取值范围是[day,week,month,quarter]", unit));
        }
        jsonObj.put(unit, jsonArray);
        return StringTools.fastFormat(jsonObj);
    }

    private Calendar convert2Calendar(String dateString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(dateString);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    private String convert2String(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(calendar.getTime());
    }

    private String convert2StringStart(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        return sdf.format(calendar.getTime());
    }

    private String convert2StringEnd(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        return sdf.format(calendar.getTime());
    }

    private void codeSnippet() throws ParseException {
        Calendar calendar = Calendar.getInstance();
        Calendar start = (Calendar) calendar.clone();
        Calendar end = (Calendar) calendar.clone();
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        end.set(Calendar.HOUR_OF_DAY, 23);
        end.set(Calendar.MINUTE, 59);
        end.set(Calendar.SECOND, 59);

        String str = "2012-5-27";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(str);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
    }
}
