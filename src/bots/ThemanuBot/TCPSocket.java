package bots.ThemanuBot;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class TCPSocket {
	private static TCPSocket instance = null;
	
	private TCPSocket () {
	}
	
	public void openSocket (int port) throws IOException {
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
	
	public void receive (Socket client) throws IOException {
		Scanner in = new Scanner(client.getInputStream());
		byte paket = in.;
		
	}
	
	public void send (Socket client, byte[] paket) throws IOException {
		PrintWriter out = new PrintWriter(client.getOutputStream(), true);
		out.print(paket);
	}
	
	public static TCPSocket getInstance () {
		if (instance == null) {
			instance = new TCPSocket();
		}
		return instance;
	}
}
