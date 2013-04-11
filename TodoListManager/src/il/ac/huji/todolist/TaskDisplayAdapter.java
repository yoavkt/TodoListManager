package il.ac.huji.todolist;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

public class TaskDisplayAdapter extends SimpleCursorAdapter {
	
	private Context _context;
	private final String DUE_DATE_STRING="No due date";
	private final String STRING_FORMAT="dd/MM/yyyy";
	
	/*public TaskDisplayAdapter(TodoListManagerActivity activity, List<Task> tasks) {
			super(activity, android.R.layout.simple_list_item_1, tasks);
	}*/

	
public TaskDisplayAdapter(Context context,
		Cursor cursor, String[] from, int[] to) {
			super(context,R.layout.row, cursor, from, to);

		_context=context;
	}


@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	
	Cursor cursor = (Cursor) getItem(position);
	cursor.moveToPosition(position);
	
	//Task task = getItem(position);
	LayoutInflater inflater = (LayoutInflater) _context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	View view = inflater.inflate(R.layout.row, null);
	
	TextView txtName = (TextView)view.findViewById(R.id.txtTodoTitle);
	//txtName.setText(task.get_taskTxt());
	txtName.setText(cursor.getString(1));
	//TextView txtDate = (TextView)view.findViewById(R.id.txtTodoDueDate);
	TextView txtDate = (TextView)view.findViewById(R.id.txtTodoDueDate);
	

	if (cursor.getLong(2)==-1)
		txtDate.setText(DUE_DATE_STRING);
	else
	{
		Date taskDate=new Date(cursor.getLong(2));
		txtDate.setText((new SimpleDateFormat(STRING_FORMAT)).format(taskDate));
		Date todayDate=new Date();
		if (taskDate.before(todayDate)){
			txtDate.setTextColor(Color.RED);
			txtName.setTextColor(Color.RED);
		}	
	}
	return view;
	}
}
