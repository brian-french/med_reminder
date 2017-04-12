package com.example.mcwmedicationr;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.text.format.Time;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
	
	private static final String TAG = "MED_ALARM_RECEIVER";
	public static PowerManager.WakeLock br_wl = null;

	@Override
	public void onReceive(Context context, Intent intent) {
		
		PowerManager pw = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		br_wl = pw.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "AlarmReceiver");
		br_wl.acquire();
		
		//Need to check that the alarm time makes sense since we sometimes get phantom alarms
		long now = System.currentTimeMillis();
		AppPreferences prefs = new AppPreferences(context);
		
		Log.d(TAG, "launching alarm");
		//start the alarm activity
		Intent alarm = new Intent(context, MedReminderDialog.class);
    	alarm.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

    	alarm.putExtras(intent.getExtras());
    	context.startActivity(alarm);

	}
	
	private static String getDateString() {
		Time now = new Time();
		now.setToNow();
		String nowStr = now.format("%Y/%m/%d");
		return nowStr;
	}

}
