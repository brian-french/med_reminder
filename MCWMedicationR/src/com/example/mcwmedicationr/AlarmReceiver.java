package com.example.mcwmedicationr;


import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.text.format.Time;
import edu.cmu.ices.EMA.Constants;
import edu.cmu.ices.EMA.service.logging.Log;

public class AlarmReceiver extends BroadcastReceiver {
	
	public static PowerManager.WakeLock br_wl = null;

	@Override
	public void onReceive(Context context, Intent intent) {
		
		PowerManager pw = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		br_wl = pw.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "AlarmReceiver");
		br_wl.acquire();
		
		//Need to check that the alarm time makes sense since we sometimes get phantom alarms
		long now = System.currentTimeMillis();
		AppPreferences prefs = new AppPreferences(context);
		SharedPreferences shared_prefs = context.getSharedPreferences(Constants.EMA_PREFS, 0);
//		Scheduler sched = new Scheduler(context);
		SmartScheduler smart = new SmartScheduler(context);
		String action = intent.getAction();
		if (action.equalsIgnoreCase(Constants.ALARM_ACTIVITY_BOD)) {
			smart.BODAlarmFired();
		}
		if (action.equalsIgnoreCase(Constants.ALARM_ACTIVITY_EOD)) {
			smart.EODAlarmFired();
			
			// need to figure out if we actually want to fire this and reschule and return if not
			Time today = new Time();
			today.setToNow();
			boolean didBODThisWeek = smart.getDidBODThisWeek(); //shared_prefs.getBoolean("DID_BOD_THIS_WEEK", false);
			boolean didAbort = smart.getAborted();
			didBODThisWeek &= !didAbort;
			System.out.println("Did BOD: " + didBODThisWeek);
			if (today.weekDay == 6) {
				// last day of the week, clear the flag and prompt regardless 
//				Editor edit = shared_prefs.edit();
//				edit.putBoolean("DID_BOD_THIS_WEEK", false);
//				edit.commit();
				smart.setDidBODThisWeek(false);
				smart.setAborted(false);
				didBODThisWeek = false;
			}
			
			// reschedule for tomorrow, it may not prompt but needs rescheduling every time it fi
			smart.scheduleEODReminder(true);
			
			if (didBODThisWeek) {
				// don't want to prompt user today, reschedule for tomorrow and return
				clearWakeLock();
				return;
			}
		}
		if (action.equalsIgnoreCase(Constants.ALARM_ACTIVITY_RANDOM)) {
			// this is a reminder alarm, reschedule for the next hour preemptively
			Time time = new Time();
			time.setToNow();
			prefs.setHourlyReminder(time.hour);
			int snoozeCount = intent.getIntExtra("SNOOZE_CNT", -1);
			if (snoozeCount == -1) {
				// only increment the count for the first time the alarm fires, not when snoozed
				smart.incrementIntervalCount();
			}
			int cnt = smart.getIntervalCount();
			//smart.rescheduleHourlyReminder();
			intent.putExtra(Constants.ALARM_TIMEOUT, 5L * Constants.MINUTE_MILLIS); // hack change the reminder alarm to 5 minutes max
			intent.putExtra(Constants.ALARM_MESSAGE, "Start scheduled interview #" + cnt + "?");
		}
		if (action.equalsIgnoreCase(Constants.ALARM_ACTIVITY_BLOOD) || action.equalsIgnoreCase(Constants.ALARM_ACTIVITY_BLOOD_WARNING)) {
			System.out.println("Received blood reminder alarm");
			if (!smart.getMode().equalsIgnoreCase(Constants.MODE_MONITORING)) {
				// its not actually a monitoring day, so reschedule for tomorrow and return without firing the alarm
				smart.scheduleBloodReminder(Constants.BLOOD_HOUR, Constants.BLOOD_MINUTE, true);
				clearWakeLock();
				System.out.println("Rescheduled blood alarm since today is not a monitoring day");
				return;
			}
		}
		if (action.equalsIgnoreCase(Constants.ALARM_ACTIVITY_FORCE_BOD)) {
			// force the bod icon to the screen, hopefully this will reduce the occurence of the icon switching bug
			smart.BODAlarmFired();
			smart.scheduleBODIconForce(false);
			clearWakeLock();
			return;
		}
		if (action.equalsIgnoreCase(Constants.ALARM_ACTIVITY_FORCE_EOD)) {
			// force the eod icon to the screen, hopefully this will reduce the occurence of the icon switching bug
			smart.EODAlarmFired();
			smart.scheduleEODIconForce(false);
			clearWakeLock();
			return;
		}
		
