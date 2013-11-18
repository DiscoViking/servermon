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

public class MonitorPicker extends JFrame {
	private ArrayList<MonitorInfo> monitorList;
	private JList<MonitorInfo> mList;
	private ServermonController mController;
	
	public MonitorPicker(ServermonController controller) {
		monitorList = new ArrayList<MonitorInfo>();
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
		
		mList = new JList<MonitorInfo>();
		mList.setPreferredSize(new Dimension(200,300));
		mList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		mList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				mController.loadMonitor(mList.getSelectedValue());
			}
		});
		add(mList);
		
		pack();
		
		loadServers();
		//this.setResizable(false);
	}
	
	public void loadServers() {
		IOManager io = new IOManager();
		monitorList = io.getMonitors();
		Collections.sort(monitorList, new Comparator<MonitorInfo>() {
			@Override
			public int compare(MonitorInfo s1, MonitorInfo s2) {
				return s1.filename.compareTo(s2.filename);
			}
		});
		
		mList.setListData(monitorList.toArray(new MonitorInfo[0]));
		mList.invalidate();
	}
}
