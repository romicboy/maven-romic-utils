package com.romic.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Loger {
	
	private static Date date;
	private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static String time;
	
	public static void info(String domian, String message){
		Loger.date = new Date();
		Loger.time = Loger.df.format(Loger.date);
		System.out.println(Loger.time + " " + domian + " " + message);
	}
	
	public static void info(String message){
		Loger.date = new Date();
		Loger.time = Loger.df.format(Loger.date);
		System.out.println(Loger.time + " " + message);
	}
}
