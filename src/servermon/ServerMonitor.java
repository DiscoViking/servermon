package servermon;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jsqueak.Visualiser;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class ServerMonitor extends Thread {
	private ServerReader mReader;
	private String hostname, username, password, filename;
	private Visualiser mVisualiser;
	
	public ServerMonitor(ServerReader reader, String hostname, String username, String password, String filename) {
		mReader = reader;
		this.hostname = hostname;
		this.username = username;
		this.password = password;
		this.filename = filename;
	}
	
	public void setVisualiser(Visualiser vis) {
		mVisualiser = vis;
	}
	
	@Override
	public void run() {
		JSch shell = new JSch();
		JSch.setConfig("StrictHostKeyChecking", "no");
		Channel channel = null;
		BufferedReader fromServer = null;
		OutputStream toServer = null;
        String command = "tail -fv "+filename; 
        
        try {
	        Session session = shell.getSession(username, hostname, 22);
	        session.setPassword(password);
	        session.connect();  
	        
	        channel = session.openChannel("shell");
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
        try {
	        fromServer = new BufferedReader(new InputStreamReader(channel.getInputStream()));  
	        toServer = channel.getOutputStream();
	        channel.connect();  
	        toServer.write((command + "\n").getBytes());
	        toServer.flush();
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
        
        //Pattern fileChange = Pattern.compile("==> (.*) <==");
        Pattern fileChange = Pattern.compile("^(\\S+\\s+){7}(.*?)\\[.*$");
        
        while (true) {
        	byte[] bytes;
			try {
				String text = fromServer.readLine();
				Matcher matcher = fileChange.matcher(text);
				if (mVisualiser != null && matcher.find()) {
					String filename = matcher.group(2);
					if (filename.length() > 3) {
						byte[] part1 = filename.substring(0,filename.length()/3).getBytes();
						byte[] part2 = filename.substring(filename.length()/3,2*filename.length()/3).getBytes();
						byte[] part3 = filename.substring(2*filename.length()/3,filename.length()).getBytes();
						
						int R = 0;
						for (byte b : part1) {
							R += Integer.reverse((int) b);
						}
						R /= part1.length;
						
						int G = 0;
						for (byte b : part2) {
							G += Integer.reverse((int) b);
						}
						G /= part1.length;
						
						int B = 0;
						for (byte b : part3) {
							B += Integer.reverse((int) b);
						}
						B /= part1.length;
						
						R = Math.max(0,Math.min(255, R));
						G = Math.max(0,Math.min(255, G));
						B = Math.max(0,Math.min(255, B));
						Color c = new Color(R,G,B);
						
						mVisualiser.setForeground(c);
					}
				}
				bytes = text.getBytes();
				mReader.sendBytes(bytes);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}
}
