package cn.tool.code;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jiazhen
 * @description: mybatis日志中的sql语句重组(根据mapper中的预编译sql与参数)
 * @email jarzhen@163.com
 * @datetime 2022/8/31 9:00
 * @version: 1.0
 */
public class RecombineSql {
    @Test
    public void test1() throws IOException {
        String filePath = Thread.currentThread().getContextClassLoader().getResource("logs/mybatisSql.log").getPath();
        File file = new File(filePath);
        String logs = FileUtils.readFileToString(file, "utf-8");
        System.out.println(recombineSql(logs));
    }

    private String recombineSql(String logs) {
        Pattern pSql = Pattern.compile("(?<=Preparing: )(.*?)\r?\n");
        Pattern pParameters = Pattern.compile("(?<=Parameters:)(.*?)(\r?\n|$)");
        Pattern pLocation = Pattern.compile("(?<= ?)(\\?)");
        Pattern pParameter = Pattern.compile("(?<=,? )(.*?)(\\((\\w+)\\))?,");
        Matcher mSql = pSql.matcher(logs);
        Matcher mParameters = pParameters.matcher(logs);
        List<String> sqlList = new ArrayList<>();
        while (mSql.find()) {
            String sql = mSql.group(1);
            System.out.println(sql);
            String location;
            String parameter;
            String parameterType;
            if (mParameters.find()) {
                String parametersInlogs = mParameters.group(1);
                System.out.println(parametersInlogs);
                parametersInlogs = parametersInlogs + ",";
                Matcher mLocation = pLocation.matcher(sql);
                List<String> locationList = new ArrayList<>();
                while (mLocation.find()) {
                    location = mLocation.group(1);
                    locationList.add(location);
                }
                Matcher mParameter = pParameter.matcher(parametersInlogs);
                List<String> parameterList = new ArrayList<>();
                List<String> parameterTypeList = new ArrayList<>();
                while (mParameter.find()) {
                    parameter = mParameter.group(1);
                    parameterList.add(parameter);
                    parameterType = mParameter.group(2);
                    parameterTypeList.add(parameterType);
                }
                System.out.println(StringUtils.join(locationList.toArray(), ","));
                System.out.println(StringUtils.join(parameterList.toArray(), ","));
                System.out.println(StringUtils.join(parameterTypeList.toArray(), ","));
                int l = locationList.size();
                int p = parameterList.size();
                int pt = parameterTypeList.size();
                if (l == p && p == pt) {
                    for (int i = 0; i < parameterList.size(); i++) {
                        String find = locationList.get(i);
                        String replace = parameterList.get(i);
                        if (StringUtils.equals("(String)", parameterTypeList.get(i)) ||
                                StringUtils.equals("(Date)", parameterTypeList.get(i)) ||
                                StringUtils.equals("(Timestamp)", parameterTypeList.get(i))) {
                            replace = String.format("'%s'", replace);
                        }
                        sql = StringUtils.replaceOnce(sql, find, replace);
                    }
                    System.out.println(String.format("占位符数量[%d]、参数数量[%d]、参数类型数量[%d]一致,将重组sql:", l, p, pt));
                    System.out.println(sql);
                } else {
                    throw new RuntimeException(String.format("占位符数量[%d]、参数数量[%d]、参数类型数量[%d]不一致，请检查！", l, p, pt));
                }
            }
            sqlList.add(sql + ";");
        }
        return StringUtils.join(sqlList, "\n");
    }
}