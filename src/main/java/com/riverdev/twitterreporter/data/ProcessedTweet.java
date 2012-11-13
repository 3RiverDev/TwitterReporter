package com.riverdev.twitterreporter.data;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import twitter4j.Status;

public class ProcessedTweet {
	
	final private static String TOSTRING_DELIMITER = " ";
	
	final private Status originalTweet;
	final private List<String> tokens;
	
	public ProcessedTweet(Status originalTweet, List<String> tokens) {
		this.originalTweet = originalTweet;
		this.tokens = tokens;
	}

	public Status getOriginalTweet() {
		return originalTweet;
	}

	public List<String> getTokens() {
		return tokens;
	}
	
	public String tokensToString() {
		return tokens.size() > 0 ? StringUtils.join(tokens, TOSTRING_DELIMITER)
				: "REMOVED";
	}
}
