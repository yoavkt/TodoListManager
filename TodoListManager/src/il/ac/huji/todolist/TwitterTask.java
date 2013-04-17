package il.ac.huji.todolist;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;


public class TwitterTask extends Task {
	String _twitterID=null;
	public TwitterTask(JSONObject jsonObject) throws JSONException {
		super(jsonObject);
		_twitterID=jsonObject.getString("id");
	}
	public TwitterTask(String taskTxt, Date date, String twitterID) {
		super(taskTxt, date);
		_twitterID=twitterID;
	}
	public Task toTask(){
		return new Task(_taskTxt,_date);
	}
	public String get_twitterID() {
		return _twitterID;
	}
}
