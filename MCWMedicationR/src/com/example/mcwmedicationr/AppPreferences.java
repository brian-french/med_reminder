package com.example.mcwmedicationr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import edu.cmu.ices.EMA.Constants;
import edu.cmu.ices.EMA.service.logging.Log;

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
	
	public void setStartWeight(double weight) {
		editor.putLong(Constants.PREFS_START_WEIGHT, Double.doubleToLongBits(weight));
		editor.commit();
	}
	
	public double getStartWeight() {
		double startWeight = 0.0;
		long weight = prefs.getLong(Constants.PREFS_START_WEIGHT, 0L);
		if (weight != 0) {
			startWeight = Double.longBitsToDouble(weight);
		}
		return startWeight;
	}
	
	public void setCurrentWeight(double weight) {
		editor.putLong(Constants.PREFS_CURRENT_WEIGHT, Double.doubleToLongBits(weight));
		editor.commit();
	}
	
	public double getCurrentWeight() {
		double currentWeight = 0.0;
		long weight = prefs.getLong(Constants.PREFS_CURRENT_WEIGHT, 0L);
		if (weight != 0) {
			currentWeight = Double.longBitsToDouble(weight);
		}
		return currentWeight;
	}
	
	// append new interview data to the unsent data string, needs to be synchronized in case an interview tries to add
	// new data in the middle of sending the data to the server.
	public void addInterviewData(String data) {
		synchronized(slock) {
			String unsent_data = prefs.getString(Constants.PREFS_UNSENT_DATA, "");
			// if no unsent data exists, set unsent_data to data, otherwise append the new data to the string
			if (unsent_data.equalsIgnoreCase("")) {
				unsent_data = data;
			} else {
				unsent_data += ";;" + data;
			}
			editor.putString(Constants.PREFS_UNSENT_DATA, unsent_data);
			editor.commit();
		}
	}

	// gets the currently unsent data and returns a string array, or null if no unsent data exists
	public String[] getInterviewData() {
		String[] data = null;
		synchronized(slock) {
			String unsent_data = prefs.getString(Constants.PREFS_UNSENT_DATA, "");
			editor.remove(Constants.PREFS_UNSENT_DATA);
			editor.commit();
			// split the unsent data if it exists and return it
			if (!unsent_data.equalsIgnoreCase("")) {
				data = unsent_data.split(";;");
			}
		}
		return data;
	}
	
	public void addAlarmScheduledData(String data) {
		synchronized(slock) {
			String unsent_data = prefs.getString(Constants.PREFS_UNSENT_ALARMS, "");
			if (unsent_data.equalsIgnoreCase("")) {
				unsent_data = data;
			} else {
				unsent_data += ";;" + data;
			}
			editor.putString(Constants.PREFS_UNSENT_ALARMS, unsent_data);
			editor.commit();
		}
	}
	
	public String[] getAlarmScheduledData() {
		String[] data = null;
		synchronized(slock) {
			String unsent_data = prefs.getString(Constants.PREFS_UNSENT_ALARMS, "");
			editor.remove(Constants.PREFS_UNSENT_ALARMS);
			editor.commit();
			// split the unsent data if it exists and return it
			if (!unsent_data.equalsIgnoreCase("")) {
				data = unsent_data.split(";;");
			}
		}
		return data;
	}
	
	public void addAlarmPromptChangeEvent(String data) {
		synchronized(slock) {
			String unsent_data = prefs.getString(Constants.PREFS_UNSENT_ALARM_CHANGES, "");
			if (unsent_data.equalsIgnoreCase("")) {
				unsent_data = data;
			} else {
				unsent_data += ";;" + data;
			}
			editor.putString(Constants.PREFS_UNSENT_ALARM_CHANGES, unsent_data);
			editor.commit();
		}
	}
	
	public String[] getAlarmPromptChangeEvent() {
		String[] data = null;
		synchronized(slock) {
			String unsent_data = prefs.getString(Constants.PREFS_UNSENT_ALARM_CHANGES, "");
			editor.remove(Constants.PREFS_UNSENT_ALARM_CHANGES);
			editor.commit();
			// split the unsent data if it exists and return it
			if (!unsent_data.equalsIgnoreCase("")) {
				data = unsent_data.split(";;");
			}
		}
		return data;
	}
	
	// getter and setter methods for the alarm time preferences
	// these values are only really needed to handle recovery from reboot
	public long getRandomAlarmTime() {
		return prefs.getLong(Constants.PREFS_ALARM_RANDOM, -1);
	}
	
	public void setRandomAlarmTime(long time) {
		editor.putLong(Constants.PREFS_ALARM_RANDOM, time);
		editor.commit();
	}
	
	public int getBODAlarmHour() {
		return prefs.getInt(Constants.PREFS_ALARM_BOD_HOUR, -1);
	}
	
	public void setBODAlarmHour(int time) {
		editor.putInt(Constants.PREFS_ALARM_BOD_HOUR, time);
		editor.commit();
	}
	
	public int getBODAlarmMinute() {
		return prefs.getInt(Constants.PREFS_ALARM_BOD_MINUTE, -1);
	}
	
	public void setBODAlarmMinute(int time) {
		editor.putInt(Constants.PREFS_ALARM_BOD_MINUTE, time);
		editor.commit();
	}
	
	public int getEODAlarmHour() {
		return prefs.getInt(Constants.PREFS_ALARM_EOD_HOUR, -1);
	}
	
	public void setEODAlarmHour(int time) {
		editor.putInt(Constants.PREFS_ALARM_EOD_HOUR, time);
		editor.commit();
	}
	
	public int getEODAlarmMinute() {
		return prefs.getInt(Constants.PREFS_ALARM_EOD_MINUTE, -1);
	}
	
	public void setEODAlarmMinute(int time) {
		editor.putInt(Constants.PREFS_ALARM_EOD_MINUTE, time);
		editor.commit();
	}
	
	public void setLastRandomTime(double time) {
		//String str = Double.toHexString(time);
		//editor.putString(Constants.PREFS_LAST_RANDOM_TIME, str);
		long bitvalue = Double.doubleToLongBits(time);
		editor.putLong(Constants.PREFS_LAST_RANDOM_TIME, bitvalue);
		//editor.putFloat(Constants.PREFS_LAST_RANDOM_TIME, (float) time);
		editor.commit();
	}

	public double getLastRandomTime() {
		//return prefs.getFloat(Constants.PREFS_LAST_RANDOM_TIME, 0);
		//return Double.valueOf(prefs.getString(Constants.PREFS_LAST_RANDOM_TIME, null));
		return Double.longBitsToDouble(prefs.getLong(Constants.PREFS_LAST_RANDOM_TIME, 0));
	}
	
	public void setStartTime(long startUTC) {
		editor.putLong(Constants.PREFS_START_TIME, startUTC);
		editor.commit();
	}
	
	public long getStartTime() {
		return prefs.getLong(Constants.PREFS_START_TIME, 0);
	}
	
	public void incrementSuspendAccumulator(long duration) {
		long accum = prefs.getLong(Constants.PREFS_SUSPEND_ACCUMULATOR, 0);
		Log.w("APP PREFS", "Incrementing suspend: current=" + accum + ", adding: " + duration);
		editor.putLong(Constants.PREFS_SUSPEND_ACCUMULATOR, accum + duration);
		editor.commit();
	}
	
	public long getSuspendAccumulator() {
		return prefs.getLong(Constants.PREFS_SUSPEND_ACCUMULATOR, 0);
	}
	
	public void setSchedulerParameter(String param, double value) {
		editor.putFloat(param, (float)value);
		//editor.putLong(param, Double.doubleToLongBits(value));
		editor.commit();
	}
	
	public double getSchedulerParameter(String param) {
		double value = prefs.getFloat(param, 0);
		//double value = Double.longBitsToDouble(prefs.getLong(param, 0));
		return value;
	}
	
	public void incrementCount(String counter) {
		int value = prefs.getInt(counter, 0);
		editor.putInt(counter, value + 1);
		editor.commit();
	}
	
	public int getCount(String counter) {
		return prefs.getInt(counter, 0);
	}
	
	public void setHourlyReminder(int hour) {
		editor.putInt("HourlyReminderHour", hour);
		editor.commit();
	}
	
	public int getHourlyReminder() {
		int hour = prefs.getInt("HourlyReminderHour", -1);
		return hour;
	}
	
	public void clearHourlyReminder() {
		editor.remove("HourlyReminderHour");
		editor.commit();
	}

	public void clearCount(String counter) {
		editor.putInt(counter, 0);
		editor.commit();
	}

	public int getEODContentVersion() {
		
		String version_string = prefs.getString("EOD_CONTENT_VERSION", null);
		int[] versions;
		
		if (version_string == null) {
			// generate new permutation of the numbers 0 to 6
			versions = new int[7];
			List<Integer> list = new ArrayList<Integer>();
			for (int i=0; i<7; i++) {
				list.add(i);
			}
			Collections.shuffle(list);
			
			for (int i=0; i<7; i++) {
				versions[i] = list.get(i);
			}
			
		} else {
			String[] version_array = version_string.split(",");
			versions = new int[version_array.length];
			for (int i=0; i < version_array.length; i++) {
				versions[i] = Integer.parseInt(version_array[i]);
			}
		}
		
		// convert version numbers based on current weight and base weight of participant
		// so that eod interviews that ask about weight are only used if they have lost
		// at least 1 pound since the start of their participation
		double baseWT = getStartWeight();
		double currWT = getCurrentWeight();
		int X=-1, Y=-1, Z=-1, W=-1;
		if ((baseWT - currWT) >= 1.0) {
			X = 7;
			Y = 2;
			Z = 8;
			W = 3;
		} else {
			X = 2;
			Y = 7;
			Z = 3;
			W = 8;
		}
		for (int i=0; i<versions.length; i++) {
			if (versions[i] == X) {
				versions[i] = Y;
			}
			if (versions[i] == Z) {
				versions[i] = W;
			}
		}
		
		// regenerate the versions string using index 1 to 6
		version_string = "";
		for (int i=1; i < versions.length; i++) {
			version_string += versions[i] + ",";
		}
		// if the string is empty store null
		if (version_string.equalsIgnoreCase("")) {
			version_string = null;
		}
		editor.putString("EOD_CONTENT_VERSION", version_string);
		editor.commit();
		
		System.out.println("CURRENT EOD CONTENT SCHEDULE: " + version_string + " basewt = " + baseWT + " currwt = " + currWT);
		// return the 0 index version
		return versions[0];
	}
	
	public boolean getNewIncentiveLabel() {
		
		String incentive_string = prefs.getString("INCENTIVE_LABEL", null);
		boolean[] incentives;
		
		if (incentive_string == null) {
			// generate new permutation of the numbers 0 to 6
			double prompts_per_day = Math.exp(getSchedulerParameter(Constants.PREFS_ALPHA_1));
			int prompts_per_week = (int) (7 * prompts_per_day);
			int incentives_per_week = 1;
			List<Boolean> list = new ArrayList<Boolean>();
			for (int i=0; i<incentives_per_week; i++) {
				list.add(Boolean.valueOf(true));
			}
			for (int i=0; i<(prompts_per_week - incentives_per_week); i++) {
				list.add(Boolean.valueOf(false));
			}
			Collections.shuffle(list);
			
			int size = list.size();
			incentives = new boolean[size];
			for (int i=0; i<size; i++) {
				incentives[i] = list.get(i).booleanValue();
			}
			
		} else {
			String[] incentive_array = incentive_string.split(",");
			incentives = new boolean[incentive_array.length];
			for (int i=0; i < incentive_array.length; i++) {
				incentives[i] = Boolean.parseBoolean(incentive_array[i]);
			}
		}
		
		// regenerate the versions string using index 1 to 6
		incentive_string = "";
		for (int i=1; i < incentives.length; i++) {
			incentive_string += incentives[i] + ",";
		}
		// if the string is empty store null
		if (incentive_string.equalsIgnoreCase("")) {
			incentive_string = null;
		}
		editor.putString("INCENTIVE_LABEL", incentive_string);
		editor.commit();
		
		System.out.println("INCENTIVE_LABEL: " + incentive_string);
		// return the 0 index version
		return incentives[0];
	}

	public void saveShutdownTime(long time) {
		editor.putLong(Constants.PREFS_SHUTDOWN, time);
		editor.commit();
		
	}
	
	public long getShutdownTime() {
		long time = prefs.getLong(Constants.PREFS_SHUTDOWN, 0);
		editor.remove(Constants.PREFS_SHUTDOWN);
		editor.commit();
		return time;
	}
	
