package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Restaurant {
	
	@Id
	private int id;
	
	private String name;
	
	private int rating;
	
	@OneToOne
	private Location location;
}