		// HACK FOR MCW INCREASED THE TOTAL ALARM TIME TO 25 MINUTES WITH 8 MINUTES OF SILENCE BETWEEN 30 SECOND PROMPTS 
		intent.putExtra(Constants.ALARM_TIMEOUT, 25L * Constants.MINUTE_MILLIS);

//		String alarmClass = intent.getStringExtra(Constants.ALARM_CLASS).toLowerCase();
//		long scheduleTime = 0;
//		if (alarmClass.contains("random")) {
//			scheduleTime = sched.convertTimeToMilliseconds(prefs.getLastRandomTime());
//			System.out.println("ALARM RECEIVER RANDOM INTERVIEW!!!!!! time diff = " + (System.currentTimeMillis() - scheduleTime) +" scheduleTime = " + scheduleTime + " currentTime = " + System.currentTimeMillis() );
//		} else if (alarmClass.contains("bod")) {
//			int bodHour = prefs.getBODAlarmHour();
//			int bodMin = prefs.getBODAlarmMinute();
//			Time bod = new Time();
//			bod.setToNow();
//			bod.set(0, bodMin, bodHour, bod.monthDay, bod.month, bod.year);
//			scheduleTime = bod.toMillis(false);
//			
//		} else if (alarmClass.contains("eod")) {
//			int eodHour = prefs.getEODAlarmHour();
//			int eodMin = prefs.getEODAlarmMinute();
//			Time eod = new Time();
//			eod.setToNow();
//			eod.set(0, eodMin, eodHour, eod.monthDay, eod.month, eod.year);
//			scheduleTime = eod.toMillis(false);
//		}
		// compare scheduletime to current time, if different is too big and hasn't been delayed
//		if (intent.getIntExtra(Constants.ALARM_TELEPHONY_DELAYS, 0) == 0) {
//			long timeDiff = Math.abs(scheduleTime - now);
//			if (timeDiff > 15L * Constants.MINUTE_MILLIS) {
//				// the timing of this alarm is way off front the scheduled, can kill this alarm
//				// TODO should I reschedule??????
//				return;
//			}
//		}
		
		
		//Need to check that the alarm time makes sense since we sometimes get phantom alarms
		long time = System.currentTimeMillis();
//		AppPreferences prefs = new AppPreferences(context);
//		Scheduler sched = new Scheduler(context);
		AlarmScheduler alarmSched = new AlarmScheduler(context);
		String alarmClass = intent.getStringExtra(Constants.ALARM_CLASS);//
		if (alarmClass == null) alarmClass = "";
		alarmClass = alarmClass.toLowerCase();
		long scheduleTime = 0;
		
		Log.w("ALARM RECEIVER", "alarm fired: " + time);
		
