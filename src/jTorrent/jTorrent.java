package jTorrent;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class jTorrent {
	public static void main(String[] args) {
		File f = new File("test.torrent");
		try {
			Scanner scan2 = new Scanner(f);
			Scanner scan = new Scanner("d848248:announce");
			String s = scan2.next();
			//String s2 = scan2.next();
			parseDictionary(s);
			//scanner not needed now. Loop through string. 
			//System.out.println(s);
			//System.out.println(s.indexOf(':'));
			//step one find first colon
			//the string should go from 
			int firstColon = s.indexOf(':');
			int firstDigit = 0;
			//number before colon.. go backward to find integers?
			for(int i = firstColon-1; i > 0; i--) {
				if(s.charAt(i)>=48 && s.charAt(i)<=57)
					firstDigit = i;
				else
					break;
			}
			
			//String z = "-5";
			//int a = Integer.parseInt(z);
			//System.out.println(a);
			//System.out.println(firstDigit);
			//s.substring does NOT include the last char.
			//ystem.out.println("Length: " + s.substring(firstDigit, firstColon));
			//any number of integers ending in a colon, trim the string? or is there a better way?
			
			//what will every string start with?
			//d, l, i, 
			//does every torrent file start with a dictionary?
			
			
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	//be able to return a dictionary of dictionaries
	public static void parseDictionary(String s) {
		//what happens if there's a d, but it's part of the url link?
		int firstD = s.indexOf('d');
		//if there is no number after the d, needs to go to the next one
		while(firstD!=-1 && !charIsInt(s.charAt(2))) {
			//beginning index, just needs to come after the d. 
			s=s.substring(1);
			firstD = s.indexOf('d');
			if(firstD!=-1) {
				s=s.substring(firstD);
				System.out.println(s);
			} else break;
		}
		//wouldn't this just be s.charAt(2)?
		if(firstD!=-1 && charIsInt(s.charAt(2))) {
			System.out.println(s.substring(1));
			parseDictionary(s.substring(1));
			System.out.println("Found dictionary at : " + firstD);

		}
//		int firstDigit = 0;
//		//number before colon.. go backward to find integers?
//		for(int i = firstColon-1; i > 0; i--) {
//			if(s.charAt(i)>=48 && s.charAt(i)<=57)
//				firstDigit = i;
//			else
//				break;
//		}
	}
	public static boolean charIsInt(char c) {
		//if(s.charAt(i)>=48 && s.charAt(i)<=57)
		return (c>=48 && c<=57) ? true : false;
	}

}
