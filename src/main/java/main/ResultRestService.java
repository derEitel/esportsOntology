package main;


import java.util.Collection;
import java.util.Collections;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.SearchParameters;
import com.flickr4java.flickr.test.TestInterface;

@Path("/results")
public class ResultRestService {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Result getDefaultUserInJSON() {
        ResultService resultService = new ResultService();
        return resultService.getDefaultResult();
    }
}

