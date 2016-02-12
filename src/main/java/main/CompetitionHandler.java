package main;

import java.util.Iterator;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

@Path("/competition")
public class CompetitionHandler {
	@GET
	@Path("{name}")
	@Produces(MediaType.APPLICATION_JSON)
    public Response getTeam (@PathParam("name") String name) {
		long t0 = System.currentTimeMillis();
		
		Iterator<Object> iter = new JSONObject(ResultService.getCompetitions()).getJSONArray("data").iterator();

		JSONObject competition = null;
		while(iter.hasNext() && competition==null) {
			JSONObject temp = (JSONObject)iter.next();
			
			if (temp.get("name").equals(name)) {
				competition = temp;
				//picture retrieval.
				competition.put("photo",ResultService.searchFlickr(name));
				System.out.println("The photo of the competitions is: "+competition.getString("photo"));
			}
		}
		Response response = Response.status(200).entity(competition.toString()).build();
		
		long t1 = System.currentTimeMillis();
    	System.out.println("Time to retrieve a competition by name : "+(t1-t0));
    	
    	return response;
    }
}
