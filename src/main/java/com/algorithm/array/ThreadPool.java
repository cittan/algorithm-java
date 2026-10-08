package com.algorithm.array;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class ThreadPool {

    public static void main(String[] args) {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(
            4,
            8,
            10,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(10),
            new ThreadPoolExecutor.CallerRunsPolicy()
        );

        for (int i = 0; i < 4; i++) {
            final int id = i;
            Future<Integer> future = pool.submit(() -> {
                System.out.println("提交任务" + id);
                Thread.sleep(1000);
                return 42;
            });
        }
        pool.shutdown();
    }
}
