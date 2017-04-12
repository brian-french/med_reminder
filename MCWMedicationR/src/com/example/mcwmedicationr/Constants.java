package com.example.mcwmedicationr;

import java.io.File;

import android.app.AlarmManager;
import android.graphics.Color;
import android.os.Environment;
import android.text.format.Time;

public class Constants {
	
	// shared preference constants
	public static final String EMA_PREFS = "edu.cmu.ices.EMA.PREFERENCES";
	public static final String PREFS_ALARM_RANDOM = "edu.cmu.ices.EMA.preferences.RANDOM_INTERVIEW_TIME";
	public static final String PREFS_ALARM_BOD_HOUR = "edu.cmu.ices.EMA.preferences.BOD_INTERVIEW_HOUR";
	public static final String PREFS_ALARM_BOD_MINUTE = "edu.cmu.ices.EMA.preferences.BOD_INTERVIEW_MINUTE";
	public static final String PREFS_ALARM_EOD_HOUR = "edu.cmu.ices.EMA.preferences.EOD_INTERVIEW_TIME_HOUR";
	public static final String PREFS_ALARM_EOD_MINUTE = "edu.cmu.ices.EMA.preferences.EOD_INTERVIEW_TIME_MINUTE";
	public static final String PREFS_UNSENT_DATA = "edu.cmu.ices.EMA.preferences.UNSENT_DATA";
	public static final String PREFS_PARTICIPANT_ID = "edu.cmu.ices.EMA.preferences.ID";
	public static final String PREFS_UNSENT_ALARMS = "edu.cmu.ices.EMA.preferences.UNSENT_ALARMS";
	public static final String PREFS_UNSENT_ALARM_CHANGES = "edu.cmu.ices.EMA.preferences.UNSENT_ALARM_CHANGES";

	// scheduler parameters
	public static final String PREFS_ALPHA_1 = "edu.cmu.ices.EMA.preferences.ALPHA_1";
	public static final String PREFS_ALPHA_2 = "edu.cmu.ices.EMA.preferences.ALPHA_2";
	public static final String PREFS_ALPHA_3 = "edu.cmu.ices.EMA.preferences.ALPHA_3";
	public static final String PREFS_BETA_1 = "edu.cmu.ices.EMA.preferences.BETA_1";
	public static final String PREFS_BETA_2 = "edu.cmu.ices.EMA.preferences.BETA_2";
	public static final String PREFS_GAMMA_1 = "edu.cmu.ices.EMA.preferences.GAMMA_1";
	public static final String PREFS_GAMMA_2 = "edu.cmu.ices.EMA.preferences.GAMMA_2";
	public static final String PREFS_GAMMA_3 = "edu.cmu.ices.EMA.preferences.GAMMA_3";
	public static final String PREFS_GAMMA_4 = "edu.cmu.ices.EMA.preferences.GAMMA_4";
	public static final String PREFS_RHO_MAX = "edu.cmu.ices.EMA.preferences.RHO_MAX";
	public static final String PREFS_RESET_RANDOM_SCHEDULE = "edu.cmu.ices.EMA.preferences.RESET_RANDOM_SCHEDULE";
	public static final String PREFS_LAST_RANDOM_TIME = "edu.cmu.ices.EMA.preferences.LAST_RANDOM";
	public static final String PREFS_START_TIME = "edu.cmu.ices.EMA.preferences.START_TIME";
	public static final String PREFS_SUSPEND_ACCUMULATOR = "edu.cmu.ices.EMA.preferences.SUSPEND_ACCUMULATOR";
	public static final String PREFS_RANDOM_PROMPT_COUNT = "edu.cmu.ices.EMA.preferences.RANDOM_PROMPT_COUNT";
	public static final String PREFS_SAMPLED_EVENT_COUNT = "edu.cmu.ices.EMA.preferences.SAMPLED_EVENT_COUNT";
	public static final String PREFS_SHUTDOWN = "edu.cmu.ices.EMA.preferences.SHUTDOWN";
	public static final String PREFS_SHUTDOWN_PROXY = "edu.cmu.ices.EMA.prefs.SHUTDOWN_PROXY";
	public static final String PREFS_DAILY_SUSPEND_ACCUMULATED = "edu.cmu.ices.EMA.preferences.DAILY_SUSPEND_ACCUMULATED";
	public static final String PREFS_START_WEIGHT = "edu.cmu.ices.EMA.preferences.START_WEIGHT";
	public static final String PREFS_CURRENT_WEIGHT = "edu.cmu.ices.EMA.preferences.CURRENT_WEIGHT";
	public static final String PREFS_COMPLETE_COUNT = "edu.cmu.ices.EMA.prefs.COMPLETE_COUNT";
	public static final String PREFS_ABANDON_COUNT = "edu.cmu.ices.EMA.prefs.ABANDON_COUNT";
	public static final String PREFS_MISS_COUNT = "edu.cmu.ices.EMA.prefs.MISS_COUNT";
	public static final double MAX_INTENSITY = 10000.0;
	public static final String PREFS_NON_SUSPEND_DURATION = "edu.cmu.ices.EMA.prefs.NON_SUSPEND_DURATION";
	public static final long PREFS_DEFAULT_NON_SUSPEND_DURATION = 16L * AlarmManager.INTERVAL_HOUR;
	public static final String PREFS_EOD_SUSPEND_DATE = "edu.cmu.ices.EMA.prefs.EOD_SUSPEND_DATE";
	public static final String PREFS_DELAYED_PROMPT = "edu.cmu.ices.EMA.prefs.DELAYED_PROMPT";
	public static final String PREFS_DELAYED_ACTION = "edu.cmu.ices.EMA.prefs.DELAYED_ACTION";
	public static final String PREFS_EMA_VERSION = "edu.cmu.ices.EMA.prefs.EMA_VERSION";
	public static final String PREFS_ASSESS_PROB = "edu.cmu.ices.EMA.prefs.ASSESS_PROB";
	
