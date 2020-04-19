package jTorrent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownServiceException;
import java.util.Map;

public class UrlTools {

	public String getRequest(String uri, String hash) throws IOException {

		HttpURLConnection con = null;
		InputStream stream = null;
		String response = "";
		StringBuffer content = null;

		try {
			URL url = new URL(uri + hash);

			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Content-Type", "text/plain");

			int status = con.getResponseCode();


			try {
				stream = con.getInputStream();

				if (status > 299) {
					stream = con.getErrorStream();
				} else {
					stream = con.getInputStream();
				}

				// params
				//
				//			Map<String, String> parameters = new HashMap<>();
				//			parameters.put("info_hash", hash);

				//			con.setDoOutput(true);
				//			DataOutputStream out = new DataOutputStream(con.getOutputStream());
				//			out.writeBytes(getParamsString(parameters));

				//String contentType = con.getHeaderField("Content-Type");
				//			

			} catch (UnknownServiceException e) {
				System.out.println("Broken stream");
				System.err.println(e.getMessage());
			} 

			BufferedReader in = new BufferedReader(new InputStreamReader(stream));


			content = new StringBuffer();
			while ((response = in.readLine()) != null) {
				content.append(response);
			}
			in.close();

			con.disconnect();

		} catch (MalformedURLException e) {
			System.out.println("Broken URL");
			System.err.println(e.getMessage());
		}

		return content.toString();
	}

	public static String encodeValue(String value) {
		try {
			return URLEncoder.encode(value, "UTF-8");
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex.getCause());
		}
	}

	public static String getParamsString(Map<String, String> params) throws UnsupportedEncodingException {
		StringBuilder result = new StringBuilder();

		for (Map.Entry<String, String> entry : params.entrySet()) {
			result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
			result.append("&");
		}

		String resultString = result.toString();
		return resultString.length() > 0 ? resultString.substring(0, resultString.length() - 1) : resultString;
	}

}
