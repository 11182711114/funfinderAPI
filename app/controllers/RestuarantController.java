package controllers;


import java.util.ArrayList;

import javax.persistence.*;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.*;

import models.Location;
import models.Restaurant;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import parser.*;

public class RestuarantController extends Controller {

	
/**
 * Gets restaurant by location, saves in db
 * @param userLat - user lattitude
 * @param userLng - user longitude
 * @param searchRadie - radie from the users location to search
 * 
 * @return successmessage if successfully loaded to db or failmessage
 */
	public Result getRestaurantsNearby(double userLat, double userLng, int searchRadie){
		ResultParser respesp = new ResultParser();
		ArrayList<parser.ParsedRestaurant> results = respesp.searchNearby(userLat, userLng, searchRadie);

		for(ParsedRestaurant pl : results){
			String adress = pl.getLocation().getAddress();
			double lat = pl.getLocation().getLattitude();
			double lng = pl.getLocation().getLongitude();
			Location locat = new Location(adress, lat, lng);
			try{
			Ebean.save(locat);

			String name = pl.getName();
			double rating = pl.getRating();
			String id = pl.getId();
			String locid = ""+locat.getId();
			Restaurant newRestaurant = new Restaurant(id, name, rating, locid);
//			try {
				newRestaurant.save();
			} catch (PersistenceException pe) { // duplicate user
				System.out.println("DUPLICATE ERROR: " + pe);
//				return internalServerError("FAILED EXECUTING COMMAND");
			} 
		}
		return ok("SUCCESSFULLY EXECUTED COMMAND");
	}

	/**
	 * Gets restaurants nearby som text search,saves in db
	 * 
	 * @param textSearch - the text input from user
	 * 			this can also be used to search on restaurants name and it will return
	 * 			 all restaurants by that name in sthlm
	 * 
	 * @return successmessage if successfully loaded to db or failmessage
	 */
	public Result getRestaurantsByText(String textSearch){
		ResultParser respesp = new ResultParser();
		ArrayList<parser.ParsedRestaurant> results = respesp.searchText(textSearch);

		for(ParsedRestaurant pl : results){
			String adress = pl.getLocation().getAddress();
			double lat = pl.getLocation().getLattitude();
			double lng = pl.getLocation().getLongitude();
			Location locat = new Location(adress, lat, lng);
			Ebean.save(locat);

			String name = pl.getName();
			double rating = pl.getRating();
			String id = pl.getId();
			String locid = ""+locat.getId();
			Restaurant newRestaurant = new Restaurant(id, name, rating, locid);
			try {
				newRestaurant.save();
			} catch (PersistenceException pe) { // duplicate user
				System.out.println("DUPLICATION ERROR: " + pe);
			}
		}
		return ok("SUCCESSFULLY EXECUTED COMMAND");
	}



	/**
	 * finds restaurant by located id
	 * @param id 
	 * @return restaurant as json, or not found message
	 */
	public Result getRestaurantById(String id){
//		Restaurant rest = Restaurant.find.byId(id);
		Restaurant rest = Ebean.find(Restaurant.class).where().eq("id", id).findUnique();
		if(rest == null)
			return notFound("Restaurant doesn't exist");
		JsonNode result = Json.toJson(rest);
		return ok(result);
	}

	/**
	 * finds restaurant by name of the restaurant
	 * @param name - of the restaurant
	 * @return restaurant result as json, or not found message
	 */
	public Result getRestaurantByName(String name) {
		Restaurant rest = Ebean.find(Restaurant.class).where().eq("name", name).findUnique();

		if (rest == null)
			return notFound("restaurant not found");
		JsonNode result = Json.toJson(rest);
		return ok(result);
	}

}
