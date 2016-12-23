package bridgempp.bot.wrapper;

import java.util.Scanner;

import bridgempp.CalendarBot;
import bridgempp.bot.messageformat.MessageFormat;




/**
 *
 */
public class BotWrapper {
	static CalendarBot calbot;
	
	public static void main (String[] args) {
		Schedule.startExecutorService();
		//
		calbot = new CalendarBot();
		calbot.initializeBot();
		
		Scanner s = new Scanner(System.in);
		while (true) {			
			try {
				Thread.sleep(1000);
				String msg = s.nextLine();
				if (msg.equals("")) continue;
				if (msg.equals("exit")) break;
				sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		s.close();
	}
	
	public BotWrapper () {
	}
	
	public static void printMessage (Message message, Bot bot) {
		System.out.println(message.getPlainTextMessage());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			
		}	
	}
	
	public static void sendMessage (String msg) {
		calbot.messageReceived(new Message("bot", msg, MessageFormat.PLAIN_TEXT));
	}
	
	
}