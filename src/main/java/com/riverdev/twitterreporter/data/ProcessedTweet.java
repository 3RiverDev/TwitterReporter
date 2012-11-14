package com.riverdev.twitterreporter.data;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import twitter4j.Status;

@Entity
public class ProcessedTweet {

	private static final String TOSTRING_DELIMITER = " ";

	private long id;

	private String originalTweet;

	private double lat;

	private double lon;

	private List<String> tokens;

	public static ProcessedTweet create(Status tweet, List<String> tokens) {
		ProcessedTweet pt = new ProcessedTweet();
		pt.setOriginalTweet( tweet.getText() );
		pt.setLat( tweet.getGeoLocation().getLatitude() );
		pt.setLon( tweet.getGeoLocation().getLongitude() );
		pt.setTokens( tokens );
		return pt;
	}

	public String tokensToString() {
		return tokens.size() > 0 ? StringUtils.join( tokens, TOSTRING_DELIMITER ) : "REMOVED";
	}

	@Id
	@GeneratedValue
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getOriginalTweet() {
		return originalTweet;
	}

	public void setOriginalTweet(String originalTweet) {
		this.originalTweet = originalTweet;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	@Transient
	public List<String> getTokens() {
		return tokens;
	}

	public void setTokens(List<String> tokens) {
		this.tokens = tokens;
	}
}
