package com.example.mcwmedicationr;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

public class FlareReceiver extends BroadcastReceiver {
	
	public static PowerManager.WakeLock br_wl = null;

	@Override
	public void onReceive(Context context, Intent intent) {
		
		PowerManager pw = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		br_wl = pw.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "FlareReceiver");
		br_wl.acquire();
		

		//start the alarm activity
		Intent alarm = new Intent(context, DailyFlareDialog.class);
    	alarm.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	context.startActivity(alarm);
    	return;
    	//PendingIntent pi = PendingIntent.getActivity(context, Constants.ALARM_ALERT_RQ_CODE, alarm, 0);
    	//am.set(AlarmManager.RTC_WAKEUP, now + 10, pi);

	}

}
