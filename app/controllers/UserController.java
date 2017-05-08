package controllers;

import java.util.List;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import models.User;
import play.libs.Json;
import play.mvc.*;

public class UserController extends Controller {
	
	public Result getUserById(String id) {
		User user = User.find.byId(Long.parseLong(id));
		
		//FIXME Return something better?
		if(user == null)
			return notFound();
		
		JsonNode result = Json.toJson(user);
		return ok(result);
	}
	
	public Result getAllUsers() {
		List<User> users = User.find.all();
		JsonNode result = Json.toJson(users);
		return ok(result);
	}
	
	public Result createUser(String firstName, String lastName, String birthDate, String email, String password) {
		User newUser = new User(firstName, lastName, birthDate, email, password);
		
		Ebean.save(newUser);
	
		return getUserById(Integer.toString(newUser.id));
	}
	
}