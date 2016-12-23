package bridgempp.bot.wrapper;



/**
 * The Class to be implemented by a BridgeMPP Bot
 *
 */
public abstract class Bot {

	public String name;
	public String configFile;

	/**
	 * Initialize the Bot Called when the Bot is loaded by the Botwrapper
	 */
	public abstract void initializeBot();

	/**
	 * Overwrite this if you need a deinitialize as well This will be run
	 * asynchronously
	 */
	public void deinitializeBot() {

	}

	/**
	 * Message Received Called when the Bot receives a BridgeMPP Message
	 * 
	 * @param message
	 *            The BridgeMPP Message
	 */
	public abstract void messageReceived(Message message);

	/**
	 * Send Message Sends this BridgeMPP Message to the target Group
	 * 
	 * @param message
	 *            The BridgeMPP Message to send
	 */
	public void sendMessage(Message message) {
		BotWrapper.printMessage(message, this);
	}
}