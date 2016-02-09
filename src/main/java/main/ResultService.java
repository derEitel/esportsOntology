package main;

import main.Result;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Iterator;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import fr.inria.acacia.corese.exceptions.EngineException;
import fr.inria.edelweiss.kgram.core.Mappings;
import fr.inria.edelweiss.kgraph.core.*;
import fr.inria.edelweiss.kgraph.query.QueryProcess;
import fr.inria.edelweiss.kgtool.load.Load;
import fr.inria.edelweiss.kgtool.load.LoadException;
import fr.inria.edelweiss.kgtool.print.ResultFormat;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.SearchParameters;
import com.flickr4java.flickr.test.TestInterface;

public class ResultService {
	private Graph g;
	private JSONObject competitions;
	private JSONObject teams;
	
    public Result getDefaultResult() {
        if (g==null)
        	initializeGraph();
        
        if (competitions==null)
        	this.competitions = queryJSON("competitionsQuery.txt");
        
        if (teams==null)
        	this.teams = queryJSON("teamsQuery.txt");
        
        Result user = new Result();
        user.setCompetitions(competitions.toString());
        user.setTeams(teams.toString());

        return user;
    }

    public void initializeGraph() {
		this.g = Graph.create(true);
		Load ld = Load.create(g);
		try {
			ld.loadWE("ontology/event.ttl");
			ld.loadWE("ontology/esports.rdf");

		} catch (LoadException le) {
			System.out.println("Error loading: " + le.toString());
		}
    }
    
	public JSONObject queryJSON(String path) {
		String query = "";
		try {
			query = new String(Files.readAllBytes(Paths
					.get("ontology/"+path)));
		} catch (IOException ioe) {
			System.out.println("Error reading the query: " + ioe.toString());
		}

		// querying corese
		QueryProcess exec = QueryProcess.create(g);
		ResultFormat originalXML = null;
		try {
			Mappings map = exec.query(query);
			originalXML = ResultFormat.create(map);
		} catch (EngineException ee) {
			System.out.println("Error executing the query: " + ee.toString());
		}

		// transformation xml to JSON
		JSONObject originalJson = XML.toJSONObject(originalXML.toString());

		// cumulative variable
		JSONArray resultsArray = new JSONArray();
		// gathering the results
		Iterator<Object> resultsIterator = originalJson.getJSONObject("sparql")
				.getJSONObject("results").getJSONArray("result").iterator();
		while (resultsIterator.hasNext()) {
			// gathering the bindings
			Iterator<Object> bindingsIterator = ((JSONObject) resultsIterator
					.next()).getJSONArray("binding").iterator();
			JSONObject newValue = new JSONObject();
			while (bindingsIterator.hasNext()) {
				// reconstructing the json
				JSONObject temp = (JSONObject) bindingsIterator.next();
				String key = temp.getString("name");
				Object value = null;
				try {
					if (temp.names().get(1).toString().equals("literal")) {
						if (temp.getJSONObject("literal").names().length()>1)
							value = temp.getJSONObject("literal").get("content");
						else
							value = "";
					} else if (temp.names().get(1).toString().equals("uri"))
						value = temp.getString("uri");
					else throw new Exception ("Value not supported yet.");
				} catch (Exception e) {
					System.out.println("Exception while parsing the content: "+e.toString());
				}
				
				newValue.put(key, value);
			}
			newValue.put("photo", flickr(newValue.get("name").toString()));
			resultsArray.put(newValue);

		}
		
		return new JSONObject().put("data", resultsArray);
	}
	
	public String flickr (String name) {
    	String apiKey = "6fe5dffa0292b31684a2c21b9199e733";
    	String sharedSecret = "65058d771d850a7a";
    	Flickr f = new Flickr(apiKey, sharedSecret, new REST());
    	
    	String url="";
    	try{
    		SearchParameters sp = new SearchParameters ();
    		sp.setText(name);
    		url = f.getPhotosInterface().search(sp, 0, 0).get(0).getSmall320Url();
		} catch (FlickrException fe) {
			System.out.println("Flickr: "+fe.toString());
		} catch (Exception e) {
			System.out.println("Flickr parsing: "+e.toString());
		}
    	return url;
    }
}
