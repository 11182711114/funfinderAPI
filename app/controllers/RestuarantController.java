package controllers;

import java.util.ArrayList;

import javax.persistence.PersistenceException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;

import models.Restaurant;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import parser.*;

public class RestuarantController extends Controller {


	public Result getRestaurantById(String id) {
		Restaurant rest = Restaurant.find.byId(Long.parseLong(id));

		return rest != null ? ok(Json.toJson(rest)) : notFound();
	}

	/*
	 * Results requested by client are not to be saved into the database but only parsed and 
	 *  forwarded to the client.
	 */

	public void getRestaurantsNearby(double lat, double lng, int rad){
		ResultParser respesp = new ResultParser();
		ArrayList<parser.Restaurant> results = respesp.searchNearby(lat, lng, rad);
		
		for(parser.Restaurant rest : results){
			String name = rest.getName();
			double rating = rest.getRating();
			String id = rest.getId();
			
			Restaurant newRestaurant = new Restaurant(id, name, rating);
			try {
				newRestaurant.save();
			} catch (PersistenceException pe) { // duplicate user
				pe.printStackTrace();
			}
		}
//		PrettyPrinted Json (use this while testing)
//		JsonNode node = Json.toJson(results);
//		ObjectMapper mapper = new ObjectMapper();
//		try {
//			String pretty = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
//			return ok(pretty);
//		} catch (JsonProcessingException e) {
//			e.printStackTrace();
//		}
//		return null;
		//		Clean JSON response
		//		return results != null ? ok(Json.toJson(results)) : notFound();
	}

	public void getRestaurantsByLocation(String location){
		ResultParser respesp = new ResultParser();
		ArrayList<parser.Restaurant> results = respesp.searchText(location);

		
		//		Clean JSON response
		//		return results != null ? ok(Json.toJson(results)) : notFound();
	}

	public Result getRestaurant(String id){
		Restaurant rest = Restaurant.find.byId(id);
		if(rest == null)
			return notFound("Restaurant doesn't exist");
		String json = "{\"uid\":" + rest.getId() + ",\"name\":\"" + rest.getName() + "\",\"rating\":\"" + rest.getRating() + "\"}";
		
		JsonNode jn = Json.parse(json);
		return ok(jn);
	}
		
}
