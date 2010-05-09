package com.loadtrend.app.test.soundrecorder.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class IOOperation {
	public static void writeObject(String filePath, Object object) {
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(filePath);
			ObjectOutputStream outputStream = new ObjectOutputStream(
					fileOutputStream);
			outputStream.writeObject(object);
			outputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Object readObject(String filePath) {
		try {
			if (!IOOperation.isFileExist(filePath))
				return null;
			Object object = null;
			FileInputStream fileInputStream = new FileInputStream(filePath);
			ObjectInputStream inputStream = new ObjectInputStream(
					fileInputStream);
			object = inputStream.readObject();
			inputStream.close();
			return object;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
			return null;
		}
	}

	public static boolean isFileExist(String filePath) {
		File file = new File(filePath);
		return file.exists();
	}

	public static boolean copy(String sourcFilename, String targetFilename) {
		try {
			File file_in = new File(sourcFilename);
			File file_out = new File(targetFilename);
			FileInputStream in1 = new FileInputStream(file_in);
			FileOutputStream out1 = new FileOutputStream(file_out);
			byte[] bytes = new byte[1024];
			int c;
			while ((c = in1.read(bytes)) != -1)
				out1.write(bytes, 0, c);
			in1.close();
			out1.close();
			return true; // if success then return true
		} catch (Exception e) {
			System.out.println("Error!");
			return false; // if fail then return false
		}
	}
}