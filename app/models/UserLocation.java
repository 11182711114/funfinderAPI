package models;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class UserLocation {

	@OneToOne
	private User user;
	
	@OneToOne
	private Location location;
	
	
}
