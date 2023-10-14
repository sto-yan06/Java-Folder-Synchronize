package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class CheckSumCalculator {

	public void checkSumCalculator(String[] args) {

	}
	
	
	// CheckSum MD5 for Files
	public static String calculateFileCheckSum(File file) throws NoSuchAlgorithmException, IOException{
		
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		FileInputStream fis = new FileInputStream(file);
		byte[] buffer = new byte[8192];
		int bytesRead;
		while((bytesRead = fis.read(buffer)) != -1) {
			md5.update(buffer, 0, bytesRead);
		}
		byte[] digest = md5.digest();
		fis.close();
	
		StringBuilder result = new StringBuilder();
		for(byte b : digest) {
			result.append(String.format("%02x", b));
		}
		return result.toString();
		
	}
	
	
	// CheckSum MD5 for Folders using "listFiles" method to check each file from each directory created.
	public static String calculateFolderCheckSum(String folderPath) {
		
		List<File> files = listFiles(new File(folderPath));
		
		List<String> fileChecksums = new ArrayList<>();
		
		for(File file : files) {
			
			try {
				String checksum = calculateFileCheckSum(file);
				fileChecksums.add(checksum);
			}
			catch(NoSuchAlgorithmException | IOException e){
				e.printStackTrace();
			}
		}
		
		StringBuilder folderChecksum = new StringBuilder();
		
		for(String checksum : fileChecksums) {
			folderChecksum.append(checksum);
		}
		return folderChecksum.toString();
	}
	
	
	// This method lists all the files in a given folder and its subdirectories. It returns the list "File" with all files found.
	public static List<File> listFiles(File folder){
		
		List<File>fileList = new ArrayList<>();
		
		File[] files = folder.listFiles();
		
		if(files != null) {
			
			for(File file: files) {

				if(file.isFile()) {
					fileList.add(file);
				}
				
				else if(file.isDirectory()) {
					fileList.addAll(listFiles(file));
				}
			}
		}
		
		return fileList;
	}
}
