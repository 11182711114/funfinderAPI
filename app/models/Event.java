package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import play.data.validation.Constraints;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "Event")
public class Event extends Model {

	@Id
	@Column(name = "eventId")
	private int eventid;

	@ManyToOne
	@JoinColumn(name = "user")
	@JsonManagedReference
	User user;

	@Constraints.Required
	@Column(name = "date")
	private LocalDate date;

	@Constraints.Required
	@Column(name = "time")
	private LocalTime time;

	@ManyToMany(mappedBy ="events")
	private List<Restaurant> restaurants;


	//FIXME CHANGE AND CONNECT TO LOCATION TABLE
	@Column(name = "location")
	private String location;
	
	

	/*
	 * If user sends the text name of the location for the event
	 */
	public Event(String date, String time, String location, User user) {
		this.date = LocalDate.parse(date);
		this.time = LocalTime.parse(time);
		this.location = location;
		this.user = user;
	} 
	
	public Event(String date, String time, String location, User user, List<Restaurant> rests) {
		this.date = LocalDate.parse(date);
		this.time = LocalTime.parse(time);
		this.location = location;
		this.user = user;
		this.restaurants = rests;
	} 

	/*
	 * if user sends the geo-location of their location for the event
	 * 	could also be made to take the searchradius for the event
	 */
	public Event(String date, String time, double lat, double lng, User user) {
		this.date = LocalDate.parse(date);
		this.time = LocalTime.parse(time);
		this.user = user;
	}


	public LocalDate getDate(){
		return date;
	}

	public LocalTime getTime(){
		return time;
	}

	public User getUser(){
		return user;
	}

	public int getId(){
		return eventid;
	}
	
	public void setRestaurant(List<Restaurant> rests){
		this.restaurants = rests;
	}

	public List<Restaurant> getRestaurants(){
		return restaurants;
	}


	public static Finder<Long, Event> find = new Finder<Long, Event>(Event.class);
}



