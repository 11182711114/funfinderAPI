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
	@Column(name = "id")
	private int id;
	
	
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
	
	@Override
	public boolean equals(Object obj){
		return obj instanceof Location && ((Location) obj).latitude == latitude && ((Location) obj).longitude == longitude;
	} 

	public static Finder<Long, Location> find = new Finder<Long, Location>(Location.class);

//	public static Finder<String, Location> find = new Finder<String, Location>(Location.class);
	
	public int getId(){
		return id;
	}
	
}
