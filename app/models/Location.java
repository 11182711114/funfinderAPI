package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Location")
public class Location {
	
	private String adress;
	
	private double latitude;
	
	private double longitude;
	
	@Id
	private int id;
	
}
