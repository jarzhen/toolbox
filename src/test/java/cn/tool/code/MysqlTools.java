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
 * @ClassName MysqlTools
 * @description: TODO
 * @email jarzhen@163.com
 * @datetime 2022年 09月 21日 9:44
 * @version: 1.0
 */
public class MysqlTools {

    @Test
    public void test1() throws IOException {
        String sqlExampleFilePath = Thread.currentThread().getContextClassLoader().getResource("mysql/insertExample.sql").getPath();
        File sqlExampleFile = new File(sqlExampleFilePath);
        String insertExample = FileUtils.readFileToString(sqlExampleFile, "utf-8");
        String textFilePath = Thread.currentThread().getContextClassLoader().getResource("mysql/text.txt").getPath();
        File textFile = new File(textFilePath);
        String text = FileUtils.readFileToString(textFile, "utf-8");
        String sqls = textToSql(insertExample, text);
        File sqlFile = new File("D:/git_project/toolbox/src/test/resources/mysql/insert.sql");
        FileUtils.write(sqlFile, sqls, "utf-8");
        System.out.println(sqls);
    }

    public String textToSql(String insertExample, String text) {
        Pattern pText = Pattern.compile("(.*?)(\r?\n|$)");
        Matcher mText = pText.matcher(text);
        List<String[]> dataList = new ArrayList<String[]>();
        while (mText.find()) {
            String findText = mText.group(1);
            System.out.println(findText);
            if (findText.equals("")) {
                continue;
            }
            dataList.add(findText.split("\t"));
        }
        Pattern pInsertExample = Pattern.compile("^(.*?)(\r?\n|$)");
        Matcher mInsertExample = pInsertExample.matcher(insertExample);
        String sqlTemplate = "";
        while (mInsertExample.find()) {
            String findInsertExample = mInsertExample.group(1);
            String[] split = findInsertExample.split(" VALUES ");
            String valuesTemplate = split[1].replaceAll("\\d+", "?");
            valuesTemplate = valuesTemplate.replaceAll("'.*?'", "'?'");
            sqlTemplate = split[0] + " VALUES " + valuesTemplate;
        }
        System.out.println(sqlTemplate);
        List<String> sqlList = new ArrayList<>();
        for (String[] strings : dataList) {
            Pattern pPlaceholder = Pattern.compile("\\?");
            int i = 0;
            String sql = sqlTemplate;
            Matcher mPlaceholder = pPlaceholder.matcher(sql);
            while (mPlaceholder.find()) {
                String find = mPlaceholder.group(0);
                String replace = strings[i];
                if (StringUtils.equals("(null)", replace)) {
                    replace = "null";
                }
                sql = StringUtils.replaceOnce(sql, find, replace);
                i++;
            }
            sql = StringUtils.replace(sql, "'null'", "null");
            sqlList.add(sql);
        }

        return StringUtils.join(sqlList, "\r\n");
    }
}
