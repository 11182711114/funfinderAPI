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
	public Result getUserById(Long id) {
		User user = User.find.byId(id);
		
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
		if(!data.has("email") || !data.has("password")) 
			return badRequest("fields missing");
			
		String email = data.get("email").textValue();
		String password = data.get("password").textValue();
		
		User user = Ebean.find(User.class).where().eq("email", email).and().eq("password", password).findUnique();
		
		if (user == null)
			return notFound();
		
		return ok("K"+ ","+ user.getId());
	}
	
	public Result updateUserInformation(Long userId) {
		User user = User.find.byId(userId);
		if(user == null)
			return notFound();
		
		JsonNode data = request().body().asJson();
		if(data.has("email"))
			user.setEmail(data.get("email").asText());
		if(data.has("password"))
			user.setPassword(data.get("password").asText());
		
		user.update();
		return ok("user updated");
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
		
		return ok("user created: " + newUser.getId());
	}
	
	public Result updateProfile() {
		JsonNode jn = request().body().asJson();
		Long uid = jn.get("uid").asLong();
		String bio = jn.get("bio").asText();
		String hobbies = jn.get("hobbies").asText();
		
		User user = User.find.byId(uid);
		
		Profile toUpdate = Ebean.find(Profile.class).where().eq("user", user).findUnique();
		boolean newProfile = false;
		if(toUpdate == null) {
			toUpdate = new Profile();
			toUpdate.setUser(user);
			newProfile = true;
		}
		if (bio != null && bio != "")
			toUpdate.setBio(bio);
		if (hobbies != null && hobbies != "")
			toUpdate.setHobbies(hobbies);
		
		if (newProfile)
			Ebean.save(toUpdate);
		else {
			//FIXME shitty workaround for no @Id in profile
			Profile tmp = toUpdate.copy();
			Ebean.createQuery(Profile.class).where().eq("user", user).delete();
			Ebean.save(tmp);
			
		}
		return ok("profile updated");
	}
	
	public Result getProfile(Long id) {
		User user = User.find.byId(id);
		if(user == null)
			return notFound("user not found");
		
		Profile prof = Ebean.find(Profile.class).where().eq("user", user).findUnique();
		if (prof == null)
			return notFound("No profile found");		
		
		String json = "{\"uid\":" + user.getId() + ",\"bio\":\"" + prof.getBio() + "\",\"hobbies\":\"" + prof.getHobbies() + "\"}";
		JsonNode jn = Json.parse(json);
		return ok(jn);
	}
	
}