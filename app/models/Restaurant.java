package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.avaje.ebean.Model.Finder;

@Entity
public class Restaurant {
	
	@Id
	private int id;
	
	private String name;
	
	private int rating;
	
	@OneToOne
	private Location location;
	
	public static Finder<Long, Restaurant> find = new Finder<Long,Restaurant>(Restaurant.class);
}
