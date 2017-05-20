package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name = "Event")
public class Event extends Model {

	@Id
	@Column(name = "eventId")
	private int eventId;

	@Constraints.Required
	@Column(name = "date")
	private LocalDate date;

	@Constraints.Required
	@Column(name = "time")
	private LocalDate time;

	@ManyToMany(mappedBy ="events")
	public List<Restaurant> restaurants;

	
	//FIXME CHANGE AND CONNECT TO LOCATION TABLE
	@Column(name = "location")
	public String location;
	

	public Event(String date, String time, String location) {
		this.date = LocalDate.parse(date);
		this.time = LocalDate.parse(time);
		this.location = location;
	}

	public LocalDate getDate(){
		return date;
	}

	public LocalDate getTime(){
		return time;
	}
	
	public int getId(){
		return eventId;
	}
	
	public List<Restaurant> getRestaurants(){
		return restaurants;
	}

	public static Finder<Long, Event> find = new Finder<Long, Event>(Event.class);
}



