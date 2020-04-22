package jTorrent;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class jTorrent {

	public static void main(String[] args) {
		jParser parser = new jParser();
		File testFile = new File("test.torrent");
		String tEncoding = "";
		dList decodedValues = null;
		InputStream stream = null;
		dList trackerIP = null;
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

			trackerIP = new dList();
			
			for (DecodedValue decodedValue : trackers) {
				String tracker = decodedValue.getContents();
				boolean isHttps = decodedValue.getContents().startsWith("https");
				boolean isUdp = decodedValue.getContents().startsWith("udp");
				// TODO: How do we get the rest of the info here?
				if(!isUdp) {
//					String result = urlTools.getRequest(tracker, isHttps);
				} else {
					//TODO Decide type
					int start = tracker.indexOf("//") + 2;
					int end = tracker.indexOf(":");
					end = tracker.indexOf(":", end + 1);
					String site = tracker.substring(start, end);
					try {
						InetAddress result = urlTools.getUdpRequest(site); 
						DecodedValue dT = new DecodedValue(result.getHostName(), result.getHostAddress());
						trackerIP.add(dT);
						
					} catch (UnknownHostException e) {
						System.out.println("Try next host");
					}
				}
				
				
//				 String peer_id = "&peer_id=ABCDEFGHIJKLMNOPQRST";
//				 String rest = "&port=6881&uploaded=0&downloaded=0&left=727955456&event=started&numwant=100&no_peer_id=1&compact=1";
			}

		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
		System.out.println(trackerIP.toString());
		System.out.println("finish");
	}
}
