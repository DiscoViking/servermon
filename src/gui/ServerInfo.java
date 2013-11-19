package gui;

public class ServerInfo {
	public String hostname, username;
	
	public ServerInfo(String host, String user) {
		hostname = host;
		username = user;
	}
	
	@Override
	public String toString() {
		return hostname;
	}
}
