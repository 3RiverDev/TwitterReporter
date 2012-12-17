package org.threeriverdev.twitterreporter;

import org.apache.camel.Exchange;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.threeriverdev.twitterreporter.data.HibernateUtil;
import org.threeriverdev.twitterreporter.data.ProcessedTweet;


public class TopicProcessor {
	
	@Autowired
	private HibernateUtil hibernateUtil;
	
	public void process(Exchange exchange) {
		ProcessedTweet pt = exchange.getIn().getBody(ProcessedTweet.class);
		
		if (pt.getTokens().length > 0) {
			
			// store the whole ProcessedTweet
			Session s = hibernateUtil.getSessionFactory().openSession();
			
			s.beginTransaction();
			s.persist( pt );
			s.getTransaction().commit();
			s.close();
		}
	}
	
	// TODO: Another method run on a timer.
	// Ex: Every 15 min., grab all ProcessedTweets from the last hour.  Run
	// through a DF algorithm to find topics.
	// Store the "topic" in a table, as well as FKs to the tweets that composed
	// it in an FK table.
}