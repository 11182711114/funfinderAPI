package controllers;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.PersistenceException;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;

import models.Profile;
import models.User;
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
			return notFound("user not found");
		
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
	
	public Result authenticateUser() {
		JsonNode data = request().body().asJson();
		System.out.println(data);
		String email = data.get("email").textValue();
		String password = data.get("password").textValue();
		
		User user = Ebean.find(User.class).where().eq("email", email).and().eq("password", password).findUnique();
		
		if (user == null)
			return notFound();
		
		return ok("K"+ ","+ user.id);
	}
	
	/** Returns all current users in Json
	 * @return All current users in Json
	 */
	public Result getAllUsers() {
		List<User> users = User.find.all();
		JsonNode result = Json.toJson(users);
		return ok(result);
	}
	
	/** Creates a new user with a generated Id from json sent by POST
	 * @param firstname
	 * @param lastname
	 * @param birthdate - String "yyyy-MM-dd" e.g. "2012-12-24"
	 * @param email
	 * @param password
	 * @return the user as it would be returened by {@link #getUserById(String)}
	 */
	public Result createUser() {
		JsonNode jn = request().body().asJson();
		String firstname = jn.get("firstname").asText();
		String lastname = jn.get("lastname").asText();
		String birthdate = jn.get("birthdate").asText();
		String email = jn.get("email").asText();
		String password = jn.get("password").asText();
		User newUser = new User(firstname, lastname, birthdate, email, password);
		
		try {
			newUser.save();
		} catch (PersistenceException pe) { // duplicate user
			return badRequest("exists");
		}
		
		return ok("user created");
	}
	
	public Result updateProfile() {
		JsonNode jn = request().body().asJson();
		String uid = jn.get("uid").asText();
		String bio = jn.get("bio").asText();
		String hobbies = jn.get("hobbies").asText();
		
		Profile newProfile = new Profile(uid,bio,hobbies);
		
		Ebean.save(newProfile);
		
		return ok("profile updated");
	}
	
}