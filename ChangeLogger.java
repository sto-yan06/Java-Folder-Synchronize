package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChangeLogger {
	private String logFilePath;
	private BufferedWriter bw;
	
	
	public ChangeLogger(String logFilePath) {
		this.logFilePath = logFilePath;
		
	}
	
	public void open() throws IOException{ // Opens file to append LogInfo
		bw = new BufferedWriter(new FileWriter(logFilePath, true));
	}
	
	
	public void log(String message) throws IOException{ // The message added 
		SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String timeStamp = dateFormat.format(new Date());
		bw.write(timeStamp + ": " + message);
		bw.newLine();
	}
	
	public void close() { // Closes file
		try {
			if(bw != null) {
				bw.close();
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
}
