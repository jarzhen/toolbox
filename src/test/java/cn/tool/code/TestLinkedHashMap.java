package cn.tool.code;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.Test;

import java.util.*;

public class TestLinkedHashMap {
    @Test
    public void test1() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("1", "1");
        map.put("3", "3");
        map.put("4", "4");
        map.put("2", "2");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
        System.out.println("-------------");
        map.put("4", "5");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
    }

    @Test
    public void test2() {
        //自定义比较器，降序排列
        Comparator<Integer> cmp = new Comparator<Integer>() {
            public int compare(Integer e1, Integer e2) {
                return e2 - e1;  //降序
            }
        };
        //不用比较器，默认升序排列
        Queue<Integer> q = new PriorityQueue<>();
        q.add(8);
        q.add(5);
        q.add(13);
        q.add(2);
        q.add(2);
        System.out.println("**********不使用比较器********************");
        while (!q.isEmpty()) {
            System.out.print(q.poll() + " ");
        }
        System.out.println();
        System.out.println("**********使用比较器********************");
        //使用自定义比较器，降序排列
        Queue qq = new PriorityQueue(cmp);
        qq.add(8);
        qq.add(5);
        qq.add(13);
        qq.add(2);
        qq.add(2);
        while (!qq.isEmpty()) {
            System.out.print(qq.poll() + " ");
        }
    }

    @Test
    public void test3() {
        ArrayList<String> list = new ArrayList<String>(5);
//        list.add(1,"b");
//        list.add(2,"c");
//        list.add(3,"d");
//        list.add(4,"e");
//        list.add(5,"f");

        list.add("b");
        list.add("c");
        list.add("d");
        list.add("e");
        list.add("f");
        list.add("a");
        for (String s : list) {
            System.out.println(s);
        }
        System.out.println("-------------");
        list.remove("a");
        list.add(0,"a");
        for (String s : list) {
            System.out.println(s);
        }
    }

    @Test
    public void test4() {
        Map<Entity,String> map = new LinkedHashMap<>();
        Entity e1 = new Entity();
        e1.setName("zs");
        e1.setSex("nan");
        e1.setInfo("11");
        map.put(e1,"1");
        Entity e2 = new Entity();
        e2.setName("zs");
        e2.setSex("nan");
        e1.setInfo("22");
        map.put(e1,"2");

        System.out.println(JSON.toJSONString(map));
    }
}


class Entity{
    private String name;
    private String sex;

    private String info;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Entity entity = (Entity) o;

        return new EqualsBuilder().append(name, entity.name).append(sex, entity.sex).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(name).append(sex).toHashCode();
    }

    @Override
    public String toString() {
        return "Entity{" +
                "name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", info='" + info + '\'' +
                '}';
    }
}