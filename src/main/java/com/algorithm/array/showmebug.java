package com.algorithm.array;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class showmebug {
    private static final Object LOCK = new Object();
    private final Map<LocalDateTime, Transfer> records = new HashMap<>();

    public static class Account {
        private final String name;
        private long balance;

        public Account(String name, long balance) {
            if (balance < 0) {
                throw new IllegalArgumentException("余额不能为负数");
            }
            this.name = name;
            this.balance = balance;
        }

        public boolean decrease(long amount) {
            synchronized (LOCK) {
                if (amount <= 0 || balance < amount) {
                    return false;
                }
                balance -= amount;
                return true;
            }
        }

        public boolean increase(long amount) {
            synchronized (LOCK) {
                if (amount <= 0 || balance > Long.MAX_VALUE - amount) {
                    return false;
                }
                balance += amount;
                return true;
            }
        }

        public long getBalance() {
            synchronized (LOCK) {
                return balance;
            }
        }
    }

    public static class Transfer {
        public final String from;
        public final String to;
        public final long amount;

        public Transfer(String from, String to, long amount) {
            this.from = from;
            this.to = to;
            this.amount = amount;
        }

        @Override
        public String toString() {
            return from + " -> " + to + ": " + amount + "分";
        }
    }

    public boolean transfer(Account from, Account to, long amount) {
        synchronized (LOCK) {
            if (from == null || to == null || from == to || amount <= 0
                    || to.balance > Long.MAX_VALUE - amount) {
                return false;
            }
            if (!from.decrease(amount)) {
                return false;
            }
            to.increase(amount);

            LocalDateTime time = LocalDateTime.now();
            while (records.containsKey(time)) {
                time = LocalDateTime.now();
            }
            records.put(time, new Transfer(from.name, to.name, amount));
            return true;
        }
    }

    public Map<LocalDateTime, Transfer> getRecords() {
        synchronized (LOCK) {
            return new HashMap<>(records);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        showmebug bank = new showmebug();
        Account a = new Account("A", 10000);
        Account b = new Account("B", 10000);

        Thread t1 = new Thread(() -> bank.transfer(a, b, 2000));
        Thread t2 = new Thread(() -> bank.transfer(b, a, 3000));
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("余额不足，转账结果：" + bank.transfer(a, b, 99999));
        System.out.println("A余额：" + a.getBalance() + "分");
        System.out.println("B余额：" + b.getBalance() + "分");
        bank.getRecords().forEach((time, record) -> System.out.println(time + " " + record));
    }
}
