package bots.ThemanuBot;

import java.io.File;

import bots.ThemanuBot.componets.AllComponents;
import bridgempp.bot.wrapper.Bot;
import bridgempp.bot.wrapper.Message;

public class ThemanuBot extends Bot {
	public static final String DIRECTORY = null;
	public int status = 0;

	@Override
	public void initializeBot() {
		//load log
		MyLogger.load();
		//load protocols
		File file1 = new File(DIRECTORY + "protocols1.csv");
		File file2 = new File(DIRECTORY + "protocols2.csv");
		try {
			CsvReader.getInstance().extract(file1, file2);
		} catch (NumberFormatException e) {
			MyLogger.logCritical("File \"" + (CsvReader.getInstance().isLastModified(file1, file2) ? file1.getName() : file2.getName()) + "\" is corrupt");
		} catch (NullPointerException e2) {
			MyLogger.logCritical("Could not load protocols. May \"" + file1.getName() + "\" or \"" + file2.getName() + "\" does not exist");
		}
		//load components
		ComponentSwitch.getInstance().setComponents(new AllComponents().getComponents());
		
		
	}

	@Override
	public void messageReceived(Message message) {
		// TODO Auto-generated method stub
		
	}
	
	public static void stop () {
		//TODO
	}

}
