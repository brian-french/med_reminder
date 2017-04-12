package com.example.mcwmedicationr;


import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class IDSetup extends Activity {
	
	TextView text;//, reenterText;
	EditText edit;//, reenter;
	ListView list;
	Button cancel;
	Button save;
	Button erase;
	Toast t;
	TimePicker time;
	
	private static final int ID_STATE = 0;
	private static final int MENSTR_STATE = 1;
	private static final int TIME_STATE = 2;
	protected int state = ID_STATE;
	protected boolean changeSched = false;
	String id = "";
	int menstr = 0;
	String[] listItems = { "No", "Yes" };
	
	SharedPreferences prefs;
	Editor editor;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.id_setup_layout);
		text = (TextView)findViewById(R.id.IDSetupText);
		//reenterText = (TextView)findViewById(R.id.IDSetupTextReenter);
		edit = (EditText)findViewById(R.id.IDSetupEdit);
		list = (ListView)findViewById(R.id.IDSetupList);
		list.setAdapter(new ArrayAdapter<String>(this,  android.R.layout.simple_list_item_multiple_choice, listItems));
		list.setVisibility(View.INVISIBLE);
		//reenter = (EditText)findViewById(R.id.IDSetupReenter);
		cancel = (Button)findViewById(R.id.IDSetupCancelButton);
		save = (Button)findViewById(R.id.IDSetupSaveButton);
		erase = (Button)findViewById(R.id.IDSetupClearButton);
		time = (TimePicker)findViewById(R.id.IDSetupTimePicker);
		
		prefs = getSharedPreferences(Constants.EMA_PREFS, 0);
		editor = prefs.edit();

		initButtons();
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	
	private void initButtons() {
		time.setVisibility(View.INVISIBLE);
		
		cancel.setVisibility(View.VISIBLE);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// clean up and exit
				if (t != null) {
					t.cancel();
				}
				finish();
			}
			
		});
		
		save.setVisibility(View.VISIBLE);
		if (state == ID_STATE) {
			save.setText("Set ID");
		} else {
			save.setText("Set Schedule");
			text.setText("Touch the text field below to enter a participant schedule: ");
		}
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String idValue = null;
				if (state == ID_STATE) {
					idValue = edit.getText().toString();
					if (!idValue.equalsIgnoreCase("")) {
						//check that config folder for this id exists
						File configFile = new File(Environment.getExternalStorageDirectory()
				                .getAbsolutePath(), idValue + "_config");
						if (true) { //(configFile.exists()) {
							
							id = idValue;
							save.setText("Save");
							text.setText("Does participant currently having periods?");
							edit.setVisibility(View.INVISIBLE);
							list.setVisibility(View.VISIBLE);
							state = MENSTR_STATE;
							
						} else {
							t = Toast.makeText(getBaseContext(), "Error: configuration files for participant ID: " + idValue + " not found.", Toast.LENGTH_LONG);
							t.show();
						}
						
					} else {
						t = Toast.makeText(getBaseContext(), "You forgot to enter a participant ID.", Toast.LENGTH_SHORT);
						t.show();
					}
				}
				else if (state == MENSTR_STATE) {
					int selection = list.getCheckedItemPosition();
					if (selection != ListView.INVALID_POSITION) {
						menstr = selection;
						System.out.println(menstr);
						
						// initial config, set the reminder alarm time
						edit.setVisibility(View.INVISIBLE);
						list.setVisibility(View.INVISIBLE);
						save.setText("Set Daily Reminder Time");
						text.setText("Enter time for end of day reminder alarm.");
						time.setVisibility(View.VISIBLE);
						time.setCurrentHour(18);
						time.setCurrentMinute(0);
						state = TIME_STATE;
						
					} else {
						t = Toast.makeText(getBaseContext(), "You forgot to answer the question.", Toast.LENGTH_SHORT);
						t.show();
					}
				}
