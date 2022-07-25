package com.github.kejiwomenzou.concurrency.thread;

/**
 * 启动两个线程, 一个输出 1,3,5,7…99, 另一个输出 2,4,6,8…100 最后 STDOUT 中按序输出 1,2,3,4,5…100
 */
public class PrintInTurn {

    private volatile int num = 1;

    public synchronized void printOdd() {
        while (true) {
            if (num > 100) {
                break;
            }
            if (num % 2 == 0) {
                System.out.println("线程" + Thread.currentThread().getName() + ": " + num);
                num++;
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                notifyAll();
            } else {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public synchronized void printEven() {
        while (true) {
            if (num > 100) {
                break;
            }
            if (num % 2 != 0) {
                System.out.println("线程" + Thread.currentThread().getName() + ": " + num);
                num++;
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                notifyAll();
            } else {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void main(String[] args) {
        final PrintInTurn print = new PrintInTurn();
        new Thread(print::printEven, "ThreaA").start();
        new Thread(print::printOdd, "ThreaB").start();
    }
}
