package com.surveyo.zzlobot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * TODO Rethink replaceLines()
 * */
final public class Utils {

	static public String readFile(File file) throws FileNotFoundException {
		String output = "";
		Scanner scan = new Scanner(new FileReader(file));
		try {
			while (scan.hasNext()) {
				output += "\n" + scan.nextLine();
			}
		} finally {
			scan.close();
		}

		if (!output.equals("")) {
			// Removing first \n.
			return output.substring(1);
		}
		return "";
	}

	static public String readFile(String path) throws FileNotFoundException {
		return readFile(new File(path));
	}

	/**
	 * Reads specified amount of lines from the start of the file.
	 * 
	 * @param file
	 *            - Your File object.
	 * @param linesFromStart
	 *            - Amount of lines from the start of the file.
	 * @author arcan770077f;
	 * @return File contents.
	 **/
	static public String readString(File file, int linesFromStart)
			throws FileNotFoundException {
		String output = "";
		int counter = 0;

		Scanner scan = new Scanner(new FileReader(file));
		try {
			while (scan.hasNext() && counter < linesFromStart) {
				counter++;
				output += "\n" + scan.nextLine();
			}
		} finally {
			scan.close();
		}

		if (!output.equals("")) {
			// Removing first \n.
			return output.substring(1);
		}
		return "";
	}

	/**
	 * Reads specified amount of lines from the start of the file.
	 * 
	 * @param file
	 *            - Your File object.
	 * @param linesFromStart
	 *            - Amount of lines from the start of the file.
	 * @author arcan770077f;
	 * @return File contents.
	 **/
	static public String readString(String path, int linesFromStart)
			throws FileNotFoundException {
		return readString(new File(path), linesFromStart);
	}

	/**
	 * @param sourceArray
	 *            Array of variables declaration lines of the Macro.
	 * */
	static public String replaceLines(String[] sourceArray, int[] lineIndices,
			String[] replacements) throws FileNotFoundException,
			NullPointerException {
		if (sourceArray.equals(null) || lineIndices.equals(null)
				|| replacements.equals(null)) {
			return null;
		} else {

			for (int i = 0; i <= sourceArray.length - 1; i++) {
				for (int j = 0; j <= lineIndices.length - 1; j++) {
					if (i == lineIndices[j] - 1) {
						sourceArray[i] = replacements[j];
					}
				}
			}

			String output = "";
			for (int i = 0; i <= sourceArray.length - 1; i++) {
				output += "\n" + sourceArray[i];
			}

			output += SourceArray.SOURCE_REM_PART_FIRST;
			output += SourceArray.SOURCE_REM_PART_SECOND;
			output += SourceArray.SOURCE_REM_PART_THIRD;
			output += SourceArray.SOURCE_REM_PART_FOURTH;
			return output.substring(1); // remove first \n sign
		}
	}

	static public int[] joinArrays(int[] A, int[] B) {
		int aLen = A.length;
		int bLen = B.length;

		int[] C = new int[aLen + bLen];
		System.arraycopy(A, 0, C, 0, aLen);
		System.arraycopy(B, 0, C, aLen, bLen);
		return C;
	}

	static public <T> T[] joinArrays(T[] A, T[] B) {
		int aLen = A.length;
		int bLen = B.length;

		@SuppressWarnings("unchecked")
		T[] C = (T[]) Array.newInstance(A.getClass().getComponentType(), aLen
				+ bLen);
		System.arraycopy(A, 0, C, 0, aLen);
		System.arraycopy(B, 0, C, aLen, bLen);
		return C;
	}

	static public void writeToFile(File file, String content)
			throws IOException, FileNotFoundException {
		FileWriter fw = new FileWriter(file);
		fw.write(content);
		fw.close();
	}

	static public void writeToFile(String path, String content)
			throws IOException, FileNotFoundException {
		FileWriter fw = new FileWriter(path);
		fw.write(content);
		fw.close();
	}

	/**
	 * Appends string data to the specified file.
	 * 
	 * @param file
	 *            - Your File object.
	 * @param onNewLine
	 *            - Insert string on a new line (true)?
	 * @throws IOException
	 *             File is locked or try again.
	 * @author arcan770077f;
	 * */
	static public void appendString(File file, String strToAppend,
			boolean onNewLine) throws FileNotFoundException, IOException {
		FileWriter fw = new FileWriter(file);
		String fileContents;

		try {
			fileContents = readFile(file);
			fw.write(fileContents + (onNewLine ? "\n" : "") + strToAppend);
		} catch (NullPointerException e) {
			// File is empty.
			fileContents = "";
			fw.write(fileContents + strToAppend);
		}

		fw.close();
	}

	/**
	 * Appends string data to the specified file.
	 * 
	 * @param path
	 *            - Path to the file.
	 * @param onNewLine
	 *            - Insert string on a new line (true)?
	 * @throws IOException
	 *             File is locked or try again.
	 * @author arcan770077f;
	 * */
	static public void appendStringToFile(String path, String strToAppend,
			boolean onNewLine) throws FileNotFoundException, IOException {
		FileWriter fw = new FileWriter(path);
		String fileContents;

		try {
			fileContents = readFile(path);
			fw.write(fileContents + (onNewLine ? "\n" : "") + strToAppend);
		} catch (NullPointerException e) {
			// File is empty.
			fileContents = "";
			fw.write(fileContents + strToAppend);
		}

		fw.close();
	}

	/**
	 * Removes element under certain index from JSONArray
	 * 
	 * @author arcan770077f;
	 * @return modified JSONArray;
	 * */
	static public JSONArray removeFromJSONArray(JSONArray jsonArray,
			int itemForRemovalIndex) {
		ArrayList<Object> list = new ArrayList<Object>();
		if (!jsonArray.equals(null)) {
			for (int i = 0; i < jsonArray.length(); i++) {
				try {
					list.add(jsonArray.get(i));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			list.remove(itemForRemovalIndex);
			return new JSONArray(list);
		}
		return null;
	}
}
