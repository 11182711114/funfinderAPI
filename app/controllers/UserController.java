package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.User;
import play.db.ebean.Transactional;
import play.libs.Json;
import play.mvc.*;

public class UserController extends Controller {
	
	public Result getUser() {
		Long id = 2L;
		User user = User.find.byId(id);
		JsonNode result = Json.toJson(user);
		return ok(result);
	}
	
	@Transactional
	public Result getUserByID(Long id) {
		User user = User.find.byId(id);
		JsonNode result = Json.toJson(user);
		return ok(result);
	}
	
	
}