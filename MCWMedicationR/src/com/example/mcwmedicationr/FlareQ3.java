package com.example.mcwmedicationr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;

public class FlareQ3 extends Activity {

	Button back, next;
	SeekBar seekbar;
	Intent inputIntent;
	
	private static final String DATAKEY = "com.example.mcwmedicationr.q3";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		inputIntent = getIntent();
		
		setContentView(R.layout.flare_q3);
		back = (Button) findViewById(R.id.BackButton);
		next = (Button) findViewById(R.id.NextButton);
		seekbar = (SeekBar) findViewById(R.id.seekBar);
		
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(FlareQ3.this, FlareQ2.class);
				i.putExtras(inputIntent.getExtras());
				i.putExtra(DATAKEY, seekbar.getProgress());
				startActivity(i);
				finish();
			}
			
		});
		
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(FlareQ3.this, FlareQ4.class);
				i.putExtras(inputIntent.getExtras());
				i.putExtra(DATAKEY, seekbar.getProgress());
				startActivity(i);
				finish();
			}
			
		});
		
		int position = inputIntent.getIntExtra(DATAKEY, -1);
		if (position != -1) {
			seekbar.setProgress(position);
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
}
