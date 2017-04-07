package com.example.mcwmedicationr;


import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;
import android.view.KeyEvent;
import edu.cmu.ices.EMA.Constants;
import edu.cmu.ices.EMA.R;
import edu.cmu.ices.EMA.service.logging.Log;

public class Alarm extends Activity {
	
	private RingtoneManager rm;
	private MediaPlayer mMediaPlayer;
	private boolean mPlaying = false;
	private Vibrator vibe;
//	private final long[] vibePattern = {200, 300};
	private AlertDialog dialog;
	private WakeLock wl;
	
	private int alarmType;
//	private final String alarms[] = {"Alarm_Buzzer.ogg", "Alarm_Classic.ogg", "Alarm_Beep_01.ogg",};
	private Handler handler;
	
	String message;
	boolean snoozeable;
	Intent intent;
	Intent snoozeIntent;
	int requestCode;
	private int oldAlarmVolume;
	AudioManager audioManager;// = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
	
	AlarmScheduler sched;
	
	AppPreferences prefs;
	int vol;
	int ringtone;
	boolean vibrate;
	int snoozeCount = -1;
	
	//notification intent for cancelling
	PendingIntent notificationIntent;
	
    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        long time = System.currentTimeMillis();
        
        prefs = new AppPreferences(getApplicationContext());
        
        AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
        vol = am.getStreamVolume(AudioManager.STREAM_RING);
        //if (vol != 0) {
		vol = prefs.getVolumeSetting();
        //}
        
        
        ringtone = prefs.getRingtoneSetting();
        System.out.println("Ringtone index = " + ringtone);
        vibrate = prefs.getVibrateSetting();
        
        rm = new RingtoneManager(this);
    	rm.setType(RingtoneManager.TYPE_ALARM);
        
        intent = getIntent();
        // set the prompt time if it hasn't already been set
        if (intent.getLongExtra(Constants.ALARM_PROMPTTIME, 0L) == 0L) {
        	intent.putExtra(Constants.ALARM_PROMPTTIME, System.currentTimeMillis()); // store the prompt time for the interview
        }
		alarmType = intent.getIntExtra(Constants.ALARM_TYPE, Constants.ALARM_0);
		long alarmTimeout = intent.getLongExtra(Constants.ALARM_TIMEOUT, 0);
		message = intent.getStringExtra(Constants.ALARM_MESSAGE);
		snoozeCount = intent.getIntExtra("SNOOZE_CNT", -1);
		snoozeable = intent.getBooleanExtra(Constants.ALARM_CAN_SNOOZE, false);
		snoozeable &= snoozeCount != 0;
		requestCode = intent.getIntExtra(Constants.ALARM_RQ_CODE, Constants.ALARM_ALERT_RQ_CODE);
		if (message == null) {
			message = "Dismiss alarm?";
		}
		
		sched = new AlarmScheduler(getApplicationContext());

		handler = new Handler();
		
		registerReceiver(timeoutReceiver, new IntentFilter(Constants.ALARM_TIMEOUT));
		if (alarmTimeout != 0) {
			scheduleTimeout(intent, alarmTimeout);
//			handler = new Handler();
//			handler.postDelayed(timeoutAlarm, alarmTimeout);
//			Log.d("onCreate", "alarm timeout");
		}
		
		snoozeIntent = new Intent();//getApplicationContext(), Alarm.class);
		snoozeIntent.setAction(intent.getStringExtra(Constants.ALARM_ACTION));
		snoozeIntent.putExtras(intent.getExtras());
		if (snoozeCount == -1) {
			snoozeIntent.putExtra("SNOOZE_CNT", 2); // first time snoozing can do two more
		} else {
			snoozeIntent.putExtra("SNOOZE_CNT", snoozeCount - 1);
		}
		//snoozeIntent.removeExtra(Constants.ALARM_CAN_SNOOZE); // can only snooze once
		
		audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message)
		       .setCancelable(false)
		       .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   // launch the interview the alarm has been dismissed
		        	   launchInterview(intent);
		               stop();
		           }
		       })
		       .setOnKeyListener(new OnKeyListener() {

				@Override
				public boolean onKey(DialogInterface arg0, int arg1,
						KeyEvent arg2) {
					switch(arg2.getKeyCode()) {
					case KeyEvent.KEYCODE_BACK:
					case KeyEvent.KEYCODE_CALL:
					case KeyEvent.KEYCODE_CAMERA:
					case KeyEvent.KEYCODE_SEARCH:
						return true;
					case KeyEvent.KEYCODE_VOLUME_DOWN:
					case KeyEvent.KEYCODE_VOLUME_UP:

						final AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
				        // do not play alarms if stream volume is 0
				        // (typically because ringer mode is silent).
				        // make alarm volume controlled by the ringer volume
				        //audioManager.setStreamVolume(AudioManager.STREAM_ALARM, audioManager.getStreamVolume(AudioManager.STREAM_RING), 0);
				        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, 0, 0);
					}
					return false;
				}
		    	   
		       });
		if (snoozeable) {
			builder.setNegativeButton("Snooze Alarm", new DialogInterface.OnClickListener() {
		    	   @Override
		    	   public void onClick(DialogInterface dialog, int which) {
		    		   //AlarmScheduler sched = new AlarmScheduler(getApplicationContext());
		    		   long alarmTime = System.currentTimeMillis() + (9L * Constants.MINUTE_MILLIS);
//		    		   Intent i = new Intent();//getApplicationContext(), Alarm.class);
//		    		   i.setAction(intent.getStringExtra(Constants.ALARM_ACTION));
//		    		   i.putExtras(intent.getExtras());
//		    		   i.removeExtra(Constants.ALARM_CAN_SNOOZE);
		    		   //intent.removeExtra(Constants.ALARM_CAN_SNOOZE);

		    		   prefs.setDelayedPrompt(requestCode);
		    		   prefs.setDelayedAction(snoozeIntent.getAction());

		    		   sched.rescheduleIntent(snoozeIntent, requestCode, alarmTime);
		    		   //sched.rescheduleIntent(intent, Constants.ALARM_SNOOZE_RQ_CODE, alarmTime);
		    		   //sched.scheduleOneShotAlarm(Constants.ALARM_ACTIVITY_DELAYED, null, message, Constants.ALARM_DELAYED_RQ_CODE, alarmTime, snoozeable);
		    		   stop();
		    	   }
		       });
		} else {
			// this is returning from an already delayed alarm prompt
			// clear the delayedPromptValue
			prefs.clearDelayedPrompt();
			prefs.clearDelayedAction();
		}
		dialog = builder.create();
		
		PowerManager pm = (PowerManager)getBaseContext().getSystemService(POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "com.ices.cmu.EMA.service.Alarm");
		wl.acquire();
		
		//TODO may want to reintroduce the keypad lock disable here...
		
		initAlarm();
		
		// alarm toggling set up
		registerReceiver(alarmToggleReceiver, new IntentFilter(Constants.ALARM_TOGGLE_ACTION));
		Intent alarmToggle = new Intent();
		alarmToggle.setAction(Constants.ALARM_TOGGLE_ACTION);
		alarmToggle.putExtra("toggle_count", 5);
		sched.rescheduleIntent(alarmToggle, Constants.ALARM_TOGGLE_RQ_CODE, time + (Constants.MINUTE_MILLIS / 2));
//		handler.postAtTime(this.turnOffAlarm, time + Constants.MINUTE_MILLIS);
//		handler.postAtTime(this.turnOnAlarm, time + (2L * Constants.MINUTE_MILLIS));
//		handler.postAtTime(this.turnOffAlarm, time + (3L * Constants.MINUTE_MILLIS));
//		handler.postAtTime(this.turnOnAlarm, time + (4L * Constants.MINUTE_MILLIS));
		
		if (AlarmReceiver.br_wl != null) {
			if (AlarmReceiver.br_wl.isHeld()) {
				System.out.println("Releasing alarm receiver wakelock");
				AlarmReceiver.br_wl.release();
				AlarmReceiver.br_wl = null;
			}
		}
		
    }
    
    private void cancelNotification(Context ctx) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) ctx.getSystemService(ns);
        nMgr.cancelAll();
    }
    
    @Override
    public void onResume() {
    	super.onResume();
		
		
    }
    
