/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.threeriverdev.twitterreporter;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import twitter4j.FilterQuery;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * A Camel Router
 */
public class TwitterBot {
	
	private static final double GRIDSIZE = 10.0;

    public static void main(String... args) throws Exception {
        FilterQuery query = new FilterQuery();
        List<double[]> locations = new ArrayList<double[]>();
        
    	for (double swLat = 25.0; swLat <= 49.0; swLat = swLat + GRIDSIZE) {
			for (double swLon = -125.0; swLon <= -67.0; swLon = swLon + GRIDSIZE) {
				double neLat = swLat + GRIDSIZE;
				double neLon = swLon + GRIDSIZE;
				
				double[] swLocation = new double[2];
				swLocation[0] = swLon;
				swLocation[1] = swLat;
				locations.add(swLocation);
				
				double[] neLocation = new double[2];
				neLocation[0] = neLon;
				neLocation[1] = neLat;
				locations.add(neLocation);
			}
		}
    	
    	query.locations(locations.toArray(new double[0][0]));
    	
        try {
        	URL url = TwitterBot.class.getResource("/oauth.properties");
            Properties p = new Properties();
        	InputStream inStream = url.openStream();
            p.load(inStream);
            
            ConfigurationBuilder confBuilder = new ConfigurationBuilder();
            confBuilder.setOAuthConsumerKey(p.getProperty("consumer.key"));
            confBuilder.setOAuthConsumerSecret(p.getProperty("consumer.secret"));
            confBuilder.setOAuthAccessToken(p.getProperty("access.token"));
            confBuilder.setOAuthAccessTokenSecret(p.getProperty("access.token.secret"));
            Configuration conf = confBuilder.build();
            
            TwitterStream twitterStream = new TwitterStreamFactory(conf).getInstance();
            twitterStream.addListener(new TweetProcessor());
            twitterStream.filter(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
