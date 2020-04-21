package Test.jTorrent;


import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jTorrent.*;

class jParserTest {
	InputStream stream;
	String validEncoding;
	dList expectedDecodings;
	jParser jParser;
	
	@BeforeEach
	public void initData() {
		validEncoding = "d3:bar4:spam3:fooi42ee";
		stream = new ByteArrayInputStream(validEncoding.getBytes(StandardCharsets.UTF_8));
		
		expectedDecodings = new dList(); 
		
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
		
		dList actualDecodings = jParser.parseDictionary(stream);
		
		assertArrayEquals(expectedDecodings, actualDecodings);
	}


	private void assertArrayEquals(dList expectedDecodes, dList actualDecodings) {
		
		if(expectedDecodes.size() != actualDecodings.size()) {
			fail("The expected decodings length[" + expectedDecodes.size() + "] did not match actual size[" + actualDecodings.size() + "]");
		}
		
		DecodedValue dv = null, adv = null;
		for(int i = 0; i < expectedDecodes.size(); i++) {
			dv = expectedDecodes.get(i);
			adv = actualDecodings.get(i);
				if(!dv.getContents().equals(adv.getContents())) {
					fail("expected contents [" + dv.getContents() +  "] did not match actual contents[" + adv.getContents() + "]");
				}
				
				if(dv.getLength() != adv.getLength()) {
					fail("expected length [" + dv.getLength() +  "] did not match actual length[" + adv.getLength() + "]");
				}
			}
		}
}
