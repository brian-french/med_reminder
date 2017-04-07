package edu.cmu.ices.EMA.service;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.format.Time;
import edu.cmu.ices.EMA.Constants;
import edu.cmu.ices.EMA.service.logging.AbstractLogger;
import edu.cmu.ices.EMA.service.logging.RebootLogger;

public class BootReceiver extends BroadcastReceiver {

	SharedPreferences pref;
	AppPreferences prefs;
//	Scheduler sched;
	//AlarmScheduler sched;
	
	private void logEvent(SmartScheduler smart, Context context) {
		String logFile = "reboot.csv";
		
		String id = smart.getParticipantID();
		
		AbstractLogger log = new RebootLogger();
		log.LogEntry(id+"_"+logFile, "Boot", null, context, true);
		
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// This broadcast receiver runs on boot up, its sole purpose is to make sure alarms are rescheduled after
		// the phone has been rebooted
		
		//sched = new AlarmScheduler(context);
		SmartScheduler smart = new SmartScheduler(context);
		pref = context.getSharedPreferences(Constants.EMA_PREFS, 0);
		
		Time bodTime = smart.getBODTime();
		Time now = new Time();
		now.setToNow();
		if (now.year == bodTime.year && now.month == bodTime.month && now.monthDay == bodTime.monthDay) {
			// the phone was rebooted on a monitoring day, need to reschedule the interval interview
			long nowMills = now.toMillis(false);
			long nextInterval = bodTime.toMillis(false);
			
			while (nextInterval < nowMills) {
				nextInterval += (Constants.MINUTE_MILLIS * 85);
			}
			smart.rescheduleHourlyReminderAtTime(nextInterval);
		}
		// need to reschedule the end of day prompt to ask if the next day is monitoring day
		smart.scheduleEODReminder(false);

		// log the boot event
		logEvent(smart, context);
		
	}

}
