package test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.junit.Test;

import sched.ScheduleRequest;
import data.in.DataRetriever;

public class DataRetrieverTest {

	//Oh my god.
	String XML_ONE_ENTRY = "<all test='hi'><fs_response status=\"success\" timestamp=\"2014-02-25 17:07:06\"><results><result id=\"7627079\"><metas><meta id=\"result_status\">Complete</meta><meta id=\"date_start\">2014-02-25 17:03:05</meta><meta id=\"date_finish\">2014-02-25 17:06:29</meta><meta id=\"date_update\">2014-02-25 17:06:29</meta><meta id=\"user_ip\">66.253.168.140</meta><meta id=\"user_browser\">Chrome</meta><meta id=\"user_os\">Windows</meta><meta id=\"user_referrer\">N/A</meta><meta id=\"payment_amount\">0.00</meta></metas><items><item id=\"0\" index=\"1\" type=\"text\"><value>test</value></item><item id=\"1\" index=\"2\" type=\"text\"><value>test</value></item><item id=\"2\" index=\"3\" type=\"text\"><value>12/22/1992</value></item><item id=\"68\" index=\"4\" type=\"list\"><value index=\"0\">Male</value></item><item id=\"6\" index=\"5\" type=\"text\"><value>888-888-8888</value></item><item id=\"3\" index=\"6\" type=\"text\"><value>123 Test Lane</value></item><item id=\"4\" index=\"7\" type=\"text\"><value>Test City</value></item><item id=\"5\" index=\"8\" type=\"text\"><value>88888</value></item><item id=\"41\" index=\"9\" type=\"text\"><value>test</value></item><item id=\"42\" index=\"10\" type=\"text\"><value>test</value></item><item id=\"7\" index=\"11\" type=\"text\"><value>test@tester.com</value></item><item id=\"8\" index=\"12\" type=\"text\"><value>999-999-9999</value></item><item id=\"9\" index=\"13\" type=\"text\"><value>111-111-1111</value></item><item id=\"10\" index=\"14\" type=\"text\"><value>test@testee.com</value></item><item id=\"11\" index=\"15\" type=\"text\"><value>12th</value></item><item id=\"12\" index=\"16\" type=\"text\"><value>Test High</value></item><item id=\"13\" index=\"17\" type=\"text\"><value>Test City</value></item><item id=\"14\" index=\"18\" type=\"text\"><value>88889</value></item><item id=\"43\" index=\"19\" type=\"text\"><value>1st</value></item><item id=\"54\" index=\"20\" type=\"text\"><value>Accordion</value></item><item id=\"23\" index=\"21\" type=\"text\"><value>4</value></item><item id=\"52\" index=\"22\" type=\"text\"><value>Theremin</value></item><item id=\"24\" index=\"23\" type=\"text\"><value>5</value></item><item id=\"34\" index=\"25\" type=\"list\"><value index=\"1\">No</value></item><item id=\"57\" index=\"26\" type=\"text\"><value>n/a</value></item><item id=\"49\" index=\"27\" type=\"list\"><value index=\"1\">No</value></item><item id=\"35\" index=\"28\" type=\"text\"><value>n/a</value></item><item id=\"36\" index=\"29\" type=\"text\"><value>No</value></item><item id=\"38\" index=\"31\" type=\"text\"><value>N/A</value></item><item id=\"74\" index=\"33\" type=\"list\"><row index=\"0\"><value index=\"0\">1</value></row><row index=\"1\"><value index=\"0\">3</value></row><row index=\"2\"><value index=\"0\">2</value></row></item><item id=\"61\" index=\"34\" type=\"text\"><value>Test Test Test</value></item><item id=\"71\" index=\"35\" type=\"text\"><value>sever408@umn.edu</value></item></items></result></results></fs_response></all>";
	
//	@Test
	public void testDataRetrieverDumb() {
		DataRetriever dr = new DataRetriever();
		dr.parseAllFormData();//This doesn't work yet. lol.
	}
	
	@Test
	public void testRawFormDataParse() throws Exception{
		SAXReader read = new SAXReader();
		Document doc = read.read(new File("./src/test/test.xml"));
		StringBuilder sb = new StringBuilder(doc.asXML());
		List<ScheduleRequest> lst = new ArrayList<ScheduleRequest>();
		DataRetriever dr = new DataRetriever();
		lst = dr.parseRawFormData(sb.toString());
		System.out.println(lst);
	}
	
//	@Test
	public void Idlikesomedataplease() throws Exception{
		DataRetriever dr = new DataRetriever();
		int i = dr.getNumberOfDataPages();
		StringBuilder allRaw = new StringBuilder("<all>");
		for(int j = 0; j<i; j++){
			allRaw.append(dr.retrieveRawFormData(j));
		}
		allRaw.append("</all>");
		Files.write(FileSystems.getDefault().getPath("", "test.xml"), allRaw.toString().getBytes(), StandardOpenOption.CREATE);
	}

}
