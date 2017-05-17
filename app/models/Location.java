package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.avaje.ebean.Model;
import com.avaje.ebean.Model.Finder;

@Entity
@Table(name = "Location")
public class Location extends Model{
	

	@Column(name = "adress")
	private String adress;
	
	@Column(name = "latitude")
	private double latitude;
	
	@Column(name = "longitude")
	private double longitude;
	
	@Id
	private String id;
	
	public Location (String adress, double latitude, double longitude){
		this.adress = adress;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String getAdress() {
		return adress;
	}

	public double getLat(){
		return latitude;
	}
	
	public double getLang(){
		return longitude;
	}

	public static Finder<String, Location> find = new Finder<String, Location>(Location.class);
	
	public String getId(){
		return id;
	}
}
