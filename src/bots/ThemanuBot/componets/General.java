package bots.ThemanuBot.componets;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import bots.ThemanuBot.Component;
import bots.ThemanuBot.Packet;
import bots.ThemanuBot.User;

public class General extends Component {
	private List<User> users = new ArrayList<>();
	
	public General () {}

	@Override
	public void receive(Packet paket) {
		//set user
		user = findUser(user);
		//TODO
		
	}
	
	private User findUser (User user) {
		for (User u : users) {
			if (user.getSocket() == u.getSocket()) {
				return u;
			}
		}
		return null;
	}
	
	public User getUser (Socket socket) {
		return findUser(new User(null, null, -1, socket));
	}
	
	public void removeUser (Socket socket) {
		users.remove(findUser(new User(null, null, -1, socket)));
	}
	
	
}
