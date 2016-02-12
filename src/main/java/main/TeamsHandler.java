package main;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/teams")
public class TeamsHandler {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTeams() {
    	return Response.status(200).entity(ResultService.getTeams()).build();
    }
}
