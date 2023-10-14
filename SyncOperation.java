package main;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.io.*;

public class SyncOperation {

	private String sourceFolder;
	private String destinationFolder;
	private ChangeLogger logOp;
	
	public SyncOperation(String sourceFolder, String destinationFolder, String logFilePath) {
		
		this.sourceFolder = sourceFolder;
		this.destinationFolder = destinationFolder;
		this.logOp = new ChangeLogger(logFilePath);
	}
	
	public void performSynchronization() {
		synchronizeFolders(new File(sourceFolder), new File(destinationFolder));
	}
	
	// Here is the whole logic behind moving and copying files/folders
	
	
	private void synchronizeFolders(File source, File destination) {
		File[] sourceFiles = source.listFiles();
		File[] destFiles = destination.listFiles();
		
		for(File srcFile : sourceFiles) {
			String srcName = srcFile.getName();
			File destFile = new File(destination, srcName);
			
			if(srcFile.isDirectory()) {
				if(!destFile.exists() && destFile.mkdir()) {
					// If there is no folder or any missing folder in destination path it creates them
					logAndSync("Created folder in destination: " + destFile.getPath());
				}
				synchronizeFolders(srcFile, destFile); // and it repeats the proccess to make sure it has coppied all the subdirectories.
			}	

			else if(srcFile.isFile()) {
						if(!destFile.exists() || srcFile.lastModified() != destFile.lastModified()) {
							//If both Files exists but the source file is modified, it also modifies the destination file
							copyFile(srcFile, destFile);
							logAndSync("Updated file: " + destFile.getPath());
				}
			}	
		}
		
		
		// SPECIAL CASE: if there is a missing file in the source directory but it exists in the destination directory
		// It asks the user whether delete it or not, and also logs the process
		
		for(File destFile : destFiles) {
			if(!new File(source, destFile.getName()).exists()) {
				System.out.println("Do you want to delete: " + destFile.getName() + "' from destination? (yes/no): ");
				Scanner scanner = new Scanner(System.in);
				String response = scanner.nextLine();
				
				if(response.equalsIgnoreCase("yes")) {
					if(destFile.isDirectory()) {
						deleteFolder(destFile);
					}
					else {
						deleteFile(destFile);
					}
					logAndSync("Deleted from destination: " + destFile.getPath());
				}
				else {
					logAndSync("Refused to delete: "+ destFile.getPath());
				}
			}
			
		}
	}
	
	private void copyFile(File sourceFile, File destFile) {
		try {
			
			InputStream in = new FileInputStream(sourceFile);
			OutputStream out = new FileOutputStream(destFile);
			
			byte[] buffer = new byte [1024];
			int length;
			while((length =  in.read(buffer)) > 0) {
				out.write(buffer, 0 ,length);
			}
			in.close();
			out.close();
			
			destFile.setLastModified(sourceFile.lastModified()); // UPDATED LAST MODIFIED TIME STAMP
			logAndSync("Copied file to destination: " + destFile.getPath());
			
			
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private void deleteFile(File file) {
		if(file.delete()) {
			logAndSync("Deleted file from destination: " + file.getPath());
		}
		else {
			logAndSync("Failed to delete file from destination: " + file.getPath());
		}
	}
	
	private void deleteFolder(File folder) {
		File[] files = folder.listFiles();
		for(File file : files) {
			if(file.isDirectory()) {
				deleteFolder(file);
			}
			else {
				deleteFile(file);
			}
		}
		
		if(folder.delete()) {
			logAndSync("Deleted folder from destination: " + folder.getPath());
		}
		else {
			logAndSync("Failed to delete folder from destination: " + folder.getPath());
		}
	}
	
	private void logAndSync(String message) {
		logMessage(message);
		
	}
	
	private void logMessage(String message) {
		try {
			logOp.open();
			logOp.log(message);
			logOp.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
}


