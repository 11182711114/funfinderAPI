package controllers;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.*;

import models.Location;
import models.Restaurant;
import play.Logger;
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
	public static List<Restaurant> getRestaurantsNearby(double userLat, double userLng, int searchRadie){
		ResultParser respesp = new ResultParser();
		List<parser.ParsedRestaurant> results = respesp.searchNearby(userLat, userLng, searchRadie);
		List<Restaurant> returnList = new ArrayList<Restaurant>();

		for(ParsedRestaurant pl : results){
			try{
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
				newRestaurant.save();
				returnList.add(newRestaurant);
			} catch (PersistenceException pe) { // duplicate user
				System.out.println("DUPLICATE ERROR: " + pe);
			} 
		}
		return returnList;
	}


	/**
	 * Gets restaurants nearby som text search,saves in db
	 * 
	 * @param textSearch - the text input from user
	 * 			this can also be used to search on restaurants name and it will return
	 * 			 all restaurants by that name in sthlm
	 * @return 
	 * 
	 * @return successmessage if successfully loaded to db or failmessage //FIXME
	 * @return void
	 */
	public static List<Restaurant> getRestaurantsByText(String textSearch){
		Logger.info("IN RESTCONTROLLER: "+textSearch);
		ResultParser respesp = new ResultParser();
		List<parser.ParsedRestaurant> results = respesp.searchText(textSearch);
		List<Restaurant> returnList = new ArrayList<Restaurant>();
		for(ParsedRestaurant pl : results){
			try {
				String id = pl.getId();
				Restaurant res = Restaurant.find.byId(id);
				if(res==null){
					String adress = pl.getLocation().getAddress();
					double lat = pl.getLocation().getLattitude();
					double lng = pl.getLocation().getLongitude();
					Location locat = new Location(adress, lat, lng);
					Ebean.save(locat);

					String name = pl.getName();
					double rating = pl.getRating();

					String locid = ""+locat.getId();
					Restaurant newRestaurant = new Restaurant(id, name, rating, locid);
					newRestaurant.save();
					returnList.add(newRestaurant);
				}
				else
					returnList.add(res);
			} catch (PersistenceException pe) { // duplicate user
				System.out.println("DUPLICATION ERROR: " + pe);
			}
		}
		return returnList;
	}

	/*
	 * creates a list of restaurants for the first steps of creating an event
	 */
	public Result findEventRestaurants(String textSearch){		
		List<Restaurant> returnList = getRestaurantsByText(textSearch);
		return ok(Json.toJson(returnList));
	}

	/**
	 * finds restaurant by located id
	 * @param id 
	 * @return restaurant as json, or not found message
	 */
	public Result getRestaurantById(String id){
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
