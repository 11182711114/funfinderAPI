package controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.persistence.PersistenceException;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Query;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSqlBuilder;
import com.fasterxml.jackson.databind.JsonNode;

import models.BookedEvent;
import models.Event;
import models.Restaurant;
import models.User;
import models.UserBasic;
import play.Logger;
import play.data.FormFactory;
import play.db.ebean.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class EventController extends Controller{



	public Result findMatch(Long eventId) {
		String sql = "SELECT distinct User.id as id " + 
				"FROM User, Event, Event_Rest, Restaurant " +
				"WHERE Event.eventId = Event_Rest.atEvent AND Event_Rest.atRest = Restaurant.id AND time BETWEEN :timeStart AND :timeEnd AND Restaurant.id IN ( "+
					"SELECT id " +
					"FROM Event, Event_Rest, Restaurant " +
					"WHERE Event.eventId = Event_Rest.atEvent AND Event_Rest.atRest = Restaurant.id AND user = :userId) "+ 
						"AND NOT user = :userId;";
		
		RawSql rawSql = RawSqlBuilder.parse(sql).tableAliasMapping("User", "user").create();
		
		Query<UserBasic> query = Ebean.find(UserBasic.class);
		query.setRawSql(rawSql);
		Event event = Event.find.byId(eventId);
		query.setParameter("userId", event.getUser())
			.setParameter("timeStart", event.getTime().minusMinutes(15))
			.setParameter("timeEnd", event.getTime().plusMinutes(15));
		List<UserBasic> rtn = query.findList();
		List<User> users = rtn.stream().map(UserBasic::getUser).collect(Collectors.toList());
		Logger.info(users.toString());
		return ok(Json.toJson(users));
	}

	/*
	 * creates from user input a new event, takes two possible routes
	 * 	if the user entered text location it saves the textlocation and fills 
	 * 		with restaurants based on that textsearch
	 * else if the user gives event her nearby position the search for restaurants are
	 *  made from the coordinates of the user.
	 *  
	 *  FIXME: when in testing (using POSTMAN) special characters (å,ä,ö) wasn't 
	 *   correct after post was received and breaks the method, maybe fixed when going via heroku
	 *  	TODO: TEST THIS
	 */
	public Result createEvent(){
		JsonNode jn = request().body().asJson();
		if(jn.get("location").asText().isEmpty())
			return badRequest("fields missing");
		User user = User.find.byId(jn.get("uid").asLong());
		String date = jn.get("date").asText();
		String time = jn.get("time").asText();
		try{
			Event newEvent;
			if(jn.has("location")){
				String location = jn.get("location").asText();
				Logger.info("Making a new event @"+ location);
				
				newEvent = new Event(date, time, location, user);
				Logger.info("Saving");
				List<Restaurant> rests = fillEvent(location);
				newEvent.setRestaurant(rests);
				Logger.info("fetched");

				newEvent.save();
				Ebean.saveManyToManyAssociations(newEvent, "restaurants");
				Logger.info("done");
			}
			else{ //if coordinates are sent insted of textlocation
				double lat = jn.get("latitude").asDouble();
				double lng = jn.get("longitude").asDouble();
				newEvent = new Event(date, time, lat, lng);
				List<Restaurant> rests = fillEvent(lat,lng);
				newEvent.setRestaurant(rests);
				newEvent.save();
				Ebean.saveManyToManyAssociations(newEvent, "restaurants");
			}
		}catch(NullPointerException np){
			return badRequest("NULLPOINTER ERROR: "+np);
		}catch(PersistenceException pe){
			return badRequest("DML BIND ERROR: "+pe);
		}catch(Exception exp){
			return badRequest("OTHER ERROR: "+ exp);
		}
		return ok("SUCCESS: Event created");
	}


	/*
	 * fillEvent method "fill the event" with the nearby restaurants
	 * 	either as a textsearch on the location or by supplying the coordinates
	 * submethod supplied by the restaurantcontroller, classic spaghetti-coding  
	 */
	private List<Restaurant> fillEvent(String textsearch){
		return RestuarantController.getRestaurantsByText(textsearch);
	}
	/*
	 * used when method takes coordinates as location input,
	 * don't implement until client has geolocation-method implementer
	 * TODO don't forget to if-statement in createEvent() method
	 */
	private List<Restaurant> fillEvent(double lat, double lng){
		return RestuarantController.getRestaurantsNearby(lat, lng, 800);//here the radium is hardcoded
	}

	/*
	 * created a BookedEvent instance from the information in json format
	 *  of the event the user has choose to meet with. 
	 *  It then delete these events (preliminary events) so no one can be double booked
	 */
	public Result createBookedEvent(){
		JsonNode jn = request().body().asJson();
		Logger.info("accessing");
		try{
			Logger.info("test");
			User user1 = User.find.byId(jn.get("myUid").asLong());
			Event prelEvent = Event.find.byId(jn.get("eventId").asLong());
			
			Logger.info("found");
			User user2 = prelEvent.getUser();
			LocalDate date = prelEvent.getDate();
			LocalTime time = prelEvent.getTime();
			Restaurant rest = Restaurant.find.byId(jn.get("id").asText());
			BookedEvent newBooking = new BookedEvent(user1, user2, date, time, rest);
			
			Logger.info("saving");
			newBooking.save();
			
			//TODO remove user1's event ??!
			
			
			//remove user2s event aka this event
			deleteEvent(jn.get("evid").asLong());
			Logger.info("prelEvent deleted");
		}catch(PersistenceException pe){
			return badRequest("DML BIND ERROR: "+pe);			
		}catch(NullPointerException np){
			return badRequest("NULLPOINTER EXCEPTION: "+np);
		}
		return ok("created BookedEvent");
	}

	private void deleteEvent(Long id){
		Event eve = Event.find.byId(id);
		eve.delete();
	}

	public Result getEventById(Long id){
		Event event = Event.find.byId(id);
		if(event==null)
			return notFound("event not found");
		JsonNode result = Json.toJson(event);
		return ok(result);
	}

	public Result getAllEvents(){
		List<Event> events = Event.find.all();
		JsonNode result = Json.toJson(events);
		return ok(result);
	}

}

