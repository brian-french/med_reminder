package com.example.mcwmedicationr;


import android.text.format.Time;
import edu.cmu.ices.EMA.Constants;
import edu.cmu.ices.EMA.interview.AbstractContent;
import edu.cmu.ices.EMA.interview.InterviewData;

public class CSVLogger extends AbstractLogger {

	@Override
	protected String formatLogEntry(Object d, String[] contentHeader) {
		InterviewData data = (InterviewData)d;
		StringBuilder sb = new StringBuilder();
		
		sb.append(data.getMode()).append(",");
		int status = data.getStatus();
		String stat = "";
		switch(status) {
		case Constants.INTERVIEW_COMPLETE:
			stat = "completed";
			break;
		case Constants.INTERVIEW_ABANDONED:
			stat = "abandoned";
			break;
		case Constants.INTERVIEW_MISSED:
			stat = "missed";
			break;
		case Constants.INTERVIEW_CANCELLED:
			stat = "cancelled";
			break;
		}
		sb.append(stat).append(",");
		Time t = new Time();
		t.set(data.getStartTime());
		sb.append(t.format(DATE_FORMAT)).append(",");
		t.set(data.getEndTime());
		sb.append(t.format(DATE_FORMAT)).append(",");
		sb.append(data.getStartTime()).append(",");
		long delay = data.getDelayStop() - data.getDelayStart();
		sb.append(delay).append(",");
		sb.append(data.getDelayReason()).append(",");
		
		// print the content specific portion of the log
		sb.append(data.getResponseByKey(contentHeader[0])).append(",").append(data.getResponseTimeByKey(contentHeader[0]));
		for (int i=1; i<contentHeader.length; i++) {
			//sb.append(",").append(data.getItemNumber(i));
			sb.append(",").append(data.getResponseByKey(contentHeader[i]));
			sb.append(",").append(data.getResponseTimeByKey(contentHeader[i]));
		}
//		sb.append(data.getItemNumber(0)).append(",").append(data.getResponse(0)).append(",").append(data.getResponseTime(0));
//		for (int i=1; i<data.getNumberResponses(); i++) {
//			sb.append(",").append(data.getItemNumber(i));
//			sb.append(",").append(data.getResponse(i));
//			sb.append(",").append(data.getResponseTime(i));
//		}
		//sb.append("\r\n");
		
		// log this to the debug log
		String prompt, start, end;
		t.set(data.getPromptTime());
		prompt = t.format(DATE_FORMAT);
		t.set(data.getStartTime());
		start = t.format(DATE_FORMAT);
		t.set(data.getEndTime());
		end = t.format(DATE_FORMAT);
		Log.w("CSVLogger formatEntry", "prompt = " + prompt + ", start = " + start + ", end = " + end);
		String enc = LogEncryption.encryptIt(sb.toString());
		if (enc.contains("\n")) Log.e("CSVLogger encrypt", "Encrypted string contains newlines!!!!");
		return LogEncryption.encryptIt(sb.toString());
	}

	@Override
	protected String formatLogHeader(Object d, String[] contentHeader) {
		InterviewData data = (InterviewData)d;
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<COMMON_HEADER.length; i++) {
			sb.append(COMMON_HEADER[i]).append(",");
		}
		sb.append(contentHeader[0]).append(",").append(contentHeader[0]).append("_TIME");
		for (int i=1; i<contentHeader.length; i++) {
			sb.append(",").append(contentHeader[i]);
			sb.append(",").append(contentHeader[i]).append("_TIME");
		}
//		sb.append("I0").append(",").append("Q0").append(",").append("T0");
//		for (int i=1; i<data.getNumberResponses(); i++) {
//			sb.append(",").append("I").append(i);
//			sb.append(",").append("Q").append(i);
//			sb.append(",").append("T").append(i);
//		}
		//sb.append("\r\n");
		
		return LogEncryption.encryptIt(sb.toString());
	}

}
