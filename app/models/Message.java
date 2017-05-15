package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.avaje.ebean.Model.Finder;

@Entity
@Table(name = "Message")
public class Message {

	@Id
	private int id;
	
	@ManyToOne
	@JoinColumn(name = "sender")
	private User sender;
	
	@ManyToOne
	@JoinColumn(name = "receiver")
	private User receiver;
	
	private String message;
	

	public Message(User sender, User receiver, String message) {
		this.sender = sender;
		this.receiver = receiver;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getId() {
		return id;
	}

	public User getSender() {
		return sender;
	}

	public User getReceiver() {
		return receiver;
	}
	
	public static Finder<Long, Message> find = new Finder<Long,Message>(Message.class);
}
