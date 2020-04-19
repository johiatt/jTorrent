package jTorrent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class jParser 
{
	
	UrlTools urlTools = new UrlTools();
	
	//be able to return a dictionary of dictionaries
	public String getFirstLine(File file) throws IOException 
	{
//		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
//		
//		String fullLine = "";
//		
//		while(bufferedReader.ready())
//		{
//			//maybe we use a separate reader?
//			String append = bufferedReader.readLine();
//			fullLine += append;
//		}
//		
//		bufferedReader.close();
//		System.out.println(fullLine.length());
//		
//		return fullLine;
		return new String ( Files.readAllBytes( Paths.get(file.getPath())));
	}
	
	//value pairs
	public dList parseDictionary(InputStream stream) throws IOException {
		dList dictionary = new dList();
		while(stream.available()!=0) {
			//grab char and reset stream position
			//System.out.println(stream.available());
			DecodedValue input; 
			stream.mark(0);
			char c = (char)stream.read();
			stream.reset();
			if(c=='d') {
				c = (char)stream.read();
				dictionary.addAll(parseDictionary(stream));
			} else if (c =='l') {
				c = (char)stream.read();
				dictionary.addAll(parseList(stream));	
			} else if (c =='i') {
				c = (char)stream.read();
				dictionary.add(parseInt(stream));	
			} else if (charIsInt(c)) {
				//find pieces, read in length and the sets of 20 bytes, 40 hex.
				input = parseString(stream);
				if(input.getContents().contentEquals("pieces")) {
					dictionary.add(input);
					DecodedValue pieces = parseInt(stream);
					dictionary.add(pieces);
					dictionary.addAll(parseHashBytes(stream, Integer.parseInt(pieces.getContents())));
				} else
					dictionary.add(input);
			} else if (c =='e') {
				c = (char)stream.read();
				return dictionary;
			} 
		}
		return dictionary;
	}
	
	public dList parseList(InputStream stream) throws IOException{
		int length;
		dList list = new dList();
		DecodedValue input; 
		//add any of these things TO this list, and at an e, return the entire list..
		while(stream.available()!=0) {
			//grab char and reset stream position
			//System.out.println(stream.available());
			stream.mark(0);
			char c = (char)stream.read();
			stream.reset();
			if(c=='d') {
				c = (char)stream.read();
				list.addAll(parseDictionary(stream));
			} else if (c =='l') {
				c = (char)stream.read();
				list.addAll(parseList(stream));
			} else if (c =='i') {
				c = (char)stream.read();
				list.add(parseInt(stream));
			}  else if (charIsInt(c)) {
				//c = (char)stream.read();
				list.add(parseString(stream));
			}  else if (c =='e') {
				c = (char)stream.read();
				return list;
			}
		}
		return list;
	}
	
	private DecodedValue parseString(InputStream stream) throws IOException {
		return getStreamChars(stream, getLength(stream));
	}
	
	private DecodedValue parseInt(InputStream stream) throws IOException {
		Integer length = getLength(stream);
		DecodedValue dv = new DecodedValue(length, length.toString());
		return dv;
	}
	
	public dList parseHashBytes(InputStream stream, int pieces) throws IOException{
		dList list = new dList();
		int hexCount = 0;
		String hex;
		while (stream.available() != 0 && hexCount < (pieces/20)) {
			hex = "";
			//adds 7 F's when reading nothing?
			//40 hex chars, 20 bytes.
			//use byte array rather than string? 
			
			for (int i = 0; i < 20; i++) {
				if (stream.available() != 0) {
					byte b = (byte) stream.read();
					//byte b = (byte) stream.read();type name = new type();
					//byteArray[i] = (byte) stream.read();
					//pull out byte, convert to char, i need 20 bytes
					//char ch = (char)b;
//					//the byte is a signed integer, 
					//String z = ("\\x");
					//System.out.println(z);
//					//saved as \\ but prints as \... 
//					String hexAppend = z+"x";
//					String h = (String.format("%02X", b).toLowerCase());
//					String zs = hexAppend+h;
					hex += ("%"+(String.format("%02X", b).toLowerCase()));
					//hex += b;
				}
			}
			//http://torrent.ubuntu.com:6969/file?info_hash=%9A%813%3C%1B%16%E4%A8%3C%10%F3%05%2C%15%90%AA%DF%5E.%20
			//http://torrent.ubuntu.com:6969/announce?info_hash=%9A%813%3C%1B%16%E4%A8%3C%10%F3%05%2C%15%90%AA%DF%5E.%20&peer_id=ABCDEFGHIJKLMNOPQRST&port=6881&uploaded=0&downloaded=0&left=727955456&event=started&numwant=100&no_peer_id=1&compact=1
			
			String tracker = "http://open.acgnxtracker.com/announce?";
			String info_hash = "info_hash="+hex;
			//not in hex
			String peer_id = "&peer_id=ABCDEFGHIJKLMNOPQRST";
			String rest = "&port=6881&uploaded=0&downloaded=0&left=727955456&event=started&numwant=100&no_peer_id=1&compact=1";
			//System.out.println(tracker+info_hash+peer_id+rest);
			
			String response = urlTools.getRequest(tracker, info_hash + peer_id + rest);
			System.out.println(response);
			//hex = UrlTools.encodeValue(hex);
			hexCount++;
			//hex = hex.replace("'", "\"");
			//String test = UrlTools.encodeValue(hex);
			//String byteTest = encode(byteArray);
			//System.out.println(byteTest);

			DecodedValue contents = new DecodedValue(hex.length(), hex);
			list.add(contents);
		}
		list.add(new DecodedValue(0,hexCount + " 20 byte hex values"));
		//System.out.println(hexCount + " 20 byte hex values");
		return list;
	}
	
	private int getLength(InputStream stream) throws IOException {
		String length = "";
		do {
			//when this finds a ":", it will have gone past it. 
			char c = (char)stream.read();
			if(charIsInt(c)) {
				length+=c;
			} else 
				break;
		} while(true);
		int valueLength = Integer.parseInt(length);
		return valueLength;
	}
	
	private boolean charIsInt(char c) {
		return (c>=48 && c<=57) ? true : false;
	}
	
	private DecodedValue getStreamChars(InputStream stream, int length) throws IOException {
		DecodedValue contents = null;
		String value = "";
		
		for(int i = 0; i < length; i++) {
			value+=(char)stream.read();
		}
		
		contents = new DecodedValue(length, value);
		return contents;
	}
}
