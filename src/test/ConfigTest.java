package test;

import static org.junit.Assert.*;

import org.junit.Test;

import config.Config;

public class ConfigTest {

	@Test
	public void testConfigRead() {
		Config conf = new Config();
		conf.readConfigFromFile();
		System.out.println("API: "+conf.getAPIKey());
		System.out.println("Form URL: "+conf.getFormURL());
		assertNotNull("API Key not found", conf.getAPIKey());
		assertNotNull("Form URL not found", conf.getFormURL());
	}

}
