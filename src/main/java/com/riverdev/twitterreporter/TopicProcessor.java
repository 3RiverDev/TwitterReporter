package com.riverdev.twitterreporter;

import org.apache.camel.Exchange;

import com.riverdev.twitterreporter.data.ProcessedTweet;

public class TopicProcessor {
	
	public void process(Exchange exchange) {
		ProcessedTweet pt = exchange.getIn().getBody(ProcessedTweet.class);
		
		// TODO
		
		System.out.println(pt.tokensToString() + " (" + pt.getOriginalTweet().getText() + ")");
	}
}