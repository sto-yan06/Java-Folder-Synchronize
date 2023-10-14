package main;

import java.io.File;
import java.io.IOException;

public class Main {
	
	
	public static void main(String[] args) {
		if(args.length != 4) {
			System.out.println("Usage: Main <source_folder> <destination_folder> <logfile_path> <interval (minutes)>");
			System.exit(1);
		}
		
		String sourceFolder = args[0];
		String destinationFolder = args[1];
		String logFilePath = args[2];
		int timeInterval = Integer.parseInt(args[3]);
		
		
		checkifExists(destinationFolder, logFilePath); // Checks if exists the destination folder and/or the logfile AND IF NOT IT CREATES THEM 
		SyncOperation syncOperation = new SyncOperation(sourceFolder, destinationFolder, logFilePath); // THE ACTUAL SYNCHRONIZATION
		
		
		// Interval parameter
		while(true) {

			syncOperation.performSynchronization();
			
			try {
				if(timeInterval > 0){
					Thread.sleep(timeInterval *60* 1000);
				}
				else {
					break;
				}
				
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private static void checkifExists(String destinationFolder, String logFilePath) {
		
		// Checks if destination folder exists, if not, create it
		File folder = new File(destinationFolder);
		if(!folder.exists()) {
			folder.mkdirs();
		}
		//Checks if logfile exists, if not create it
		File logFile = new File(logFilePath);
		if(!logFile.exists()) {
			try {
				logFile.createNewFile();
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
		
	}
}
