package com.bsuir.poit.alarmclock.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bsuir.poit.alarmclock.R;
import com.bsuir.poit.alarmclock.model.AlarmTimeIntent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AlarmDetailActivity extends AppCompatActivity {
    private AlarmTimeIntent alarmTimeIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_detail);
        initSpinnerSounds();
        initIntent();
    }

    private Spinner getSpinner(){
        return (Spinner)findViewById(R.id.spinnerSound);
    }

    private TimePicker getTimePicker(){
        return (TimePicker)findViewById(R.id.timePickerAlarm);
    }

    private void initSpinnerSounds() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, soundNames);
        Spinner spinner = getSpinner();
        spinner.setAdapter(adapter);
        spinner.setPrompt("Sounds");
    }

    private void initIntent() {
        alarmTimeIntent = (AlarmTimeIntent) getIntent().getSerializableExtra(AlarmTimeActivity.ALARM_INTENT);
        if (alarmTimeIntent != null
                && alarmTimeIntent.getIntentType() == AlarmTimeIntent.IntentType.UPDATE) {
            setSelectedSound(alarmTimeIntent.getSoundName());
            setTime(alarmTimeIntent.getTime());
        }else{
            setDefault();
        }
    }

    private void setSelectedSound(String soundName) {
        Spinner spinner = getSpinner();
        ArrayAdapter<String> adapter = (ArrayAdapter<String>)getSpinner().getAdapter();
        spinner.setSelection(adapter.getPosition(soundName));
    }

    private void setTime(Date time) {
        Calendar c = Calendar.getInstance();
        c.setTime(time);
        updatePicker(c);
    }

    private void setDefault() {
        updatePicker(Calendar.getInstance());
    }

    private void updatePicker(Calendar c){
        TimePicker timePicker = getTimePicker();
        timePicker.setIs24HourView(true);
        timePicker.setHour(c.get(Calendar.HOUR_OF_DAY));
        timePicker.setMinute(c.get(Calendar.MINUTE));
    }

    public void save_click(View view){
        Intent intent = new Intent(this, AlarmTimeActivity.class);
        updateTimeIntent();
        intent.putExtra(AlarmTimeActivity.ALARM_INTENT, alarmTimeIntent);
        startActivity(intent);
    }

    private void updateTimeIntent() {
        Spinner spinner = getSpinner();
        alarmTimeIntent.setSoundName(spinner.getSelectedItem().toString());
        TimePicker timePicker = getTimePicker();
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
        c.set(Calendar.MINUTE, timePicker.getMinute());
        alarmTimeIntent.setTime(c.getTime());
    }

    public void cancel_click(View view){
        Toast.makeText(getApplicationContext(), "Cancel changes", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, AlarmTimeActivity.class);
        startActivity(intent);
    }

    private static List<String> soundNames;

    static {
        soundNames = new ArrayList<>();
        Field[] fields = R.raw.class.getFields();
        for (Field field : fields) {
            String name = field.getName();
            if (name.contains("song")) {
                soundNames.add(name);
            }
        }
    }
}
