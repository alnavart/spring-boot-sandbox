package com.example.springboot.sandbox.commons;

public class Commons {

    public static ThreadLocal<String> USER;

    static {
    	USER = new ThreadLocal<>();
    }
}