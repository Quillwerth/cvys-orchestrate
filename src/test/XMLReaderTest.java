package test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class XMLReaderTest {

	static String XML1 = "<outer> <mid id='2'>\"Hi.\"</mid> </outer> ";
	static String XML2 = "<outer> <mid> <low> \"First.\" </low> <low> \"Second.\" </low> <low> \"Third.\" </low> </mid> </outer> ";
	
	@BeforeClass
	public static void init(){
		try {
			Files.write(FileSystems.getDefault().getPath("", "file1.xml"), XML1.getBytes(), StandardOpenOption.CREATE_NEW);
			Files.write(FileSystems.getDefault().getPath("", "file2.xml"), XML2.getBytes(), StandardOpenOption.CREATE_NEW);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testReadXML1() throws DocumentException {
		SAXReader read = new SAXReader();
		Document doc = read.read(new File("file1.xml"));
		Element e = doc.getRootElement();
		for(Iterator<Element> i = e.elementIterator("mid"); i.hasNext();){
			Element inner = i.next();
			assertEquals("we wanted \"Hi.\" We found "+inner.getText(), inner.getText(), "\"Hi.\"");
			assertEquals(inner.attributeValue("id"), "2");
		}
	}
	
	@Test
	public void testReadXML2() throws DocumentException {
		SAXReader read = new SAXReader();
		Document doc = read.read(new File("file2.xml"));
		Element e = doc.getRootElement();
		System.out.println(e.element("mid").elements("low"));
	}
	
	@AfterClass
	public static void end(){
		try {
			Files.delete(FileSystems.getDefault().getPath("", "file1.xml"));
			Files.delete(FileSystems.getDefault().getPath("", "file2.xml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
