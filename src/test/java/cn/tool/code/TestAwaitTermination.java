package cn.tool.code;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @author jiazhen
 * @ClassName TestXXX
 * @description: TODO
 * @email jarzhen@163.com
 * @datetime 2024年 07月 15日 16:01
 * @version: 1.0
 */
public class TestAwaitTermination {
    public static ExecutorService executorService = Executors.newFixedThreadPool(10);
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            executorService.submit(() -> {
                System.out.println(Thread.currentThread().getName());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

        }
        System.out.println("开始等待");
        boolean b = executorService.awaitTermination(3, TimeUnit.SECONDS);
        System.out.println("等待完毕");
        if (b) {
            System.out.println("分线程已经结束");
        }
        System.out.println(Thread.currentThread().getName());
    }
}
