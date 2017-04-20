package com.bsuir.poit.alarmclock.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;

import com.bsuir.poit.alarmclock.R;
import com.bsuir.poit.alarmclock.adapter.AlarmClockViewAdapter;
import com.bsuir.poit.alarmclock.adapter.AlarmTime;
import com.bsuir.poit.alarmclock.model.AlarmTimeIntent;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

public class AlarmTimeActivity extends AppCompatActivity {
    public static final String ALARM_INTENT = "alarm_intent";

    private static final CopyOnWriteArrayList<AlarmTime> alarmTimes;
    private CopyOnWriteArrayList<Pair<AlarmTime, MediaPlayer>> players;
    private Timer timer;

    static {
        alarmTimes = new CopyOnWriteArrayList<>();
    }

    private void initTimer() {
        timer = new Timer();
        players = new CopyOnWriteArrayList<>();
        timer.schedule(new TimerTask(){

            @Override
            public void run() {
                Calendar currentTime = Calendar.getInstance();
                for (AlarmTime alarm: alarmTimes){
                    if (!alarm.isPlay() && alarm.equalsTime(currentTime)){
                        alarm.setPlay(true);
                        players.add(new Pair<>(alarm, createPlayer(alarm)));
                    }
                }
            }

            private MediaPlayer createPlayer(final AlarmTime alarm) {
                final MediaPlayer player = MediaPlayer.create(getApplicationContext(),
                        getIdentifier(alarm.getName()));
                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        stopPlay(new Pair<>(alarm, player));
                    }
                });
                player.start();
                return player;
            }
        }, 0L, 5L * 1000);
    }

    private int getIdentifier(String name) {
        return getResources().getIdentifier(name, "raw", getPackageName());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_time);

        initBundle();
        initAdapter();
        initTimer();
    }

    private void initBundle() {
        AlarmTimeIntent alarmTimeIntent = (AlarmTimeIntent)getIntent().getSerializableExtra(ALARM_INTENT);
        if (alarmTimeIntent != null) {
            AlarmTime alarmTime = getAlarmTime(alarmTimeIntent);
            if (alarmTimeIntent.getIntentType() == AlarmTimeIntent.IntentType.ADD) {
                alarmTimes.add(alarmTime);
            } else {
                alarmTimes.set(alarmTimeIntent.getPositionAlarm(), alarmTime);
            }
        }
    }

    private AlarmTime getAlarmTime(AlarmTimeIntent alarmTimeIntent) {
        return new AlarmTime(alarmTimeIntent.getSoundName(), alarmTimeIntent.getTime());
    }

    private void initAdapter() {
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.alarm_recycler_view);
        AlarmClockViewAdapter adapter = new AlarmClockViewAdapter(alarmTimes);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void add_alarm_click(View view){
        Intent intent = new Intent(this, AlarmDetailActivity.class);
        intent.putExtra(ALARM_INTENT, new AlarmTimeIntent(AlarmTimeIntent.IntentType.ADD));
        startActivity(intent);
        finish();
    }

    private void stopPlay(Pair<AlarmTime,MediaPlayer> alarmPlayer){
        alarmPlayer.second.stop();
        alarmPlayer.first.setPlay(false);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        timer.cancel();
        for (Pair<AlarmTime,MediaPlayer> alarmPlayer : players){
            stopPlay(alarmPlayer);
        }
    }
}
