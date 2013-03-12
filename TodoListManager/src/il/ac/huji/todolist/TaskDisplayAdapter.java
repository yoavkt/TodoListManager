package il.ac.huji.todolist;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TaskDisplayAdapter extends ArrayAdapter<Task> {

	public TaskDisplayAdapter(TodoListManagerActivity activity, List<Task> tasks) {
			super(activity, android.R.layout.simple_list_item_1, tasks);
	}

	
@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	Task task = getItem(position);
	LayoutInflater inflater = LayoutInflater.from(getContext());
	View view = inflater.inflate(R.layout.row, null);
	TextView txtColor;
	if (position % 2 == 0 ){
		txtColor =(TextView)view.findViewById(R.id.blue);
	}
	else {
		txtColor =(TextView)view.findViewById(R.id.red);
	}

	txtColor.setText(task.get_taskTxt());
	return view;
	}
}
