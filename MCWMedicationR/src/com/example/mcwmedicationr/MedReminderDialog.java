package com.example.mcwmedicationr;

import java.io.IOException;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MedReminderDialog extends Activity {

	public static final String MEDREMINDLOG = "MedRemind_log.csv";
	
	private RingtoneManager rm;
	private MediaPlayer mMediaPlayer;
	private boolean mPlaying = false;
	private Vibrator vibe;
	private WakeLock wl;
	
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
	
	Button record, snooze, week1;
	private long time;
	
    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        
        // prevent a touch event outside the dialog window from canceling the prompt
        setFinishOnTouchOutside(false);
        
        setContentView(R.layout.activity_med_reminder_dialog);
        record = (Button) findViewById(R.id.reminderRecordButton);
        snooze = (Button) findViewById(R.id.reminderSnoozeButton);
        week1 = (Button) findViewById(R.id.reminderWeek1Button);

        time = System.currentTimeMillis();
        
        prefs = new AppPreferences(getApplicationContext());

		vol = prefs.getVolumeSetting();
        ringtone = prefs.getRingtoneSetting();
        vibrate = prefs.getVibrateSetting();
        
        rm = new RingtoneManager(this);
    	rm.setType(RingtoneManager.TYPE_ALARM);
        
        intent = getIntent();
        // set the prompt time if it hasn't already been set
        if (intent.getLongExtra(Constants.ALARM_PROMPTTIME, 0L) == 0L) {
        	intent.putExtra(Constants.ALARM_PROMPTTIME, time); // store the prompt time for the interview
        }
		
		long alarmTimeout = 0; //intent.getLongExtra(Constants.ALARM_TIMEOUT, 0);
		message = intent.getStringExtra(Constants.ALARM_MESSAGE);
		snoozeCount = intent.getIntExtra("SNOOZE_CNT", -1);
		snoozeable = intent.getBooleanExtra(Constants.ALARM_CAN_SNOOZE, false);
		snoozeable &= snoozeCount != 0;
		requestCode = intent.getIntExtra(Constants.ALARM_RQ_CODE, Constants.ALARM_ALERT_RQ_CODE);
		if (message == null) {
			message = "Dismiss alarm?";
		}
		
		sched = new AlarmScheduler(getApplicationContext());
		
		snoozeIntent = new Intent(this, AlarmReceiver.class);//getApplicationContext(), Alarm.class);
		snoozeIntent.putExtras(intent.getExtras());
		
		audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		
		record.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// launch the interview the alarm has been dismissed
        	   logMedication();
        	   scheduleNextReminder(time);
               stop();
			}
			
		});
		
		snooze.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

    		   long alarmTime = time + (10L * Constants.MINUTE_MILLIS);
    		   sched.rescheduleIntent(snoozeIntent, requestCode, alarmTime);
    		   stop();
			}
			
		});
		
		week1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				scheduleNextReminder(time);
				stop();
				finish();
			}
			
		});
		
		PowerManager pm = (PowerManager)getBaseContext().getSystemService(POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "com.ices.cmu.EMA.service.Alarm");
		wl.acquire();
		
		// release the wake lock in the alarm receiver
		if (AlarmReceiver.br_wl != null) {
			if (AlarmReceiver.br_wl.isHeld()) {
				System.out.println("Releasing alarm receiver wakelock");
				AlarmReceiver.br_wl.release();
				AlarmReceiver.br_wl = null;
			}
		}
		
		//TODO may want to reintroduce the keypad lock disable here...
		
		// alarm toggling set up
		registerReceiver(alarmToggleReceiver, new IntentFilter(Constants.ALARM_TOGGLE_ACTION));
		Intent alarmToggle = new Intent();
		alarmToggle.setAction(Constants.ALARM_TOGGLE_ACTION);
		alarmToggle.putExtra("toggle_count", 1);
		sched.rescheduleIntent(alarmToggle, Constants.ALARM_TOGGLE_RQ_CODE, time + (Constants.MINUTE_MILLIS / 2));
		
        if (!prefs.getIsEnabled()) {
        	// this has been disabled, just eat this alarm and close
        	finish();
        	return;
        }
        
		initAlarm();
    }
    
	private void scheduleNextReminder(long time) {
		
		long nextAlarm = prefs.getNextAlarmTime(time);
		Intent i = new Intent(getApplicationContext(), AlarmReceiver.class);
		sched.rescheduleIntent(i, requestCode, nextAlarm);
	}
    
    @Override
    public void onDestroy() {
    	stop();
    	cancelTimeout();
    	unregisterReceiver(alarmToggleReceiver);
    	audioManager.setStreamVolume(AudioManager.STREAM_ALARM, oldAlarmVolume, 0);
    	
    	if (wl.isHeld())
        	wl.release();
    	super.onDestroy();
    }
    
 // override the keydown event listener to capture keypresses
 	// disables all keys except HOME, END and POWER. END is handled by
 	// changing the system settings for end button behavior, home and 
 	// power need to be researched...
 	@Override
 	public boolean onKeyDown(int keyCode, KeyEvent event) {
 	    if (keyCode == KeyEvent.KEYCODE_BACK) {
 	        // disable the back button while in the interview
 	    	return true;
 	    }
 	    if (keyCode == KeyEvent.KEYCODE_CALL) {
 	    	// disable the call button
 	    	return true;
 	    }
 	    if (keyCode == KeyEvent.KEYCODE_CAMERA) {
 	    	// disable camera
 	    	return true;
 	    }
 	    if (keyCode == KeyEvent.KEYCODE_SEARCH) {
 	    	// disable search
 	    	return true;
 	    }
 	    if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
 	    	return true;
 	    }
 	    if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
 	    	return true;
 	    }
 	    return super.onKeyDown(keyCode, event);
 	}
 	
 	private void logMedication() {
 		Logger logger = new Logger(this);
 		logger.LogEntry(MEDREMINDLOG, System.currentTimeMillis(), null);
 	}
    
    private void initAlarm() {

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

    private BroadcastReceiver alarmToggleReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			long now = System.currentTimeMillis();
			long delay = Constants.MINUTE_MILLIS / 2; // 30 seconds on
			int count = intent.getIntExtra("toggle_count", 0);
			if (count == 1) {
				count = 0;
				pauseAlarm();
				delay *= 2L; // one minutes off
			} else {
				count = 1;
				restartAlarm();
			}
			
			// reschedule
			intent.putExtra("toggle_count", count);
			sched.rescheduleIntent(intent, Constants.ALARM_TOGGLE_RQ_CODE, now + delay);
		}
    };
    
    
}
