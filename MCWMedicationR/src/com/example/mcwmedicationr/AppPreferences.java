package com.example.mcwmedicationr;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.format.Time;

public class AppPreferences {
	
	private static final Object slock = new Object();
	
	SharedPreferences prefs;
	Editor editor;
	
	public AppPreferences(Context context) {
		prefs = context.getSharedPreferences(Constants.EMA_PREFS, 0);
		editor = prefs.edit();
	}
	
	public void setParticipantID(String id) {
		editor.putString(Constants.PREFS_PARTICIPANT_ID, id);
		editor.commit();
	}
	
	public String getParticipantID() {
		return prefs.getString(Constants.PREFS_PARTICIPANT_ID, null);
	}
	
	public void setStartTime(long startUTC) {
		editor.putLong(Constants.PREFS_START_TIME, startUTC);
		editor.commit();
	}
	
	public long getStartTime() {
		return prefs.getLong(Constants.PREFS_START_TIME, 0);
	}
	
	public void setHourOne(int hour) {
		editor.putInt(Constants.PREFS_HOUR_ONE, hour);
		editor.commit();
	}
	
	public void setMinuteOne(int minute) {
		editor.putInt(Constants.PREFS_MINUTE_ONE, minute);
		editor.commit();
	}
	
	public void setHourTwo(int hour) {
		editor.putInt(Constants.PREFS_HOUR_TWO, hour);
		editor.commit();
	}
	
	public void setMinuteTwo(int minute) {
		editor.putInt(Constants.PREFS_MINUTE_TWO, minute);
		editor.commit();
	}
	
	public long getNextAlarmTime(long current) {
		int hour1 = prefs.getInt(Constants.PREFS_HOUR_ONE, -1);
		int minute1 = prefs.getInt(Constants.PREFS_MINUTE_ONE, -1);
		int hour2 = prefs.getInt(Constants.PREFS_HOUR_TWO, -1);
		int minute2 = prefs.getInt(Constants.PREFS_MINUTE_TWO, -1);
		
		Time now = new Time();
		now.set(current);
		
		Time alarm1 = new Time();
		alarm1.setToNow();
		alarm1.set(0, minute1, hour1, alarm1.monthDay, alarm1.month, alarm1.year);
		
		Time alarm2 = new Time();
		alarm2.setToNow();
		alarm2.set(0, minute2, hour2, alarm2.monthDay, alarm2.month, alarm2.year);
		
		if (Time.compare(now, alarm2) < 0) {
			// before the second alarm time
			return alarm2.toMillis(false);
		}
		
		// after the second alarm of the day so the next one is tomorrow
		return alarm1.toMillis(false) + (Constants.HOUR_MILLIS * 24L);
	}
	
	public void setReboot() {
		editor.putBoolean("reboot", true);
		editor.commit();
	}
	
	public boolean getReboot() {
		boolean reboot = prefs.getBoolean("reboot", false);
		if (reboot) {
			editor.remove("reboot");
			editor.commit();
		}
		return reboot;
	}

	public int getVolumeSetting() {
		return prefs.getInt(Constants.PREFS_VOLUME_SETTING, Constants.PREFS_DEFAULT_VOLUME_SETTING);
	}
	
	public void setVolumeSetting(int vol) {
		editor.putInt(Constants.PREFS_VOLUME_SETTING, vol);
		editor.commit();
	}

	public boolean getVibrateSetting() {
		return prefs.getBoolean(Constants.PREFS_VIBRATE_SETTING, Constants.PREFS_DEFAULT_VIBRATE_SETTING);
	}
	
	public void setVibrateSetting(boolean set) {
		editor.putBoolean(Constants.PREFS_VIBRATE_SETTING, set);
		editor.commit();
	}

	public int getRingtoneSetting() {
		return prefs.getInt(Constants.PREFS_RINGTONE_SETTING, Constants.PREFS_DEFAULT_RINGTONE_SETTING);
	}
	
	public void setRingtonSetting(int ringtone) {
		editor.putInt(Constants.PREFS_RINGTONE_SETTING, ringtone);
		editor.commit();
	}
}
