package il.ac.huji.todolist;



import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class TodoListManagerActivity extends Activity {

	private ArrayAdapter<Task> adapter;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list_manager);
        List<Task> tasks = new ArrayList<Task>();
        tasks.add(new Task(
         "Plan your day!"));
        ListView listCourses =
         (ListView)findViewById(R.id.lstTodoItems);
        adapter = new TaskDisplayAdapter(this, tasks);
        listCourses.setAdapter(adapter);
        
    }
           

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.todo_list_manager, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	String taskName= ((EditText) findViewById(R.id.edtNewItem)).getText().toString();
    	switch (item.getItemId()) {
    		case R.id.menuItemAdd:
    			adapter.add(new Task(taskName));
    			break;
    		case R.id.menuItemDelete:
    			 ListView taskList = (ListView)findViewById(R.id.lstTodoItems);
    		     Task selected=(Task) taskList.getItemAtPosition(taskList.getSelectedItemPosition());
    		     adapter.remove(selected);
    			break;
    	}
     return true;
    }
    
}
