package com.algorithm.array;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class PcTest {

    public static final Lock lock = new ReentrantLock();
    public static final Condition condition = lock.newCondition();
    public static boolean flag = false;

    public static void main(String[] args) {
        Thread producer = new Thread(() -> {
            lock.lock();
            try {
                System.out.println("procuded");
                flag = true;
                System.out.println("p finished");
                condition.signalAll();
            } finally {
                lock.unlock();
            }
        });

        Thread consumer = new Thread(() -> {
            lock.lock();
            try {
                System.out.println("consumed");
                while (!flag) {
                    condition.await();
                }
                System.out.println("c finished");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        });
        producer.start();
        consumer.start();
    }
}
