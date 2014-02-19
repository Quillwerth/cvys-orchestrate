package data.in;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import config.Config;
import debug.Logger;
import sched.ScheduleRequest;


/**
 * Makes API requests to FormSite to fetch student input.
 * @author Patrick
 *
 */
public class DataRetriever {
	Config c;
	
	public DataRetriever(){
		c = new Config();
		c.readConfigFromFile();
	}
	
	public List<ScheduleRequest> parseAllFormData(){
		//TODO
		//Find how many pages of form data we need
		//Get all pages of form data
		//Create ScheduleRequest objects out of all students
		return null;
	}
	
	/**
	 * Retrieve one page of the FormSite data, using an API call.
	 * @param page - The page number of the request data: FormSite limits to 100 results a page, so iteration is necessary.
	 * @return A StringBuilder containing the full page text.
	 */
	public StringBuilder retrieveRawFormData(int page){
		String fullURL = c.getFormURL()+"results?fs_api_key="+c.getAPIKey()+"&fs_page="+page;
		URL url;
		try {
			url = new URL(fullURL);
		} catch (MalformedURLException e) {
			Logger.outError("Orchestrate has detected an error with the URL. Please ensure that the FormURL in config.txt is formatted correctly.",e);
			Logger.outError("The value we have for FormURL is currently:"+c.getFormURL());
		}
		URLConnection conn = url.openConnection();
		
	}
}
