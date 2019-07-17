package org.warn.utils.text;

import java.util.StringTokenizer;

public class TextHelper {

	public static int getWordCount( String s ) {
		int count = 0;
		StringTokenizer st = new StringTokenizer(s);
		while( st.hasMoreTokens() ) {
			if( st.nextToken().matches("^[a-zA-Z0-9]*$") )
				count++;
		}
		return count;
	}
	
	public static boolean isEmpty( String s ) {
		return s == null || s.isEmpty();
	}

}
