package com.bsuir.poit.alarmclock.model;

import com.bsuir.poit.alarmclock.adapter.AlarmTime;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class AlarmTimeIntent implements Serializable{
    public enum IntentType{
        ADD, UPDATE
    }

    private String soundName;
    private Date time;
    private int positionAlarm;
    private IntentType intentType;

    public String getSoundName() {
        return soundName;
    }

    public void setSoundName(String soundName) {
        this.soundName = soundName;
    }

    public AlarmTimeIntent(IntentType intentType) {
        this.intentType = intentType;
    }

    public AlarmTimeIntent(IntentType intentType, int positionAlarm, AlarmTime alarmTime) {
        this.positionAlarm = positionAlarm;
        this.intentType = intentType;
        this.soundName = alarmTime.getName();
        this.time = alarmTime.getTime();
    }

    public IntentType getIntentType() {
        return intentType;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getPositionAlarm() {
        return positionAlarm;
    }
}
