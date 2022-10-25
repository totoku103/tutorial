package me.totoku103.tutorial.resourceservice.component;

import java.util.concurrent.atomic.AtomicInteger;

public class CustomThreadLocal {
    private static final ThreadLocal<Integer> counter = new ThreadLocal<>();

    public static void increment(int i) {
        counter.set(i);
    }

    public static void remove() {
        counter.remove();
    }

    public static Integer get() {
        return counter.get();
    }

    public static void print() {
        System.out.println(counter.get());
    }

    public static Integer flush() {
        Integer integer = counter.get();
        counter.remove();
        return integer;
    }
}
