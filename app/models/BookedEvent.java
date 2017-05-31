package models;

import java.time.LocalDate;

import java.time.LocalTime;
import javax.persistence.*;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * Created the final stage of event, the BookedEvent,
 * Since the event is bound by the agreed time, date and users there is 
 *  no point in changing these variables (no setters). If so the method for finding 
 *   an event should start from the beginning.
 *   
 *   Constructor takes user1, user2, restaurant, date & time
 *    
 */

@Entity
@Table(name ="BookedEvent")
public class BookedEvent extends Model{

	@Id
	private int id;

	@OneToOne
	@JoinColumn(name= "user1", referencedColumnName="id")
	@JsonManagedReference
	private User user1;

	@OneToOne
	@JoinColumn(name= "user2", referencedColumnName="id")
	@JsonManagedReference
	private User user2;

	@OneToOne
	@JoinColumn(name= "restaurant", referencedColumnName="id")
	private Restaurant restaurant;

	
	@Column(name="date")
	private LocalDate date;


	@Column(name="time")
	private LocalTime time;

	public BookedEvent(User us1, User us2, String date, String time, Restaurant rest){
		this.user1 = us1;
		this.user2 = us2;
		this.date = LocalDate.parse(date);
		this.time = LocalTime.parse(time);
		this.restaurant = rest;
	}
	
	public BookedEvent(User us1, User us2, LocalDate date, LocalTime time, Restaurant rest){
		this.user1 = us1;
		this.user2 = us2;
		this.date = date;
		this.time = time;
		this.restaurant = rest;
	}

	public User getUser1(){
		return user1;
	}

	public User getUser2(){
		return user2;
	}

	public LocalDate getDate() {
		return date;
	}


	public LocalTime getTime() {
		return time;
	}

	public int getId(){
		return id; 
	}

	public static Finder<Long, BookedEvent> find = new Finder<Long, BookedEvent>(BookedEvent.class);

}
