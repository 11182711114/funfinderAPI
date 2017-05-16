package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.avaje.ebean.Model;
import com.avaje.ebean.Model.Finder;

import parser.Location;


@Entity
@Table(name = "Restaurant")
public class Restaurant extends Model{
	

	@Id
	@Column(name = "id")
	private String id;
	
//	private final List<String> types = new ArrayList<>();
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "rating")
	private double rating = -1;
	
	@OneToOne
	@JoinColumn(name = "loc_id")
	private Location location;
	
	
	
	public Restaurant(String id, String name, double rating){
		this.id = id;
		this.name = name;
		this.rating = rating;
	}
	
	public String getId(){
		return id;
	}
	
	public String getName(){
		return name;
	}
	
	public double getRating(){
		return rating;
	}

	public static Finder<String, Restaurant> find = new Finder<String,Restaurant>(Restaurant.class);
	
}