	public static final String ALPHA_1 = "ALPHA_1";
	public static final String ALPHA_2 = "ALPHA_2";
	public static final String ALPHA_3 = "ALPHA_3";
	public static final String BETA_1 = "BETA_1";
	public static final String BETA_2 = "BETA_2";
	public static final String GAMMA_1 = "GAMMA_1";
	public static final String GAMMA_2 = "GAMMA_2";
	public static final String GAMMA_3 = "GAMMA_3";
	public static final String GAMMA_4 = "GAMMA_4";
	public static final String RHO = "RHO";
	
	// alarm settings
	public static final String PREFS_VOLUME_SETTING = "edu.cmu.ices.EMA.preferences.VOLUME_SETTING";
	public static final int PREFS_DEFAULT_VOLUME_SETTING = -1;
	public static final String PREFS_VIBRATE_SETTING = "edu.cmu.ices.EMA.preferences.VIBRATE_SETTING";
	public static final boolean PREFS_DEFAULT_VIBRATE_SETTING = true;
	public static final String PREFS_RINGTONE_SETTING = "edu.cmu.ices.EMA.preferences.RINGTONE_SETTING";
	public static final int PREFS_DEFAULT_RINGTONE_SETTING = -1;
	
	public static final String BRMGphoneNumber = "(412) 327-4199";
	
	// ui constants
	public static final int HIGHLIGHT = Color.WHITE;

	// cat control constants
	public static final int CAT_PRE_INTERMITTENT_PHASE = 0;
	public static final int CAT_POST_INTERMITTENT_PHASE = 1;
	public static final int CAT_ADAPTIVE_PHASE = 2;
	public static final int[] CAT_NUM_ITEMS = { 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3};
	public static final int CAT_NUM_SCALES = CAT_NUM_ITEMS.length; //13;
	public static final int CAT_MAX_NUM_ITEMS = 3;
	
