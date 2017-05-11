package controllers;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.PersistenceException;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import models.User;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.*;

public class UserController extends Controller {
	
	@Inject FormFactory formFactory;
	
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
	
	public Result getUserByEmail(String email) {
		User user = Ebean.find(User.class).where().eq("email", email).findUnique();
		
		if (user == null)
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
	
	/** Creates a new user with a generated Id from a HTML form sent by POST
	 * @param firstname
	 * @param lastname
	 * @param birthdate - String "yyyy-MM-dd" e.g. "2012-12-24"
	 * @param email
	 * @param password
	 * @return the user as it would be returened by {@link #getUserById(String)}
	 */
	public Result createUser() {
		DynamicForm df = formFactory.form().bindFromRequest();
		String firstname = df.get("firstname");
		String lastname = df.get("lastname");
		String birthdate = df.get("birthdate");
		String email = df.get("email");
		String password = df.get("password");
		User newUser = new User(firstname, lastname, birthdate, email, password);
		
		try {
			newUser.save();
		} catch (PersistenceException pe) { // duplicate user
			return badRequest("exists");
		}
//		if (Integer.toString(newUser.id) == null)
//			return badRequest("User with that email already found");
//		return created(Json.toJson(newUser));
		return getUserById(Integer.toString(newUser.id));
	}
	
}