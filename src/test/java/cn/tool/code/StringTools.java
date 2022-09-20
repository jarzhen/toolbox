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
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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

    public static String fastFormat(JSONObject jsonObject){
        return JSON.toJSONString(jsonObject, SerializerFeature.PrettyFormat,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteDateUseDateFormat);
    }
}