	// interview response type contants
	public static final int INTERVIEW_LIST_TYPE = 0;
	public static final int INTERVIEW_TIME_TYPE_AM = 1;
	public static final int INTERVIEW_TIME_TYPE_PM = 8;
	public static final int INTERVIEW_DELAYED_TYPE = 2;
	public static final int INTERVIEW_MULTI_LIST_TYPE = 3;
	public static final int INTERVIEW_DROPDOWN_TYPE = 4;
	public static final int INTERVIEW_SEEKBAR_TYPE = 5;
	public static final int INTERVIEW_NUMBER_ENTRY_TYPE = 6;
	public static final int INTERVIEW_TEXT_ENTRY_TYPE = 7;
	public static final int INTERVIEW_DURATION_TYPE = 9;
	public static final int INTERVIEW_NUMBER_PICKER_TYPE = 10;
	public static final int INTERVIEW_DYNAMIC_TYPE = 11;
	public static final int INTERVIEW_LIST_0INDEX_TYPE = 12;
	public static final int INTERVIEW_MULTI_LIST_0INDEX_TYPE = 13;
	public static final int INTERVIEW_ANALOG_SEEKBAR_TYPE = 14;
	public static final int INTERVIEW_SELECTED_FROM_TYPE = 15;
	
	// interview completion constants
	public static final int INTERVIEW_COMPLETE = 0;
	public static final int INTERVIEW_ABANDONED = 1;
	public static final int INTERVIEW_MISSED = 2;
	public static final int INTERVIEW_CANCELLED = 3;
	
	// ema operational mode constants
	public static final String MODE_TRAINING = "t";
	public static final String MODE_PRACTICE = "p";
	public static final String MODE_MONITORING = "m";
	public static final String MODE_NON_MONITORING = "n";
	public static final String MODE_FINAL = "f";
	
	// duration of the monitoring period in days
	public static final int MONITORING_LENGTH = 4;
	
	// timing constants
	public static final long MINUTE_MILLIS = (60L * 1000L);
	public static final long HOUR_MILLIS = (60L * MINUTE_MILLIS);
	public static final long DAY_MILLIS = (24L * HOUR_MILLIS);
	
	// scheduling constants
	public static final long SAL_DELAY = (30L * Constants.MINUTE_MILLIS);
	public static final long SAL_DUR =  0;
	public static final long REM_DELAY = (20L * Constants.MINUTE_MILLIS);
	public static final long REM_DUR = (10000);
	public static final long SYNC_DELAY = (10L * Constants.MINUTE_MILLIS);
	public static final long SYNC_DUR = 0;
	public static final boolean DEBUG = false; //TODO set DEBUG to false before final deployment
	