		if (alarmClass.contains("random")) {
			// cancel any other random prompts that have been scheduled
			// hopefully this will prevent multiple triggerings as experienced by one person
			alarmSched.cancelAlarm(intent.getAction(), intent.getIntExtra(Constants.ALARM_RQ_CODE, 0));
			
//			scheduleTime = sched.convertTimeToMilliseconds(prefs.getLastRandomTime());
//			Log.w("ALARM RECEIVER", "RANDOM INTERVIEW!!!!!! time diff = " + (System.currentTimeMillis() - scheduleTime) + " currentTime = " + System.currentTimeMillis() +" scheduleTime = " + scheduleTime);
//			
//			// check the timing of the random against current time and make sure that this alarm makes sense to happen now
//			if (scheduleTime > time) {
//				// the scheduled time is later than the current time, this is obviously an alarm that fired too early,
//				// just reschedule it
//				Log.e("ALARM RECEIVER", "Random fired early: current = " + time + " scheduled = " + scheduleTime);
//				sched.rescheduleRandomAfterSuspendAccumulation();
//				return;
//			}
//			
//			// check that the random prompt is firing between the bod and eod time
//			if (sched.duringSuspensionTime(time)) {
//				Log.e("ALARM RECEIVER", "Current time = " + time + " during suspend period!!");
//				// this current random prompt went off during the suspend period of the participant
//				// need to adjust the suspend accumulator and reschedule the alarm
//				if (sched.duringSuspensionTime(scheduleTime)) {
//					//adjust the suspend accumulator
//					
//					Log.e("ALARM RECEIVER", "Scheduled time = " + scheduleTime + " during suspend period");
//					
//					long suspendAcc = 0;
//					int bodHour = prefs.getBODAlarmHour();
//					int bodMin = prefs.getBODAlarmMinute();
//					Time bod = new Time();
//					bod.set(scheduleTime);
//					bod.set(0, bodMin, bodHour, bod.monthDay, bod.month, bod.year);
//					if (scheduleTime < bod.toMillis(false)) {
//						suspendAcc = (bod.toMillis(false) - scheduleTime) + AlarmManager.INTERVAL_HALF_HOUR;
//						Log.e("ALARM RECEIVER", "Before beginning of day, accumulating: " + suspendAcc);
//					}
//					
//					int eodHour = prefs.getEODAlarmHour();
//					int eodMin = prefs.getEODAlarmMinute();
//					Time eod = new Time();
//					eod.set(scheduleTime);
//					eod.set(0, eodMin, eodHour, eod.monthDay, eod.month, eod.year);
//					if (scheduleTime > eod.toMillis(false)) {
//						suspendAcc = sched.computeSuspendDuration() - (scheduleTime - eod.toMillis(false)) + AlarmManager.INTERVAL_HALF_HOUR;
//						Log.e("ALARM RECEIVER", "After end of day, accumulating: " + suspendAcc);
//					}
//					
//					prefs.incrementSuspendAccumulator(suspendAcc);
//					
//					// reschedule the random
//					sched.rescheduleRandomAfterSuspendAccumulation();
//				}
//				
//				return;
//			}
			
		} else if (alarmClass.contains("bod")) {
			
			Time bod = smart.getBODTime();
			Time t = new Time();
			t.setToNow();
			
			if ((t.toMillis(false) - bod.toMillis(false)) < 12L * Constants.HOUR_MILLIS) {
				// bod fired within 12 hours of the last bod completion, this must be an error
				return;
			}
			
//			int bodHour = prefs.getBODAlarmHour();
//			int bodMin = prefs.getBODAlarmMinute();
//			Time bod = new Time();
//			bod.setToNow();
//			bod.set(0, bodMin, bodHour, bod.monthDay, bod.month, bod.year);
//			scheduleTime = bod.toMillis(false);
//			
//			Log.w("ALARM RECEIVER", "BOD Interview: " + System.currentTimeMillis());
//			
//			// check if there is a random schedule reset pending
//			if (prefs.getResetFlag()) {
//				prefs.setResetFlag(false);
//				Log.w("ALARM RECEIVER", "resetting random scheduler");
//				
//				// clear the schedule state parameters
//				prefs.setStartTime(System.currentTimeMillis()); // start time is reset to now
//				prefs.incrementSuspendAccumulator(0L - prefs.getSuspendAccumulator()); // reset the suspend accumulator back to zero
//				prefs.clearCount(Constants.PREFS_RANDOM_PROMPT_COUNT); // reset the random prompt count back to zero
//				prefs.clearCount(Constants.PREFS_SAMPLED_EVENT_COUNT); // reset the self initiated counter back to zero
//				prefs.setLastRandomTime(0.0); // reset the last interview time to 0
//				
//				// final schedule the new first random interview, this will also cancel any pending random
//				//sched.rescheduleRandom();
//			}
			
		} else if (alarmClass.contains("eod")) {
			int eodHour = prefs.getEODAlarmHour();
			int eodMin = prefs.getEODAlarmMinute();
			Time eod = new Time();
			eod.setToNow();
			eod.set(0, eodMin, eodHour, eod.monthDay, eod.month, eod.year);
			scheduleTime = eod.toMillis(false);
			
			Log.w("ALARM RECEIVER", "EOD Interview: " + System.currentTimeMillis());
			
			String currDate = getDateString();
//			if (!currDate.equalsIgnoreCase(prefs.getEODSuspendDate())) {
//				prefs.setEODSuspendDate(currDate);
//				
//				// increment the suspend accumulator and reschedule the pending random interview to the next day
//				long suspend = sched.computeSuspendDuration();
//				prefs.incrementSuspendAccumulator(suspend);
//				
//				sched.rescheduleRandomAfterSuspendAccumulation();
//			}
			
		}
		// compare scheduletime to current time, if different is too big and hasn't been delayed
//		if (intent.getIntExtra(Constants.ALARM_TELEPHONY_DELAYS, 0) == 0) {
//			long timeDiff = Math.abs(scheduleTime - now);
//			if (timeDiff > 15L * Constants.MINUTE_MILLIS) {
//				// the timing of this alarm is way off front the scheduled, can kill this alarm
//				// TODO should I reschedule??????
//				return;
//			}
//		}
		
		
		//check for active phone call
		//long time = System.currentTimeMillis();
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		int callstate = tm.getCallState();
		String class_name = intent.getStringExtra(Constants.ALARM_CLASS);
		int rq_code = intent.getIntExtra(Constants.ALARM_RQ_CODE, 0);
		//AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		
		// set the prompt time if it hasn't already been set
        if (intent.getLongExtra(Constants.ALARM_PROMPTTIME, 0L) == 0L) {
        	intent.putExtra(Constants.ALARM_PROMPTTIME, time); // store the prompt time for the interview
        }
		
