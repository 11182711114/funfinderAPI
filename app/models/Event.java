package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

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
	public int eventId;

	@Constraints.Required
	@Column(name = "date")
	public String date;

	@Constraints.Required
	@Column(name = "time")
	public String time;

	
	//Many events can be held near many restaurants, and vice versa.
	@ManyToMany
	@JoinTable(name= "EVENT_REST",
				joinColumns={@JoinColumn(name="event", referencedColumnName="eventId")},
				inverseJoinColumns={@JoinColumn(name="restaurant", referencedColumnName="id")})
	public List<Restaurant> restaurants;


	//One user may only hold one event at a time?
//	@ManyToOne
//	@JoinColumn(name = "user", referencedColumnName = "id")
//	private User user;


	public Event(String date, String time) {
		this.date = date;
		this.time = time;
	}

	public String getDate(){
		return date;
	}

	public String getTime(){
		return time;
	}
	
	public int getId(){
		return eventId;
	}

	public static Finder<Long, Event> find = new Finder<Long, Event>(Event.class);
}



