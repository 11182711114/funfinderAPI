package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.persistence.PersistenceException;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Query;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSqlBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import models.BookedEvent;
import models.Event;
import models.Message;
import models.Restaurant;
import models.User;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class EventController extends Controller {

	public Result findMatch(Long eventId) {
		String sql = "SELECT distinct EventId eventid " +
				"FROM User INNER JOIN Event on Event.user = User.id INNER JOIN Event_Rest ON Event.eventId = Event_Rest.atEvent INNER JOIN Restaurant ON Restaurant.id = Event_Rest.atRest " +
				"WHERE date = :date AND time BETWEEN :timeStart AND :timeEnd AND Restaurant.id IN ( " +
				"	SELECT id " +
				"	FROM Event ev INNER JOIN Event_Rest er ON ev.eventId = er.atEvent INNER JOIN Restaurant r ON er.atRest = r.id " +
				"   WHERE user = :userId) " +
				"AND NOT user = :userId;";
				
		RawSql rawSql = RawSqlBuilder.parse(sql).tableAliasMapping("Event", "event").create();
		
		Query<Event> query = Ebean.find(Event.class);
		query.setRawSql(rawSql);
		Event event = Event.find.byId(eventId);
		if (event == null)
			return badRequest("no such event");
		
		query.setParameter("userId", event.getUser().getId())
			.setParameter("date", event.getDate())
			.setParameter("timeStart", event.getTime().minusMinutes(15))
			.setParameter("timeEnd", event.getTime().plusMinutes(15));
		Logger.trace("Executing sql:\n" + query.getRawSql().getSql().toString());
		List<Event> events = query.findList();
		
		Logger.info(events.toString());
		
		JsonNode result = Json.toJson(events);

		List<Integer> seen = new ArrayList<>();
	
		events.forEach(e -> {
			if (event.seen(e)) 
				seen.add(e.getId());
			else 
				event.setSeen(e);
		});
		event.save();
		
		result.forEach(evnt -> {
			if (evnt instanceof ObjectNode) {
				((ObjectNode) evnt).remove("restaurants");
				if (seen.contains(evnt.get("id").asInt()))
					((ObjectNode) evnt).put("seen", "true");
				else 
					((ObjectNode) evnt).put("seen", "false");
			}
		});
		
		return ok(result);
	}
	
	public Result createEventWithRestaurants() {
		Logger.debug("creating new event with restaurants");
		try {
			JsonNode jn = request().body().asJson();
			User user = User.find.byId(jn.get("uid").asLong());
			if (user == null)
				return badRequest("no such user");
			Logger.debug("user: " + user.getFirstname() + " " + user.getId());
			String date = jn.get("date").asText();
			Logger.debug("date: " + date);
			String time = jn.get("time").asText();
			Logger.debug("time: " + time);
			String location = jn.get("location").asText();
			Logger.debug("location: " + location);
			
			
			JsonNode restaurants = jn.get("restaurants");
			restaurants.forEach(p -> Logger.debug("rest: " + p.toString()));
			List<String> RestaurantsAsStrings = new ArrayList<>();
			restaurants.forEach(rst -> RestaurantsAsStrings.add(rst.get("id").asText()));
			List<Restaurant> rests = Restaurant.find.where().in("id", RestaurantsAsStrings).findList();

			Event newEvent = new Event(date, time, location, user, rests);
			
			newEvent.save();
			Ebean.saveManyToManyAssociations(newEvent, "restaurants");
			
			return ok(Json.toJson(newEvent));
		} catch (NullPointerException e) {
			Logger.debug("null request");
			return badRequest("null");
		}
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
		String[] dateArr = date.split("-");
		
		Logger.debug("date is: " + date);
		if (dateArr[1].length() != 2)
			dateArr[1] = "0" + dateArr[2];
		if (dateArr[2].length() != 2)
			dateArr[2] = "0" + dateArr[2];
		date = dateArr[0] +"-"+ dateArr[1] +"-"+ dateArr[2];
		Logger.debug("new date format is: " + date);
		
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

	public Result getEventByUser(Long userId) {
		User user = User.find.byId(userId);
		if (user == null)
			return notFound("no such user");
		
		List<Event> events = Event.find.where().eq("user", user).findList();
		if (events.isEmpty())
			return notFound("no events for this user");
					
		return ok(Json.toJson(events));
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
	
	// v2
	public Result createBookedEvent(){
		Logger.debug("header: " + request().getHeader("Content-type"));
		JsonNode jn = request().body().asJson();
		Logger.info("Booking..");
		try{
			Event myEvent = Event.find.byId(jn.get("myEid").asLong());
			if(myEvent == null)
				return notFound("no event with myEid");
			User myUser = myEvent.getUser();
			
			Event otherEvent = Event.find.byId(jn.get("otherEid").asLong());
			if(otherEvent == null)
				return notFound("no event with otherEid");
			User otherUser = otherEvent.getUser();
			
			List<Restaurant> commonRests = new ArrayList<>(myEvent.getRestaurants());
			commonRests.retainAll(otherEvent.getRestaurants());
			
			if (commonRests.size()<1) {
				Logger.debug("Common restaurants is: " + commonRests.size());
				return badRequest("0 common restaurants");
			}
			
			int i = new Random().nextInt(commonRests.size());
			
			BookedEvent newBooking = new BookedEvent(myUser, otherUser, myEvent.getDate(), myEvent.getTime(), commonRests.get(i));

			Message toOther = new Message(myUser, otherUser, "Hej! Vi har matchats för att äta :)");
			Ebean.save(toOther);
			
			Logger.info("saving");
			newBooking.save();
			
			Ebean.delete(myEvent);
			Ebean.delete(otherEvent);
			
			Logger.info("Events deleted");
		}catch(PersistenceException pe){
			Logger.debug("DML BIND ERROR: "+pe);
			return badRequest("DML BIND ERROR: "+pe);			
		}catch(NullPointerException np){
			Logger.debug("NULLPOINTER EXCEPTION: "+np);
			return badRequest("NULLPOINTER EXCEPTION: "+np);
		}
		return ok("created BookedEvent");
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
//	public Result createBookedEvent(){
//		JsonNode jn = request().body().asJson();
//		Logger.info("booking..");
//		try{
//			User user1 = User.find.byId(jn.get("myUid").asLong());
//			Event prelEvent = Event.find.byId(jn.get("eventId").asLong());
//			if(prelEvent == null)
//				return notFound("no event with that id");
//			//			findMatch(eventId);
//
//			Logger.info("found choosen event");
//			User user2 = prelEvent.getUser();
//			LocalDate date = prelEvent.getDate();
//			LocalTime time = prelEvent.getTime();
//			Restaurant rest = Restaurant.find.byId(jn.get("id").asText());
//			BookedEvent newBooking = new BookedEvent(user1, user2, date, time, rest);
//
//			Logger.info("saving");
//			newBooking.save();
//
//			//TODO connect messaging ...
//			//	connectMessaging()
//
//			//TODO Send push-notice to users of their booked event
//			//	sendBookedNotice()
//
//			//TODO TEST THIS: remove user1's event ??!
//			Event u1Ev = Ebean.find(Event.class).where().eq("user", user1).and().eq("date", date).and().eq("time", time).findUnique();
//			Ebean.delete(u1Ev);
//			//remove user2s event aka this event
//			Ebean.delete(jn.get("evid").asLong());
//			//deleteEvent(jn.get("evid").asLong());
//			Logger.info("prelEvent deleted");
//		}catch(PersistenceException pe){
//			return badRequest("DML BIND ERROR: "+pe);			
//		}catch(NullPointerException np){
//			return badRequest("NULLPOINTER EXCEPTION: "+np);
//		}
//		return ok("created BookedEvent");
//	}

	public Result getBookedEvents(Long userId) {
		User user = User.find.byId(userId);
		if (user == null)
			return badRequest("no such user");
		
		BookedEvent be = user.getBookedEvent();
		if(be == null)
			return badRequest("no events");
		
		return ok(Json.toJson(be));
	}
	
	public Result deleteBookedEvent(Long bookedEventId) {
		BookedEvent bk = BookedEvent.find.byId(bookedEventId);
		if (bk == null)
			return notFound("no such booked event");
		return ok(""+bk.delete());
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

