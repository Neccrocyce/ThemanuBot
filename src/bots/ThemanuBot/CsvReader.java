package bots.ThemanuBot;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class CsvReader {
	private static CsvReader instance = null;
	private String[][] protocols = new String[256][2];
	private int[][] parameters = new int[256][6];
	private String[] l4protocols = new String[256];
	
	
	private CsvReader () {
	}
	
	public static CsvReader getInstance () {
		if (instance == null) {
			instance = new CsvReader();
		}
		return instance;
	}
	
	/**
	 * 
	 * @param file
	 * @return content of file as String or null if some error occurred
	 */
	private String read (File file) {
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
			e.printStackTrace();
			return null;
		}
		return in;
	}
	
	private boolean isLastModified (File newFile, File oldFile) {
		if (oldFile.exists() && newFile.exists()) {
			return newFile.lastModified() > oldFile.lastModified();
		}
		return false;
	}
	
	/**
	 * 
	 * @param file
	 * @throws NumberFormatException if protocols.csv is corrupt
	 */
	public void extract(File csv1, File csv2) throws NumberFormatException {
		String content;
		if(isLastModified(csv1, csv2)) {
			content = read(csv1);
		}
		else {
			content = read(csv2);
		}
		
		//replaces "," by ";"
		boolean escaped = false;
		for (int i = 0; i < content.length(); i++) {
			if (content.charAt(i) == '"') {
				escaped = !escaped;
				content = content.substring(0,i) + content.substring(i + 1);
				i--;
			}
			else if (content.charAt(i) == ',' && !escaped) {
				content = content.substring(0, i) + ";" + content.substring(i + 1);
			}
		}
		
		//split each line
		String obj[] = content.split(content.contains("\r") ? "\r\n" : "\n");
		
		//
		for (int i = 0; i < 256; i++) {
			String val[] = obj[i + 26].split(";");
			if (val[1].toLowerCase().equals("reserved") || val[1].equals("")) {
				continue;
			}
			//copy component, identifier
			System.arraycopy(val, 1, protocols[i], 0, 2);
			//copy L4-Protocol
			l4protocols[i] = val[3];
			//copy Booleans, ..., Strings
			for (int j = 0; j < 6; j++) {
				parameters[i][j] = Integer.parseInt(val[j + 4]);
			}
		}
	}
	
	public String[][] getProtocols() {
		return protocols;
	}
	
	public int[][] getParameters() {
		return parameters;
	}
	
	public String[] getL4protocols() {
		return l4protocols;
	}
}
