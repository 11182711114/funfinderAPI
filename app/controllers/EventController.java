package controllers;

import java.util.List;

import javax.persistence.PersistenceException;

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
    
    public Result createEvent(){
    	JsonNode jn = request().body().asJson();
    	String date = jn.get("date").asText();
    	String time = jn.get("time").asText();
    	String location = jn.get("location").asText();
    	
    	Event newEvent = new Event(date, time, location);
    	try{
    		newEvent.save();
    	}catch(PersistenceException pe){
    		return badRequest("DUPLICATE ERROR: "+pe);
    	}
    	return ok("Event created: "+newEvent.getId());
    }

    public Result getAllEvents(){
    	List<Event> events = Event.find.all();
    	JsonNode result = Json.toJson(events);
    	return ok(result);
    }
    
    public Result getEventById(Long id){
    	Event event = Event.find.byId(id);
    	if(event==null)
    		return notFound("event not found");
    	
    	JsonNode result = Json.toJson(event);
    	return ok(result);
    }
    
}