//    @Override
//    public void onPause() {
//    	stop();
//    	cancelTimeout();
//    	//handler.removeCallbacks(timeoutAlarm);
//    	audioManager.setStreamVolume(AudioManager.STREAM_ALARM, oldAlarmVolume, 0);
//    	super.onPause();
//    }
    
    @Override
    public void onDestroy() {
    	stop();
    	cancelTimeout();
    	unregisterReceiver(timeoutReceiver);
    	unregisterReceiver(alarmToggleReceiver);
    	//handler.removeCallbacks(timeoutAlarm);
//    	handler.removeCallbacks(this.turnOffAlarm);
//    	handler.removeCallbacks(this.turnOnAlarm);
    	audioManager.setStreamVolume(AudioManager.STREAM_ALARM, oldAlarmVolume, 0);
    	
    	//cancel notification
    	cancelNotification(getApplicationContext());
    	
    	if (wl.isHeld())
        	wl.release();
    	super.onDestroy();
    }
    
    private void launchInterview(Intent intent) {
    	Intent i;
    	String class_name = intent.getStringExtra(Constants.ALARM_CLASS);
		//long now = System.currentTimeMillis();
		if (class_name != null) {
			try {
				i = new Intent(getApplicationContext(), Class.forName(class_name));
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
				// load the intent extras up in case any of them are relevant to the interview (e.g. delay count before prompt?, or the intended prompt time)
				i.putExtras(intent.getExtras());
				getApplicationContext().startActivity(i);
				//PendingIntent pi = PendingIntent.getActivity(context, rq_code, i, 0);
				//am.set(AlarmManager.RTC_WAKEUP, now, pi); //HACK, have to launch the activity by scheduling it with the alarm manager instead of calling startActivity so that multiple activities can overlap for some reason
				Log.w("ALARM", "starting the intent: " + class_name);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				Log.e("ALARM", "failed to load class to start the intent: " + class_name);
			}
		}

    }
    
    private void initAlarm() {
//Uri alert = Uri.parse("/system/media/audio/alarms/" + Constants.ALARMS[alarmType]);
    	//Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//    	Uri alert = Uri.parse("android.resource://edu.cmu.ices.EMA/" + edu.cmu.ices.EMA.R.raw.alarm_buzzer);//raw/alarm_buzzer.ogg");

    	Uri alert;
    	if (ringtone == Constants.PREFS_DEFAULT_RINGTONE_SETTING) {
    		alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
    	} else {
    		System.out.println("Ringtone index = " + ringtone);
    		rm.getCursor();
    		alert = rm.getRingtoneUri(ringtone);
    		if (alert == null) {
    			alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
    		}
    		System.out.println("ALARM SELECTED: " + alert.getPath());
    	}
 	
		// TODO: Reuse mMediaPlayer instead of creating a new one and/or use
    	// RingtoneManager.
    	mMediaPlayer = new MediaPlayer();
    	//mMediaPlayer = MediaPlayer.create(getApplicationContext(), edu.cmu.ices.EMA.R.raw.alarm_buzzer);
    	mMediaPlayer.setOnErrorListener(new OnErrorListener() {
          public boolean onError(MediaPlayer mp, int what, int extra) {
              Log.e("ALARM", "Error occurred while playing audio.");
              mp.stop();
              mp.release();
              mMediaPlayer = null;
              return true;
          }
      });
      
      vibe = (Vibrator)getBaseContext().getSystemService(VIBRATOR_SERVICE);
      if (vibrate) {
    	  vibe.vibrate(Constants.VIBE_PATTERN, 0);
      }
      
      mMediaPlayer.reset();
      
      try {
			mMediaPlayer.setDataSource(getApplicationContext(), alert);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

      try {
			startAlarm(mMediaPlayer);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mPlaying = true;
		dialog.show();
    }
	
	/**
     * Stops alarm audio and disables alarm if it not snoozed and not
     * repeating
     */
    public void stop() {
        
        if (mPlaying) {
            mPlaying = false;

            // Stop audio playing
            if (mMediaPlayer != null) {
                mMediaPlayer.stop();
                mMediaPlayer.release();
                mMediaPlayer = null;
            }
            
            // Stop vibrator
            vibe.cancel();
        }
        
//        if (wl.isHeld())
//        	wl.release();
        dialog.dismiss();
        finish();
    }
	
	 // Do the common stuff when starting the alarm.
    private void startAlarm(MediaPlayer player)
            throws java.io.IOException, IllegalArgumentException,
                   IllegalStateException {
        
        // do not play alarms if stream volume is 0
        // (typically because ringer mode is silent).
        // make alarm volume controlled by the ringer volume
        oldAlarmVolume = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);

        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, vol, 0);

        if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
            player.setAudioStreamType(AudioManager.STREAM_ALARM);
            player.setLooping(true);
            player.prepare();
            player.start();
        }
    }
    
    private void scheduleTimeout(Intent intent, long time) {
    	Intent timeout = new Intent();
    	timeout.setAction(Constants.ALARM_TIMEOUT);
    	timeout.putExtras(intent.getExtras());
    	
    	sched.rescheduleIntent(timeout, Constants.ALARM_TIMEOUT_RQ_CODE, System.currentTimeMillis() + time);
    }
    
    private void cancelTimeout() {
    	sched.cancelAlarm(Constants.ALARM_TIMEOUT, Constants.ALARM_TIMEOUT_RQ_CODE);
    	sched.cancelAlarm(Constants.ALARM_TOGGLE_ACTION, Constants.ALARM_TOGGLE_RQ_CODE);
    }
    
    private void pauseAlarm() {
	    if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
			mMediaPlayer.pause();
		}
		if (vibe != null) {
			vibe.cancel();
		}
    }
    
    private void restartAlarm() {
    	if (mMediaPlayer != null && vol > 0) {
			mMediaPlayer.start();
		}
		if (vibe != null && vibrate) {
			vibe.vibrate(Constants.VIBE_PATTERN, 0);
		}
    }
    
