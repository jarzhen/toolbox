package cn.tool.code;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jiazhen
 * @description: 对比两个列表输出json格式对比结果
 * @email jarzhen@163.com
 * @datetime 2O22年 O9月 O3日 9:O5
 * @version: 1.O
 */

public class StringTools {

    public static List<String> readCsv(String filePath) {
        List<String> list = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath))) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);
            for (CSVRecord record : records) {
                list.add(record.get(0));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public String correctPath(String path) {
        if (StringUtils.startsWith(path, "/")) {
            return path.substring(1);
        }
        return path;
    }

    @Test
    public void compareAndPrint() {
        String leftPath = Thread.currentThread().getContextClassLoader().getResource("csv/left.csv").getPath();
        leftPath = correctPath(leftPath);
        String rightPath = Thread.currentThread().getContextClassLoader().getResource("csv/right.csv").getPath();
        rightPath = correctPath(rightPath);
        List<String> left = readCsv(leftPath);
        List<String> right = readCsv(rightPath);
        System.out.println(doublelistSorting(left, right));
    }


    public String doublelistSorting(List<String> left, List<String> right) {
        Set<String> leftSet = new LinkedHashSet<>();
        Set<String> rightSet = new LinkedHashSet<>();
        Set<String> togetherSet = new LinkedHashSet<>();
        for (String s : left) {
            leftSet.add(s);
            togetherSet.add(s);
        }
        for (String s : right) {
            rightSet.add(s);
            togetherSet.add(s);
        }
        List<String> leftHas = new ArrayList<>();
        List<String> togetherHas = new ArrayList<>();
        List<String> rightHas = new ArrayList<>();
        for (String s : togetherSet) {
            if (leftSet.contains(s) && rightSet.contains(s)) {
                togetherHas.add(s);
            } else if (leftSet.contains(s)) {
                leftHas.add(s);
            } else if (rightSet.contains(s)) {
                rightHas.add(s);
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("leftHas", leftHas);
        jsonObject.put("togetherHas", togetherHas);
        jsonObject.put("rightHas", rightHas);
        return fastFormat(jsonObject);
    }

    public static String fastFormat(JSONObject jsonObject) {
        return JSON.toJSONString(jsonObject, SerializerFeature.PrettyFormat,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteDateUseDateFormat);
    }

    @Test
    public void test() {
        for (int i = 0; i < 20; i++) {
            String msgid = "00000000" + StringUtils.repeat("1", i);
            System.out.println(msgid.substring(msgid.length() - 8));
        }
    }

    @Test
    public void test2() {
        Map<String, List<String>> data = new HashMap<>();

        List<String> data1 = new ArrayList<>();
        // 添加数据
        data1.add("value1");
        data1.add("value2");
        data1.add("value3");

        List<String> data2 = new ArrayList<>();
        // 添加数据
        data2.add("value1");
        data2.add("value2");
        // 添加数据
        data.put("key1", data1);
        data.put("key2", data2);
        int sum = data.entrySet().stream().mapToInt(entry -> entry.getValue().size()).sum();
        // 打印数据
        System.out.println(sum);
    }

    @Test
    public void test3() {
        Pattern pattern = Pattern.compile("^(TRUE|FALS)$");
        List<String> list = new ArrayList<>();
        list.add("TRUE");
        list.add("TRUES");
        list.add("ATRUE");
        list.add("ATRUES");
        list.add("FALS");
        list.add("FALSS");
        list.add("AFALS");
        list.add("AFALSS");
        for (String s : list) {
            System.out.println(s.matches("^(TRUE|FALS)$"));
        }
    }

    @Test
    public void test4() {
        String s = "1";
        switch (s) {
            case "0":
                throw new RuntimeException("0");
            case "1":
                throw new RuntimeException("1");
            case "2":
                throw new RuntimeException("2");
        }
    }
}