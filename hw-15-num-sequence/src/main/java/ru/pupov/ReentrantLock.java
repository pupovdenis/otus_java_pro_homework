package ru.pupov;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public class ReentrantLock {
    private static final Logger logger = LoggerFactory.getLogger(ReentrantLock.class);

    private final Lock lock = new java.util.concurrent.locks.ReentrantLock();
    private long lastThreadId = Integer.MIN_VALUE;

    public static void main(String[] args) throws InterruptedException {
        new ReentrantLock().go();
    }

    private void go() throws InterruptedException {
        var t1 = new Thread(this::numbersOuter);
        t1.setName("t1");
        t1.start();

        var t2 = new Thread(this::numbersOuter);
        t2.setName("t2");
        t2.start();

        t1.join();
        t2.join();
    }

    private void numbersOuter() {
        boolean isIncrement = true;
        int lastNum = 1;
        while (true) {
            lock.lock();
            try {
                if (lastThreadId != Thread.currentThread().getId()) {
                    isIncrement = lastNum < 10 && (lastNum <= 1 || isIncrement);
                    logger.info(String.valueOf(isIncrement ? lastNum++ : lastNum--));
                    lastThreadId = Thread.currentThread().getId();
                    sleep();
                }
            } finally {
                lock.unlock();
            }
        }
    }

    private static void sleep() {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(1));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
