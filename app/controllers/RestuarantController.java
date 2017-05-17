package controllers;

import java.util.ArrayList;

import javax.persistence.PersistenceException;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;

import models.Location;
import models.Restaurant;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import parser.*;

public class RestuarantController extends Controller {


	public Result getRestaurantById(String id) {
		Restaurant rest = Restaurant.find.byId(id);

		return rest != null ? ok(Json.toJson(rest)) : notFound();
	}

	/*
	 * Results requested by client are not to be saved into the database but only parsed and 
	 *  forwarded to the client.
	 */

	public Result getRestaurantsNearby(double userLat, double userLng, int searchRadie){
		ResultParser respesp = new ResultParser();
		ArrayList<parser.ParsedRestaurant> results = respesp.searchNearby(userLat, userLng, searchRadie);

		for(parser.ParsedRestaurant rest : results){
			createLocation(rest.getLocation());
			String name = rest.getName();
			double rating = rest.getRating();
			String id = rest.getId();
			
			Restaurant newRestaurant = new Restaurant(id, name, rating);
			try {
				newRestaurant.save();
			} catch (PersistenceException pe) { // duplicate user
				System.out.println("SAVE ERROR " + pe);
			}
		}
		return ok("success");
	}

	public Result getRestaurantsByLocation(String location){
		ResultParser respesp = new ResultParser();
		ArrayList<parser.ParsedRestaurant> results = respesp.searchText(location);
	
		return ok("success");
	}

	public Location createLocation(ParsedLocation loc){
		String adress = loc.getAddress();
		double latitude = loc.getLattitude();
		double longitude = loc.getLongitude();
		Location locat = new Location(adress, latitude, longitude);
		locat.save();
		return locat;
	}
	
	//	public Result getRestaurant(String id){
	//		Restaurant rest = Restaurant.find.byId(id);
	//		if(rest == null)
	//			return notFound("Restaurant doesn't exist");
	//		String json = "{\"uid\":" + rest.getId() + ",\"name\":\"" + rest.getName() + "\",\"rating\":\"" + rest.getRating() + "\"}";
	//		
	//		JsonNode jn = Json.parse(json);
	//		return ok(jn);
	//	}

}
