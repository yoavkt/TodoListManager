package il.ac.huji.todolist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONException;

/**
 * This object is a basic JSON based web server handler
 * This is an abstract class that has the methoes of converting the web stream to a json string.
 * You must implement the method that creates the web service string
 * and the method that parse the json string into an array list of T objects
 * @param <T> the type of object to be returned after the json stringis parsed
 */
public abstract class JSONHandler<T> {

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
	public ArrayList<T> getArrayListFromWebService(String search) throws IOException, JSONException{
		URL url = new URL(getSearchString(search));
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		return getDataFromJsonString(readStream(conn.getInputStream()));
	}
	protected  abstract String getSearchString(String s);
	protected abstract ArrayList<T> getDataFromJsonString(String jsonResult) throws JSONException;
}