	// alarm stuff
	public static final String ACTION_INTERVIEW = "edu.cmu.ices.EMA.ACTION.INTERVIEW";
	public static final String ALARM_KEY = "edu.cmu.ices.EMA.service.ALARM_KEY";
	public static final String ALARM_KEY_BOD = "edu.cmu.ices.EMA.service.ALARM_KEY_BOD";
	public static final String ALARM_KEY_SAL = "edu.cmu.ices.EMA.service.ALARM_KEY_SAL";
	public static final String ALARM_KEY_REM = "edu.cmu.ices.EMA.service.ALARM_KEY_REM";
	public static final String ALARM_KEY_BP = "edu.cmu.ices.EMA.service.ALARM_KEY_BP";
	public static final String ALARM_ALERT = "edu.cmu.ices.EMA.service.ALARM_ALERT";
	public static final String ALARM_ACTIVITY = "edu.cmu.ices.EMA.service.ALARM_ACTIVITY";
	public static final String ALARM_ACTIVITY_RANDOM = "edu.cmu.ices.EMA.service.ALARM_ACTIVITY_RANDOM";
	public static final String ALARM_ACTIVITY_BOD = "edu.cmu.ices.EMA.service.ALARM_ACTIVITY_BOD";
	public static final String ALARM_ACTIVITY_EOD = "edu.cmu.ices.EMA.service.ALARM_ACTIVITY_EOD";
	public static final String ALARM_ACTIVITY_EVENT = "edu.cmu.ices.EMA.service.ALARM_ACTIVITY_EVENT";
	public static final String ALARM_ACTIVITY_WEEKLY = "edu.cmu.ices.EMA.service.ALARM_ACTIVITY_WEEKLY";
	public static final String ALARM_ACTIVITY_DELAYED = "edu.cmu.ices.EMA.service.ALARM_ACTIVITY_DELAYED";
	public static final String ALARM_ACTIVITY_30MIN = "edu.cmu.ices.EMA.service.ALARM_ACTIVITY_30MIN";
	public static final String ALARM_ACTIVITY_REM = "edu.cmu.ices.EMA.service.ALARM_ACTIVITY_REM";
	public static final String ALARM_ACTIVITY_BPSYNC = "edu.cmu.ices.EMA.service.ALARM_ACTIVITY_BPSYNC";
	public static final String ALARM_ACTIVITY_BLOOD = "edu.cmu.ices.EMA.service.ALARM_ACTIVITY_BLOOD";
	public static final String ALARM_ACTIVITY_BLOOD_WARNING = "edu.cmu.ices.EMA.service.ALARM_ACTIVITY_BLOOD_WARNING";
	public static final String ALARM_ACTIVITY_FORCE_EOD = "edu.cmu.ices.EMA.service.ALARM_ACTIVITY_FORCE_EOD";
	public static final String ALARM_ACTIVITY_FORCE_BOD = "edu.cmu.ices.EMA.service.ALARM_ACTIVITY_FORCE_BOD";
	public static final String ALARM_ACTIVITY_EKG = "edu.cmu.ices.EMA.service.ALARM_ACTIVITY_EKG";
	public static final String ALARM_CLASS = "edu.cmu.ices.EMA.service.ALARM_CLASS";
	public static final String ALARM_MESSAGE = "edu.cmu.ices.EMA.service.ALARM_MESSAGE";
	public static final String ALARM_TIMEOUT = "edu.cmu.ices.EMA.service.ALARM_TIMEOUT";
	public static final String ALARM_TYPE = "edu.cmu.ices.EMA.service.ALARM_TYPE";
	public static final String ALARM_PROMPTTIME = "edu.cmu.ices.EMA.service.ALARM_PROMPTTIME";
	public static final String ALARM_INTERVIEW_TIMEOUT = "edu.cmu.ices.EMA.service.ALARM_INTERVIEW_TIMEOUT";
	public static final String ALARM_TELEPHONY_DELAYS = "edu.cmu.ices.EMA.service.ALARM_TELEPHONY_DELAYS";
	public static final String ALARM_ACTION = "edu.cmu.ices.EMA.service.ALARM_ACTION";
	public static final String WIDGET_UPDATE = "edu.cmu.ices.EMAAppWidget.UPDATE";
	public static final String INTERVIEW_TIMEOUT = "edu.cmu.ices.EMA.interview.INTERVIEW_TIMEOUT";
	public static final String INTERVIEW_DELAY = "edu.cmu.ices.EMA.interview.INTERVIEW_DELAY";
	public static final String INTERVIEW_RANDOM_INCENTIVE = "edu.cmu.ices.EMA.interview.INTERVIEW_RANDOM_INCENTIVE";
	public static final String ALARM_TOGGLE_ACTION = "edu.cmu.ices.EMA.service.ALARM_TOGGLE_ACTION";

	public static final int ALARM_0 = 0;
	public static final int ALARM_1 = 1;
	public static final int ALARM_2 = 2;
	
	public static final String ALARM_RQ_CODE = "edu.cmu.ices.EMA.service.ALARM_RQ_CODE";
	public static final int ALARM_BOD_RQ_CODE = 0;
	public static final int ALARM_EOD_RQ_CODE = 1;
	public static final int ALARM_RANDOM_RQ_CODE = 2;
	public static final int ALARM_ALERT_RQ_CODE = 3;
	public static final int ALARM_WEEKLY_RQ_CODE = 4;
	public static final int ALARM_DELAYED_RQ_CODE = 5;
	public static final int ALARM_EVENT_RQ_CODE = 6;
	public static final int ACTIVITY_WIDGET_RQ_CODE = 7;
	public static final int UPDATE_INSTALL_RQ_CODE = 8;
	public static final int UPDATE_NOTIFICATION_ID = 9;
	public static final int ALARM_TIMEOUT_RQ_CODE = 10;
	public static final int INTERVIEW_TIMEOUT_RQ_CODE = 11;
	public static final int ALARM_SNOOZE_RQ_CODE = 12;
	public static final int ALARM_TOGGLE_RQ_CODE = 13;
	public static final int INTERVIEW_DELAY_RQ_CODE = 14;
	public static final int INTERVIEW_RETURN_RQ_CODE = 15;
	public static final int ALARM_BLOOD_RQ_CODE = 19;
	public static final int ALARM_BLOOD_WARNING_RQ_CODE = 20;
	public static final int ALARM_FORCE_BOD_RQ_CODE = 21;
	public static final int ALARM_FORCE_EOD_RQ_CODE = 22;
	
