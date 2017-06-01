	package controllers;

import java.time.LocalDate;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.PersistenceException;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;

import models.Profile;
import models.User;
import play.Logger;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class UserController extends Controller {
	
	@Inject FormFactory formFactory;
	
	/** Gets a user by Id
	 * @param id - the id of the user to be returned
	 * @return the user in Json
	 */
	public Result getUserById(Long id) {
		User user = User.find.byId(id);
		if(user == null)
			return notFound("user not found");
		
		JsonNode result = Json.toJson(user);
		return ok(result);
	}
	
	public Result getUserByEmail(String email) {
		User user = Ebean.find(User.class).where().eq("email", email).findUnique();
		if (user == null)
			return notFound("user not found");
		
		JsonNode result = Json.toJson(user);
		return ok(result);
		
	}
	
	public Result authenticateUser() {
		Logger.debug("Logging in user");
		JsonNode data = request().body().asJson();
		if(!data.has("email") || !data.has("password")) 
			return badRequest("fields missing");
			
		String email = data.get("email").textValue();
		String password = data.get("password").textValue();
		Logger.debug("User email: " + email + " password: " + password);
		
		User user = Ebean.find(User.class).select("id").where().eq("email", email).and().eq("password", password).findUnique();
		if (user == null)
			return notFound("user not found");

		Logger.debug("User found: " + user.getId());
		return ok("K"+ ","+ user.getId());
	}
	
	public Result authenticateFacebookUser(String accessToken) {
		JsonNode rq = request().body().asJson();
		String name = rq.get("name").asText();
		String[] names = name.split(" ");
		String firstname = names[0];
		String lastname = names[1];
			
//		FacebookLoginInfo info = FacebookLoginInfo.find.byId(accessToken);
//		
//		if (info == null)
//			info = new FacebookLoginInfo(accessToken);

		User user = User.find.where().eq("email", firstname+lastname).findUnique();		
		if (user == null) {
			user = new User(firstname, lastname, LocalDate.now().toString(), firstname+lastname, "asdasdasdada");
			user.save();
		}
		
		return ok("K" + "," + user.getId());
	}
	
	public Result updateUserInformation(Long userId) {
		User user = User.find.byId(userId);
		if(user == null)
			return notFound("user not found");
		
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
		String job = jn.get("job").asText();
		
		Logger.debug("Updating user profile: " + uid + " with values: " + " " + bio +" "+ hobbies +" "+ job);
		
		User user = User.find.byId(uid);
		if (user == null)
			return notFound("user not found");
		
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
		if (job != null && job != "")
			toUpdate.setJob(job);
		
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
		
		Logger.debug("profile: " + user.getId() +"\n"+ prof.getBio() +"\n"+ prof.getHobbies() + prof.getJob());
		
		String json = "{\"uid\":" + user.getId() + ",\"bio\":\"" + prof.getBio() + "\",\"hobbies\":\"" + prof.getHobbies() + "\",\"job\":\"" + prof.getJob() + "\"}";
		JsonNode jn = Json.parse(json);
		return ok(jn);
	}
	
}