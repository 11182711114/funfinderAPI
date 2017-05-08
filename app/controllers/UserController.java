package controllers;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import models.User;
import play.libs.Json;
import play.mvc.*;

public class UserController extends Controller {
	
	/** Gets a user by Id
	 * @param id - the id of the user to be returned
	 * @return the user in Json
	 */
	public Result getUserById(String id) {
		User user = User.find.byId(Long.parseLong(id));
		
		//FIXME Return something better?
		if(user == null)
			return notFound();
		
		JsonNode result = Json.toJson(user);
		return ok(result);
	}
	
	/** Returns all current users in Json
	 * @return All current users in Json
	 */
	public Result getAllUsers() {
		List<User> users = User.find.all();
		JsonNode result = Json.toJson(users);
		return ok(result);
	}
	
	
	/** Creates a new user with a generated Id using parameters
	 * @param firstName
	 * @param lastName
	 * @param birthDate - String "yyyy-MM-dd" e.g. "2012-12-24"
	 * @param email
	 * @param password
	 * @return the user as it would be returened by {@link #getUserById(String)}
	 */
	public Result createUser(String firstName, String lastName, String birthDate, String email, String password) {
		User newUser = new User(firstName, lastName, birthDate, email, password);
		
		newUser.save();
	
		return getUserById(Integer.toString(newUser.id));
	}
	
}