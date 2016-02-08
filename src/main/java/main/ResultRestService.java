package main;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/results")
public class ResultRestService {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Result getDefaultUserInJSON() {
        ResultService resultService = new ResultService();
        return resultService.getDefaultResult();
    }
}

