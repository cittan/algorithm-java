package com.algorithm.array;

/**
 * TurnPrint
 */
public class TurnPrint {
    private static Object lock = new Object();
    static int num = 1;
    static final int MAX = 100;
    
    public static void print(boolean isTrue) {
        synchronized(lock) {
            while(num < MAX) {
                while((num % 2 == 1) != isTrue){
                    try {
                        lock.wait();
                    } catch (Exception e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                if(num > MAX) {
                    break;
                }
                System.out.println(num);
                num ++;
                lock.notifyAll();
            }
        }
    }

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> print(true));
        Thread thread2 = new Thread(() -> print(false));
        thread1.start();
        thread2.start();
    }
}
