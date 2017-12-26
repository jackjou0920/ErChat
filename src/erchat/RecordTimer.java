/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erchat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class RecordTimer extends Thread {

    private DateFormat dateFormater = new SimpleDateFormat("HH:mm:ss");
    private boolean isRunning = false;
    private boolean isReset = false;
    private long startTime;
    private String spendTime = "";

    public void run() {
        isRunning = true;
        startTime = System.currentTimeMillis();

        while (isRunning) {
            try {
                Thread.sleep(1000);
                setSpendTime(toTimeString());
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                if (isReset) {
                    setSpendTime("00:00:00");
                    isRunning = false;
                    break;
                }
            }
        }
    }

    // Cancel counting record/play time.
    void cancel() {
        isRunning = false;
    }

    // Reset counting to "00:00:00"
    void reset() {
        isReset = true;
        isRunning = false;
    }

    private String toTimeString() {
        long now = System.currentTimeMillis();
        Date current = new Date(now - startTime);
        dateFormater.setTimeZone(TimeZone.getTimeZone("GMT"));
        String timeCounter = dateFormater.format(current);
        return timeCounter;
    }

    public String getSpendTime() {
        return spendTime;
    }

    public void setSpendTime(String spendTime) {
        this.spendTime = spendTime;
    }
}