//				else if (state == SCHED_STATE) {
//					idValue = edit.getText().toString();
//					String validate = reenter.getText().toString();
//					if (!idValue.equalsIgnoreCase(validate)) {
//						t = Toast.makeText(getBaseContext(), "The two schedule strings do NOT match. Please check them", Toast.LENGTH_SHORT);
//						t.show();
//						return;
//					}
//					if (!idValue.equalsIgnoreCase("")) {
//						sched = idValue;
//						boolean sched_ok = verifySchedule(sched);
//						if (sched_ok) {
//							if (changeSched) {
//								//clearConfig();
//								saveConfig(id, sched, -1, -1, -1, -1);
//							
//								// cleanup
//								if (t != null) {
//									t.cancel();
//								}
//								finish();
//							} else {
//								// initial config, set the bod alarm time
//								edit.setVisibility(View.INVISIBLE);
//								save.setText("Set BOD Time");
//								text.setText("Enter time for beginning of day alarm.");
//								time.setVisibility(View.VISIBLE);
//								time.setCurrentHour(7);
//								time.setCurrentMinute(30);
//								state = TIME_STATE;
//								reenterText.setVisibility(View.INVISIBLE);
//								reenter.setEnabled(false);
//								reenter.setVisibility(View.INVISIBLE);
//							}
//						} else {
//							t = Toast.makeText(getBaseContext(), "Schedule format incorrect", Toast.LENGTH_SHORT);
//							t.show();
//						}
//					} else {
//						t = Toast.makeText(getBaseContext(), "You forgot to enter a participant schedule.", Toast.LENGTH_SHORT);
//						t.show();
//					}
//				}
				else { //if (state == TIME_STATE) {
					time.clearFocus(); // hack to make sure the time is updated if the keyboard was used
					int hour = time.getCurrentHour();
					int minute = time.getCurrentMinute();
					Log.d("IDSETUP REMINDER TIME", "hour = " + hour + " minute = " + minute);
					
					clearConfig();
					saveConfig(id, menstr, hour, minute);
					
					// schedule the daily reminder to do a collection this week?
					// reinitialize the smartScheduler so the config is reloaded
//					smartScheduler = new SmartScheduler(getApplicationContext());
//					smartScheduler.scheduleEODReminder(false);
					
					// start the alarm type and volume setting activity
					Intent intent = new Intent(getBaseContext(), ChangeAlarmActivity.class);
		        	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		        	startActivity(intent);
					
					finish();
				}
//				} else if (state == EOD_TIME_STATE) {
//					time.clearFocus();
//					int hour = time.getCurrentHour();
//					int min = time.getCurrentMinute();
//					clearConfig();
//					saveConfig(id, sched, bodHour, bodMin, hour, min);
//					SmartScheduler scheduler = new SmartScheduler(getApplicationContext());
//					scheduler.scheduleEODReminder(hour, min, false);
//					scheduler.scheduleBOD(bodHour, bodMin);
//					scheduler.rescheduleHourlyReminder();
//					if (t != null) {
//						t.cancel();
//					}
//					finish();
//				}
				
