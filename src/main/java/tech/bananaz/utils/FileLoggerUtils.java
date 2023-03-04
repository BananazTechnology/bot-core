package tech.bananaz.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class FileLoggerUtils {
	
	public static File createFile(String location, String name, String extension) throws Exception {
		String fileLocation = (location.endsWith("/")) ? location : location + "/";
		String fileNameNoSpaces = name.replace(" ", "_");
		String fileName = String.format("%s%s.%s", fileLocation, fileNameNoSpaces, extension);
		File newFile = new File(fileName);
		
		newFile.mkdirs();
		
		File parent = newFile.getParentFile();
		
		if(!parent.canWrite()) {
			throw new Exception(String.format("The directory %s cannot be written to", parent.getAbsolutePath()));
		}
		
		newFile.createNewFile();
		
		return newFile;
	}
	
	public static boolean write(File file, String line) throws Exception {
		long startBytes = file.length();
		if(!file.canWrite()) {
			throw new Exception(String.format("The file %s cannot be written to", file.getAbsolutePath()));
		}
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
		
		writer.append(line);
		writer.close();
		
		long endBytes = file.length();
		return (endBytes > startBytes);
	}

}
