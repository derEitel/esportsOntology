package main;

import java.util.Iterator;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

@Path("/team")
public class TeamHandler {
	@GET
	@Path("{name}")
	@Produces(MediaType.APPLICATION_JSON)
    public Response getTeam (@PathParam("name") String name) {
		long t0 = System.currentTimeMillis();
		
		Iterator<Object> iter = new JSONObject(ResultService.getTeams()).getJSONArray("data").iterator();

		JSONObject team = null;
		while(iter.hasNext() && team==null) {
			JSONObject temp = (JSONObject)iter.next();
			if (temp.get("name").equals(name)) {
				team = temp;
				//picture retrieval.
				team.put("photo",ResultService.searchFlickr(name));
			}
		}
		
		Response response = Response.status(200).entity(team.toString()).build();
		
		long t1 = System.currentTimeMillis();
    	System.out.println("Time to retrieve a team by name : "+(t1-t0));
		
		return response;
    }
}
