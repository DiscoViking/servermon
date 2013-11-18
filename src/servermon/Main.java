package servermon;

import java.awt.Color;

import javax.swing.SwingUtilities;

import jsqueak.AudioBufferPipe;
import jsqueak.AudioDeviceOutputPipe;
import jsqueak.AudioSinkPipe;
import jsqueak.RadialFFTFrequencyVisualiser;
import jsqueak.StreamVisualiser;
import jsqueak.Visualiser;
import jsqueak.VisualiserWindow;

public class Main {
	public static void main(String[] args) {
		//PeakListener peakListener = new PeakListener(buffer, 300,500);
		//buffer.activateLowPassFilter(100);
		//peakListener.addPeakHandler(new LetterDetector("letterData.txt", 1));
		//peakListener.addPeakHandler(new LetterTrainer("letterData.txt"));
		
		String mixerName = ".*Primary Sound Driver.*";
		AudioDeviceOutputPipe outputPipe = new AudioDeviceOutputPipe(mixerName);
		AudioBufferPipe bufferPipe = new AudioBufferPipe(50000);
		AudioSinkPipe sinkPipe = new AudioSinkPipe();
		
		final VisualiserWindow w = new VisualiserWindow();
		
		Visualiser vis = new RadialFFTFrequencyVisualiser(bufferPipe.getbuffer());
		//Visualiser vis = new StreamVisualiser(bufferPipe.getbuffer(),10001);
		vis.setBackground(Color.BLACK);
		vis.setForeground(Color.GREEN);
		w.add(vis);

		//peakListener.addPeakHandler((SegmentVisualiser)vis);
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
//            	w.setUndecorated(true);
//            	w.setOpacity(0.5f);
            	w.begin();
            	w.setBounds(0,0,600,200);
            	w.setAlwaysOnTop(true);
            }
        });
		String filename = "/var/log/messages";
		if (args.length > 3) {
			filename = args[3];
		}
		String quietMode = "";
		if (args.length > 4) {
			quietMode = args[4];
		}
		final ServerReader mon = new ServerReader(args[0], args[1], args[2], filename);
		mon.getMonitor().setVisualiser(vis);
		
		bufferPipe.readFrom(mon);
		outputPipe.readFrom(bufferPipe);
		sinkPipe.readFrom(bufferPipe);
		
		int ii = 0;
		while (true){
			if (quietMode.equals("-quiet")) {
				sinkPipe.pump();
			}
			else {
				outputPipe.pump();
			}
			
			//Do all drawing on the Event thread
			if (ii % 10 == 0) {
				SwingUtilities.invokeLater(new Runnable() {
		            public void run() {
		            	w.repaint();
		            }
		        });
			}
			ii++;
		}
	}
}
