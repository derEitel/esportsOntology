package main;

import java.util.Iterator;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

@Path("/team")
public class TeamHandler {
	@GET
	@Path("{name}")
	@Produces(MediaType.APPLICATION_JSON)
    public Response getTeam (@PathParam("name") String name) {
    	ResultService resultService = new ResultService();
		Iterator<Object> iter = new JSONObject(resultService.getDefaultResult().getTeams()).getJSONArray("data").iterator();

		JSONObject team = null;
		while(iter.hasNext() && team==null) {
			JSONObject temp = (JSONObject)iter.next();
			if (temp.get("name").equals(name))
				team = temp;
		}
		
		return Response.status(200).entity(team.toString()).build();
    }
}
