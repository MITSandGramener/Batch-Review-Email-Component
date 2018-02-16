package com.ssl.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

public class ReadProperty {
	
	private static Properties props = null;
	private static ReadProperty readProp = new ReadProperty();
	public final Properties getAsProperties() {
			InputStream inStream = getClass().getResourceAsStream("/com/ssl/properties/Config.properties");
			Properties props = new Properties();
			//System.out.println("Reading ReadProperty properties ########:");
			//Throws an RuntimeException if it fails to load properties
			try{
				props.load(inStream);
			} catch (IOException e) {
				throw new RuntimeException("Error loading properties for ");
			}finally{
				try{
					inStream.close();
				} catch (IOException e) {
					throw new RuntimeException("Error closing the InputStream.");
				}
			}
			return props;
			 
	}
	private synchronized Properties getDetails(){
		//System.out.println("properties:"+props);
		if(props != null)//if properties is loaded already then return
			return props;
		try{
			props = getAsProperties();
			return props;
		}catch(Exception e){
			props = null;//make sure that the object is null
			System.out.println("Exception while reading config.properties:"
																+e.getMessage());
			throw new RuntimeException("Exception while reading config.properties:"
																+e.getMessage());
		}
	}
	public static String getProperty(String key){
		return readProp.getDetails().getProperty(key);
	}

}
