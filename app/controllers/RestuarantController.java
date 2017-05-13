package controllers;

import java.util.ArrayList;
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

//		for(parser.Restaurant res : results)
//			System.out.println(res.toString()); //TODO remove tester.

		return results != null ? ok(Json.toJson(results)) : notFound();
	}

	public Result getRestaurantsByLocation(String location){
		ResultParser respesp = new ResultParser();
		ArrayList<parser.Restaurant> results = respesp.searchText(location);
		
		return results != null ? ok(Json.toJson(results)) : notFound();
	}

}
