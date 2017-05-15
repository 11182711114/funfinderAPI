package controllers;

import java.util.ArrayList;

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

	public Result getRestaurantsNearby(double lat, double lng, int rad){
		ResultParser respesp = new ResultParser();
		ArrayList<parser.Restaurant> results = respesp.searchNearby(lat, lng, rad);

//		PrettyPrinted Json (use this while testing)
		JsonNode node = Json.toJson(results);
		ObjectMapper mapper = new ObjectMapper();
		try {
			String pretty = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
			return ok(pretty);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
//		Clean JSON response
//		return results != null ? ok(Json.toJson(results)) : notFound();
	}

	public Result getRestaurantsByLocation(String location){
		ResultParser respesp = new ResultParser();
		ArrayList<parser.Restaurant> results = respesp.searchText(location);
		
//		PrettyPrinted Json (use this while testing)
		JsonNode node = Json.toJson(results);
		ObjectMapper mapper = new ObjectMapper();
		try {
			String pretty = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
			return ok(pretty);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
		
//		Clean JSON response
//		return results != null ? ok(Json.toJson(results)) : notFound();
	}

}
