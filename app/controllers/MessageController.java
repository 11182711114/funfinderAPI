package controllers;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;

import models.Message;
import models.User;
import play.libs.Json;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;

public class MessageController extends Controller{
	
	@Inject 
	FormFactory formFactory;
	
	public Result sendMessage() {
		DynamicForm df = formFactory.form().bindFromRequest();
		
		User from = User.find.byId(Long.parseLong(df.get("from")));
		User to = User.find.byId(Long.parseLong(df.get("to")));
		String message = df.get("message");
		
		Message msg = new Message(from,to,message);
		Ebean.save(msg);
		
		return ok(Json.toJson(msg));
	}
	
	public Result getMessages(Long userId) {
		User user = User.find.byId(userId);
		
		List<Message> toReturn = Message.find.where().eq("sender", user).findList();
		toReturn.addAll(Message.find.where().eq("receiver", user).findList());
		
		return ok(Json.toJson(toReturn));
	}
	
	public Result getMessagesFromSpecificUser(Long receiverId, Long senderId) {
		User receiver = User.find.byId(receiverId);
		User sender = User.find.byId(senderId);
		List<Message> msgs = Message.find.where().eq("sender", sender).and().eq("receiver", receiver).findList();
		
		JsonNode toReturn = Json.toJson(msgs);
		
		msgs.forEach(msg -> { 
			msg.setSeen(true);
			Ebean.update(msg);
		});
		
		return ok(toReturn);
	}
	
	public Result getNewMeta(Long userId) {
		User user = User.find.byId(userId);
		
		List<User> userActivityNotSeen = Message.find.select("sender").where().eq("seen", false).findList()
				.stream().map(Message::getSender).distinct().collect(Collectors.toList());
		
		return ok(Json.toJson(userActivityNotSeen));
	}
}
