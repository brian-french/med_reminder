package com.example.mcwmedicationr;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.Environment;
import edu.cmu.ices.EMA.Constants;

public abstract class AbstractLogger {
	
	protected abstract String formatLogEntry(Object data, String[] logHeader);
	protected abstract String formatLogHeader(Object data, String[] logHeader);
	
	protected static final String[] COMMON_HEADER = {"MODE", "STATUS", "START_DATE", "START_TIME", "END_DATE", "END_TIME", "TIMESTAMP", "DELAY", "DELAY_REASON"};
	
	protected static final String DATE_FORMAT = "%Y/%m/%d,%I:%M:%S %p";
	
	//private static final String LOG_DIR = "EMA_LOGS";
	
	public void LogEntry(String logFile, Object data, String[] logHeader, Context context, boolean receiverCall) {
		String logEntry = formatLogEntry(data, logHeader);
		String header = null;
		
		File log = getLogFilePath(logFile);
		if (log != null) {
			if (!log.exists()) {
				header = formatLogHeader(data, logHeader);
			}
			
			if (logHeader != null) {
				verifyLogHeader(logFile, data, logHeader);
			}
			
			// open the file and write it
			PrintWriter writer = null;
			//FileOutputStream writer = null;
			try {
				writer = new PrintWriter(new FileOutputStream(log,true));
				if (header != null) {
					//writer.write(header.getBytes());
					writer.println(header);
				}
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
			
			// added to fix the problem a newly created files not being visible via usb
			if (!receiverCall) {
				// have to gate this since the boot receiver logging with crash if it makes this call
				MediaScannerConnection.scanFile(context, new String[] { log.getAbsolutePath() }, null, null);
			}

		} else {
			Log.e("LOGGING", "Error: failed to open file: " + logFile);
			throw new RuntimeException("Error: failed to open file: " + logFile + "! Please call for assistance");
		}
	}
	
	private List<String> verifyLogHeader(String logFileName, Object data, String[] logHeader) {
		File log = getLogFilePath(logFileName);
		List<String> lines = null;
		try {
		    BufferedReader br = new BufferedReader(new FileReader(log));

		    String[] existingHeader = br.readLine().trim().split(",");
//		    System.out.println(existingHeader.length);
//		    for (String head: existingHeader) {
//		    	System.out.println(head);
//		    }
		    System.out.println("checking header lengths");
//		    for (String header : logHeader) {
//		    	System.out.println(header);
//		    }
		    String formattedHeader = formatLogHeader(data, logHeader);
		    String[] newHeaderSplit = formattedHeader.split(",");
		    if (existingHeader.length < newHeaderSplit.length) {
		    	System.out.println("existing header < new header");
		    	// need to regenerate the file with the full header
		    	String newHeader = formatLogHeader(data, logHeader);
		    	String line;
				lines = new ArrayList<String>();
				lines.add(newHeader);
				while ((line = br.readLine()) != null) {
					lines.add(line);
				}
				br.close();
				br = null;
				
				// replace the 
		    	fixLogHeader(log, lines);
		    }

		}
		catch (IOException e) {
		    //You'll need to add proper error handling here
		}
		
		return lines;
	}
	
	private void fixLogHeader(File file, List<String> lines) {
		PrintWriter writer = null;
		File newFile = null;
		if (file.exists()) {
			// create a new temp file to write into so we can't accidentally bork the old file

			newFile = getLogFilePath(file.getName() + ".tmp");

		}
		if (newFile != null) {
			try {
				writer = new PrintWriter(new FileOutputStream(newFile, false));
	
				for (String line: lines) {
					writer.println(line);
				}
				writer.flush();
				writer.close();
				writer = null;
				
				// rename the newFile to overwrite the old file
				newFile.renameTo(file);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				Log.e("LOGGING", e.getMessage());
				// make this error visible to the user so they will call in!!!!
				throw new RuntimeException("ERROR: failed to write interview data to log! Please call for assistance");
			}
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
