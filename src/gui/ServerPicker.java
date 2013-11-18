package gui;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ServerPicker extends JFrame {
	private ArrayList<ServerInfo> serverList;
	private JList<ServerInfo> mList;
	private ServermonController mController;
	
	public ServerPicker(ServermonController controller) {
		serverList = new ArrayList<ServerInfo>();
		mController = controller;
	}
	
	/**
	 * Sets up window and makes it visible.
	 */
	public void begin() {
		setVisible(true);
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		createBufferStrategy(2);
		
		mList = new JList<ServerInfo>();
		mList.setPreferredSize(new Dimension(200,300));
		mList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		mList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				mController.loadServer(mList.getSelectedValue());
			}
		});
		add(mList);
		
		pack();
		
		loadServers();
		//this.setResizable(false);
	}
	
	public void loadServers() {
		IOManager io = new IOManager();
		serverList = io.getServers();
		Collections.sort(serverList, new Comparator<ServerInfo>() {
			@Override
			public int compare(ServerInfo s1, ServerInfo s2) {
				return s1.hostname.compareTo(s2.hostname);
			}
		});
		
		mList.setListData(serverList.toArray(new ServerInfo[0]));
		mList.invalidate();
	}
}
