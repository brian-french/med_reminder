package com.example.mcwmedicationr;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

public class Logger {
	
	private Context context;
	protected static final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss a";
	
	//private static final String LOG_DIR = "EMA_LOGS";
	public Logger(Context context) {
		this.context = context;
	}
	
	public void LogEntry(String logFile, long time, String data) {
		
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        Date netDate = (new Date(time));
        String logEntry = sdf.format(netDate);
        if (data != null) {
        	logEntry += "," + data;
        }
        logEntry = LogEncryption.encryptIt(logEntry);
		
		File log = getLogFilePath(logFile);
		if (log != null) {
			
			if (!log.exists() && data != null) {
				// write a header
				writeHeader(log);
			}

			// open the file and write it
			PrintWriter writer = null;
			//FileOutputStream writer = null;
			try {
				writer = new PrintWriter(new FileOutputStream(log,true));
				//writer.write(logEntry.getBytes());
				writer.println(logEntry);
				writer.flush();
				writer.close();
				writer = null;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				Log.e("LOGGING", e.getMessage());
				// make this error visible to the user so they will call in!!!!
				throw new RuntimeException("ERROR: failed to write interview data to log! Please call for assistance");
			}
			if (writer != null) {
				writer.close();
			}
			
			// have to scan the new file to make it show up on linux/mac
			Intent mediaScannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			Uri fileContentUri = Uri.fromFile(log); 
			mediaScannerIntent.setData(fileContentUri);
			context.sendBroadcast(mediaScannerIntent); 

		} else {
			Log.e("LOGGING", "Error: failed to open file: " + logFile);
			throw new RuntimeException("Error: failed to open file: " + logFile + "! Please call for assistance");
		}
	}
	
	public List<String> loadReminderLog(String logFileName) {
		File log = getLogFilePath(logFileName);
		List<String> lines = new ArrayList<String>();
		
		if (log.exists()) {
			try {
			    BufferedReader br = new BufferedReader(new FileReader(log));
	
			    String line = br.readLine();
			    while( line != null) {
			    	lines.add(line.trim());
			    	line = br.readLine();
			    }
			    
			    br.close();
			}
			catch (IOException e) {
			    //You'll need to add proper error handling here
			}
		}
		
		return lines;
	}
	
	private void writeHeader(File log) {
		String header = "TIME,FLARE_LENGTH,HOW_BAD,HOW_INTERFERE,PELVIC_PAIN,PAIN_ELSEWHERE,URGENCY_FREQ,FATIGUE,DISABILITY,OTHER,OTHER_SPECIFY";
		header = LogEncryption.encryptIt(header);
		PrintWriter writer = null;
		//FileOutputStream writer = null;
		try {
			writer = new PrintWriter(new FileOutputStream(log,true));
			//writer.write(logEntry.getBytes());
			writer.println(header);
			writer.flush();
			writer.close();
			writer = null;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Log.e("LOGGING", e.getMessage());
			// make this error visible to the user so they will call in!!!!
			throw new RuntimeException("ERROR: failed to write interview data to log! Please call for assistance");
		}
		if (writer != null) {
			writer.close();
		}
	}
	
	private File getLogFilePath(String logFileName) {
		
		File file = null;
		File root = Environment.getExternalStorageDirectory();
		if (root.canWrite()) {
			File dir = new File(root, Constants.EMADIR);
			if (!dir.exists()) {
				dir.mkdir();
			}
			dir = new File(dir, Constants.LOGDIR);
			if (!dir.exists())
				dir.mkdir();
			
			//new file
			file = new File(dir, logFileName);

		}
		return file;
		
	}

}
