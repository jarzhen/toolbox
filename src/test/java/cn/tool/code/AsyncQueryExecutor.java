package cn.tool.code;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AsyncQueryExecutor {
    @Test
    public void executeQueries() throws InterruptedException, ExecutionException {
        // 1. 异步执行三次查询
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
            try {
                query1();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {
            try {
                query2();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        CompletableFuture<Void> future3 = CompletableFuture.runAsync(() -> {
            try {
                query3();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        List<CompletableFuture<Void>> futureList = new ArrayList<>();
        futureList.add(future1);
        futureList.add(future2);
        futureList.add(future3);
        CompletableFuture<Void>[] args = futureList.toArray(new CompletableFuture[0]);
        // 2. 合并所有任务并等待完成
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(args);
        allFutures.get(); // 阻塞直到所有任务完成
    }

    // 示例查询方法（替换为实际逻辑）
    private void query1() throws InterruptedException {
        Thread.sleep(2000);
        System.out.println("Query 1 executed");
    }

    private void query2() throws InterruptedException {
        Thread.sleep(4000);
        System.out.println("Query 2 executed");
    }

    private void query3() throws InterruptedException {
        Thread.sleep(3000);
        System.out.println("Query 3 executed");
    }

}