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
	@JoinColumn(name = "from")
	private User from;
	
	@ManyToOne
	@JoinColumn(name = "to")
	private User to;
	
	private String message;
	

	public Message(User from, User to, String message) {
		this.from = from;
		this.to = to;
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

	public User getFrom() {
		return from;
	}

	public User getTo() {
		return to;
	}
	
	public static Finder<Long, Message> find = new Finder<Long,Message>(Message.class);
}
