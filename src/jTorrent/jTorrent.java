package jTorrent;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class jTorrent {
	public static void main(String[] args) {
		
		jParser parser = new jParser();
		File testFile = new File("test.torrent");
		String tEncoding = "";
		
		
		try {
			tEncoding = parser.getFirstLine(testFile);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println(tEncoding);
		
		//TODO:
		List<DecodedValue> values = parser.getDictionary(tEncoding);
		
		System.out.println(parser.toString(values));
	}


}
