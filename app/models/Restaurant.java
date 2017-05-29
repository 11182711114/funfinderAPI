package models;



import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;

import play.Logger;


@Entity
@Table(name = "Restaurant")
public class Restaurant extends Model{


	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "name")
	private String name;

	@Column(name = "rating")
	private double rating = -1;


	@OneToOne
	@JoinColumn(name = "location", referencedColumnName = "id")
	private Location location;

	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(name="Event_Rest",
			joinColumns=@JoinColumn(name="atRest", referencedColumnName="id"),
			inverseJoinColumns=@JoinColumn(name="atEvent", referencedColumnName="eventId"))
	@JsonBackReference
	private List<Event> events;
	
	public Restaurant(String id, String name, double rating, String locid){
		this.location = Location.find.byId(Long.parseLong(locid));
		this.id = id;
		this.name = name;
		this.rating = rating;
	}

	public Restaurant(String id, String name, double rating, Location locz){
		this.id = id;
		this.name = name;
		this.rating = rating;
		this.location = locz;
	}

	public Restaurant(){};

	public String getId(){
		return id;
	}

	public String getName(){
		return name;
	}

	public double getRating(){
		return rating;
	}
	
	public String getAdress(){
		if(location==null)
			return "not available";
		return location.getAdress();	
	}

	public List<Event> getEvents(){
		return events;
	}

	public static Finder<String, Restaurant> find = new Finder<String,Restaurant>(Restaurant.class);


}
