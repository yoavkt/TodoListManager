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


public class FlickrHandler extends JSONHandler<FlickrImage> {
	private  String flickrAPIAddress = "http://api.flickr.com/services/rest/";
	private  String flickrAPISearch = "?method=flickr.photos.search";
	private  String flickrAPIID = "&api_key=6478053b90635698cb4afe8e2430a657";
	private  String getInJSONFormat = ";&format=json&jsoncallback=?";
	private String textParam="&text=";
	private String mainJSONObject="photos";
	private String secondaryJSONObject="photo";
	
	protected String getSearchString(String toSearch) {
		return flickrAPIAddress + flickrAPISearch + flickrAPIID + textParam
				+ toSearch + getInJSONFormat;
	}
	protected ArrayList<FlickrImage> getDataFromJsonString(String jsonResult) throws JSONException {
		ArrayList<FlickrImage> FIList = new ArrayList<FlickrImage>();
		jsonResult=jsonResult.replace("jsonFlickrApi(", "");
		jsonResult=jsonResult.replace("})", "}");
		
		JSONObject json= new JSONObject(jsonResult);
		JSONObject photosOBJ = json.getJSONObject(mainJSONObject);
		JSONArray arr=photosOBJ.getJSONArray(secondaryJSONObject);
		for (int i = 0; i < arr.length(); i++) {
			FIList.add(new FlickrImage(arr.getJSONObject(i)));
		}
		return FIList;
	}

	/**
	 * This method download an image from a static link this is used
	 * When one prefer to transfer the url instead of the full flickrimage object
	 * @param url of the image
	 * @return the image as a bitmap
	 */
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
