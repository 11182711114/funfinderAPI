import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Guice;
import com.google.inject.Inject;

import models.Event;
import play.*;
import play.ApplicationLoader.Context;
import play.inject.guice.GuiceApplicationBuilder;
import play.inject.guice.GuiceApplicationLoader;
import play.test.FakeApplication;
import play.test.Helpers;

import org.json.JSONObject;
import org.junit.*;


import static play.test.Helpers.*;
import static org.junit.Assert.*;


/**
 *
 * Simple (JUnit) tests that can call all parts of a play app.
 * If you are interested in mocking a whole application, see the wiki for more details.
 *
 */
public class ApplicationTest {

	private static final String BASE_URL = "http://localhost:9000/event/";
	
	@Test
	public void simpleCheck() {
		int a = 1 + 1;
		assertEquals(2, a);
	}

//	@Test
//	public void createEventTest() throws Exception{
//
//		//
//		Event event = new Event("2017-05-22","10:15", "kista");
//		JSONObject obj = new JSONObject(makeRequest(
//				BASE_URL, "POST", new JSONObject(event)));
//		assertTrue(obj.getBoolean("Event created"));			
//	}

	private static String makeRequest(String myUrl, String httpMethod, JSONObject parameters) throws Exception {
		URL url = null;
		url = new URL(myUrl);
		HttpURLConnection conn = null;
		conn = (HttpURLConnection) url.openConnection();
		conn.setDoInput(true);
		conn.setRequestProperty("Content-Type", "application/json");
		DataOutputStream dos = null;
		conn.setRequestMethod(httpMethod);

		if (Arrays.asList("POST", "PUT").contains(httpMethod)) {
			String params = parameters.toString();
			conn.setDoOutput(true);
			dos = new DataOutputStream(new BufferedOutputStream(conn.getOutputStream()));
			dos.writeBytes(params);
			dos.flush();
			dos.close();
		}

		int respCode = conn.getResponseCode();
		if (respCode != 200 && respCode != 201) {
			String error = inputStreamToString(conn.getErrorStream());
			return error;
		}
		String inputString = inputStreamToString(conn.getInputStream());

		return inputString;
	}

	private static String inputStreamToString(InputStream is) throws Exception{
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		br = new BufferedReader(new InputStreamReader(is));
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		br.close();
		return sb.toString();
	}

	//    @Test
	//    public void renderTemplate() {
	//        Content html = views.html.index.render("Your new application is ready.");
	//        assertEquals("text/html", html.contentType());
	//        assertTrue(html.body().contains("Your new application is ready."));
	//    }


}
