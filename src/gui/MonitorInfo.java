package gui;

public class MonitorInfo {
	public String filename, regex;
	public int group;
	
	public MonitorInfo(String file, String reg, int group) {
		filename = file;
		regex = reg;
		this.group = group;
	}
	
	@Override
	public String toString() {
		return filename;
	}
}
