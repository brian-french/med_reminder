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
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.Log;

public class Logger {
	
	private Context context;
	protected static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	
	//private static final String LOG_DIR = "EMA_LOGS";
	public Logger(Context context) {
		this.context = context;
	}
	
	public void LogEntry(String logFile, long time) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        Date netDate = (new Date(time));
        String logEntry = sdf.format(netDate);
		
		File log = getLogFilePath(logFile);
		if (log != null) {

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
			// have to gate this since the boot receiver logging with crash if it makes this call
			MediaScannerConnection.scanFile(context, new String[] { log.getAbsolutePath() }, null, null);

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
	
			    String line = br.readLine().trim();
			    while( line != null) {
			    	lines.add(line);
			    	line = br.readLine().trim();
			    }
			    
			    br.close();
			}
			catch (IOException e) {
			    //You'll need to add proper error handling here
			}
		}
		
		return lines;
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