//				String idValue = edit.getText().toString();
//				if (!idValue.equalsIgnoreCase("")) {
//					
//					if (state == ID_STATE) {
//						id = idValue;
//						edit.setText("");
//						save.setText("Set Schedule");
//						text.setText("Touch the text field below to enter a participant schedule: ");
//						state = SCHED_STATE;
//					} else {
//						sched = idValue;
//						boolean sched_ok = verifySchedule(sched);
//						if (sched_ok) {
//							clearConfig();
//							saveConfig(id, sched);
//						
//							// cleanup
//							if (t != null) {
//								t.cancel();
//							}
//							finish();
//						} else {
//							t = Toast.makeText(getBaseContext(), "Schedule format incorrect", Toast.LENGTH_SHORT);
//							t.show();
//						}
//					}
//				} else {
//					if (state == ID_STATE) {
//						t = Toast.makeText(getBaseContext(), "You forgot to enter a participant ID.", Toast.LENGTH_SHORT);
//					} else {
//						t = Toast.makeText(getBaseContext(), "You forgot to enter a participant schedule.", Toast.LENGTH_SHORT);
//					}
//					t.show();
//				}
				
			}
			
		});
		
		erase.setVisibility(View.INVISIBLE);
		erase.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clearConfig();
				finish();
			}
			
		});

	}
	
	private void clearConfig() {
		editor.clear();
		editor.commit();
		File file;
//		file = Constants.getConfigFile(Constants.CONFIGFILE);
//		if (file.exists()) {
//			file.delete();
//		}
		file = Constants.getConfigFile(Constants.DEBUGLOG);
		if (file.exists()) {
			file.delete();
		}
		file = Constants.getConfigFile(Constants.WARNINGLOG);
		if (file.exists()) {
			file.delete();
		}
		file = Constants.getConfigFile(Constants.ERRORLOG);
		if (file.exists()) {
			file.delete();
		}
		
		// clear any extant alarms that have been scheduled
		AlarmScheduler as = new AlarmScheduler(getApplicationContext());
		as.cancelAlarm(Constants.ALARM_ACTIVITY_RANDOM, Constants.ALARM_RANDOM_RQ_CODE);
		as.cancelAlarm(Constants.ALARM_ACTIVITY_BOD, Constants.ALARM_BOD_RQ_CODE);
		as.cancelAlarm(Constants.ALARM_ACTIVITY_DELAYED, Constants.ALARM_DELAYED_RQ_CODE);
	}
	
	protected void saveConfig(String id, int menstr, int hour, int minute) {
		Time t = new Time();
		t.setToNow();
		long scheduleStartDateMillis;
		if (!Constants.DEBUG) {
			scheduleStartDateMillis = t.toMillis(false) + Constants.DAY_MILLIS;
		} else {
			scheduleStartDateMillis = t.toMillis(false);
		}
		editor.putString("id", id);
		editor.putInt("menstr", menstr);
		editor.putInt("remindHour", hour);
		editor.putInt("remindMinute", minute);
		//editor.putString("schedule", sched);
		editor.putInt("dynamicAppCount", 0);
		editor.putString("dynamicApp_0", "BOD Interview");
		editor.putLong("scheduleStartDateMillis", scheduleStartDateMillis);
		
		editor.commit();
		Intent intent = new Intent();
		intent.setAction("com.example.android.home");
		String[] appArray = {"EOD Interview"};
		intent.putExtra("dynamicApps", appArray);
		intent.putExtra("id", id);
		intent.putExtra("timestamp", System.currentTimeMillis());
		sendBroadcast(intent);
		
		// save the alarm volume and ringtone settings in the appPref object from lora's service
		AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		AppPreferences appPref = new AppPreferences(getApplicationContext());
		appPref.setVolumeSetting(audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM));
		appPref.setRingtonSetting(5); // I think that's the best alarm ringtone
//		File setupfile = Constants.getConfigFile(Constants.IDCONFIGFILE);
//		BufferedWriter writer = null;
//		try {
//
//			writer = new BufferedWriter(new FileWriter(setupfile));
//			
//			writer.write("id="+id);
//			writer.newLine();
//			writer.write("schedule="+sched);
//			writer.newLine();
//			
//			writer.flush();
//			writer.close();
//			
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			if (writer != null) {
//				try {
//					writer.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
	}
	
	/*
	 * This method verifies the format of the schedule string using a regex match
	 * 
	 * The format should be as below:
	 * 1. The string contains only digits and hyphens
	 * 2. at the start of the string is an optional digit
	 * 3. after every digit is at least one hyphen
	 * 4. the string can end with either a hyphen or a digit
	 */
	private boolean verifySchedule(String sched) {
		boolean ok = true;
		
		ok = sched.matches("^(\\d)?((-)+\\d)*(-)*$");
		return ok;
		
	}

}
