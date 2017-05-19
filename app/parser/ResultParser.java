package parser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.json.*;


/* 
 * The ResultParser class connects the users query to the Google Places API (web-service)
 * It allows for a search to be made on either the location of the user 
 * (coordinates) or a selected location (written in text).
 * 
 * It parses the results from google places api and returns a arrayList of created restaurants
 */

public class ResultParser{

	private static String typeSearch = null;
	private static final String PLACES_API_SOURCE = "https://maps.googleapis.com/maps/api/place";
	private static final String JSON_OUT = "/json";
	private static final String NEXT_PAGE_TOKEN = "next_page_token";
	private static final String KEY = "AIzaSyCEJku8qbNsDR7R1yGx5KGDr4g8ROw5LtU"; //TODO TA BORT KEY INNAN PUSH?
	private final static ArrayList<String> UNWANTED_TYPES = new ArrayList<String>(){{
		add("book_store"); 
		add("store");
		add("convenience store");
		add("fastfood");
	}};


	/*
	 * Allows restaurants to be searched from user given location,
	 * takes the location name as a parameter
	 * User declares location and method retrieves answer from google places api
	 *   (should) return up to 20 results
	 */
	public ArrayList<ParsedRestaurant> searchText(String location){
		typeSearch = "/textsearch";
		HttpURLConnection conn = null;
		StringBuilder jsonResults = new StringBuilder();

		try{
			StringBuilder request = new StringBuilder(PLACES_API_SOURCE);
			request.append(typeSearch);
			request.append(JSON_OUT);
			request.append("?query=restaurants+in+");
			request.append(location);//+"+Stockholm");//TODO use this to search in sthlm vicinity
			request.append("&key=" + KEY);

			System.out.println("<Connecting to Google API>"); //TODO remove: TEST

			URL url = new URL(request.toString());
			conn = (HttpURLConnection) url.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line = "";
			while ((line = br.readLine()) != null) {
				jsonResults.append(line);
			}
		}catch(MalformedURLException e){
			System.out.println("URL ERROR [search] ");
		}catch(IOException e){
			System.out.println("CONNECTION ERROR [search] "+ e);
		}finally{
			if(conn!=null)
				conn.disconnect();
		}
		parseResults(jsonResults);
		ArrayList<ParsedRestaurant> results = parseResults(jsonResults);
		return results;
	}

	/*
	 * Allows for restaurants to be retrieved by providing users coordinates
	 * takes latitude, longitude and radius for the search as method parameter
	 * 
	 * returns up to 20 matching places 
	 */
	public ArrayList<ParsedRestaurant> searchNearby(double lat, double lang, int radius){
		typeSearch = "/nearbysearch";
		HttpURLConnection conn = null;
		StringBuilder jsonResults = new StringBuilder();

		try{
			StringBuilder request = new StringBuilder(PLACES_API_SOURCE);
			request.append(typeSearch);
			request.append(JSON_OUT);
			request.append("?location=" + String.valueOf(lat)+",%20"+String.valueOf(lang));
			request.append("&radius=" + String.valueOf(radius));
			request.append("&keyword=restaurants");
			request.append("&key=" + KEY);

			System.out.println("<Connecting to Google API>"); //TODO remove: TEST

			URL url = new URL(request.toString());
			conn = (HttpURLConnection) url.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line = "";
			while ((line = br.readLine()) != null) {
				jsonResults.append(line);
			}
		}catch(MalformedURLException e){
			System.out.println("URL ERROR [search] ");
		}catch(IOException e){
			System.out.println("CONNECTION ERROR [search] "+ e);
		}finally{
			if(conn!=null)
				conn.disconnect();
		}
		ArrayList<ParsedRestaurant> results = parseResults(jsonResults);
		return results;
	}

	/*
	 * takes the next page token from searchNearby or searchLocation,
	 * if these searches have +20 object the rest is delivered on a separate json call
	 * 
	 * takes the nextPageTokes as parameter, returns results for parsing
	 */

