package controllers;

import java.util.ArrayList;

import javax.persistence.PersistenceException;

import com.avaje.ebean.Ebean;
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

		for(ParsedRestaurant pl : results){

//			System.out.println("ADDING: "+pl.toString()+" "+pl.getLocation().getAddress());//TODO remove testline
			
			String adress = pl.getLocation().getAddress();
			double lat = pl.getLocation().getLattitude();
			double lng = pl.getLocation().getLongitude();
			Location locat = new Location(adress, lat, lng);
			Ebean.save(locat);
		
			String name = pl.getName();
			double rating = pl.getRating();
			String id = pl.getId();
			String locid = ""+locat.getId();
//			System.out.println(">>>LOC ID : "+locid); //TODO remove testline
			
			Restaurant newRestaurant = new Restaurant(id, name, rating, locid);
			try {
				newRestaurant.save();
			} catch (PersistenceException pe) { // duplicate user
				System.out.println("SAVE ERROR " + pe);
				return internalServerError("FAILED EXECUTING COMMAND");
			}
		}
		return ok("SUCESSFULLY EXECUTED COMMAND");
	}

	public Result getRestaurantsByLocation(String location){
		ResultParser respesp = new ResultParser();
		ArrayList<parser.ParsedRestaurant> results = respesp.searchNearby(userLat, userLng, searchRadie);

		for(ParsedRestaurant pl : results){

//			System.out.println("ADDING: "+pl.toString()+" "+pl.getLocation().getAddress());//TODO remove testline
			
			String adress = pl.getLocation().getAddress();
			double lat = pl.getLocation().getLattitude();
			double lng = pl.getLocation().getLongitude();
			Location locat = new Location(adress, lat, lng);
			Ebean.save(locat);
		
			String name = pl.getName();
			double rating = pl.getRating();
			String id = pl.getId();
			String locid = ""+locat.getId();
//			System.out.println(">>>LOC ID : "+locid); //TODO remove testline
			
			Restaurant newRestaurant = new Restaurant(id, name, rating, locid);
			try {
				newRestaurant.save();
			} catch (PersistenceException pe) { // duplicate user
				System.out.println("SAVE ERROR " + pe);
				return internalServerError("FAILED EXECUTING COMMAND");
			}
		}
		return ok("SUCESSFULLY EXECUTED COMMAND");
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
