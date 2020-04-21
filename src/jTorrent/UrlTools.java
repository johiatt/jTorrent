package jTorrent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.HttpResponseBodyPart;
import org.asynchttpclient.Response;

public class UrlTools {

	private String fakeIdentity = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11";

	public String getRequest(String uri, boolean https) throws ProtocolException, MalformedURLException, IOException {
		URL url = new URL(uri);
		
		HttpURLConnection con = https ? (HttpsURLConnection) url.openConnection()
				: (HttpURLConnection) url.openConnection();

		if (https) {
			con.setRequestProperty("User-Agent", fakeIdentity);
		}

		con.setRequestMethod("GET");
		con.setRequestProperty("Content-Type", "text/plain");

		int status = con.getResponseCode();

		InputStream stream = (status == HttpURLConnection.HTTP_OK) ? con.getInputStream() : con.getErrorStream();

		BufferedReader in = new BufferedReader(new InputStreamReader(stream));
		StringBuffer content = new StringBuffer();

		String response = "";

		while ((response = in.readLine()) != null) {
			content.append(response);
		}

		in.close();
		con.disconnect();
		
		String result = content.toString();
		
		if(result.equals("error code: 1010")) {
			throw new ProtocolException("403 Forbidden: Try setting https flag to true");
		}

		return result;
	}
	
	public synchronized byte[] getRequestAsync(String uri, boolean https) throws ProtocolException, MalformedURLException, IOException {
		
	File file = new File("empty.data");
	
	if(file.exists()) {
		file.delete();
	}

	file.createNewFile();
	
	FileOutputStream stream = new FileOutputStream(file);
	AsyncHttpClient client = Dsl.asyncHttpClient();
	
	try {
		client.prepareGet(uri)
		    .execute(new AsyncCompletionHandler<FileOutputStream>() {
		
		        @Override
		        public State onBodyPartReceived(HttpResponseBodyPart bodyPart) throws Exception {
		            stream.getChannel()
		                .write(bodyPart.getBodyByteBuffer());
		            return State.CONTINUE;
		        }
		
		        @Override
		        public FileOutputStream onCompleted(Response response) throws Exception {
		            return stream;
		        }
		    })
		    .get();
	} catch (InterruptedException | ExecutionException e) {
		e.printStackTrace();
	}
	
	stream.getChannel().close();
	client.close();
	
	return Files.readAllBytes(file.toPath());
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
