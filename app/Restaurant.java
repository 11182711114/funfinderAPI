import java.util.*;

/*
 * TODO fix toString() and equals()
 */

public class Restaurant{

	private final List<String> types = new ArrayList<>();
//	private final String [] types = new String[10];
	private String id;
	private String name;
	private double rating = -1;
	private String website;
	private Location location = null; //TODO add this
	
	
//	 empty contructor
	  public Restaurant (){}
	  
	  
//	  constructor that takes name, id, rating and location class to create a Restaurant
	  public Restaurant(String name, String id, int rating, Location loc){
	  this.name = name;
	  this.id = id;
	  this.setLocation(loc);
	  }
	  
	 

	public String getId(){
		return id;
	}
	protected void setId(String placeId){
		this.id = placeId;
	}

	public String getName() {
		return name;
	}
	protected void setName(String name){
		this.name = name;
	}


	public Location getLocation() {
		return location;
	}


	public void setLocation(Location location) {
		this.location = location;
	}

	public double getRating(){
		return rating;
	}
	protected void setRating(double rating){
		this.rating = rating;
	}


	public String getWebsite(){
		return website;
	}
	protected void setWebsite(String website){
		this.website = website;
	}

	public List<String> getTypes(){
		return Collections.unmodifiableList(types);
	}

	//	protected Restaurant addTypes(Collection<String> placeTypes){
	protected Restaurant addTypes(String type){
		this.types.add(type);
		return this;
	}
	
	

	@Override    
	public String toString(){
		return String.format("Restaurant{id=%s}", id + " name: " + name); //TODO remove: name parameter or unnecessary info
	}

	@Override
	public boolean equals(Object obj){
		return obj instanceof Restaurant && ((Restaurant) obj).id.equals(id);
	}



}