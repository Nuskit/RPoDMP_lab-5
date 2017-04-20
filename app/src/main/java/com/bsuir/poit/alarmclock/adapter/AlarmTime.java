package com.bsuir.poit.alarmclock.adapter;

import java.util.Calendar;
import java.util.Date;

public class AlarmTime {
    private String name;
    private Date time;
    private boolean isPlay;

    public AlarmTime(String name, Date time) {
        assert name != null;
        assert time != null;
        this.name = name;
        this.time = time;
        isPlay = false;
    }

    public boolean equalsTime(Calendar calendarTime){
        Calendar c = Calendar.getInstance();
        c.setTime(time);
        return equalsTimeField(c, calendarTime, Calendar.HOUR_OF_DAY) && equalsTimeField(c, calendarTime, Calendar.MINUTE);
    }

    private static boolean equalsTimeField(Calendar left, Calendar right, int pos){
        return left.get(pos) == right.get(pos);
    }

    public boolean isPlay() {
        return isPlay;
    }

    public void setPlay(boolean play) {
        isPlay = play;
    }

    public String getName() {
        return name;
    }

    public Date getTime() {
        return time;
    }
}
