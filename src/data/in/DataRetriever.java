package data.in;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
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
		//Find how many pages of form data we need
		int numberOfPages = getNumberOfDataPages();
		System.out.println("Number of pages: "+numberOfPages);
		//Get all pages of form data
		StringBuilder allRaw = new StringBuilder();
		allRaw.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><all>");
		for(int i = 1; i<=numberOfPages; i++){
			allRaw.append(retrieveRawFormData(i));
		}
		allRaw.append("</all>");
		//Create ScheduleRequest objects out of all students
		return parseRawFormData(allRaw.toString());
	}
	
	public List<ScheduleRequest> parseRawFormData(String allRaw){
		//parse document
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(allRaw);
		} catch (DocumentException e) {
			Logger.outError("Orchestrate had a problem understanding the data coming from FormSite.", e);
			Logger.outError("This probably isn't your fault... but there's not too much you can do about it, either.");
			return null;
		}
		log.outDebug(doc.toString());
		Element root = doc.getRootElement();
		log.outDebug(root.toString());
		
		List<ScheduleRequest> reqs = new ArrayList<ScheduleRequest>();
		final int firstNameID = c.getXMLID("FirstName");
		final int lastNameID = c.getXMLID("LastName");
		final int rankID = c.getXMLID("Rank");

		//XML processing. Disgusting.
		//Recurse until we have the "results" objects out of all tags:
		ArrayList<Element> searching = new ArrayList<Element>();
		searching.add(root);
		ArrayList<Element> resultsBlocks = new ArrayList<Element>();
		while(!searching.isEmpty()){
			List<Element> elements = (searching.get(0).elements());
			for(Element e : elements){
				if(e.getName().equals("results")){
					resultsBlocks.add(e);
				}else{
					searching.add(e);
				}
			}
			searching.remove(0);
		}
		
		
		for(int i = 0; i<resultsBlocks.size(); i++){
			Element results = resultsBlocks.get(i);
			for(Iterator<Element> resultGetter = results.elementIterator("result"); resultGetter.hasNext();){
				Element result = resultGetter.next();
				String id = result.attributeValue("id");
				String firstName = null, lastName = null;
				List<Integer> dayPref = new ArrayList<Integer>();
				
				for(Iterator<Element> items = result.element("items").elementIterator("item"); items.hasNext();){
					Element item = items.next();
					int idOfElement = Integer.parseInt(item.attributeValue("id"));
					
					if(idOfElement == firstNameID){
						firstName = item.elementText("value");
						log.outDebug("found first name: "+firstName);
					}
					if(idOfElement == lastNameID){
						lastName = item.elementText("value");
					}
					if(idOfElement == rankID){
						log.outDebug("found rankings");
						for(Iterator<Element> rows = item.elementIterator("row"); rows.hasNext();){
							String rank = rows.next().elementText("value");
							log.outDebug("found rank: "+rank);
							dayPref.add(Integer.parseInt(rank));
						}
					}
				}
				reqs.add(new ScheduleRequest(id, firstName + " " + lastName, dayPref));
			}
		}
		return reqs;
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
			 br.readLine();//Burn the first line: <?xml...>
			 for(String s = br.readLine(); s != null; s = br.readLine()){
				 sb.append(s+"\n");
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
			Logger.outError("Orchestrate was unable to connect to FormSite. Please ensure you have an internet connection, and that FormSite is accessible.", e);
			Logger.outError("The attempted URL was:" +fullURL);
		}
		return pages;
	}
}
