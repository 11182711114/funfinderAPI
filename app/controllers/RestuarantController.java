package controllers;

import models.Restaurant;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class RestuarantController extends Controller {
	
	
	public Result getRestaurantById(String id) {
		Restaurant rest = Restaurant.find.byId(Long.parseLong(id));
		
		return rest != null ? ok(Json.toJson(rest)) : notFound();
	}
	
	
}
