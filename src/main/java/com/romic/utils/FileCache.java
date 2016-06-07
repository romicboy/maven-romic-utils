package com.romic.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * 文件缓存
 * @author romic <romicboy@163.com>
 */
public class FileCache {

	private File file;
	private String name;

	public FileCache(String name) {
		this.name = name + ".cache";
		try {
			File dir = new File("cache");
			if (!dir.isDirectory()) dir.mkdirs();
			file = new File(dir + "/" + this.name);
			if (!file.exists()) file.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean set(String value) {
		try {
//			FileOutputStream out = new FileOutputStream(file, true);
			FileOutputStream out = new FileOutputStream(file);
			out.write(value.getBytes("utf-8"));
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	public String get()  {
		StringBuffer sb = new StringBuffer();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String temp = null;
			temp = reader.readLine();
			while (temp != null && !temp.isEmpty()) {
				sb.append(temp);
				temp = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String result = sb.toString();
		if (result.equals("")) result = null;
		return sb.toString();
	}
}
