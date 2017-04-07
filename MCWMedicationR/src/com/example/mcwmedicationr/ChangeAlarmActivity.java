package com.example.mcwmedicationr;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Spinner;
import edu.cmu.ices.EMA.Constants;
import edu.cmu.ices.EMA.R;
import edu.cmu.ices.EMA.service.logging.Log;

public class ChangeAlarmActivity extends Activity {

	SharedPreferences prefs;
	Editor editor;
	
	MediaPlayer player;
	Vibrator vibe;
	
	CheckBox vibrate;
	SeekBar volume;
	Spinner alarms;
	ArrayAdapter<String> adapter;
	Button save, cancel;
	
	int currVol;
	boolean vibeEnabled;
	int selectedRingtone;
	
	// existing alarm settings
	RingtoneManager rm;
	AudioManager audioManager;
	int oldAlarmVolume;
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		currVol = savedInstanceState.getInt("currVol");
		savedInstanceState.getBoolean("vibeEnabled");
		savedInstanceState.getInt("selectedRingtone");
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		
		outState.putInt("currVol", currVol);
		outState.putBoolean("vibeEnabled", vibeEnabled);
		outState.putInt("selectedRingtone", selectedRingtone);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.change_alarm_layout);
		
		prefs = getSharedPreferences(Constants.EMA_PREFS, 0);
		editor = prefs.edit();
		
		rm = new RingtoneManager(this);
		rm.setType(RingtoneManager.TYPE_ALARM);
		
		// get an audio service manager and save the current volume setting for the alarm stream
		audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		oldAlarmVolume = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);
		
		player = new MediaPlayer();
		player.setOnErrorListener(new OnErrorListener() {
	          public boolean onError(MediaPlayer mp, int what, int extra) {
	              Log.e("Alarm", "Error occurred while playing audio.");
	              mp.stop();
	              mp.release();
	              player = null;
	              return true;
	          }
		});
		
		vibe = (Vibrator)getBaseContext().getSystemService(VIBRATOR_SERVICE);
		
		volume = (SeekBar) findViewById(R.id.volumeSeekBar);
		vibrate = (CheckBox) findViewById(R.id.vibrateCheckBox);
		alarms = (Spinner) findViewById(R.id.alarmSpinner);
		save = (Button) findViewById(R.id.alarmSave);
		cancel = (Button) findViewById(R.id.alarmCancel);
		
		if (savedInstanceState != null) {
			onRestoreInstanceState(savedInstanceState);
		} else {
			currVol = prefs.getInt(Constants.PREFS_VOLUME_SETTING, Constants.PREFS_DEFAULT_VOLUME_SETTING);
			vibeEnabled = prefs.getBoolean(Constants.PREFS_VIBRATE_SETTING, Constants.PREFS_DEFAULT_VIBRATE_SETTING);
			selectedRingtone = prefs.getInt(Constants.PREFS_RINGTONE_SETTING, Constants.PREFS_DEFAULT_RINGTONE_SETTING);
		}
		
		// volume setting stuff
		volume.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM));
		System.out.println("max vol = " + volume.getMax());
		if (currVol == -1) {
			currVol = volume.getMax() / 2;
		}
		volume.setProgress(currVol);
		volume.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				if (event.getAction() == MotionEvent.ACTION_UP) {
					currVol = volume.getProgress();
					
					playFeedback();
				}
				return false;
			}
			
		});
		
		// vibrate setting stuff
		vibrate.setChecked(vibeEnabled);
		vibrate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				vibeEnabled = vibrate.isChecked();
				
				playFeedback();
			}
			
		});
	
		// ringtone setting stuff
		if (selectedRingtone == -1) {
			Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
			
			selectedRingtone = rm.getRingtonePosition(alert);
		}
		Cursor ringtones = rm.getCursor();
		int count = ringtones.getCount();
		List<String> names = new ArrayList<String>();
		for (int i=0; i<count; i++) {
			names.add(rm.getRingtone(i).getTitle(getApplicationContext()));
		}
		adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, names); 
		alarms.setAdapter(adapter);
		
		alarms.setSelection(selectedRingtone);
		
		alarms.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				selectedRingtone = alarms.getSelectedItemPosition();
				
				playFeedback();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
			
		});
		
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("vol = " + currVol + " vibe = " + vibeEnabled + " ringtone = " + selectedRingtone);
				editor.putInt(Constants.PREFS_VOLUME_SETTING, currVol);
				editor.putBoolean(Constants.PREFS_VIBRATE_SETTING, vibeEnabled);
				editor.putInt(Constants.PREFS_RINGTONE_SETTING, selectedRingtone);
				editor.commit();
				
				finish();
				
			}
			
		});
		
		System.out.println("onCreate vol = " + currVol + " vibe = " + vibeEnabled + " ringtone = " + selectedRingtone);
		
	}

	@Override
	public void onDestroy() {
		// clean up the audio playback stuff
		if (player != null) {
			if (player.isPlaying()) {
				player.stop();
			}
			player.release();
			player = null;
		}
		
		// cancel the vibration motor
		vibe.cancel();
		
		// restore old alarm stream volume
        // audioManager.setStreamVolume(AudioManager.STREAM_ALARM, oldAlarmVolume, 0);
		
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	private void playFeedback() {
		Uri tone = rm.getRingtoneUri(selectedRingtone);
		
		if (player != null) {
			if (player.isPlaying()) {
				player.stop();
			}
			player.reset();
			try {
				// start the audio playback
				startAudio(tone);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (vibeEnabled) {
			vibe.cancel();
			vibe.vibrate(Constants.VIBE_PATTERN, -1);
		} else {
			vibe.cancel();
		}
	}
	
	private void startAudio(Uri tone) throws IllegalArgumentException, SecurityException, IllegalStateException, IOException {
        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, currVol, 0);
        if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
        	player.setDataSource(getApplicationContext(), tone);
            player.setAudioStreamType(AudioManager.STREAM_ALARM);
            player.prepare();
            player.start();
        }
	}

}