		if (callstate != TelephonyManager.CALL_STATE_IDLE) {
			// there is some telephone activity (either inbound or outbound) going on, delay this alarm
			// TODO the intent should contain the scheduled alarm time in UTC milliseconds so the prompt time can be set properly in onCreate!!!!!!
			// TODO should keep track of whether there was a delay or not??????? HOW????

			int delays = intent.getIntExtra(Constants.ALARM_TELEPHONY_DELAYS, 0);
			delays ++;
			intent.putExtra(Constants.ALARM_TELEPHONY_DELAYS, delays);
			PendingIntent pi = PendingIntent.getBroadcast(context, Constants.ALARM_DELAYED_RQ_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT); //ALWAYS USE UPDATE CURRENT

			// reschedule the alarm for 5 minutes in the future
			//am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (5 * Constants.MINUTE_MILLIS), pi);
			AlarmScheduler scheduler = new AlarmScheduler(context);
			scheduler.rescheduleIntent(intent, rq_code, time + (5L * Constants.MINUTE_MILLIS));
			Log.w("ALARM RECEIVER", "delaying the intent due to active call: " + class_name);
		} else {
			// no telephone activity, go ahead and launch the activity
			
			//start the interview activity
//			Intent i;
//			//long now = System.currentTimeMillis();
//			if (class_name != null) {
//				try {
//					i = new Intent(context, Class.forName(class_name));
//					i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					// load the intent extras up in case any of them are relevant to the interview (e.g. delay count before prompt?, or the intended prompt time)
//					i.putExtras(intent.getExtras());
//					context.startActivity(i);
//					//PendingIntent pi = PendingIntent.getActivity(context, rq_code, i, 0);
//					//am.set(AlarmManager.RTC_WAKEUP, now, pi); //HACK, have to launch the activity by scheduling it with the alarm manager instead of calling startActivity so that multiple activities can overlap for some reason
//					Log.d("intentReceiver", "starting the intent: " + class_name);
//				} catch (ClassNotFoundException e) {
//					e.printStackTrace();
//					Log.d("intentReceiver", "failed to load class to start the intent: " + class_name);
//				}
//			}
			
			Log.w("ALARM RECEIVER", "starting the alarm activity: " + class_name);
			//start the alarm activity
			Intent alarm = new Intent(context, Alarm.class);
        	alarm.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        	//i.setAction(Constants.ALARM_ALERT);
        	//alarm.putExtra(Constants.ALARM_TYPE, Constants.ALARM_0);
        	//alarm.putExtra(Constants.ALARM_KEY, Constants.ALARM_KEY_BOD);
//        	if (msg != null) {
//        		i.putExtra(Constants.ALARM_MESSAGE, msg);
//        	}
//        	if (timeout > 0) {
//        		i.putExtra(Constants.ALARM_TIMEOUT, timeout);
//        	}
        	alarm.putExtras(intent.getExtras());
        	context.startActivity(alarm);
        	return;
        	//PendingIntent pi = PendingIntent.getActivity(context, Constants.ALARM_ALERT_RQ_CODE, alarm, 0);
        	//am.set(AlarmManager.RTC_WAKEUP, now + 10, pi);
		}
		
		// if we get here we never launched the alarm so release the wakelock
		clearWakeLock();
	}
	
	private void clearWakeLock() {
		
		if (br_wl != null) {
			if (br_wl.isHeld()) {
				br_wl.release();
				br_wl = null;
			}
		}
	}
	
	private static String getDateString() {
		Time now = new Time();
		now.setToNow();
		String nowStr = now.format("%Y/%m/%d");
		return nowStr;
	}

}
