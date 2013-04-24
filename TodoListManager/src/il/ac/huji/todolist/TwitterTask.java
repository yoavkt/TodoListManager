package il.ac.huji.todolist;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Extends the norml class and contain the twitter id field
 * @author yoavk_000
 *
 */
public class TwitterTask extends Task {
	
	String _twitterID=null;
	
	/**
	 * Creats a obect from a JSON object
	 * @param jsonObject 
	 * @throws JSONException
	 */
	public TwitterTask(JSONObject jsonObject) throws JSONException {
		super(jsonObject);
		_twitterID=jsonObject.getString("id");
	}
	public TwitterTask(String taskTxt, Date date, String twitterID) {
		super(taskTxt, date);
		_twitterID=twitterID;
	}
	/**
	 * Converts this task to a normal taks
	 * @return
	 */
	public Task toTask(){
		return new Task(_taskTxt,_date);
	}
	public String get_twitterID() {
		return _twitterID;
	}
}
