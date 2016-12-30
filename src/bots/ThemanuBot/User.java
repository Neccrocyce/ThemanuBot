package bots.ThemanuBot;

import java.net.Socket;

public class User {
	private String name;
	private String nickname;
	/**
	 * rank of user; possible values:
	 * 0: admin, 1: named user, 2: guest
	 */
	private int rank;
	private Socket socket;
	
	public User (String name, String nickname, int rank, Socket socket) {
		this.name = name;
		this.nickname = nickname;
		this.rank = rank;
		this.socket = socket;
	}
	
	public int getRank() {
		return rank;
	}
	
	public String getNickname() {
		return nickname;
	}
	
	public String getName() {
		return name;
	}
	
	public Socket getSocket() {
		return socket;
	}
}
