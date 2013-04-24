package il.ac.huji.todolist;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.ContactsContract.Directory;

/**
 * Extends the handler class to handlle some of the basic functionality of 
 * the twitter  API
 * @author yoavk_000
 *
 */
/**
 * @author yoavk_000
 * 
 */
public class TwitterHandler extends JSONHandler<TwitterTask> {
	final String searchTweeterAPI = "http://search.twitter.com/search.json?q=%23";

	@Override
	protected String getSearchString(String s) {
		return searchTweeterAPI + s + "&rpp=100";
	}

	@Override
	protected ArrayList<TwitterTask> getDataFromJsonString(String jsonResult) {
		ArrayList<TwitterTask> FIList = new ArrayList<TwitterTask>();
		try {
			JSONObject json = new JSONObject(jsonResult);

			JSONArray arr = json.getJSONArray("results");
			for (int i = 0; i < arr.length(); i++) {
				FIList.add(new TwitterTask(arr.getJSONObject(i)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return FIList;
	}

	/**
	 * Search for the file dat.dat and extract the default hashtag If the method
	 * cant find the folder it will create a file containing the tag todolist
	 * 
	 * @return
	 * @throws IOException
	 */
	public String getDefaultHashTag() throws IOException {
		String line;
		String everything = "";
		BufferedReader br = new BufferedReader(new FileReader(
				TodoListManagerConstants.DATA_FILE));
		StringBuilder sb = new StringBuilder();
		line = br.readLine();
		while (line != null) {
			sb.append(line);
			line = br.readLine();
		}
		br.close();
		everything = sb.toString();

		return everything;
	}
	public boolean defaultTagFileExists(){
		File direct = new File(TodoListManagerConstants.DATA_FILE_DIR);
		if (!direct.exists())
			return false;
		File file = new File(TodoListManagerConstants.DATA_FILE
				+ "/dat.dat");
		if (!file.exists())
			return false;
		return true;
	}
	/**
	 * Write the string tag name into the dat.dat file
	 * 
	 * @param tagname
	 * @throws IOException
	 */
	public void setDefaultHashTag(String tagname) throws IOException {
		File direct = new File(TodoListManagerConstants.THUMBNAIL_FILE_DIR);
		if (!direct.exists())
			direct.mkdir();
		File file = new File(TodoListManagerConstants.DATA_FILE);
		FileOutputStream fos = new FileOutputStream(file);
		fos.write((tagname + "\n").getBytes());
		fos.flush();
		fos.close();
	}

}
