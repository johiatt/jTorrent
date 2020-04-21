package jTorrent;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import org.slf4j.Logger;

public class jTorrent {
	
	static Logger _logger = new UrlTools().logger;

	public static void main(String[] args) {
		jParser parser = new jParser();
		File testFile = new File("test.torrent");
		String tEncoding = "";
		dList decodedValues = null;
		InputStream stream = null;
		UrlTools urlTools = new UrlTools();

		try {
			tEncoding = parser.getFirstLine(testFile);
			stream = new ByteArrayInputStream(tEncoding.getBytes());

			decodedValues = parser.parseDictionary(stream);
			stream.close();

			dList trackers = parser.filterTrackers(decodedValues);

			System.out.println(trackers.toString());

			// TODO: Should probably be careful about how this is done so we don't DDOS
			// anyone.

			for (DecodedValue decodedValue : trackers) {
				String tracker = decodedValue.getContents();
				boolean isHttps = decodedValue.getContents().startsWith("https");
				boolean isUdp = decodedValue.getContents().startsWith("udp");
				// TODO: How do we get the rest of the info here?
				if(!isUdp) {
//					String result = urlTools.getRequest(tracker, isHttps);
				} else {
					//TODO Decide type
					Object result = urlTools.getUdpRequest(tracker); 
				}
				
//				 String peer_id = "&peer_id=ABCDEFGHIJKLMNOPQRST";
//				 String rest = "&port=6881&uploaded=0&downloaded=0&left=727955456&event=started&numwant=100&no_peer_id=1&compact=1";
				// System.out.println(result);
			}

		} catch (Exception e) {
			_logger.debug(e.getMessage());
			System.err.println(e.getMessage());
		}

		System.out.println("finish");
	}
}
