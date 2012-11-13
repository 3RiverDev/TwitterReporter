package com.riverdev.twitterreporter;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.camel.Exchange;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

import twitter4j.GeoLocation;
import twitter4j.Status;

import com.riverdev.twitterreporter.data.ProcessedTweet;

public class TweetProcessor {
	
	/** Regular expressions used during "noise cleanup" */
	private static final Pattern P_URL = Pattern.compile("(http:\\/\\/|https:\\/\\/)?([a-zA-Z0-9\\-_]+\\.)+[a-zA-Z0-9\\-_]+(\\/[A-Za-z0-9\\-_%&\\?\\/.=]*)*");
	private static final Pattern P_REPLY = Pattern.compile("@[^\\s]*");
	private static final Pattern P_ENCODED = Pattern.compile("&.*;");
	private static final Pattern P_NON_ALPHANUMERIC = Pattern.compile("[^a-zA-Z0-9\\s]*");
	
	/** Minimum number of characters left, after cleanup, to be considered. */
	private static final int MIN_NUM_TOKEN_CHARS = 4;
	
	private final StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
	
	public void process(Exchange exchange) {
		Status tweet = exchange.getIn().getBody(Status.class);
		
		List<String> tokens = new ArrayList<String>();
		
		GeoLocation location = tweet.getGeoLocation();
		// The tweet must be geotagged.
		// Skip any accounts flagged with non-English languages.
		// TODO: Skip anything with non-USASCII characters?  c3-ff
		if (location != null && tweet.getUser().getLang().equals("en")) {
			String text = tweet.getText();
			
			// remove URLs
			text = P_URL.matcher(text).replaceAll("");
			
			// remove replies
			text = P_REPLY.matcher(text).replaceAll("");
			
			// remove XHTML encoded characters
			text = P_ENCODED.matcher(text).replaceAll("");
			
			// remove non-alphanumeric characters
			text = P_NON_ALPHANUMERIC.matcher(text).replaceAll("");

			
			// Lucene StandardAnalyzer
			TokenStream ts = analyzer.tokenStream("contents", new StringReader(text));
			try {
				ts.reset();
				while (ts.incrementToken()) {
					String term = ts.getAttribute(CharTermAttribute.class).toString();
					if (term.length() >= MIN_NUM_TOKEN_CHARS) {
						tokens.add(term);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		exchange.getIn().setBody(new ProcessedTweet(tweet, tokens));
	}
}