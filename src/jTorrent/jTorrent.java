package jTorrent;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class jTorrent {
	
	public static void main(String[] args){
        jParser parser = new jParser();
        File testFile = new File("test.torrent");
        String tEncoding = "";
        dList decodedValues = null;
        InputStream stream = null;
        
        try {
            tEncoding = parser.getFirstLine(testFile);
            //System.out.println(tEncoding);
            stream = new ByteArrayInputStream(tEncoding.getBytes());
            
            decodedValues = parser.parseDictionary(stream);
            stream.close();
        } catch (IOException e) {
        	System.err.println(e.getMessage());
        }
//        System.out.println(decodedValues.toString());
        System.out.println("finish");
    }
}
