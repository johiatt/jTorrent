package jTorrent;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class jTorrent {
	public static void main(String[] args) throws IOException {
		
		jParser parser = new jParser();
		File testFile = new File("test.torrent");
		String tEncoding = "";
		
		
		try {
			tEncoding = parser.getFirstLine(testFile);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//System.out.println(tEncoding);
		InputStream stream = new ByteArrayInputStream(tEncoding.getBytes(StandardCharsets.UTF_8));
		//TODO:
		List<Object> values = parser.parseDictionary(stream);
		
		//System.out.println(parser.toString(values));
		System.out.println("finish");
	}


}
