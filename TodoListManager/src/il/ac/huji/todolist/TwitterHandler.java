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

public class TwitterHandler extends JSONHandler<TwitterTask> {
	final String searchTweeterAPI="http://search.twitter.com/search.json?q=%23";
	//http://search.twitter.com/search.json?q=%23todoapp
	
	@Override
	protected String getSearchString(String s) {
		return searchTweeterAPI+s+"&rpp=100";
	}
	
	@Override
	protected ArrayList<TwitterTask> getDataFromJsonString(String jsonResult) {
		ArrayList<TwitterTask> FIList = new ArrayList<TwitterTask>();
		try{
		JSONObject json= new JSONObject(jsonResult);

		JSONArray arr=json.getJSONArray("results");
		for (int i = 0; i < arr.length(); i++) {
			FIList.add(new TwitterTask(arr.getJSONObject(i)));
		}}		catch (Exception e) {
			e.printStackTrace();
		}
		
		return FIList;
	}
	public String getDefaultHashTag() throws IOException{
		String line;
		 String everything="";
		BufferedReader br = new BufferedReader(new FileReader("/data/data/il.ac.huji.todolist/files/dat.dat"));
	    try {
	        StringBuilder sb = new StringBuilder();
	         line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            line = br.readLine();
	        }
	         everything = sb.toString();
	    } finally {
	        br.close();
	    }
		return everything;
	}
	public void setDefaultHashTag(String tagname) throws IOException{
		File file = new File("/data/data/il.ac.huji.todolist/files/dat.dat");
		FileOutputStream fos = new FileOutputStream(file);
		fos.write((tagname+"\n").getBytes());
		fos.flush();
		fos.close();
	}
	
	

}
