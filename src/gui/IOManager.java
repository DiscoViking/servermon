package gui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class IOManager {
	private String SERVER_FILE = "data\\servers.txt";
	private String MONITOR_FILE = "data\\monitors.txt";
	
	public IOManager() {
	}
	
	public ArrayList<ServerInfo> getServers() {
		ArrayList<ServerInfo> serverList = new ArrayList<ServerInfo>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(SERVER_FILE));
		    try {
		        String line = br.readLine();
		        
		        while (line != null) {
			        String[] splitLine = line.split(" ");
			        String hostname = splitLine[0];
			        String username = splitLine[1];
			        ServerInfo server = new ServerInfo(hostname, username);
					serverList.add(server);
					line = br.readLine();
		        }
		        
		    } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				br.close();
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return serverList;
	}
	
	public void saveServer(ServerInfo server) {
		try {
		    PrintWriter out = new PrintWriter(new BufferedWriter(
		    							new FileWriter(SERVER_FILE, true)));
		    StringBuilder builder = new StringBuilder();
		    builder.append(server.hostname);
		    builder.append(" ");
		    builder.append(server.username);
		    out.println(builder.toString());
		    out.close();
		} catch (IOException e) {
		    //oh noes!
		}
	}
	
	public ArrayList<MonitorInfo> getMonitors() {
		ArrayList<MonitorInfo> monitorList = new ArrayList<MonitorInfo>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(MONITOR_FILE));
		    try {
		        String line = br.readLine();
		        
		        while (line != null) {
			        String[] splitLine = line.split(" ");
			        String filename = splitLine[0];
			        String regex = splitLine[1];
			        int group = Integer.parseInt(splitLine[2]);
			        MonitorInfo monitor = new MonitorInfo(filename,regex,group);
			        monitorList.add(monitor);
					line = br.readLine();
		        }
		        
		    } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				br.close();
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return monitorList;
	}
	
	public void saveMonitor(MonitorInfo monitor) {
		try {
		    PrintWriter out = new PrintWriter(new BufferedWriter(
		    							new FileWriter(MONITOR_FILE, true)));
		    StringBuilder builder = new StringBuilder();
		    builder.append(monitor.filename);
		    builder.append(" ");
		    builder.append(monitor.regex);
		    builder.append(" ");
		    builder.append(monitor.group);
		    out.println(builder.toString());
		    out.close();
		} catch (IOException e) {
		    //oh noes!
		}
	}
}
