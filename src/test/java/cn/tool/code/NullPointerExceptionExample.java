package cn.tool.code;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NullPointerExceptionExample {
    private final static Logger log = LoggerFactory.getLogger(NullPointerExceptionExample.class);
    @Test
    public void test1() {
        try {
            method1();
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
    }

    private void method1(){
        String str = null;
        System.out.println(str.length());
    }
}