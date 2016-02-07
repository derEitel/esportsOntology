package main;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
//import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
//import twitter4j.auth.RequestToken;

public class TwitterAPIConsumer {
	
	public List<Status> getTweets(String keyword) {
		final Twitter twitter = new TwitterFactory().getInstance();
		
		final AccessToken accessToken = new AccessToken("56775994-X0tDfax5EHwj4UrvIwusf4AyeXY8FOrdG1tFw4B2G", "Sh99gehjm9ogaEDspUtn7IXcsi0mcjRe5uglQjQFB4pjD");
	    twitter.setOAuthConsumer("Ydw442CnlQCFwJYrVNXwg7YdZ", "Cu4xYut8IGsCRt1gNVSPTxLby7IxWzbLQK2PFvCiGpLHLtoJ5V");
	    twitter.setOAuthAccessToken(accessToken);
	    
	    
        if (keyword.isEmpty()) {
            System.out.println("No keyword specified");
            System.exit(-1);
        }
        try {
            Query query = new Query(keyword);
            QueryResult result;
            query.count(10);
            
            
            result = twitter.search(query);
            List<Status> tweets = result.getTweets();
            // Print the Tweets
            for (Status tweet : tweets) {
                System.out.println(tweet.getCreatedAt() +  " @" + tweet.getUser().getScreenName() + " - " + tweet.getText());
            }
            return tweets;
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());
            System.exit(-1);
        }
        return null;
    }

}

