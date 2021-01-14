package com.example.noticekangwon.notifiThread;

import com.example.noticekangwon.Activity.MainActivity;

import java.util.Random;

public class ServiceThread extends Thread {
    private boolean isRun = true;
    private Random rand = new Random();
    private int randomTime = 10; // min
    private int basicTime = 3 * 1000 * 60 * 60; // 맨 앞이 시간
    private int sleepTime = basicTime;

    public void stopForever() {
        synchronized (this) {
            this.isRun = false;
        }
    }

    public void run() {
        while (isRun) {
            // fetchData(0)
            sleepTime = basicTime + rand.nextInt(randomTime)*1000*60;
            System.out.println("Waiting " + sleepTime / 1000 + " sec");
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
