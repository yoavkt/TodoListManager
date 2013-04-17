package il.ac.huji.todolist;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TwitterHandler extends JSONHandler<TwitterTask> {
	final String searchTweeterAPI="http://search.twitter.com/search.json?q=%23";
	//http://search.twitter.com/search.json?q=%23todoapp
	
	@Override
	protected String getSearchString(String s) {
		return searchTweeterAPI+s;
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
	protected boolean checkIfHasNew(String hashTag){
		
		return false;
	}
	

}
