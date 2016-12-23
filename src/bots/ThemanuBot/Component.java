package bots.ThemanuBot;

import java.io.IOException;
import java.net.Socket;

public abstract class Component {
	protected TCPSocket tcpSocket = TCPSocket.getInstance();	
	
	public Component () {
		
	}
	
	/**
	 * 
	 * @param client
	 */
	public abstract void recieve(Socket client, Paket paket);
	
	/**
	 * @throws IOException 
	 * 
	 * @param client
	 * @param paket
	 * @throws  
	 */
	public void sendTCP (Socket client, Paket p) throws IllegalArgumentException, IOException {
		byte[] paket = p.pack();
		tcpSocket.send(client, paket);
	}
}
