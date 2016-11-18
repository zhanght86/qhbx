package com.aeon.bi.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

public class Util {
	
	public static boolean isNull(String value) {
		return (value == null || value == "");
	}
	
	public static String formatDate(Date date, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
	}
	
	public static String getYesterdayStr(String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		return dateFormat.format(cal.getTime());
	}
	
	public static String getMessage(String key) {
		ResourceBundle bundle = ResourceBundle.getBundle("language");
		if (bundle!= null && bundle.containsKey(key)) {
			return bundle.getString(key);
		}
		return "";
	}
}