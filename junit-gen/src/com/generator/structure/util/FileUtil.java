package com.generator.structure.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * General purpose methods for working with files.
 * 
 * @author Samuel Y. Deschamps [samueldeschamps at gmail dot com]
 * @since July/2014
 */
public class FileUtil {

	/**
	 * Reads a text file into a list of strings.
	 * 
	 * @param file
	 *            the file to be read
	 * @return a list containing one string for each line in the text file
	 * @throws IOException
	 *             if the file doesn't exists or an I/O error occur
	 */
	public static List<String> fileToString(File file) throws IOException {
		List<String> result = new ArrayList<>();
		FileReader reader = new FileReader(file);
		BufferedReader bufReader = new BufferedReader(reader);
		try {
			String line;
			while ((line = bufReader.readLine()) != null) {
				result.add(line);
			}
		} finally {
			bufReader.close();
			reader.close();
		}
		return result;
	}
	
	public static String fileToText(File file) throws IOException {
		List<String> stringList = fileToString(file);
		String lineBreak = System.getProperty("line.separator");
		
		StringBuilder sb = new StringBuilder();
		for (String str : stringList) {
			sb.append(str);
			sb.append(lineBreak);
		}
		return sb.toString();
	}

	public static void writeTextToFile(File file, String text) throws IOException {
		FileWriter writer = new FileWriter(file);
		try {
			writer.write(text);
		} finally {
			writer.close();
		}
	}

}
