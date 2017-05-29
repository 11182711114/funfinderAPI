package controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

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
import models.EventBasic;
import parser.ResultParser;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class EventController extends Controller{

	public Result findMatch(Long eventId) {
		String sql = "SELECT distinct EventId event " +
				"FROM User INNER JOIN Event on Event.user = User.id INNER JOIN Event_Rest ON Event.eventId = Event_Rest.atEvent INNER JOIN Restaurant ON Restaurant.id = Event_Rest.atRest " +
				"WHERE date = :date AND time BETWEEN :timeStart AND :timeEnd AND Restaurant.id IN ( " +
				"	SELECT id " +
				"	FROM Event ev INNER JOIN Event_Rest er ON ev.eventId = er.atEvent INNER JOIN Restaurant r ON er.atRest = r.id " +
				"   WHERE user = :userId) " +
				"AND NOT user = :userId;";
				
		RawSql rawSql = RawSqlBuilder.parse(sql).tableAliasMapping("Event", "event").create();
		
		Query<EventBasic> query = Ebean.find(EventBasic.class);
		query.setRawSql(rawSql);
		Event event = Event.find.byId(eventId);
		if (event == null)
			return badRequest("no such event");
		
		query.setParameter("userId", event.getUser().getId())
			.setParameter("date", event.getDate())
			.setParameter("timeStart", event.getTime().minusMinutes(15))
			.setParameter("timeEnd", event.getTime().plusMinutes(15));
		Logger.trace("Executing sql:\n" + query.toString());
		List<EventBasic> events = query.findList();
		List<Event> users = events.stream().map(EventBasic::getEvent).collect(Collectors.toList());
		Logger.info(users.toString());
		
		JsonNode result = Json.toJson(users);
		
		return ok(result);
	}
	
	public Result createEventWithRestaurants() {
		JsonNode jn = request().body().asJson();
		User user = User.find.byId(jn.get("uid").asLong());
		if (user == null)
			return badRequest("no such user");
		String date = jn.get("date").asText();
		String time = jn.get("time").asText();
		String location = jn.get("location").asText();
		
		
		JsonNode restaurants = jn.get("restaurants");
		List<String> RestaurantsAsStrings = new ArrayList<>();
		restaurants.forEach(rst -> RestaurantsAsStrings.add(rst.get("id").asText()));
		List<Restaurant> rests = Restaurant.find.where().in("id", RestaurantsAsStrings).findList();

		Event newEvent = new Event(date, time, location, user, rests);
		
		newEvent.save();
		Ebean.saveManyToManyAssociations(newEvent, "restaurants");
		
		return ok(Json.toJson(newEvent));
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
		if(jn.get("uid").asText().isEmpty()) //TODO add more tests, empty fields or missing fields
			return badRequest("fields missing");
		User user = User.find.byId(jn.get("uid").asLong());
		String date = jn.get("date").asText();
		String time = jn.get("time").asText();
		try{
			Event newEvent;
			if(jn.has("location")){
				String location = jn.get("location").asText();
				Logger.info("Making a new event @"+ location);
				List<Restaurant> savedRests = new ArrayList<>();

				JsonNode swipedRests = jn.get("restaurants");
				Logger.info("ARRAY SIZE: "+swipedRests.size());
				if(swipedRests.isArray()){
					for(JsonNode jRest : swipedRests){
						String searchMe = jRest.asText();
						Restaurant newRest = Restaurant.find.byId(searchMe);
						savedRests.add(newRest);
					}
				}
				newEvent = new Event(date, time, location, user);
				newEvent.setRestaurant(savedRests);
				newEvent.save();
				Ebean.saveManyToManyAssociations(newEvent, "restaurants");

				Logger.info("saved&done");
//				return ok(Json.toJson(newEvent)); if event is to be returned
				return ok(Json.toJson("EVENT CREATED"));
			}
			else{ //if coordinates are sent insted of textlocation
				double lat = jn.get("latitude").asDouble();
				double lng = jn.get("longitude").asDouble();
				newEvent = new Event(date, time, lat, lng, user);
				Logger.info("now restaurants...");
				List<Restaurant> savedRests = new ArrayList<>();
				JsonNode swipedRests = jn.get("restaurants");
				if(swipedRests.isArray()){
					for(JsonNode jRest : swipedRests){
						Restaurant newRest = Restaurant.find.byId(jRest.get("id").asText());
						savedRests.add(newRest);
					}
				}
				newEvent.setRestaurant(savedRests);
				newEvent.save();
				Ebean.saveManyToManyAssociations(newEvent, "restaurants");				
				Logger.info("saved and done");
//				return ok(Json.toJson(savedRests));
				return ok(Json.toJson("EVENT CREATED"));
			}
		}catch(NullPointerException np){
			return badRequest("NULLPOINTER ERROR: "+np);
		}catch(PersistenceException pe){
			return badRequest("DML BIND ERROR: "+pe);
		}catch(Exception exp){
			return badRequest("OTHER ERROR: "+ exp);
		}
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
	 * 
	 * takes (user, eventId) to create method
	 * FIXME : how does it choose restaurant from list?
	 *
	 * TODO : Should also create a messageconnection between the two users
	 * TODO : Should delete the two users preliminary-events, so no-one can be doulbebooked
	 */
	public Result createBookedEvent(){
		JsonNode jn = request().body().asJson();
		Logger.info("booking..");
		try{
			User user1 = User.find.byId(jn.get("myUid").asLong());
			Event prelEvent = Event.find.byId(jn.get("eventId").asLong());
			//			findMatch(eventId);

			Logger.info("found choosen event");
			User user2 = prelEvent.getUser();
			LocalDate date = prelEvent.getDate();
			LocalTime time = prelEvent.getTime();
			Restaurant rest = Restaurant.find.byId(jn.get("id").asText());
			BookedEvent newBooking = new BookedEvent(user1, user2, date, time, rest);

			Logger.info("saving");
			newBooking.save();

			//TODO connect messaging ...
			//	connectMessaging()

			//TODO Send push-notice to users of their booked event
			//	sendBookedNotice()

			//TODO TEST THIS: remove user1's event ??!
			Event u1Ev = Ebean.find(Event.class).where().eq("user", user1).and().eq("date", date).and().eq("time", time).findUnique();
			Ebean.delete(u1Ev);
			//remove user2s event aka this event
			Ebean.delete(jn.get("evid").asLong());
			//deleteEvent(jn.get("evid").asLong());
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

