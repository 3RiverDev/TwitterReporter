package org.threeriverdev.twitterreporter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.threeriverdev.twitterreporter.data.HibernateUtil;
import org.threeriverdev.twitterreporter.data.ProcessedTweet;



public class TopicProcessor {
	
	// TODO: Another method run on a timer.
	// Ex: Every 15 min., grab all ProcessedTweets from the last hour.  Run
	// through a DF algorithm to find topics.
	// Store the "topic" in a table, as well as FKs to the tweets that composed
	// it in an FK table.
	
	// size of lat/lon grid cells to search within (in degrees)
	private static final double GRID_SIZE = .5;
	
	// size of time period to search (in minutes)
	private static final int TIME_SIZE = 30;
	
	public static void main(String... args) {
		final Calendar start = Calendar.getInstance();
		// month is 0 based
		start.set(2013, 3, 12, 0, 0);
		final Calendar now = Calendar.getInstance();
		while (start.before(now)) {
			final Calendar end = Calendar.getInstance();
			end.setTime(start.getTime());
			end.add(Calendar.MINUTE, TIME_SIZE);
			generateTopics(start.getTime(), end.getTime());
			start.add(Calendar.MINUTE, TIME_SIZE);
		}
	}
	
	public static void run() {
		final Calendar now = Calendar.getInstance();
		final Calendar past = Calendar.getInstance();
		past.add(Calendar.MINUTE, -TIME_SIZE);
		generateTopics(past.getTime(), now.getTime());
	}
	
	private static void generateTopics(Date start, Date end) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		try {
			// load them all in mem
			final List<ProcessedTweet> tweets = session.createCriteria(ProcessedTweet.class).add(
					Restrictions.between("timestamp", start, end)).list();
			
			if (tweets.size() > 1) {
				// not sure if this is best -- might be better to do this in the
				// queries, but evaluate performance
				for (double swLat = 24.0; swLat <= (50.0 - GRID_SIZE); swLat = swLat + GRID_SIZE) {
				    for (double swLon = -125.0; swLon <= (-67.0 - GRID_SIZE); swLon = swLon + GRID_SIZE) {
				    	final double neLat = swLat + GRID_SIZE;
				    	final double neLon = swLon + GRID_SIZE;
				    	final double midLat = swLat + (GRID_SIZE / 2.0);
				    	final double midLon = swLon + (GRID_SIZE / 2.0);
				    	
				    	final List<String> gridWords = new ArrayList<String>();
				    	final List<ProcessedTweet> gridTweets = new ArrayList<ProcessedTweet>();
				    	for (ProcessedTweet tweet : tweets) {
				    		if (tweet.getLat() >= swLat && tweet.getLat() <= neLat
				    				&& tweet.getLon() >= swLon && tweet.getLon() <= neLon) {
				    			Collections.addAll(gridWords, tweet.getTokens());
				    			gridTweets.add(tweet);
				    		}
				    	}
				    	
				    	// TODO: Is this a good test?  Could probably be *a lot* higher.
				    	if (gridWords.size() > 10) {
				    		// DF
				    		Set<String> uniqueGridWords = new HashSet<String>(gridWords);
				    		for (String uniqueGridWord : uniqueGridWords) {
				    			double count = Collections.frequency(gridWords, uniqueGridWord);
				    			double df = count / ((double)gridWords.size());
				    			// TODO
				    			if (count > 5 && df > .05) {
				    				System.out.println(uniqueGridWord + ": " + df + " (" + swLat + ", " + swLon + ")");
				    			}
				    		}
				    	}
				    }
				}
			}
		} finally {
			session.getTransaction().commit();
			session.close();
		}
	}
}