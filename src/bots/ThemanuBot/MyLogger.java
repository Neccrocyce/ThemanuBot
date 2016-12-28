package bots.ThemanuBot;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MyLogger {
	private static List<String[]> log = new ArrayList<>();
	private static int maxSize = 10000;
	private static String file = "themanu.log";
	
	
	public static void logInfo (String msg) {
		log.add(new String[] {"INFO", msg});
		reduceLog();
		save();
	}
	
	public static void logWarning (String msg) {
		log.add(new String[] {"WARNING", msg});
		reduceLog();
		save();
	}
	
	public static void logError (String msg) {
		log.add(new String[] {"ERROR", msg});
		reduceLog();
		save();
	}
	
	public static void logCritical (String msg) {
		log.add(new String[] {"CRITICAL", msg});
		reduceLog();
		save();
	}
	
	private static void reduceLog () {
		while (log.size() > maxSize) {
			log.remove(0);
		}
	}
	
	public static String getLogAll () {
		return getLog(true, true, true, true);
	}
	
	public static String getLog (boolean info, boolean warning, boolean error, boolean critical) {
		String content = "";
		for (String[] strings : log) {
			switch (strings[0]) {
			case "INFO":
				if (info) {
					content += strings[0] + ": " + strings[1] + "\n";
				}
				break;
			case "WARNING":
				if (warning) {
					content += strings[0] + ": " + strings[1] + "\n";
				}
				break;
			case "ERROR":
				if (error) {
					content += strings[0] + ": " + strings[1] + "\n";
				}
				break;
			case "CRITICAL":
				if (critical) {
					content += strings[0] + ": " + strings[1] + "\n";
				}
				break;
			}
		}
		return content;
	}
	
	private static void save () {
		FileWriter w = null;
		try {
			w = new FileWriter(ThemanuBot.DIRECTORY + file);
			String content = getLogAll();
			w.write(content, 0, content.length());
			w.close();
		} catch (IOException e) {
			//TODO
		} 
	}
	
	public static void load () {
		if (!new File(file).exists()) {
			return;
		}
		String in = "";
		FileReader r = null;
		Scanner s = null;
		try {
			r = new FileReader(file);
			s = new Scanner(r);
			s.useDelimiter("\\Z");
			in = s.next();
			s.close();
			
		} catch (IOException e) {
			//TODO
		}
		
		//split each line
		String obj[] = in.split(in.contains("\r") ? "\r\n" : "\n");
		
		for (String string : obj) {
			String level = string.split(": ")[0];
			log.add(new String[] {level, string.substring(level.length())});
		}
	}
}
