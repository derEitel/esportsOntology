package main;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import twitter4j.Status;

import java.util.List;

import com.google.gson.Gson;

@Path("/tweets")
public class TwitterHandler {

	@GET
	@Path("{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTweetsByName(@PathParam("name") String name) {
		long t0 = System.currentTimeMillis();
		
		TwitterAPIConsumer consumer = new TwitterAPIConsumer();
		
		List<Status> tweets = consumer.getTweets(name);
		Response response;
		if(tweets==null || tweets.isEmpty()){
			response = Response.status(200).entity("No tweets for that keyword").build();
		} else{
	        String json = new Gson().toJson(tweets);
			response = Response.status(200).entity(json).build();
		}
		
		long t1 = System.currentTimeMillis();
    	System.out.println("Time to retrieve a competition by name : "+(t1-t0));
    	
    	return response;
	}

}
