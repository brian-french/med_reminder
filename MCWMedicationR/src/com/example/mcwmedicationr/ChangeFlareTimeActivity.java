package com.example.mcwmedicationr;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TimePicker;

public class ChangeFlareTimeActivity extends Activity {

	AppPreferences prefs;
	AlarmScheduler sched;
	
	TimePicker time1;
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
		
		setContentView(R.layout.change_flare_time_layout);
		
		prefs = new AppPreferences(this);
		sched = new AlarmScheduler(this);
		
		time1 = (TimePicker) findViewById(R.id.timePicker1);
		save = (Button) findViewById(R.id.timeSave);
		cancel = (Button) findViewById(R.id.timeCancel);
		
		int hour1 = prefs.getTimeValue(Constants.PREFS_FLARE_HOUR, 21);
		int minute1 = prefs.getTimeValue(Constants.PREFS_FLARE_MINUTE, 0);
		
		time1.setCurrentHour(hour1);
		time1.setCurrentMinute(minute1);
		
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
			
		});
		
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				prefs.setTimeValue(Constants.PREFS_FLARE_HOUR, time1.getCurrentHour());
				prefs.setTimeValue(Constants.PREFS_FLARE_MINUTE, time1.getCurrentMinute());
				
				long nextAlarm = prefs.getNextFlareAlarmTime();
				Intent i = new Intent(getApplicationContext(), FlareReceiver.class);
				sched.rescheduleIntent(i, Constants.ALARM_30MIN_RQ_CODE, nextAlarm);
				
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
