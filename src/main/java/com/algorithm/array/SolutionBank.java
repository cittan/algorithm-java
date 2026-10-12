package com.algorithm.array;

import java.util.concurrent.atomic.AtomicReference;

/**
 * SolutionBank
 */
public class SolutionBank {

    private final AtomicReference<Double> balance;

    private static class InsuiffientExecption extends RuntimeException {
        public InsuiffientExecption (String message) {
            super(message);
        }
    }
    
    public SolutionBank(double balance) {
        this.balance = new AtomicReference<Double>(balance);
    }

    public void desposit(double amount) {
        if(amount <= 0) {
            throw new InsuiffientExecption("message");
        }
        while(true) {
            double current = balance.get();
            double next = current + amount;
            if(balance.compareAndSet(current, next)) {
                return;
            }
        }
    }

    public void withdraw(double amount) {
        if(amount <= 0){
            throw new InsuiffientExecption("message");
        }
        while(true) {
            double current = balance.get();
            if(current < amount){
                throw new InsuiffientExecption("message");
            }
            double next = current - amount;
            if(balance.compareAndSet(current, next)){
                return;
            }
        }
    }

    public double getBalance() {
        return balance.get();
    }
}