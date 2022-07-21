package com.github.kejiwomenzou.concurrency;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest {

    private static final Lock LOCK = new ReentrantLock(true);

    private static long TICKET = 50;

    public static void saleTicket() {
        LOCK.lock();
        try {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (TICKET > 0) {
                System.out.println(Thread.currentThread().getName() + "卖出：" + (TICKET--) + "，还剩下：" + TICKET);
            }
        } finally {
            LOCK.unlock();
        }
    }

    public static void main(String[] args) {
        for (char i = 'a'; i < 102; i++) {
            new Thread(() -> {
                for (int j = 0; j < 60; j++) {
                    saleTicket();
                }
            }, String.valueOf(i)).start();
        }
    }
}
