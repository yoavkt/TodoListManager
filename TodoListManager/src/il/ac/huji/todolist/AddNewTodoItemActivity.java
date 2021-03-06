package il.ac.huji.todolist;

import java.util.Date;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;

public class AddNewTodoItemActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_todo_item);
		findViewById(R.id.btnOK).setOnClickListener(new OnClickListener() {	
		
			public void onClick(View v) {
			
				String taskName = ((EditText)findViewById(R.id.edtNewItem)).getText().toString();
				DatePicker datePicker=(DatePicker)findViewById(R.id.datePicker);
				Date taskDate=new Date(datePicker.getYear() - 1900, datePicker.getMonth(), datePicker.getDayOfMonth());
				Intent resultIntent = new Intent();
				resultIntent.putExtra(TodoListManagerConstants.NEWTODO_ACTIVITY_EXTRA_TITLE, taskName);
				resultIntent.putExtra(TodoListManagerConstants.NEWTODO_ACTIVITY_EXTRA_DATE,taskDate );
				setResult(RESULT_OK, resultIntent);
				finish();
				
				}
				
		});
		
		findViewById(R.id.btnTwitCancel).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.add_new_todo_item, menu);
		return true;
	}
	
}
