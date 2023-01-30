package ru.otus.homework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NumToNum {
    private static final Logger logger = LoggerFactory.getLogger(NumToNum.class);

    private int currCount = 0;
    private int currTh = 2;
    private int incr = 1;


    private synchronized void action(int threadNum) {
        final int maxCurr = 10;
        final int minCurr = 1;

        while(!Thread.currentThread().isInterrupted()) {
            try {
                while (currTh==threadNum) {
                    this.wait();
                }

                if (threadNum == 1) {
                    if (currCount == minCurr && incr == -1) {incr *= -1;}
                    currCount +=incr;
                }

                if (threadNum == 2 && (currCount==maxCurr)) {incr *= -1;}

                logger.info(String.valueOf(currCount));
                currTh = threadNum;
                sleep();
                notifyAll();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        NumToNum numToNum = new NumToNum();
        new Thread(() -> numToNum.action(1)).start();
        new Thread(() -> numToNum.action(2)).start();
    }

    private static void sleep() {
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
