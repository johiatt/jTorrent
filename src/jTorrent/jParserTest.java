package jTorrent;


import static org.junit.Assert.*;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class jParserTest {
	
	String validEncoding;
	List<DecodedValue> expectedDecodings;
	jParser jParser;
	
	@BeforeEach
	public void initData() {
		validEncoding = "d3:bar4:spam3:fooi42ee";
		
		expectedDecodings = new LinkedList<DecodedValue>(); 
		
		DecodedValue expectedDecoding = new DecodedValue(3, "bar");
		DecodedValue expectedDecoding1 = new DecodedValue(4, "spam");
		DecodedValue expectedDecoding2 = new DecodedValue(3, "foo");
		DecodedValue expectedDecoding3 = new DecodedValue(42, "42");
		
		expectedDecodings.add(expectedDecoding);
		expectedDecodings.add(expectedDecoding1);
		expectedDecodings.add(expectedDecoding2);
		expectedDecodings.add(expectedDecoding3);
		
		jParser = new jParser();
	}
	
	
	@Test
	void ShouldDecodeEncodedString() throws IOException {
		
		List<DecodedValue> actualDecodings = jParser.getDictionary(validEncoding);
		
		assertArrayEquals(expectedDecodings.toArray(), actualDecodings.toArray());
	}

}