package com.riverdev.twitterreporter;

import org.apache.camel.Exchange;
import org.hibernate.Session;

import com.riverdev.twitterreporter.data.ProcessedTweet;

public class TopicProcessor {
	
	public void process(Exchange exchange) {
		ProcessedTweet pt = exchange.getIn().getBody(ProcessedTweet.class);
		
		if (pt.getTokens().size() > 0) {
			System.out.println(pt.tokensToString() + " (" + pt.getOriginalTweet() + ")");
			
			// TODO: store the whole thing
//			Session s = null;
//			
//			s.beginTransaction();
//			s.persist( pt );
//			s.getTransaction();
//			s.close();
		}
	}
}