package controllers;

import com.fasterxml.jackson.databind.JsonNode;
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


}

