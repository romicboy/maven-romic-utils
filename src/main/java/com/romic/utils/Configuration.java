package com.romic.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

public class Configuration {

	private File file;
	private Map<String, String> configMap;
	private static Configuration uniqueInstance = null;
	private String filename;
	private String serializationName;

	public static Configuration getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new Configuration();
		}
		return uniqueInstance;
	}
	
	public Configuration() {
		init("conf.xml");
	}
	
	public Configuration(String filename) {
		init(filename);
	}
	
	private void init(String filename){
		setFilename(filename);
		setSerializationName("conf.obj");
		configMap = getConfigMap();
		Loger.info("configMap", String.valueOf(configMap));
	}
	
	public String get(String key){
		return configMap.get(key);
	}
	
	public void set(String key, String value){
		configMap.put(key, value);
	}
	
	/**
	 * 序列化对象
	 * @param obj
	 */
	private void setSerializationConfig(long time, Object obj){
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(serializationName));
			out.writeLong(time);
			out.writeObject(obj);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 反序列化对象
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, String> getSerializationConfig(long time){
		Map<String, String> config = null;
		try {
			File file = new File(serializationName);
			if (file.exists()) {
				ObjectInputStream in = new ObjectInputStream(new FileInputStream(serializationName));
				if ((long)in.readLong() == time) {
					config = (Map<String, String>) in.readObject();
				}
				in.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return config;
	}
	
	private Map<String, String> getConfigMap(){
		Map<String, String> config = null;
		try {
			file = getFile();
			config = getSerializationConfig(file.lastModified());
			if (null == config) {
				config = new HashMap<String, String>();
				InputStream fileInputStream = new FileInputStream(file);
				SAXBuilder builder = new SAXBuilder();
				Document document = builder.build(fileInputStream);
				fileInputStream.close();
				Element root = document.getRootElement();
				List<Element> list = root.getChildren("property");
				for (Element itemElement : list) {
					String name = null;
					String value = null;
					if (itemElement.hasAttributes()) {
					 	String nameString = itemElement.getAttributeValue("name");
					 	if (null != nameString) name = nameString;
					 	String valueString = itemElement.getAttributeValue("value");
					 	if (null != valueString) value = valueString;
					}
					if (null == name) name = itemElement.getChildText("name");
					if (null == value) value = itemElement.getChildText("value");
					if ("".equals(value)) value = null;
					config.put(name, value);
				}
				setSerializationConfig(file.lastModified(), config);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return config;
	}
	
	private File getFile() throws Exception{
		File file = new File(filename);
		if (!file.exists()) {
			URL configFileUrl = getClass().getClassLoader().getResource(filename);
			if (null != configFileUrl) {
				file = new File(configFileUrl.getPath());
			}
		}
		if (!file.exists()) {
			throw new Exception("Configuration conf.xml not exists");
		}
		return file;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getSerializationName() {
		return serializationName;
	}

	public void setSerializationName(String serializationName) {
		this.serializationName = serializationName;
	}
}
