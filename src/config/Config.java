package config;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.List;

import debug.Logger;

public class Config {
	private String apiKey = null;
	private String formURL = null;
	
	
	private final String R_GET_API_KEY = "^apiKey\\s*=.*$";
	private final String R_GET_FORM_URL= "^formURL\\s*=.*$";
	/**
	 * Get config details from a file. Expects that the file name will be config.txt, and in the program directory.
	 * @throws IOException 
	 */
	public void readConfigFromFile(){
		Path conf = FileSystems.getDefault().getPath("", "config.txt");
		if(!Files.exists(conf, LinkOption.NOFOLLOW_LINKS)){
			try{
				Files.createFile(conf);
			} catch(IOException e){
				Logger.outError("Orchestrate attempted to create the file "+conf.toString()+" but was unsuccessful. Please create the file manually before continuing.", e);
				return;
			}
			//TODO prompt user to input pertinent details to be written to file.
			return;
		}
		List<String> configLines = null;
		try{
			configLines = Files.readAllLines(conf, Charset.defaultCharset());
		}catch(IOException e){
			Logger.outError("Orchestrate tried to read config.txt, but failed. Make sure the config file isn't open in any other program.", e);
			return;
		}
		
		for(String line : configLines){
			line = line.trim();
			if(line.startsWith("#")){
				//comment, ignore
				continue;
			}
			else if(line.matches(R_GET_API_KEY)){
				apiKey = line.substring(line.indexOf('=')+1);
			}
			else if(line.matches(R_GET_FORM_URL)){
				formURL = line.substring(line.indexOf('=')+1);
			}
			else{
				Logger.warn("Unrecognized line: "+line);
			}
		}
		
		//Make sure that all expected configs have been set.
		if(apiKey==null){
			Logger.outError("API key was not found in config.txt! Please make sure that config.txt is formatted correctly.");
		}
		if(formURL==null){
			Logger.outError("Form URL was not found in config.txt! Please make sure that config.txt is formatted correctly.");
		}
		
		return;
	}
	
	/**
	 * Returns the API key for our FormSite account.
	 * @return
	 */
	public String getAPIKey(){
		return apiKey;
	}
	
	/**
	 * Returns the Form URL for our FormSite form.
	 * @return
	 */
	public String getFormURL(){
		return formURL;
	}
}
