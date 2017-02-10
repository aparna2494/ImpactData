package com.cisco.bccsp.resources;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Property
{
	private static String domain_url = "http://vm-gse-bcc-02:8080/AnyQueryService/";

	//private static String domain_url = "";
	
	public static String getDomain_url() {
		return domain_url;
	}
	
	static
	{
		/*Properties prop = new Properties();
		String propFileName = "config.properties";
 
		InputStream inputStream = Property.class.getClassLoader().getResourceAsStream(propFileName);
 
		if (inputStream != null)
			try	{
				prop.load(inputStream);
			} catch (IOException e) {
				e.printStackTrace();
			}
		else
			try {
				throw new FileNotFoundException("Property file '" + propFileName + "' not found in the classpath");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		
		domain_url = prop.getProperty("domain_url");*/
	}
}