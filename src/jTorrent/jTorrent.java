package jTorrent;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

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
            e.printStackTrace();
        }
        System.out.println(decodedValues.toString());
        System.out.println("finish");
    }
}