//    private Runnable timeoutAlarm = new Runnable() {
//
//		@Override
//		public void run() {
//			// TODO launch interview to be missed? This is needed to handle rescheduling of random interviews
//			
//			stop();
//			
//			intent.putExtra(Constants.ALARM_INTERVIEW_TIMEOUT, true);
//			launchInterview();
//		}
//    	
//    };
	
//    public class TimeoutReceiver extends BroadcastReceiver {
//
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			// TODO Auto-generated method stub
//			stop();
//			
//			intent.putExtra(Constants.ALARM_INTERVIEW_TIMEOUT, true);
//			launchInterview(intent);
//		}
//    	
//    }
    private BroadcastReceiver alarmToggleReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			long now = System.currentTimeMillis();
			long delay = Constants.MINUTE_MILLIS / 2; // 30 seconds on
			int count = intent.getIntExtra("toggle_count", 0);
			if (count % 2 == 1) {
				pauseAlarm();
				delay *= 16L; // eight minutes off
			} else {
				restartAlarm();
			}
			
			count --;
			if (count >= 0) {
				// reschedule
				intent.putExtra("toggle_count", count);
				sched.rescheduleIntent(intent, Constants.ALARM_TOGGLE_RQ_CODE, now + delay);
			}
		}
    };
    
//    private Runnable timeoutAlarm = new Runnable() {
//
//		@Override
//		public void run() {
//			// TODO launch interview to be missed? This is needed to handle rescheduling of random interviews
//			
//			stop();
//			
//			intent.putExtra(Constants.ALARM_INTERVIEW_TIMEOUT, true);
//			launchInterview();
//		}
//    	
//    };
	
//    public class TimeoutReceiver extends BroadcastReceiver {
//
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			// TODO Auto-generated method stub
//			stop();
//			
//			intent.putExtra(Constants.ALARM_INTERVIEW_TIMEOUT, true);
//			launchInterview(intent);
//		}
//    	
//    }
    
    private BroadcastReceiver timeoutReceiver = new BroadcastReceiver() {
    	
    	@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			stop();
			
			intent.putExtra(Constants.ALARM_INTERVIEW_TIMEOUT, true);
			launchInterview(intent);
		}
    };
}
