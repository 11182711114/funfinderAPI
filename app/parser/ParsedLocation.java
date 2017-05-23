package parser;

/*
 * TODO fix toString() and equals()
 */

public class ParsedLocation {

	private String address;
	private double latitude = -1;
	private double longitude = -1;
	
	public ParsedLocation(){}
	
	public ParsedLocation(String adress, double lattitude, double longitude){
		this.address = adress;
		this.setLattitude(lattitude);
		this.setLongitude(longitude);
	}
	
	public void setAddress(String address){
		this.address = address;
	}

	public String getAddress(){
		return address;
	}
	
	public double getLattitude() {
		return latitude;
	}

	public void setLattitude(double lattitude) {
		this.latitude = lattitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	@Override
	public String toString(){
		return "adress "+ address;
	}
	
	@Override
	public boolean equals(Object obj){
		return obj instanceof ParsedLocation && ((ParsedLocation) obj).latitude == latitude && ((ParsedLocation) obj).longitude == longitude;
	}
}
