package org.threeriverdev.twitterreporter.data;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.commons.lang.StringUtils;

import twitter4j.Status;

@Entity
public class ProcessedTweet implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private static final char DELIMITER = ',';

	@Id
	@GeneratedValue
	private long id;

	private double lat;

	private double lon;

	private String originalText;

	private String processedText;

	public static ProcessedTweet create(Status tweet, List<String> tokens) {
		ProcessedTweet pt = new ProcessedTweet();
		pt.setOriginalText( tweet.getText() );
		pt.setLat( tweet.getGeoLocation().getLatitude() );
		pt.setLon( tweet.getGeoLocation().getLongitude() );
		pt.setProcessedText( StringUtils.join( tokens, DELIMITER ) );
		return pt;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getOriginalText() {
		return originalText;
	}

	public void setOriginalText(String originalText) {
		this.originalText = originalText;
	}

	public String getProcessedText() {
		return processedText;
	}

	public void setProcessedText(String processedText) {
		this.processedText = processedText;
	}
	
	public String[] getTokens() {
		return StringUtils.split(processedText, DELIMITER);
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
}
