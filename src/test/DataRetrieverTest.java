package test;

import static org.junit.Assert.*;

import org.junit.Test;

import data.in.DataRetriever;

public class DataRetrieverTest {

	
	@Test
	public void testDataRetrieverDumb() {
		DataRetriever dr = new DataRetriever();
		dr.parseAllFormData();//This doesn't work yet. lol.
	}

}
