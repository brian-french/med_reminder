package com.example.mcwmedicationr;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class FlareQ4 extends Activity {

	Button back, next;
	Spinner[] symptoms = new Spinner[6];
	EditText other;
	Intent inputIntent;
	private static final String[] keys = {"com.example.mcwmedicationr.q1", "com.example.mcwmedicationr.q2", "com.example.mcwmedicationr.q3" };
	
	private static final String[] RANKKEYS = {"com.example.mcwmedicationr.symptom1", "com.example.mcwmedicationr.symptom2",
		"com.example.mcwmedicationr.symptom3", "com.example.mcwmedicationr.symptom4", "com.example.mcwmedicationr.symptom5",
		"com.example.mcwmedicationr.symptom6"};
	private static final String OTHERKEY = "com.example.mcwmedicationr.other";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		inputIntent = getIntent();
		
		setContentView(R.layout.flare_q4);
		back = (Button) findViewById(R.id.BackButton);
		next = (Button) findViewById(R.id.NextButton);
		symptoms[0] = (Spinner) findViewById(R.id.symptomRank1);
		symptoms[1] = (Spinner) findViewById(R.id.symptomRank2);
		symptoms[2] = (Spinner) findViewById(R.id.symptomRank3);
		symptoms[3] = (Spinner) findViewById(R.id.symptomRank4);
		symptoms[4] = (Spinner) findViewById(R.id.symptomRank5);
		symptoms[5] = (Spinner) findViewById(R.id.symptomRank6);
		other = (EditText) findViewById(R.id.symptomSpecify6);
		
		System.out.println(getIntent().getExtras().toString());
		
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(FlareQ4.this, FlareQ3.class);
				i.putExtras(inputIntent.getExtras());
				for (int j=0; j < symptoms.length; j++) {
					i.putExtra(RANKKEYS[j], symptoms[j].getSelectedItemPosition());
				}
				i.putExtra(OTHERKEY, other.getText().toString());
				startActivity(i);
				finish();
			}
			
		});
		
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (validateData()) {
					StringBuilder sb = new StringBuilder();
					for (String key: keys) {
						sb.append(inputIntent.getIntExtra(key, -1));
						sb.append(",");
					}
					for (int i=0; i<symptoms.length; i++) {
						sb.append(symptoms[i].getSelectedItemPosition() + 1);
						sb.append(",");
					}
					sb.append(other.getText().toString());
					String data = sb.toString();
					
					// log data to file
					Logger logger = new Logger(FlareQ4.this);
			 		logger.LogEntry(DailyFlareDialog.DAILYFLARELOG, System.currentTimeMillis(), data);
					
					finish();
				}
			}
			
		});
		
		if (inputIntent.hasExtra(OTHERKEY)) {
			for (int j=0; j < symptoms.length; j++) {
				symptoms[j].setSelection(inputIntent.getIntExtra(RANKKEYS[j], 0));
			}
			other.setText(inputIntent.getStringExtra(OTHERKEY));
		}
		
	}

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
	
	private boolean validateData() {
		boolean valid = true;
		ArrayList<Integer> values = new ArrayList<Integer>();
		for (int i=0; i<symptoms.length; i++) {
			values.add(symptoms[i].getSelectedItemPosition());
		}
		Set<Integer> unique = new HashSet<Integer>();
		unique.addAll(values);
		
		if (unique.size() != symptoms.length) {
			// there are duplicate ranks
			valid = false;
			Toast.makeText(this, "There are duplicate values in your ranking. Each symptom must have a unique rank value.", Toast.LENGTH_LONG).show();
		}
		
		if (values.get(5) != 5 & other.getText().toString().equals("")) {
			valid = false;
			Toast.makeText(this, "Please specify the other symptom that you ranked.", Toast.LENGTH_SHORT).show();
		}
		return valid;
	}
}
