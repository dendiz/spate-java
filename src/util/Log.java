package util;

import java.util.Date;

import core.Botpit;

public class Log {
	public static void info(String msg) {
		log("INFO", msg);
	}
	public static void debug(String msg){
		log("INFO", msg);
	}
	private static void log(String level, String msg) {
		if (Botpit.LOG)
			System.out.printf("[BOTPIT] %s %s %s\n",new Date(), level, msg);
	}
}
