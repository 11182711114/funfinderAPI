package controllers;

import javax.inject.Inject;

import com.avaje.ebean.Ebean;

import models.Message;
import models.User;
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
}