	private static StringBuilder searchNextPage(String nextToken){
		HttpURLConnection conn = null;
		StringBuilder jsonResults = new StringBuilder();
		try{
			StringBuilder request = new StringBuilder(PLACES_API_SOURCE);
			request.append(typeSearch);
			request.append(JSON_OUT);
			request.append("?pagetoken=");
			request.append(nextToken);
			request.append("&key=");
			request.append(KEY);

			URL url = new URL(request.toString());
			conn = (HttpURLConnection) url.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line = "";
			while ((line = br.readLine()) != null) {
				jsonResults.append(line);
			}
		}catch(MalformedURLException e){
			System.out.println("URL ERROR [search] ");
		}catch(IOException e){
			System.out.println("CONNECTION ERROR [search] "+ e);
		}finally{
			if(conn!=null)
				conn.disconnect();
		}
		return jsonResults;
	}

	/*
	 * takes the StringBuilder returned from Google places API request (fetched by 
	 * nearbySearch or textSearch) and parses the results to instances of Restaurant class.
	 * 
	 * returns a arraylist of Restaurants
	 */
	private static ArrayList<ParsedRestaurant> parseResults(StringBuilder jsonResults){

		boolean validObject = true;
		ArrayList<ParsedRestaurant> resultsList = null;
		try{ 
			JSONObject jsonObj = new JSONObject(jsonResults.toString());

			//CREATES results from array with tag "results" 
			JSONArray prediJsonArr = jsonObj.getJSONArray("results");

			if(prediJsonArr!=null){
				resultsList = new ArrayList<ParsedRestaurant>();
				for(int i=0; i<prediJsonArr.length(); i++){
					JSONObject objInArr = prediJsonArr.getJSONObject(i);

					// Create Restaurant instance
					ParsedRestaurant newRest = new ParsedRestaurant();
					newRest.setName(objInArr.getString("name"));
					newRest.setId(objInArr.getString("place_id"));
					if(objInArr.has("rating"))
						newRest.setRating(objInArr.getDouble("rating"));
					else
						newRest.setRating(-1);

					/*
					 * Here we can check for types and remove those who
					 * are not of interest, that we have saved in UNWANTED_TYPES list.
					 * else we set the id to 0
					 */
					JSONArray taggedTypes = objInArr.getJSONArray("types");
					for(int j=0; j<taggedTypes.length(); j++){
						if(!UNWANTED_TYPES.contains(taggedTypes.getString(j)))
							newRest.addTypes(taggedTypes.getString(j));
						else{
							validObject = false;
							break;
						}
					}
					/*
					 * if the place id is not 0, the object is valid to add
					 *  else we don't add it.
					 */
					if(validObject){
						//Create location instance
						ParsedLocation loc = new ParsedLocation();
						loc.setLattitude(objInArr.getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
						loc.setLongitude(objInArr.getJSONObject("geometry").getJSONObject("location").getDouble("lng"));
						if(objInArr.has("vicinity"))
							loc.setAddress(objInArr.getString("vicinity")); //VICINITY OR FORMATTED ADDRESS
						else if (objInArr.has("formatted_address"))
							loc.setAddress(objInArr.getString("formatted_address"));
						else
							loc.setAddress("not avaliable..");
						newRest.setLocation(loc);
						resultsList.add(newRest);	
					}
					validObject = true;
				}
			}

			/*
			 * the time from the JSON results is delivered to when the next page is
			 * created is slightly delayed, this allows the method to sleep for 1 second
			 * and then runs search for the nextPage 
			 * 
			 * UNCOMMENT TO ALLOW +20 PLACES TO BE FETCHED
			 */
			try {
				TimeUnit.MILLISECONDS.sleep(2000);
			} catch (InterruptedException e) {
				System.out.println("TIMEUNIT REST ERROR "+ e);
			}

			String nextPageToken = null;

			if (jsonObj.has(NEXT_PAGE_TOKEN)) {
				nextPageToken = jsonObj.getString("next_page_token");
				resultsList.addAll(parseResults(searchNextPage(nextPageToken)));
			}
		}catch(JSONException e){
			System.out.println("JSON ERROR "+ e);
		}
		return resultsList;
	}

}
