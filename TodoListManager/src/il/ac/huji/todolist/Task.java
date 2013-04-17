package il.ac.huji.todolist;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.parse.ParseObject;

import android.database.Cursor;



public class Task implements ITodoItem {
	public String _taskTxt;
	public Date _date;
	
	
	public Task(String taskTxt, Date date) {
		_taskTxt=taskTxt;
		_date=date;
	}
	public Task(String taskTxt){
		_taskTxt=taskTxt;
		_date=null;
	}
	public Task(Cursor  c){
		_taskTxt=c.getString(1);
		_date=new Date(c.getLong(2));
	}
	public Task(ParseObject i, String titleField, String dateField) {
		_taskTxt=i.getString(titleField);
		_date=new Date(i.getInt(dateField));
	}
	public Task(ParseObject i, String titleField) {
		_taskTxt=i.getString(titleField);
		_date=null;
	}
	public Task(JSONObject jsonObject) throws JSONException {
		_taskTxt=jsonObject.getString("text");
		_date=null;
	}
	public String getTitle() {
		return _taskTxt;
	}
	public Date getDueDate() {
		
		return _date;
	}
	public boolean isNullDate(){
		return (_date.equals(null));
	}
	public void set_taskTxt(String _taskTxt) {
		this._taskTxt = _taskTxt;
	}
	public void set_date(Date _date) {
		this._date = _date;
	}
	
}
