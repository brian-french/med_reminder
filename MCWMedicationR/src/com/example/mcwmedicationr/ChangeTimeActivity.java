package com.example.mcwmedicationr;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TimePicker;

public class ChangeTimeActivity extends Activity {

	AppPreferences prefs;
	AlarmScheduler sched;
	
	TimePicker time1, time2;
	Button save, cancel;
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.change_time_layout);
		
		prefs = new AppPreferences(this);
		sched = new AlarmScheduler(this);
		
		time1 = (TimePicker) findViewById(R.id.timePicker1);
		time2 = (TimePicker) findViewById(R.id.timePicker2);
		save = (Button) findViewById(R.id.timeSave);
		cancel = (Button) findViewById(R.id.timeCancel);
		
		int hour1 = prefs.getTimeValue(Constants.PREFS_HOUR_ONE, 7);
		int minute1 = prefs.getTimeValue(Constants.PREFS_MINUTE_ONE, 0);
		int hour2 = prefs.getTimeValue(Constants.PREFS_HOUR_TWO, 19);
		int minute2 = prefs.getTimeValue(Constants.PREFS_MINUTE_TWO, 0);
		
		time1.setCurrentHour(hour1);
		time1.setCurrentMinute(minute1);
		time2.setCurrentHour(hour2);
		time2.setCurrentMinute(minute2);
		
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
			
		});
		
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				prefs.setTimeValue(Constants.PREFS_HOUR_ONE, time1.getCurrentHour());
				prefs.setTimeValue(Constants.PREFS_MINUTE_ONE, time1.getCurrentMinute());
				prefs.setTimeValue(Constants.PREFS_HOUR_TWO, time2.getCurrentHour());
				prefs.setTimeValue(Constants.PREFS_MINUTE_TWO, time2.getCurrentMinute());
				
				long nextAlarm = prefs.getNextAlarmTime(System.currentTimeMillis());
				Intent i = new Intent(getApplicationContext(), AlarmReceiver.class);
				sched.rescheduleIntent(i, Constants.ALARM_ALERT_RQ_CODE, nextAlarm);
				
				if (getIntent().getBooleanExtra("init", false)) {
					// runnning the initial configuration, launch the ringtone setup next
					Intent ring = new Intent(getApplicationContext(), ChangeAlarmActivity.class);
					startActivity(ring);
				}
				
				finish();
				
			}
			
		});
		
	}

}
