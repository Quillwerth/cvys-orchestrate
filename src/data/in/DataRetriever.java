package data.in;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

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
	Logger log = new Logger(true);
	
	public DataRetriever(){
		c = new Config();
		c.readConfigFromFile();
	}
	
	public List<ScheduleRequest> parseAllFormData(){
		//TODO
		//Find how many pages of form data we need
		int numberOfPages = getNumberOfDataPages();
		System.out.println("Number of pages: "+numberOfPages);
		//Get all pages of form data
		StringBuilder allRaw = new StringBuilder();
		for(int i = 1; i<=numberOfPages; i++){
			allRaw.append(retrieveRawFormData(i));
		}
		System.out.println(allRaw);
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
		URL url = null;
		try {
			url = new URL(fullURL);
		} catch (MalformedURLException e) {
			Logger.outError("Orchestrate has detected an error with the URL. Please ensure that the FormURL in config.txt is formatted correctly.",e);
			Logger.outError("The value we have for FormURL is currently:"+c.getFormURL());
		}
		URLConnection conn;
		StringBuilder sb = new StringBuilder();
		try {
			 conn = url.openConnection();
			 BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			 for(String s = br.readLine(); s != null; s = br.readLine()){
				 sb.append(s);
			 }
		} catch (IOException e) {
			Logger.outError("Orchestrate was unable to connect to FormSite. Please ensure you have an internet connection, and that FormSite is accessible.", e);
		}
		return sb;
		
		
		
	}
	
	public int getNumberOfDataPages(){
		String fullURL = c.getFormURL()+"results/count?fs_api_key="+c.getAPIKey();
		log.outDebug(fullURL);
		URL url = null;
		try {
			url = new URL(fullURL);
		} catch (MalformedURLException e) {
			Logger.outError("Orchestrate has detected an error with the URL. Please ensure that the FormURL in config.txt is formatted correctly.",e);
			Logger.outError("The value we have for FormURL is currently:"+c.getFormURL());
		}
		SAXReader reader = new SAXReader();
		int pages = -1;
		try {
			Document doc = reader.read(url);
			Element root = doc.getRootElement();
			for( Iterator<Element> i = root.elementIterator("count"); i.hasNext(); ){
				Element count = i.next(); // should be the very first (and only)
				pages = (int) Math.ceil(Integer.parseInt(count.getText()) / 100.0);
			}
		} catch (DocumentException e) {
			//
		}
		return pages;
	}
}
