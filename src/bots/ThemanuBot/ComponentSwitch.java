package bots.ThemanuBot;

import java.net.Socket;

import bots.ThemanuBot.componets.General;

public class ComponentSwitch {
	private Component[] components;
	private General general;
	private static ComponentSwitch instance = null;
	
	private ComponentSwitch () {
		
	}
	
	public static ComponentSwitch getInstance () {
		if (instance == null) {
			instance = new ComponentSwitch();
		}
		return instance;
	}
	
	public void setComponents (Component[] components) {
		this.components = components;
		general = (General) (findComponent(new General().getClass().getName()));
		if (general == null) {
			MyLogger.logCritical("Component General was not found");
		}
	}
	
	public void receiveTCP (Socket socket, byte[] packet) {
		Packet p = extractPacket(packet);
		//test if packet is invalid
		if (p == null) {
			TCPSocket.send(socket, createErrorPacket(Component.findProtocol("ERROR", "ILLEGAL_PARAMETERS"), "" + packet[0]));
			return;
		}
		//test if protocol is unknwon
		if (Component.findComponent(p.getProtocol()) == null) {
			MyLogger.logError("Illegal usage of illegal or unknown protocol \"" + String.format("0x%02X\n", p.getProtocol()) + "\"");
			TCPSocket.send(socket, createErrorPacket(Component.findProtocol("ERROR", "UNKNOWN_PROTOCOL"), "" + p.getProtocol()));
			return;
		}
		
		//test if destination is "General"
		if (Component.findComponent(p.getProtocol()).equals(general.getClass().getName())) {
			general.receiveTCP(new User(null, null, -1, socket), p);
		} 
		else {
			//test if user is connected and logged in
			User user = general.getUser(socket);
			if (user == null) {
				MyLogger.logWarning("Received packet of an not logged in user using protocol \"" + String.format("0x%02X\n", p.getProtocol()) + "\"");
				return;
			}
			try { 
				//test for unknown component
				findComponent(Component.findComponent(p.getProtocol())).receiveTCP(user, p);
			} catch (NullPointerException e) {
				MyLogger.logError("Invalid packet received with protocol \"" + String.format("0x%02X\n", p.getProtocol()) + "\", Component \"" + Component.findComponent(p.getProtocol()) + "\" does not exist");
			}
		}
	}
	
	public void receiveUDP () {
		//TODO
	}
	
	/**
	 * 
	 * @param p
	 * @return Packet representing the byte-array or null if packet is corrupt
	 */
	private Packet extractPacket (byte[] p) {
		Packet packet = new Packet().extract(p);
		if (packet == null) {
			MyLogger.logError("Invalid packet received with protocol \"" + String.format("0x%02X\n", p[0]) + "\"");
		}
		return packet;
	}
	
	private byte[] createErrorPacket (int protocol, String msg) {
		Packet p = new Packet();
		p.setProtocol((byte) protocol);
		p.setStrings(new String[] {msg});
		return p.pack();
	}
	
	/**
	 * 
	 * @param component
	 * @return Object of class Component named {@code component} or null if no Component exists with the given name
	 */
	private Component findComponent (String component) {
		for (Component comp : components) {
			if (comp.getClass().getName().equals(component)) {
				return comp;
			}
		}
		return null;
	}
	
	/**
	 * removes connected user
	 */
	public void closeConnection (Socket socket) {
		general.removeUser(socket);
	}
	
	
}
