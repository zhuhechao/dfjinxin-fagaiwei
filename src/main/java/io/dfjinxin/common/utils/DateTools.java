package io.dfjinxin.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTools {

	public static String toString(Date date, String format) {
		try {
			return new SimpleDateFormat(format).format(date);
		} catch (Exception e) {
		}
		return null;
	}

	public static String dateToStrByForm(Date date, String form){
		SimpleDateFormat formatter = new SimpleDateFormat(form);
		String dateString = formatter.format(date);
		return dateString;
	}

	public static String toStrByForm(Date date, String form){
		SimpleDateFormat formatter = new SimpleDateFormat(form);
		String dateString = formatter.format(date);
		return dateString;
	}

	public static Date toDateByForm(String dateStr, String form){
		DateFormat formatter = new SimpleDateFormat(form);
		Date date = null;
		try {
			date = formatter.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static Date getNowDate() {
		String dateStr = toStrByForm(new Date(), "yyyy-MM-dd HH:mm:ss");
		return toDateByForm(dateStr, "yyyy-MM-dd HH:mm:ss");
	}
}
