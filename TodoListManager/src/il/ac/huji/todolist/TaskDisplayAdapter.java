package il.ac.huji.todolist;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.SimpleFormatter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TaskDisplayAdapter extends ArrayAdapter<Task> {

	private final String DUE_DATE_STRING="No due date";
	private final String STRING_FORMAT="dd/MM/yyyy";
	
	public TaskDisplayAdapter(TodoListManagerActivity activity, List<Task> tasks) {
			super(activity, android.R.layout.simple_list_item_1, tasks);
	}

	
@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	Task task = getItem(position);
	LayoutInflater inflater = LayoutInflater.from(getContext());
	View view = inflater.inflate(R.layout.row, null);
	TextView txtName = (TextView)view.findViewById(R.id.txtTodoTitle);
	txtName.setText(task.get_taskTxt());
	TextView txtDate = (TextView)view.findViewById(R.id.txtTodoDueDate);
	
	if (task.get_date()!=null){	
		txtDate.setText((new SimpleDateFormat(STRING_FORMAT)).format(task.get_date()));
		if (task.get_date().before(new Date())){
				txtDate.setTextColor(Color.RED);
				txtName.setTextColor(Color.RED);
			}
	}
	else
		txtDate.setText(DUE_DATE_STRING);
	
	return view;
	}
}
