package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import jsqueak.AudioBufferPipe;
import jsqueak.AudioDeviceInputPipe;
import jsqueak.AudioSinkPipe;
import jsqueak.NullPipe;
import jsqueak.Pipe;
import jsqueak.RadialFFTFrequencyVisualiser;
import jsqueak.Visualiser;


public class ServermonController {
	private ServermonWindow mWindow;
	private HashMap<String,ServermonParameterField> parameters;
	private Pipe inputPipe;
	private AudioBufferPipe bufferPipe;
	private AudioSinkPipe sinkPipe;
	private Visualiser vis;
	private UpdaterThread mThread;
	
	public ServermonController() {
		parameters = new HashMap<String,ServermonParameterField>();
		
		mWindow = new ServermonWindow(this);
		mWindow.setAlwaysOnTop(true);
		
		Action action = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mWindow.removeNotify();
				if (mWindow.isUndecorated()) {
					mWindow.setOpacity(1.0f);
					mWindow.setUndecorated(false);
					mWindow.showSettings();
				}
				else {
					mWindow.setUndecorated(true);
					mWindow.setOpacity(0.5f);
					mWindow.hideSettings();
				}
				mWindow.addNotify();
				mWindow.getRootPane().requestFocus();
			}};
			
		String keyStrokeAndKey = "control D";
		KeyStroke keyStroke = KeyStroke.getKeyStroke(keyStrokeAndKey);
		mWindow.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, keyStrokeAndKey);
		mWindow.getRootPane().getActionMap().put(keyStrokeAndKey, action);
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				mWindow.begin();
			}
		});
		
		inputPipe = new AudioDeviceInputPipe(".*What U Hear.*");
		bufferPipe = new AudioBufferPipe(10000);
		sinkPipe = new AudioSinkPipe();
		
		bufferPipe.readFrom(inputPipe);
		sinkPipe.readFrom(bufferPipe);
		
		vis = new RadialFFTFrequencyVisualiser(bufferPipe.getBuffer());
		vis.setBackground(Color.BLACK);
		vis.setForeground(Color.GREEN);
		vis.setPreferredSize(new Dimension(300,300));
		mWindow.add(vis,BorderLayout.CENTER);
	}
	
	public void start() {
		if (mThread == null) {
			mThread = new UpdaterThread();
		}
		if (!mThread.isAlive()) {
			mThread.start();
		}
	}
	
	public void stop() {
		if (mThread != null) {
			mThread.pleaseStop();
			mThread = null;
		}
	}
	
	private class UpdaterThread extends Thread {
		private boolean running;
		
		@Override
		public void run() {
			running = true;
			int ii = 0;
			while (running){
				sinkPipe.pump();
				
				//Do all drawing on the Event thread
				if (ii % 2 == 0) {
					SwingUtilities.invokeLater(new Runnable() {
			            public void run() {
			            	vis.repaint();
			            }
			        });
				}
				ii++;
			}
		}
		
		public void pleaseStop() {
			running = false;
		}
	}
	
	public void addField(ServermonParameterField field) {
		parameters.put(field.getLabelText(), field);
	}

	public void disconnect() {
		inputPipe.close();
		inputPipe = new NullPipe();
		bufferPipe.readFrom(inputPipe);
		bufferPipe.clearBuffer();
		stop();
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	vis.repaint();
            }
        });
	}
	
	public void connect() {
		stop();
		inputPipe.close();
		inputPipe = getInputPipe();
		bufferPipe.readFrom(inputPipe);
		start();
	}
	
	public Pipe getInputPipe() {
		return new AudioDeviceInputPipe(".*What U Hear.*");
	}
}
