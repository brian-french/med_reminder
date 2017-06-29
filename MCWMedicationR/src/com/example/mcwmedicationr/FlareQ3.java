package com.example.mcwmedicationr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class FlareQ3 extends Activity {

	Button back, next;
	SeekBar seekbar;
	TextView seekBarValue;
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
		seekBarValue = (TextView) findViewById(R.id.txvSeekBarValue);
		
		initBackButton();
		
		initNextButton();
		
		initSeekBar();
	}

	private void initBackButton() {
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
	}

	private void initNextButton() {
		next.setEnabled(false);
		next.setVisibility(View.INVISIBLE);
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
	}
	
	private void initSeekBar() {
		seekbar.addOnLayoutChangeListener(new OnLayoutChangeListener() {

			@Override
			public void onLayoutChange(View v, int left, int top, int right,
					int bottom, int oldLeft, int oldTop, int oldRight,
					int oldBottom) {
				seekbar.removeOnLayoutChangeListener(this);
				showSeekValue();
			}
			
		});
		
		seekbar.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

					showSeekValue();
		        	next.setVisibility(View.VISIBLE);
		        	next.setEnabled(true);

				return false;
			}
			
		});
		
		int position = inputIntent.getIntExtra(DATAKEY, -1);
		if (position != -1) {
			seekbar.setProgress(position);
			next.setVisibility(View.VISIBLE);
        	next.setEnabled(true);
		}
		
	}

	// seek bar current value update
    private void showSeekValue()
    {
    	String value = Integer.toString(seekbar.getProgress());
    	if (value.length() == 1) {
    		value = " " + value;
    	}
    	seekBarValue.setText(value);
        int xPos = (((seekbar.getRight() - seekbar.getLeft()) * seekbar.getProgress()) / seekbar.getMax());
        System.out.println("seekbar " + seekbar.getLeft() + " " + xPos + " " + seekbar.getRight());
        xPos *= .85;
        //if (xPos < 10) xPos = 10;
        xPos += 70;
    	seekBarValue.setPadding(xPos,20,0,0);
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
