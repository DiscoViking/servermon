package gui;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ServermonWindow extends JFrame {
	private ServermonController mController;
	private JPanel mSettingsPanel;
	
	public ServermonWindow(ServermonController controller) {
		mController = controller;
	}
	
	public ServermonController getController() {
		return mController;
	}
	
	/**
	 * Sets up window and makes it visible.
	 */
	public void begin() {
		setLayout(new BorderLayout());
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		createBufferStrategy(2);
		
		add(createSettingsPanel(),BorderLayout.NORTH);
		pack();
		//this.setResizable(false);
	}
	
	public JPanel createSettingsPanel() {
		mSettingsPanel = new JPanel();
		mSettingsPanel.setLayout(new BoxLayout(mSettingsPanel, BoxLayout.X_AXIS));
		
		JPanel serverInfoPanel = new JPanel();
		serverInfoPanel.setLayout(new BoxLayout(serverInfoPanel,BoxLayout.Y_AXIS));
		
		JPanel hostname = new ServermonParameterField(mController,"Hostname");
		JPanel username = new ServermonParameterField(mController,"Username");
		JPanel password = new ServermonParameterField(mController,"Password",true);
		
		serverInfoPanel.add(hostname);
		serverInfoPanel.add(username);
		serverInfoPanel.add(password);
		
		JPanel monitorInfoPanel = new JPanel();
		monitorInfoPanel.setLayout(new BoxLayout(monitorInfoPanel,BoxLayout.Y_AXIS));
		
		JPanel filename = new ServermonParameterField(mController,"Filename");
		JPanel regex = new ServermonParameterField(mController,"Regex");
		JPanel group = new ServermonParameterField(mController,"Group #");
		
		monitorInfoPanel.add(filename);
		monitorInfoPanel.add(regex);
		monitorInfoPanel.add(group);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		JButton connectButton = new JButton("Connect");
		connectButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent m) {
				mController.connect();
			}
		});
		
		JButton disconnectButton = new JButton("Disconnect");
		disconnectButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent m) {
				mController.disconnect();
			}
		});
		
		buttonPanel.add(connectButton);
		buttonPanel.add(disconnectButton);
		
		mSettingsPanel.add(serverInfoPanel);
		mSettingsPanel.add(monitorInfoPanel);
		mSettingsPanel.add(buttonPanel);
		return mSettingsPanel;
	}
	
	public void hideSettings() {
		mSettingsPanel.setVisible(false);
	}
	
	public void showSettings() {
		mSettingsPanel.setVisible(true);
	}
}
