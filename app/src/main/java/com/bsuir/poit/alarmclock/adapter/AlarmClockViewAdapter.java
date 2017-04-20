package com.bsuir.poit.alarmclock.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bsuir.poit.alarmclock.R;
import com.bsuir.poit.alarmclock.activity.AlarmDetailActivity;
import com.bsuir.poit.alarmclock.activity.AlarmTimeActivity;
import com.bsuir.poit.alarmclock.model.AlarmTimeIntent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AlarmClockViewAdapter extends RecyclerView.Adapter<AlarmClockViewAdapter.ViewHolder>{
    private List<AlarmTime> alarmTimes;

    public AlarmClockViewAdapter(List<AlarmTime> alarmTimes){
        this.alarmTimes = alarmTimes;
    }

    private Context context;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.activity_alarm_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final AlarmTime alarmTime = alarmTimes.get(position);
        holder.name.setText(alarmTime.getName());
        holder.time.setText(getFormatDate(alarmTime.getTime()));
        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AlarmDetailActivity.class);
                intent.putExtra(AlarmTimeActivity.ALARM_INTENT,
                        new AlarmTimeIntent(AlarmTimeIntent.IntentType.UPDATE, position, alarmTime));
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        });
    }

    private String getFormatDate(Date time) {
        return new SimpleDateFormat("HH:mm").format(time);
    }

    @Override
    public int getItemCount() {
        return alarmTimes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView time;

        ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.edit_song_name);
            time = (TextView ) itemView.findViewById(R.id.edit_clock_time);
        }
    }
}
