package com.example.mcwmedicationr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class FlareQ1 extends Activity {

	Button back, next;
	ListView responses;
	ArrayAdapter responseAdapter;
	boolean clickByMe = false;
	
	Intent inputIntent;
	
	private static final String DATAKEY = "com.example.mcwmedicationr.q1";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		inputIntent = getIntent();
		
		setContentView(R.layout.flare_q1);
		back = (Button) findViewById(R.id.BackButton);
		next = (Button) findViewById(R.id.NextButton);
		responses = (ListView) findViewById(R.id.List);
		
		initBackButton();
		
		initNextButton();
		
		initListView();
	}

	private void initBackButton() {
		back.setEnabled(false);
		back.setVisibility(View.INVISIBLE);
	}

	private void initNextButton() {
		next.setEnabled(false);
		next.setVisibility(View.INVISIBLE);
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(FlareQ1.this, FlareQ2.class);
				if (inputIntent.getExtras() != null) {
					i.putExtras(inputIntent.getExtras());
				}
				i.putExtra(DATAKEY, responses.getCheckedItemPosition());
				System.out.println(i.getIntExtra(DATAKEY, -1));
				startActivity(i);
				finish();
			}
			
		});
	}
	
	private void initListView() {
		String[] list = getResources().getStringArray(R.array.flareq1);
		//ArrayList<String> list = new ArrayList<String>();
		
		responseAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_multiple_choice, list); //android.R.layout.simple_list_item_checked, list);

		responses.setAdapter(responseAdapter);
		responses.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
				if (clickByMe) {
					clickByMe = false;
				} else {
					
					next.setVisibility(View.VISIBLE);
					next.setEnabled(true);
					
				}
			}
			
		});
		
		// restore response if needed
		int position = inputIntent.getIntExtra(DATAKEY, -1);
		if (position != -1) {
			clickPosition(position);
			next.setVisibility(View.VISIBLE);
			next.setEnabled(true);
		}
	}
	
	private void clickPosition(int position) {
		clickByMe = true;
		View last = responses.getChildAt(position);
		responses.performItemClick(last, position, position);
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
