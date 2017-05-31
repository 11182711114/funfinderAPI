package controllers;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.fasterxml.jackson.databind.JsonNode;
import com.sun.org.apache.xpath.internal.operations.And;

import models.Message;
import models.User;
import play.libs.Json;
import play.Logger;
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
		
		if (from == null || to == null || message == null || message.isEmpty())
			return badRequest();
		
		Message msg = new Message(from,to,message);
		Ebean.save(msg);
		
		return ok(Json.toJson(msg));
	}
	
	public Result getMessages(Long userId) {
		User user = User.find.byId(userId);
		if(user == null)
			return notFound();
		
		List<Message> toReturn = Message.find.where().eq("sender", user).findList();
		toReturn.addAll(Message.find.where().eq("receiver", user).findList());
		
		return ok(Json.toJson(toReturn));
	}
	
	public Result getMessagesFromSpecificUser(Long receiverId, Long senderId) {
		User receiver = User.find.byId(receiverId);
		User sender = User.find.byId(senderId);
		if(receiver == null || sender == null)
			return notFound();
		
		List<Message> msgs = Message.find.where().eq("sender", sender).and().eq("receiver", receiver).findList();
		
		JsonNode toReturn = Json.toJson(msgs);
		
		msgs.forEach(msg -> { 
			msg.setSeen(true);
			Ebean.update(msg);
		});
		
		return ok(toReturn);
	}
	
	public Result getLastMessage(Long receiver, Long sender) {
		User rec = User.find.byId(receiver);
		User send = User.find.byId(sender);

		Logger.debug("Trying to find latest message");
		Message msg = null;
		Message recMsg = null;
		try {
			msg = Message.find.setMaxRows(1).where().eq("sender", send).and().eq("receiver", rec).orderBy("sent desc").findUnique();
			Logger.debug("Getting last msg: " + msg.getId() +" "+ msg.getMessage());
		} catch (NullPointerException npe) {}
		
		try {
			recMsg = Message.find.setMaxRows(1).where().eq("sender", rec).and().eq("receiver", send).orderBy("sent desc").findUnique();
			Logger.debug("Getting last recMsg: " + recMsg.getId() +" "+ recMsg.getMessage());
		} catch (NullPointerException npe) {}
		
		if (msg == null && recMsg == null)
			return notFound("no msgs");
		else if (msg == null && recMsg != null)
			return ok(Json.toJson(recMsg));
		else if (recMsg == null && msg != null)
			return ok(Json.toJson(msg));		
		
		return ok(Json.toJson(msg.getSent().isAfter(recMsg.getSent()) ? msg : recMsg));
	}
	
	public Result getNewMessages(Long receiverId, Long senderId) {
		User receiver = User.find.byId(receiverId);
		User sender = User.find.byId(senderId);
		if(receiver == null || sender == null)
			return notFound();
		
		
		List<Message> newMsgs = Message.find.where().eq("sender", sender).and().eq("receiver", receiver).and().eq("seen", false).findList();
		
		newMsgs.forEach(msg -> {
			msg.setSeen(true);
			Ebean.update(msg);
		});
		
		return ok(Json.toJson(newMsgs));
	}
	
	public Result getNewMeta(Long userId) {
		User user = User.find.byId(userId);
		if(user == null)
			return notFound();
		
		List<User> userActivityNotSeen = Message.find.select("sender").where().eq("receiver", user).and().eq("seen", false).findList()
				.stream().map(Message::getSender).distinct().collect(Collectors.toList());
		
		return ok(Json.toJson(userActivityNotSeen));
	}
	
	public Result getAllMeta(Long userId) {
		User user = User.find.byId(userId);
		if(user == null)
			return notFound();
		
		List<Message> msgs = Message.find.select("sender").where().eq("receiver", user).findList();
		List<Message> sender = Message.find.select("receiver").where().eq("sender", user).findList();
		
		List<User> userActivity = msgs.stream().map(Message::getSender).distinct().collect(Collectors.toList());
		userActivity.addAll(sender.stream().map(Message::getReceiver).distinct().collect(Collectors.toList()));
		
		return ok(Json.toJson(userActivity));
	}
}
