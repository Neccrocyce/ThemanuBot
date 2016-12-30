package bots.ThemanuBot;

public abstract class Component {
	protected static String[][] protocols = CsvReader.getInstance().getProtocols();
	protected String[] l4protocols = CsvReader.getInstance().getL4protocols();	
	protected User user = null;
	
	public Component () {
	}
	
	protected abstract void receive(Packet packet);
	
	public boolean send (Packet packet) {
		switch (l4protocols[packet.getProtocol()]) {
		case "TCP":
			return sendTCP (packet);
		case "UDP":
			return sendUDP (packet);
		default:
			MyLogger.logError("Tried to send packet with unknown L4-protocol \"" + String.format("0x%02X\n", l4protocols[packet.getProtocol()]) + "\" at protocol \"" + String.format("0x%02X\n", packet.getProtocol()) + "\"");
			return false;
		}
	}
	

	/**
	 * search for the protocol for the given component and identifier (case insensitive)
	 * @param component name of component or null to ignore it
	 * @param identifier
	 * @return protocol for the given component and identifier (case insensitive)
	 */
	public static int findProtocol (String component, String identifier) {
		for (int i = 0; i < protocols.length; i++) {
			if (protocols[i][1].toLowerCase().equals(identifier.toLowerCase())) {
				if (component == null || protocols[i][0].toLowerCase().equals(component.toLowerCase())) {
					return i;
				}
			}
		}
		return 0;
	}
	
	public static int findProtocol(String identifier) {
		return findProtocol(null, identifier);
	}
	
	/**
	 * 
	 * @param protocol
	 * @return name of component for the given protocol or null if protocol is unused
	 */
	public static String findComponent (int protocol) {
		return protocols[protocol][0];
	}
	
	/*
	 *
	 * 
	 * It is not recommended to use any of all the following methods in subclasses
	 * 
	 *  
	 */
	
	public void receiveTCP (User user, Packet packet) {
		this.user = user;
		receive(packet);
	}
	
	public void receiveUDP () {
		//TODO
	}

	private boolean sendTCP (Packet p){
		byte[] packet = packPacket(p);
		if (packet == null) {
			MyLogger.logError("Tried to pack an invalid packet");
			return false;
		}
		return TCPSocket.send(user.getSocket(), packet);
	}
	
	private boolean sendUDP (Packet p) {
		return false;
		//TODO
	}
	
	/**
	 * 
	 * @param p
	 * @return byte-array representing the packet or null if packet is invalid
	 */
	private byte[] packPacket (Packet p) {
		byte[] packet = null;
		try {
			packet = p.pack();
		} catch (IllegalArgumentException e) {
			return null;
		}
		return packet;
	}
}
