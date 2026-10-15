package com.algorithm.array;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Completable
 */
public class Completable {

    ThreadPoolExecutor pool = new ThreadPoolExecutor(
        8,
        16,
        10,
        TimeUnit.MINUTES,
        new ArrayBlockingQueue<Runnable>(100),
        new ThreadPoolExecutor.CallerRunsPolicy()
    );
    /**
     * CompletableFuture的处理方法是，首先调用supplyAsync(有返回值)或者runAsync(没有返回值)
     */
    CompletableFuture<String> cf = CompletableFuture.supplyAsync(
        () -> "hello",
        pool
    )
        .thenApply(String::toUpperCase)
        .thenCompose(s ->
            CompletableFuture.supplyAsync(() -> s + "world", pool)
        )
        .exceptionally(e -> {
            e.printStackTrace();
            return "fallback";
        });

    /**
     * 后续处理使用 thenApply, 接受上一步的结果并作为当前函数的输入
     * thenAccept 上一轮的输出作为输入，没有输出
     * thenRun 不关心上一轮输入
     * orTimeOut(int, unit) 超时时间，抛 TimeOutException
     */
    CompletableFuture<Integer> len = CompletableFuture.supplyAsync(
        () -> "hello",
        pool
    )
        .thenApply(String::length)
        .orTimeout(10, TimeUnit.SECONDS)
        .exceptionally(e -> {
            e.printStackTrace();
            return 0;
        });

    CompletableFuture<Void> out = CompletableFuture.supplyAsync(
        () -> "hello"
    ).thenAccept(System.out::println);

    CompletableFuture<Void> out2 = CompletableFuture.runAsync(() ->
        System.out.println("hello")
    ).thenRun(() -> System.out.println("world"));

    /**
     * thenCompose 上一步结果仍然是一个任务，使用 thenCompose可以避免嵌套 CompletableFuture
     * eg.
     *  CompletableFuture<CompletableFuture<User>> nest = CompletableFuture.supplyAsync(() -> "u").thenApply((u) -> queryInUser(u));
     *  CompletableFuture<User> nest = CompletableFuture.supplyAsync(() -> "u").thenCompose((u) -> queryInUser(u));
     */


    public static void main(String[] args) {
        /**
         * thenCombine 在两个CompletableFuture中使用，等两个都完成合并结果
         * thenAcceptBoth 消费两个结果
         */
        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> "A");
        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> "B");
        CompletableFuture<String> res = f1.thenCombine(f2, (a, b) -> a + b);
    
        /**
         * 多个异步任务并行的情况
         * allOf 等待多个任务完成但不返回结果，要自己从各个 future 里取,all.join需要在方法体中，可以在后面接supplyAsync
         * anyOf 等待一个任务完成但不返回结果
         */
        CompletableFuture<Void> all = CompletableFuture.allOf(f1, f2);
        all.join();
        String s1 = f1.join();
        String s2 = f2.join();
    }
}
