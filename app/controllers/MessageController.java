package controllers;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.avaje.ebean.Ebean;

import models.Message;
import models.User;
import play.api.libs.json.Json;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;

public class MessageController extends Controller{
	
	@Inject FormFactory formFactory;
	
	public Result sendMessage() {
		DynamicForm df = formFactory.form().bindFromRequest();
		
		User from = User.find.byId(Long.parseLong(df.get("from")));
		User to = User.find.byId(Long.parseLong(df.get("to")));
		String message = df.get("message");
		
		Message msg = new Message(from,to,message);
		Ebean.save(msg);
		
		return null;
	}
	
	public Result getMessages(String userId) {
		User user = User.find.byId(Long.parseLong(userId));
		
		List<Message> toReturn = new ArrayList<>();
		toReturn.addAll(user.getMessagesReceived());
		toReturn.addAll(user.getMessagesSent());

		
//		return ok(Json.toJson(toReturn));
		// TODO Return actual messages
		return ok();
	}
}
