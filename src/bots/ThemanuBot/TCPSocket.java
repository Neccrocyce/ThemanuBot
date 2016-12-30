package bots.ThemanuBot;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPSocket {
	
	private TCPSocket () {
	}
	
	public static void openSocket (int port) throws IOException {
		ServerSocket server = new ServerSocket(port);
		while (true) {
			Socket client = null;
			
			try {
				client = server.accept();
				receive(client);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				if (client != null) {
					client.close();
				}
			}
		}
	}
	
	public static void receive (Socket client) throws IOException {
		byte[] data = new byte[1024];
		int offset = 0;
		BufferedInputStream in = new BufferedInputStream(client.getInputStream());
		
		while (true) {
			int read = in.read(data, offset, 128);
			//test if end of stream
			if (read <= 0) {
				break;
			}
			offset += 128;
			//test if packet size is larger than buffer
			if (data.length - offset < 128) {
				byte[] temp = data;
				data = new byte[temp.length * 2];
				System.arraycopy(temp, 0, data, 0, offset);
				//test if packet is too large
				if (data.length > Integer.MAX_VALUE - 1024) {
					MyLogger.logWarning("Received packet size is too large");
					return;
				}
			}
		}
		//test if packet size is 0
		if (offset == 0) {
			MyLogger.logWarning("Received packet with size 0");
			return;
		}
		
		ComponentSwitch.getInstance().receiveTCP(client, data);		
	}
	
	public static boolean send (Socket client, byte[] packet) {
		BufferedOutputStream out;
		try {
			out = new BufferedOutputStream(client.getOutputStream(), packet.length);
			out.write(packet, 0, packet.length);
			return true;
		} catch (IOException e) {
			MyLogger.logError(e.getStackTrace().toString());
			return false;
		}
		
	}
}
