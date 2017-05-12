package controllers;

import java.util.ArrayList;
import models.Restaurant;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import parser.*;

public class RestuarantController extends Controller {


	//	public Result getRestaurantById(String id) {
	//		Restaurant rest = Restaurant.find.byId(Long.parseLong(id));
	//		
	//		return rest != null ? ok(Json.toJson(rest)) : notFound();
	//	}

	/*
	 * Results requested by client are not to be saved into the database but only parsed and 
	 *  forwarded to the client.
	 */

	public Result getRestaurantsNearBy(double lat, double lng, int rad){
		ResultParser respesp = new ResultParser();
		ArrayList<parser.Restaurant> asd = respesp.searchNearby(lat, lng, rad);
		return null;
//		return asd != null ? ok(Json.toJson(asd));
	}

	public Result getRestaurantsByLocation(String location){
		ResultParser respesp = new ResultParser();
		ArrayList<parser.Restaurant> asd = respesp.searchText(location);
		
		return asd != null ? ok(Json.toJson(asd)) : notFound();
	}

}
