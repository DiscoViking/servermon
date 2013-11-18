package servermon;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import jsqueak.Pipe;

public class ServerReader extends Pipe {
	private ServerMonitor mon;
	private int BUFFER_LENGTH = 4410;
	private byte[] buffer = new byte[BUFFER_LENGTH];
	private int bytesInBuffer = 0;
	private Object syncObject = new Object();
	private int bytesRequested = 0;

	public ServerReader(String hostname, String username, String password, String filename) {
		mon = new ServerMonitor(this, hostname, username, password, filename);
		mon.start();
		
		Thread mKickerThread = new Thread() {
			@Override
			public void run() {
				while (true) {
					byte[] temp = new byte[BUFFER_LENGTH - bytesInBuffer];
					Arrays.fill(temp, (byte)0);
					sendBytes(temp);
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		mKickerThread.start();
	}
	
	public ServerMonitor getMonitor() {
		return mon;
	}
	
	public void sendBytes(byte[] bytes) {
		int length;
		length = Math.min(bytes.length, buffer.length - bytesInBuffer);
		System.arraycopy(bytes, 0, buffer, bytesInBuffer, length);
		bytesInBuffer += length;
		
		if ((bytesRequested != 0) && (bytesInBuffer >= bytesRequested)) {
			unblock();
		}
	}
	
	public int read(int[] chunk, int start, int length) {
		int bytesRead;
		bytesRequested = length*2;
		
		//Block until we have enough bytes
		while (bytesRequested > bytesInBuffer) {
			block();
		}
		
		bytesRead = bytesRequested;
		byte[] temp = new byte[bytesRead];
		int frame = 0;
		for (int i=0; i<bytesRead; i+=2) {
			ByteBuffer bB = ByteBuffer.wrap(new byte[]{buffer[i],buffer[i+1]});
			bB.order(ByteOrder.LITTLE_ENDIAN);  // if you want little-endian
			chunk[frame] = bB.getShort();
			frame += 1;
		}
		
		temp = new byte[bytesInBuffer - bytesRead];
		System.arraycopy(buffer, bytesRead, temp, 0, temp.length);
		Arrays.fill(buffer, (byte)0);
		System.arraycopy(temp, 0, buffer, 0, temp.length);
		bytesInBuffer -= bytesRead;
		bytesRequested = 0;
		return length;
	}
}
