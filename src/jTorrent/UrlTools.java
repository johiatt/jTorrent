package jTorrent;

import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.HttpResponseBodyPart;
import org.asynchttpclient.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UrlTools {

	public Logger logger;
	private boolean started;

	public UrlTools() {
		logger = LoggerFactory.getLogger(UrlTools.class);
	}

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

		if (result.equals("error code: 1010")) {
			logger.info("Firebase error: " + result);
			throw new ProtocolException("403 Forbidden: Try setting https flag to true");
		}

		return result;
	}

	public synchronized File getRequestAsync(String uri, String fileName, String fileType) throws IOException {
		File file = new File(fileName + (!fileType.isEmpty() ? fileType : ".data"));

		if (file.exists()) {
			file.delete();
		}

		file.createNewFile();

		FileOutputStream stream = new FileOutputStream(file);
		AsyncHttpClient client = Dsl.asyncHttpClient();

		try {
			client.prepareGet(uri).execute(new AsyncCompletionHandler<FileOutputStream>() {

				@Override
				public State onBodyPartReceived(HttpResponseBodyPart bodyPart) throws Exception {
					stream.getChannel().write(bodyPart.getBodyByteBuffer());
					return State.CONTINUE;
				}

				@Override
				public FileOutputStream onCompleted(Response response) throws Exception {
					return stream;
				}
			}).get();
		} catch (InterruptedException e) {
			logger.debug("Thread was interrupted " + e.getMessage());
		} catch (ExecutionException ex) {
			logger.debug("Execution error " + ex.getCause());
		}

		stream.getChannel().close();
		client.close();

		return file;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
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

	public InetAddress getUdpRequest(String tracker) throws UnknownHostException {
		try {
			if(!started) {
				startServer();
				setStarted(true);
			}
		} catch (IOException e) {
			logger.debug("UrlTools: startServer failed" + e.getMessage());
		}
		
		// https://www.baeldung.com/udp-in-java
		DatagramSocket socket = null;
		InetAddress address = null;
		
		try {
			socket = new DatagramSocket();
			address = InetAddress.getByName(tracker);
			System.out.println(address);
		} catch (SocketException e) {
			logger.debug(e.getMessage());
			System.out.println("Socket Exception" + e.getStackTrace());
		} finally {
			if (socket != null) {
				socket.close();
			}
		}

		return address;
	}

	private void startServer() throws IOException {
		DatagramSocket ds = new DatagramSocket(1234);
		byte[] receive = new byte[65535];

		DatagramPacket DpReceive = null;
		while (true) {

			// Step 2 : create a DatgramPacket to receive the data.
			DpReceive = new DatagramPacket(receive, receive.length);

			// Step 3 : revieve the data in byte buffer.
			ds.receive(DpReceive);

			System.out.println("Client:-" + data(receive));

			// Exit the server if the client sends "bye"
			if (data(receive).toString().equals("bye")) {
				System.out.println("Client sent bye.....EXITING");
				break;
			}

			// Clear the buffer after every message.
			receive = new byte[65535];
		}
	}

	public static StringBuilder data(byte[] a) {
		if (a == null)
			return null;
		StringBuilder ret = new StringBuilder();
		int i = 0;
		while (a[i] != 0) {
			ret.append((char) a[i]);
			i++;
		}
		return ret;
	}

	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}

}
