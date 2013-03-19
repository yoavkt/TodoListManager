package il.ac.huji.todolist;

import java.util.Date;



public class Task {
	public String _taskTxt;
	public Date _date;
	
	
	public Task(String taskTxt, Date date) {
		_taskTxt=taskTxt;
		_date=date;
	}
	public String get_taskTxt() {
		return _taskTxt;
	}
	public Date get_date() {
		return _date;
	}
}
