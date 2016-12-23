package bots.ThemanuBot;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPSocket {
	
	public void receivePacket (int port) {
		while (true) {
			try {
				DatagramSocket socket = new DatagramSocket(port);		
				DatagramPacket packet = new DatagramPacket(new byte[128], 1024);
				socket.receive(packet);			
				// Sender auslesen
			
				address = packet.getAddress();
				UDPServer.port    = packet.getPort();
				len     = packet.getLength();
				data    = packet.getData();
			
				System.out.printf( "Antwort von %s vom Port %d mit der Länge %d:%n%s%n",address, port, len, new String( data, 0, len ) );			
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	
	public void sendPacket (String msg, String ip, int port) {
		try {
			InetAddress ia = InetAddress.getByName(ip);
			byte[] data = msg.getBytes();
			DatagramPacket packet = new DatagramPacket(data, data.length, ia, port);
			DatagramSocket socket = new DatagramSocket();
			socket.send(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
