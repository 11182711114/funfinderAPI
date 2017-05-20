package controllers;

import java.util.List;

import javax.persistence.PersistenceException;

import com.avaje.ebean.enhance.agent.SysoutMessageOutput;
import com.fasterxml.jackson.databind.JsonNode;

import models.Event;
import models.User;
import play.db.ebean.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class EventController extends Controller{

	public Result getUser() {
		User user = User.find.byId(2L);
		JsonNode result = Json.toJson(user);
		return ok(result);
	}

	@Transactional
	public Result getUserByID(Long id) {
		User user = User.find.byId(id);
		JsonNode result = Json.toJson(user);
		return ok(result);
	}

	/*
	 * creates from user input a new event, takes two possible routes
	 * 	if the user entered text location it saves the textlocation and fills 
	 * 		with restaurants based on that textsearch
	 * else if the user gives event her nearby position the search for restaurants are
	 *  made from the coordinates of the user.
	 */
	public Result createEvent(){
		JsonNode jn = request().body().asJson();
		String date = jn.get("date").asText();
		String time = jn.get("time").asText();
		Event newEvent = null;
		try{
			if(jn.has("textlocation")){
				String location = jn.get("textlocation").asText();
				newEvent = new Event(date, time, location);
				newEvent.save();
				fillEvent(location);
			}
			else{
				double lat = jn.get("latitude").asDouble();
				double lng = jn.get("longitude").asDouble();
				newEvent = new Event(date, time, lat, lng);
				newEvent.save();
				fillEvent(lat,lng);
			}
		}catch(PersistenceException pe){
			return badRequest("DUPLICATE ERROR: "+pe);
		}catch(Exception exp){
			System.out.println("ERROR: "+ exp);
		}
		return ok("Event created: "+newEvent.getId());
	}

	public Result getAllEvents(){
		List<Event> events = Event.find.all();
		JsonNode result = Json.toJson(events);
		return ok(result);
	}

	/*
	 * fillEvent method "fill the event" with the nearby restaurants
	 * 	either as a textsearch on the location or by supplying the coordinates
	 * submethod supplied by the restaurantcontroller, classic spaghetti-coding  
	 */
	private void fillEvent(String textsearch){
		RestuarantController rest = new RestuarantController();
		rest.getRestaurantsByText(textsearch);
	}
	private void fillEvent(double lat, double lng){
		RestuarantController rest = new RestuarantController();
		rest.getRestaurantsNearby(lat, lng, 800);
	}

	public Result getEventById(Long id){
		Event event = Event.find.byId(id);
		if(event==null)
			return notFound("event not found");

		JsonNode result = Json.toJson(event);
		return ok(result);
	}

}

