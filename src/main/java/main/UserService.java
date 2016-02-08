package main;

import main.User;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

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

public class UserService {

    public User getDefaultUser() {
        User user = new User();
        user.setFirstName(queryJSON("competitionsQuery.txt").toString());
        user.setLastName(queryJSON("teamsRankingDefinitive.txt").toString());
        
        return user;
    }

    public JSONObject queryJSON(String path) {
		Graph g = Graph.create(true);
		Load ld = Load.create(g);
		try {
			ld.loadWE("ontology/event.ttl");
			ld.loadWE("ontology/esports.rdf");

			System.out.println("Alright");
		} catch (LoadException le) {
			System.out.println("Error loading: " + le.toString());
		}
		
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
		System.out.println("que pollas pasa: "+originalJson);
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
					if (temp.names().get(1).toString().equals("literal"))
						value = temp.getJSONObject("literal").get("content");
					else if (temp.names().get(1).toString().equals("uri"))
						value = temp.getString("uri");
					else throw new Exception ("Value not supported yet.");
				} catch (Exception e) {
					System.out.println("Exception while parsing the content: "+e.toString());
				}
				
				newValue.put(key, value);
			}
			resultsArray.put(newValue);
		}
		return new JSONObject().put("data", resultsArray);
	}
}
