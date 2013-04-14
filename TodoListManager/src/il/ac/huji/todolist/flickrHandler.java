package il.ac.huji.todolist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class flickrHandler {
	private final String flickrAPIAddress = "http://api.flickr.com/services/rest/";
	private final String flickrAPISearch = "?method=flickr.photos.search";
	private final String flickrAPIID = "&api_key=6478053b90635698cb4afe8e2430a657";
	private final String getInJSONFormat = ";&format=json&jsoncallback=?";
	private final String forDeb="http://search.twitter.com/search.json?q=hebrew%20university ";
	private final String forDeb2="https://maps.googleapis.com/maps/api/geocode/json?address=jerusalem&sensor=false";
	// http://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=6478053b90635698cb4afe8e2430a657&text=cat;&format=json&jsoncallback=?
	
	protected String getFlickerSearchString(String toSearch) {
		return flickrAPIAddress + flickrAPISearch + flickrAPIID + "&text="
				+ toSearch + getInJSONFormat;
	}

	protected String readStream(InputStream in) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuffer buffer = new StringBuffer();
		for (String line = reader.readLine(); line != null; line = reader
				.readLine()) {
			buffer.append(line);
			buffer.append('\n');
		}
		return buffer.toString();
	}
	protected ArrayList<flickrImage> getPicsFromString(String jsonResult) throws JSONException {
		ArrayList<flickrImage> FIList = new ArrayList<flickrImage>();
		jsonResult=jsonResult.replace("jsonFlickrApi(", "");
		jsonResult=jsonResult.replace("})", "}");
		
		JSONObject json= new JSONObject(jsonResult);
		JSONObject photosOBJ = json.getJSONObject("photos");
		JSONArray arr=photosOBJ.getJSONArray("photo");
		for (int i = 0; i < arr.length(); i++) {
			FIList.add(new flickrImage(arr.getJSONObject(i)));
		}
		return FIList;
	}
	public ArrayList<flickrImage> getImageArrayListFromFlickr(String search) throws IOException, JSONException{
		URL url = new URL(getFlickerSearchString(search));
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		return getPicsFromString(readStream(conn.getInputStream()));
	}
	public Bitmap getImageViaURL(String url){
		try {
			URL newurl = new URL(url);
			Bitmap bitmap = BitmapFactory.decodeStream(newurl.openConnection()
					.getInputStream());
			return bitmap;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
