package jTorrent;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class jParser 
{
	//be able to return a dictionary of dictionaries
	public String getFirstLine(File file) throws IOException 
	{
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
		
		String fullLine = "";
		
		if(bufferedReader.ready())
		{
			fullLine = bufferedReader.readLine();
		}
		
		bufferedReader.close();
		
		return fullLine;
	}
	
	//value pairs
	public List<DecodedValue> parseDictionary(InputStream stream) throws IOException {
		
		//List<T> mainList = new LinkedList<T>();
		//TODO: mapped values, not just a list.
		List<DecodedValue> dictionary = new LinkedList<DecodedValue>();
		//grab first length
		

		//char c = (char)input.read();
		//read one char at a time?
		
		//can we pass stream as a parameter?
		//char start = (char)stream.read();
		//build length string out of char stream
//		String length = "";
//		do {
//			char c = (char)stream.read();
//			if(c>=48 && c<=57) {
//				length+=c;
//			} else 
//				break;
//		} while(true);
		
		//mark current spot, check character, go back to mark
		//If and only if the char after a value is another number. 
		
		
//		int length = getLength(stream);
//		String streamString = getStreamChars(stream, length);
//		System.out.println(streamString);

		
		int stack = 0;
		while(stream.available()!=0) {
			//grab char and reset stream position
			//System.out.println(stream.available());
			stream.mark(0);
			char c = (char)stream.read();
			stream.reset();
			if(c=='d') {
				c = (char)stream.read();
				System.out.println("d's found : " + (++stack));
				dictionary.addAll(parseDictionary(stream));
			} else if (c =='l') {
				c = (char)stream.read();
				dictionary.addAll(parseList(stream));	
			} else if (c =='i') {
				c = (char)stream.read();
				dictionary.add(parseInt(stream));	
			} else if (charIsInt(c)) {
				//how do we know which object to add to?
				dictionary.add(parseString(stream));
			} else if (c =='e') {
				c = (char)stream.read();
				return dictionary;
			} 
		}
		//after the FINAL e, all that's left is hash bytes.
		
//		Scanner scan = new Scanner(encoding);
//		Pattern p = Pattern.compile("d(\\d+)");
//		
//		encoding = scan.findInLine(p);
//		MatchResult result = scan.match();
//	    if(result.groupCount()>=1)
//	    	System.out.println(result.group(1));
	    //find next colon.
	    
//	    System.out.println(scan.findInLine(":[a-zA-Z]+"));
//	    System.out.println(scan.findInLine(":[a-zA-Z]+"));
//	    System.out.println(scan.next(".").charAt(0));
//	    System.out.println(scan.next(".").charAt(0));
//	    System.out.println(scan.next(".").charAt(0));

	    
	    
		
		//TODO: Replace temp decoded value with values decoded/parsed from the given string.
		//DecodedValue temp = new DecodedValue(8, "Contents");
		
		//decodedValues.add(temp);
		
		return dictionary;
	}
//	
	public List<DecodedValue> parseList(InputStream stream) throws IOException{
		int length;
		List<DecodedValue> list = new LinkedList<DecodedValue>();
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


	public String toString(List<DecodedValue> list) {
		String output = "";
		
		for (DecodedValue decodedValue : list) {
			output += "length: " + decodedValue.getLength() + " - " + "contents: " + decodedValue.getContents() + "\n";
		}
		return output;
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
	
	
//	public String processValue(InputStream stream) throws IOException {
//		
//	}
//	
//	public List<String> getList(InputStream stream) throws IOException {
//		
//	}
}
