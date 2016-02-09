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
    	ResultService resultService = new ResultService();
		Iterator<Object> iter = new JSONObject(resultService.getDefaultResult().getCompetitions()).getJSONArray("data").iterator();

		System.out.println("nombre: "+name);
		JSONObject competition = null;
		while(iter.hasNext() && competition==null) {
			JSONObject temp = (JSONObject)iter.next();
			System.out.println("temporal: "+temp);
			if (temp.get("name").equals(name))
				competition = temp;
		}
		System.out.println("resultado: "+competition.toString());
		return Response.status(200).entity(competition.toString()).build();
    }
}
