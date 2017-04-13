package com.example.mcwmedicationr;


import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

public class MedReminderConfig extends Activity {
	
	Switch enable;
	Button next;
	boolean enabled ;
	AppPreferences prefs;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.med_reminder_config);
		enable = (Switch) findViewById(R.id.switch1);
		next = (Button) findViewById(R.id.button1);
		
		prefs = new AppPreferences(this);
		enabled = prefs.getIsEnabled();
		enable.setChecked(enabled);

		initButtons();
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	
	private void initButtons() {
		enable.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				enabled = isChecked;
			}
			
		});
		
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				prefs.setIsEnabled(enabled);
				if (enabled) {
					Intent i = new Intent(getApplicationContext(), ChangeTimeActivity.class);
					i.putExtra("init", true);
					startActivity(i);
				}
				finish();
			}
			
		});
	}

}
