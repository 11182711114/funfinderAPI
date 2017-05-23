package parser;
import java.util.*;

/*
 * TODO fix toString() and equals()
 */

public class ParsedRestaurant{

	private final List<String> types = new ArrayList<>();
	
	private String id;
	private String name;
	private double rating = -1;
	private ParsedLocation location = null; //TODO add this
	private String photoref;
	
	
//	 empty contructor
	  public ParsedRestaurant (){}
	  
	  
//	  constructor that takes name, id, rating and location class to create a Restaurant
	  public ParsedRestaurant(String name, String id, int rating, ParsedLocation loc){
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

	public ParsedLocation getLocation() {
		return location;
	}

	public void setLocation(ParsedLocation location) {
		this.location = location;
	}

	public double getRating(){
		return rating;
	}
	protected void setRating(double rating){
		this.rating = rating;
	}

	public List<String> getTypes(){
		return Collections.unmodifiableList(types);
	}

	protected ParsedRestaurant addTypes(String type){
		this.types.add(type);
		return this;
	}
	
	public String isPhotoref() {
		return photoref;
	}

	protected void setPhotoref(String photoref) {
		this.photoref = photoref;
	}	
	

	@Override    
	public String toString(){
		return String.format("Restaurant{id=%s}", id + " name: " + name); //TODO remove: name parameter or unnecessary info
	}

	@Override
	public boolean equals(Object obj){
		return obj instanceof ParsedRestaurant && ((ParsedRestaurant) obj).id.equals(id);
	}







}