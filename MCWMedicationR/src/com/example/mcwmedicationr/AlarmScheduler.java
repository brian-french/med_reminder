package com.example.mcwmedicationr;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmScheduler {
	
	AlarmManager am;
	Context context;
	
	public AlarmScheduler(Context context) {
		this.context = context;
		am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	}

	public void cancelAlarm(String action, int requestCode) {
		Log.d("ALARM SCHEDULER", "Cancelling alarm: " + action + ", " + requestCode);
		Intent i = new Intent();
		i.setAction(action);
		PendingIntent pi = PendingIntent.getBroadcast(context, requestCode, i, 0);
		am.cancel(pi);
	}
	
	public void rescheduleIntent(Intent i, int requestCode, long alarmTime) {

		Log.d("ALARM SCHEDULER", "Rescheduling intent: action=" + i.getAction() + ", RQcode=" + requestCode + ", time=" + alarmTime);
		PendingIntent pi = PendingIntent.getBroadcast(context, requestCode, i, PendingIntent.FLAG_UPDATE_CURRENT);
		am.cancel(pi);
		am.set(AlarmManager.RTC_WAKEUP, alarmTime, pi);
	}
	
	public void scheduleOneShotAlarm(String action, String classname, String alarmMsg, int requestCode, long alarmTime, boolean snoozeable, boolean incentive) {

		Intent i = new Intent();
		Log.d("ALARM SCHEDULER", "scheduleing one shot: action=" + action + ", class=" + classname + ", alarmMsg=" + alarmMsg + ", RQcode=" + requestCode +", time=" + alarmTime +", snooze=" + snoozeable + ", incentive=" + incentive);
		i.setAction(action);
		i.putExtra(Constants.ALARM_ACTION, action);
		i.putExtra(Constants.ALARM_CLASS, classname);
		i.putExtra(Constants.ALARM_TYPE, Constants.ALARM_0);
		i.putExtra(Constants.ALARM_MESSAGE, alarmMsg);
		i.putExtra(Constants.ALARM_TIMEOUT, (5L * Constants.MINUTE_MILLIS));
		i.putExtra(Constants.ALARM_CAN_SNOOZE, snoozeable);
		i.putExtra(Constants.INTERVIEW_RANDOM_INCENTIVE, incentive);
		i.putExtra(Constants.ALARM_RQ_CODE, requestCode);
		i.putExtra(Constants.ALARM_INTERVIEW_TIMEOUT, false);
		PendingIntent pi = PendingIntent.getBroadcast(context, requestCode, i, PendingIntent.FLAG_UPDATE_CURRENT);
		am.set(AlarmManager.RTC_WAKEUP, alarmTime, pi);
	}
	
	public void scheduleRepeatingAlarm(String action, String classname, String alarmMsg, int requestCode, long alarmTime, long interval, boolean snoozeable) {
		Log.d("ALARM SCHEDULER", "scheduleing repeating alarm: action=" + action + ", class=" + classname + ", alarmMsg=" + alarmMsg + ", RQcode=" + requestCode +", time=" + alarmTime +", snooze=" + snoozeable);
		Intent i = new Intent();
		i.setAction(action);
		i.putExtra(Constants.ALARM_ACTION, action);
		i.putExtra(Constants.ALARM_CLASS, classname);
		i.putExtra(Constants.ALARM_TYPE, Constants.ALARM_0);
		i.putExtra(Constants.ALARM_MESSAGE, alarmMsg);
		i.putExtra(Constants.ALARM_TIMEOUT, (5L * Constants.MINUTE_MILLIS));
		i.putExtra(Constants.ALARM_CAN_SNOOZE, snoozeable);
		i.putExtra(Constants.ALARM_RQ_CODE, requestCode);
		i.putExtra(Constants.ALARM_INTERVIEW_TIMEOUT, false);
		PendingIntent pi = PendingIntent.getBroadcast(this.context, requestCode, i, PendingIntent.FLAG_UPDATE_CURRENT);
		//AlarmManager am = (AlarmManager) this.context.getSystemService(Context.ALARM_SERVICE);
		am.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, interval, pi);
	}
	
}
