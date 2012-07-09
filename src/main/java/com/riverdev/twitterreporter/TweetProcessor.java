package com.riverdev.twitterreporter;

import java.util.regex.Pattern;

import org.apache.camel.Exchange;

import twitter4j.GeoLocation;
import twitter4j.Status;

public class TweetProcessor {
	
	/** Regular expressions used during "noise cleanup" */
	private static final Pattern P_URL = Pattern.compile("(http:\\/\\/|https:\\/\\/)?([a-zA-Z0-9\\-_]+\\.)+[a-zA-Z0-9\\-_]+(\\/[A-Za-z0-9\\-_%&\\?\\/.=]*)*");
	private static final Pattern P_REPLY = Pattern.compile("@[^\\s]*");
	private static final Pattern P_ENCODED = Pattern.compile("&.*;");
	private static final Pattern P_NON_ALPHANUMERIC = Pattern.compile("[^a-zA-Z0-9\\s]*");
	private static final Pattern P_NEWLINE = Pattern.compile("\\n+");
	private static final Pattern P_SPACE = Pattern.compile("\\s+");
	
	/** Minimum number of characters left, after cleanup, to be considered. */
	private static final int MIN_NUM_CHARS = 4;
	
	public void process(Exchange exchange) {
		Status tweet = exchange.getIn().getBody(Status.class);
		
		GeoLocation location = tweet.getGeoLocation();
		// The tweet must be geotagged.
		// Skip any accounts flagged with non-English languages.
		// TODO: Skip anything with non-USASCII characters?  c3-ff
		if (location != null && tweet.getUser().getLang().equals("en")) {
			String text = tweet.getText();
			
			// remove URLs
			text = P_URL.matcher(text).replaceAll("");
			
			// remove replaces
			text = P_REPLY.matcher(text).replaceAll("");
			
			// remove XHTML encoded characters
			text = P_ENCODED.matcher(text).replaceAll("");
			
			// remove non-alphanumeric characters
			text = P_NON_ALPHANUMERIC.matcher(text).replaceAll("");
			
			// replace newlines with spaces
			text = P_NEWLINE.matcher(text).replaceAll(" ");
			
			// remove extra spaces
			text = P_SPACE.matcher(text).replaceAll(" ");
			
			// trim whitespace
			text = text.trim();
			
			// if enough chars left...
			if (text.length() >= MIN_NUM_CHARS) {
				System.out.println(text + " (" + tweet.getText() + ")");
			}
		}
	}
}