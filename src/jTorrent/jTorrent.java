package jTorrent;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;

public class jTorrent {

	public static void main(String[] args) {
		jParser parser = new jParser();
		File testFile = new File("test.torrent");
		String tEncoding = "";
		dList decodedValues = null;
		InputStream stream = null;
		UrlTools urlTools = new UrlTools();

		try {
			tEncoding = parser.getFirstLine(testFile);
			// System.out.println(tEncoding);
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
				// TODO: How do we get the rest of the info here?
				// String result = urlTools.getRequest(tracker, isHttps);
				// System.out.println(result);
			}

		} catch (Exception e) {
			urlTools.logger.debug(e.getMessage());
		}

		System.out.println("finish");
	}
}
