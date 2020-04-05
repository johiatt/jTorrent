package jTorrent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

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
	
	public List<DecodedValue> getDictionary(String encoding) {
		
		List<DecodedValue> decodedValues = new LinkedList<DecodedValue>();
		
		//TODO: Replace temp decoded value with values decoded/parsed from the given string.
		DecodedValue temp = new DecodedValue(8, "Contents");
		
		decodedValues.add(temp);
		
		return decodedValues;
	}
	
	public String toString(List<DecodedValue> list) {
		String output = "";
		
		for (DecodedValue decodedValue : list) {
			output += "length " + decodedValue.getLength() + " " + "contents: " + decodedValue.getContents();
		}
		return output;
	}
}
