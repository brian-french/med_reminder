package com.example.mcwmedicationr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class FlareQ1 extends Activity {

	Button back, next;
	ListView responses;
	
	private static final String DATAKEY = "response";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.flare_q1);
		back = (Button) findViewById(R.id.BackButton);
		next = (Button) findViewById(R.id.NextButton);
		responses = (ListView) findViewById(R.id.List);
		
		back.setEnabled(false);
		back.setVisibility(View.INVISIBLE);
		
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(FlareQ1.this, FlareQ2.class);
				i.putExtra(DATAKEY, responses.getSelectedItemPosition());
				startActivity(i);
				finish();
			}
			
		});
		String data = getIntent().getStringExtra(DATAKEY);
		if (data != null) {
			String[] parts = data.split(",");
			if (parts.length == 1) {
				int position = Integer.parseInt(parts[parts.length - 1]);
				responses.setSelection(position);
			}
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
