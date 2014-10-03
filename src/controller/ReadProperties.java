package controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ReadProperties {
	
	public static String getValue(String key){
		Properties properties = new Properties();
		FileInputStream fip = null;
		try {
			fip = new FileInputStream("B:\\Jeffrey\\ProgramFiles\\logviewer\\logviewer.properties");
			properties.load(fip);
			return properties.getProperty(key);
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
