package com.algorithm.array;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Pc {

    public static final Lock lock = new ReentrantLock();
    public static final Condition condition = lock.newCondition();
    public static boolean flag = false;
    
    public static void main(String[] args) {
        Thread producer = new Thread(() -> {
            lock.lock();
            try {
                System.out.println("Producer start");
                flag = true;
                System.out.println("Producer: Production finished. Notifying consumer.");
                condition.signalAll();
            } finally {
                lock.unlock();
            }
        });

        Thread consumer = new Thread(() -> {
            lock.lock();
            try {
                System.out.println("Consumer start");
                while(!flag) {
                    condition.await();
                }
                System.out.println("Consumer: Production finished. Consuming...");
            } catch(InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        });
        consumer.start();
        producer.start();
    }
}
