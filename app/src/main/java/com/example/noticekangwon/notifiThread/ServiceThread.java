package com.example.noticekangwon.notifiThread;

import java.util.Random;

public class ServiceThread extends Thread {
    private boolean isRun = true;
    private Random rand = new Random();
    private int randomTime = 5;
    private int basicTime = 5;
    private int sleepTime = basicTime * 1000;

    public void stopForever() {
        synchronized (this) {
            this.isRun = false;
        }
    }

    public void run() {
        while (isRun) {
            // fetchData(0)
            sleepTime = (basicTime + rand.nextInt(randomTime)) * 1000;
            System.out.println("Waiting " + sleepTime / 1000 + " sec");
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
