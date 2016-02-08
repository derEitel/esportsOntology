package main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

import main.User;
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
        user.setFirstName("JonFromREST");
        user.setLastName(queryJSON().toString());
        
        return user;
    }

    public JSONObject queryJSON() {
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
					.get("ontology/teamsRankingDefinitive.txt")));
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
				Object value = temp.getJSONObject("literal").get("content");
				newValue.put(key, value);
			}
			resultsArray.put(newValue);
		}
		return new JSONObject().put("data", resultsArray);
	}
}
