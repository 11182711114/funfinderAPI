package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.avaje.ebean.Model.Finder;

import parser.Location;

@Entity
public class Restaurant {
	
	@Id
	private String id;
	
	private final List<String> types = new ArrayList<>();
	private String name;
	private double rating = -1;
	private String photoref;
	
	@OneToOne
	private Location location;
	
	public static Finder<Long, Restaurant> find = new Finder<Long,Restaurant>(Restaurant.class);
}
