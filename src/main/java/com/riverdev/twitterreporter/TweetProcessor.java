package com.riverdev.twitterreporter;

import org.apache.camel.Exchange;

import twitter4j.Status;

public class TweetProcessor {

	public void process(Exchange exchange) {
		Status tweet = exchange.getIn().getBody(Status.class);
		
		System.out.println(tweet.toString());
	}
}