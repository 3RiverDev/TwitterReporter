package org.threeriverdev.twitterreporter.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Brett Meyer
 */
public class StopwordsReader {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Set<String> words = new HashSet<String>();
		
		File outFile = new File("stopwords/generated.txt");
		outFile.delete();

		File dir = new File( "stopwords" );
		for ( File stopwordsFile : dir.listFiles() ) {
			try {
				String contents = readFile( stopwordsFile.getAbsolutePath() );
				List<String> split = Arrays.asList( contents.split( "\\s+" ) );
				System.out.println(split.get( 0 ));
				// 1st line is the url -- skip it
				words.addAll( split.subList( 1, split.size() ) );
			}
			catch ( Exception e ) {
				e.printStackTrace();
			}
		}

		try {
			BufferedWriter bw = new BufferedWriter( new FileWriter(outFile, true ) );
			for ( String word : words ) {
				bw.write( word.toLowerCase() );
				bw.newLine();
			}
			bw.close();
		}
		catch ( Exception e ) {
			e.printStackTrace();
		}

	}

	private static String readFile(String path) throws IOException {
		BufferedReader reader = new BufferedReader( new FileReader( path ) );
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		String ls = System.getProperty( "line.separator" );

		while ( ( line = reader.readLine() ) != null ) {
			stringBuilder.append( line );
			stringBuilder.append( ls );
		}

		reader.close();

		return stringBuilder.toString();
	}

}