//	public void setRandomSuspendAccumulated(boolean value) {
//		editor.putBoolean(Constants.PREFS_DAILY_SUSPEND_ACCUMULATED, value);
//		editor.commit();
//	}
//
//	public boolean getRandomSuspendAccumulated() {
//		return prefs.getBoolean(Constants.PREFS_DAILY_SUSPEND_ACCUMULATED, false);
//	}

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
	
	public void setRandomIncentive(boolean incentive) {
		editor.putBoolean(Constants.INCENTIVE, incentive);
		editor.commit();
	}
	
	public boolean getRandomIncentive() {
		return prefs.getBoolean(Constants.INCENTIVE, false);
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
	
	public int getRandomCount(String type) {
		int val = prefs.getInt(type, 0);
		editor.remove(type);
		editor.commit();
		return val;
	}

//	public void incrementRandomCount(String type) {
//		int val = prefs.getInt(type, 0);
//		val ++;
//		editor.putInt(type, val);
//		editor.commit();
//	}
	
	public void setShutdownProxyTime(long time) {
		editor.putLong(Constants.PREFS_SHUTDOWN_PROXY, time);
		editor.commit();
	}

	public long getShutdownProxyTime() {
		return prefs.getLong(Constants.PREFS_SHUTDOWN_PROXY, 0);
	}

	public void setResetFlag(boolean value) {
		editor.putBoolean(Constants.PREFS_RESET_RANDOM_SCHEDULE, value);
		editor.commit();
	}
	
	public boolean getResetFlag() {
		return prefs.getBoolean(Constants.PREFS_RESET_RANDOM_SCHEDULE, false);
	}

	public long getNonSuspendDuration() {
		return prefs.getLong(Constants.PREFS_NON_SUSPEND_DURATION, Constants.PREFS_DEFAULT_NON_SUSPEND_DURATION);
	}

	public String getEODSuspendDate() {
		return prefs.getString(Constants.PREFS_EOD_SUSPEND_DATE, "");
	}

	public void setEODSuspendDate(String currDate) {
		editor.putString(Constants.PREFS_EOD_SUSPEND_DATE, currDate);
		editor.commit();
	}

	public int getDelayedPrompt() {
		return prefs.getInt(Constants.PREFS_DELAYED_PROMPT, -1);
	}
	
	public void setDelayedPrompt(int rq_code) {
		editor.putInt(Constants.PREFS_DELAYED_PROMPT, rq_code);
		editor.commit();
	}
	
	public void clearDelayedPrompt() {
		editor.remove(Constants.PREFS_DELAYED_PROMPT);
		editor.commit();
	}

	public String getDelayedAction() {
		// TODO Auto-generated method stub
		return prefs.getString(Constants.PREFS_DELAYED_ACTION, "");
	}
	
	public void setDelayedAction(String action) {
		editor.putString(Constants.PREFS_DELAYED_ACTION, action);
		editor.commit();
	}
	
	public void clearDelayedAction() {
		editor.remove(Constants.PREFS_DELAYED_ACTION);
		editor.commit();
	}

	public String getEMAVersion() {
		return prefs.getString(Constants.PREFS_EMA_VERSION, "");
	}

	public void setEMAVersion(String packageVersion) {
		// TODO Auto-generated method stub
		editor.putString(Constants.PREFS_EMA_VERSION, packageVersion);
		editor.commit();
	}

	public void setAssessProbability(double prob) {
		editor.putLong(Constants.PREFS_ASSESS_PROB, Double.doubleToLongBits(prob));
		editor.commit();
	}
	
	public double getAssessProbability() {
		return Double.longBitsToDouble(prefs.getLong(Constants.PREFS_ASSESS_PROB, Double.doubleToLongBits(1.0)));
	}

}
