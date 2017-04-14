package com.example.mcwmedicationr;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.format.Time;

public class BootReceiver extends BroadcastReceiver {

	AppPreferences prefs;
	AlarmScheduler sched;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// This broadcast receiver runs on boot up, its sole purpose is to make sure alarms are rescheduled after
		// the phone has been rebooted

		prefs = new AppPreferences(context);
		Intent i = new Intent(context, AlarmReceiver.class);
		
		long nextAlarm = prefs.getNextAlarmTime(System.currentTimeMillis());
		sched.rescheduleIntent(i, Constants.ALARM_ALERT_RQ_CODE, nextAlarm);
		
	}

}
