package com.algorithm.array;

/**
 * Singleton
 */
public class Singleton {

    private static final Singleton instance = new Singleton();

    public Singleton() {
        
    }

    public static Singleton getInstance() {
        return instance;
    }
	
}