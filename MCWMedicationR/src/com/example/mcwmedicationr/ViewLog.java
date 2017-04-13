package com.example.mcwmedicationr;

import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ViewLog extends Activity {

	Button exit;
	TextView log;
	Logger logger;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.view_log);
		exit = (Button) findViewById(R.id.viewLogButton);
		log = (TextView) findViewById(R.id.viewLogText);
		
		exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
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

}
