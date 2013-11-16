package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class ServermonParameterField extends JPanel{
	private JLabel mLabel;
	private JTextField mField;
	
	public ServermonParameterField(ServermonController controller, String name) {
		this(controller, name, false);
	}
	
	public ServermonParameterField(ServermonController controller, String name, boolean masked) {
		Font font = new Font("Courier new",Font.PLAIN,12);
		Dimension labelSize = new Dimension(80, 15);
		Dimension panelSize = new Dimension(220,20);
		setPreferredSize(panelSize);
		setMaximumSize(panelSize);
		
		mLabel = new JLabel();
		mLabel.setText(name+": ");
		mLabel.setFont(font);
		mLabel.setPreferredSize(labelSize);
		mLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		
		if (masked) {
			mField = new JPasswordField("",15);
		}
		else {
			mField = new JTextField("",15);
		}
		mField.setFont(font);
		mField.setPreferredSize(labelSize);
		
		mField.addFocusListener(new java.awt.event.FocusAdapter() {
    	    public void focusGained(java.awt.event.FocusEvent evt) {
    	    	SwingUtilities.invokeLater( new Runnable() {

    				@Override
    				public void run() {
    					mField.selectAll();		
    				}
    			});
    	    }
    	});
		
		add(mLabel);
		add(mField);
		
		controller.addField(this);
	}
	
	public String getText() {
		return mField.getText();
	}
	
	public String getLabelText() {
		return mLabel.getText();
	}
	
	public void setText(String text) {
		mField.setText(text);
	}
}
