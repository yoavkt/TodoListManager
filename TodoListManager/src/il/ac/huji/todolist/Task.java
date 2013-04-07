package il.ac.huji.todolist;

import java.util.Date;

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
	public String getTitle() {
		return _taskTxt;
	}
	public Date getDueDate() {
		return _date;
	}
	public void set_taskTxt(String _taskTxt) {
		this._taskTxt = _taskTxt;
	}
	public void set_date(Date _date) {
		this._date = _date;
	}
}
