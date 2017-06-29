package com.example.mcwmedicationr;

import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ViewLog extends Activity {

	private static final int MENU_RINGTONE = 1;
	private static final int MENU_TIME = 2;
	private static final int MENU_FLARE = 3;
	Button exit, settings;
	TextView log;
	Logger logger;
	AppPreferences prefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.view_log);
		exit = (Button) findViewById(R.id.viewLogButton);
		log = (TextView) findViewById(R.id.viewLogText);
		settings = (Button) findViewById(R.id.viewLogSettingsButton);
		
		prefs = new AppPreferences(this);
		
		exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
			
		});
		
		settings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openOptionsMenu();
			}
			
		});
		
		logger = new Logger(this);
		List<String> entries = logger.loadReminderLog(MedReminderDialog.MEDREMINDLOG);
		// reverse the list so the most recent are at the top
		Collections.reverse(entries);
		
		StringBuilder sb = new StringBuilder();
		for (String entry : entries) {
			sb.append(entry);
			sb.append("\n");
		}
		log.setText(sb.toString());
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		boolean enabled = prefs.getIsEnabled();
		
		if (enabled) {
			MenuItem ring = menu.add(0, MENU_RINGTONE, 0, "Change ringtone settings");
			MenuItem time = menu.add(0, MENU_TIME, 0, "Change med reminder times");
			MenuItem flare = menu.add(0, MENU_FLARE, 0, "Change flare prompt time");
		}
		
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId())
			{
		case MENU_RINGTONE:
			intent = new Intent(this, ChangeAlarmActivity.class);
			startActivity(intent);
			finish();
			return true;
		case MENU_TIME:
			intent = new Intent(this, ChangeTimeActivity.class);
			startActivity(intent);
			finish();
			return true;
		case MENU_FLARE:
			intent = new Intent(this, ChangeFlareTimeActivity.class);
			startActivity(intent);
			finish();
			return true;
			}
		return super.onOptionsItemSelected(item);
	}


}