	public static final int ALARM_30MIN_RQ_CODE = 16;
	public static final int ALARM_REM_RQ_CODE = 17;
	public static final int ALARM_BPSYNC_RQ_CODE = 18;
	
	public static final int ALARM_EKG_RQ_CODE = 23;

	public static final String ALARMS[] = {"Alarm_Buzzer.ogg", "Alarm_Classic.ogg", "Alarm_Beep_01.ogg",};
	public static final int NUM_ALARMS = ALARMS.length;
	public static final long[] VIBE_PATTERN = {0, 500, 200, 500, 200, 500, 1000, 500, 200, 500, 200, 500, 2500};
	public static final String ALARM_CAN_SNOOZE = "edu.cmu.ices.EMA.service.ALARM_CAN_SNOOZE";
	
	public static final int BLOOD_HOUR = 19;
	public static final int BLOOD_MINUTE = 0;
	
	// file handling constants
	public static final String EMADIR = "EMA_APP";
	public static final String CONFIGDIR = "EMA_CONFIG";
	public static final String CONFIGFILE = "ema_saved_state.txt";
	public static final String IDCONFIGFILE = "ema_id_config.txt";
	public static final String LOGDIR = "EMA_LOGS";
	public static final String DEBUGLOG = "debug.txt";
	public static final String WARNINGLOG = "warning.txt";
	public static final String ERRORLOG = "error.txt";
	
	// JSON logging constants
	public static final String RESULT_STATUS = "RESULT_STATUS";
	public static final String TYPE = "TYPE";
	public static final String ID = "ID";
	public static final String SCHEDULED = "SCHEDULED_TIME";
	public static final String PROMPTED = "PROMPT_TIME";
	public static final String START = "START_TIME";
	public static final String END = "END_TIME";
	public static final String DELAY = "DELAY_TIME";
	public static final String STATUS = "STATUS"; // complete, abandonned, missed
	public static final String INTENSITY = "INTENSITY";
	public static final String SAMPLE_PROB = "ASSESS_PROBABILITY";
	public static final String NUM_ITEMS = "NUM_ITEMS";
	public static final String ITEM = "ITEM";
	public static final String SCHED_TIME = "SCHED_TIME";
	public static final String INCENTIVE = "INCENTIVE_ALARM";
	public static final String SHIFTED_ALARM = "SHIFTED_ALARM";
	
	// salivette coding
	public static long generateSalivetteCode() {
		//(getsystime - 24*60*60*getsysdate) + 100000*dayofweek
		//getsystime = # sec since 12am Jan 1 1904
		//getsysdate = # days since Jan 1 1904
		//dayofweek = mon=0 ... sun=6
		Time midnight = new Time();
		midnight.setToNow();
		//System.out.println("midnight millis = " + midnight.toMillis(false));
		midnight.set(0, 0, 0, midnight.monthDay, midnight.month, midnight.year);
		//System.out.println("midnight millis = " + midnight.toMillis(false));
		Time now = new Time();
		now.setToNow();
		//System.out.println("now millis = " + now.toMillis(false));
		long dayOfWeek = (now.weekDay - 1); //monday is zero
		if (dayOfWeek < 0) dayOfWeek = 6;
		long value = ((now.toMillis(false) - midnight.toMillis(false)) / 1000) + (100000L*dayOfWeek);
		
		//long sec_since_mid = (now.toMillis(false) - midnight.toMillis(false)) / 1000;
		//System.out.println("dayOfWeek = " + dayOfWeek + ", seconds since midnight = " + sec_since_mid);
		
		//String value_str = Long.toString(value).substring(4); //drop the first 4 digits so the total length is 6 digits
		
		//value = Long.parseLong(value_str);
		return value;
	}
	
	// config file stuff
	public static File getConfigFile(String configFile) {
		File file = null;
		File root = Environment.getExternalStorageDirectory();
		file = new File(root, Constants.EMADIR);
		if (!file.exists())
			file.mkdir();
		file = new File(file, Constants.CONFIGDIR);
		if (!file.exists()) {
			file.mkdir();
		}
		file = new File(file, configFile);
		
		return file;
	}
}
