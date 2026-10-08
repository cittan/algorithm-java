package com.algorithm.array;

/**
 * Singleton4
 */
public class Singleton4 {

    public Singleton4() {
        
    }
    
    public static class Singleton {
        public static final Singleton4 INSTANCE = new Singleton4();
    }

    public Singleton4 getInstance() {
        return Singleton.INSTANCE;
    }
    
}