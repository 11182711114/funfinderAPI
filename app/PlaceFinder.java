import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;

import org.eclipse.jetty.util.log.Log;

import org.json.*;

public class PlaceFinder {
	private static final String LOG_TAG = "funFinderAPI";
	private static final String PLACES_API_SOURCE = "https://maps.googleapis.com/maps/api/place";

	private static final String TYPE_DETAILS = "/details";
	private static final String TYPE_SEARCH = "/search";

	private static final String KEY = "";

	private static final String JSON_OUT = "/json";


	public static ArrayList<Place> search(String keyword, double lat, double lang, int radius){
		ArrayList<Place> resultsList = null;

		HttpURLConnection conn = null;
		StringBuilder jsonResults = new StringBuilder();

		try{
			StringBuilder strb = new StringBuilder(PLACES_API_SOURCE);
			strb.append(TYPE_SEARCH);
			strb.append(JSON_OUT);
			//			strb.append("sensor=false"); //why?
			strb.append("&key=" + KEY);
			strb.append("&keyword=" + URLEncoder.encode(keyword, "utf-8"));
			strb.append("&location=" + String.valueOf(lat)+String.valueOf(lang));
			strb.append("&radius=" + String.valueOf(radius));

			URL url = new URL(strb.toString());
			conn = (HttpURLConnection) url.openConnection();
			InputStreamReader in = new InputStreamReader(conn.getInputStream());

			int read;
			char [] buff = new char[1024];
			while((read = in.read(buff)) != -1){
				jsonResults.append(buff, 0, read);
			}

		}catch(MalformedURLException e){
			Log.e(LOG_TAG, "URL ERROR", e);
			return resultsList;
		}catch(IOException e){
			Log.e(LOG_TAG, "CONNECTION ERROR", e);
			return resultsList;
		}finally{
			if(conn!=null){
				conn.disconnect();
			}
		}
		/*
		 * time to create JSON object in hirarchy from results
		 */
		try{ 
			JSONObject jsonObj = new JSONObject(jsonResults.toString());
			JSONArray prediJsonArr = jsonObj.getJSONArray("predictions");

			//extract descriptions from results
			resultsList = new ArrayList<Place>(prediJsonArr.length());
			for(int i = 0; i < prediJsonArr.length(); i++){
				Place place = new Place();
				place.reference = prediJsonArr.getJSONObject(i).getString("reference");
				place.name = prediJsonArr.getJSONObject(i).getString("description");
				resultsList.add(place);
			}
		}catch(JSONException e){
			Log.e(LOG_TAG, "ERROR PROCESSING JSON", e);
		}
		return resultsList;
	}

	public static Place details(String ref){
		HttpURLConnection conn = null; 
		StringBuilder jsonResults = new StringBuilder();
		try{
			StringBuilder strb = new StringBuilder(PLACES_API_SOURCE);
			strb.append(TYPE_DETAILS);
			strb.append(JSON_OUT);
			//			strb.append("?sensor=false");
			strb.append("&key="+KEY);
			strb.append("&reference="+URLEncoder.encode(ref, "utf-8"));

			URL url = new URL(strb.toString());
			conn = (HttpURLConnection) url.openConnection();
			InputStreamReader in = new InputStreamReader(conn.getInputStream());

			int read;
			char [] buff = new char[1024];
			while((read = in.read(buff)) != -1){
				jsonResults.append(buff, 0, read);
			}
		}catch(MalformedURLException e){
			Log.e(LOG_TAG, "URL ERROR", e);
			return null;
		}catch(IOException e){
			Log.e(LOG_TAG, "CONNECTION ERROR", e);
			return null;
		}finally{
			if(conn!=null){
				conn.disconnect();
			}
		}

		Place place = null;
		try{
			JSONObject jsonObj = new JSONObject(jsonResults.toString()).getJSONObject("result");

			place = new Place();
			place.icon = jsonObj.getString("icon");
			place.name = jsonObj.getString("name");

		}catch(JSONException e){
			Log.e(LOG_TAG, "JSON ERROR", e);
		}
		return place;
	}

}
