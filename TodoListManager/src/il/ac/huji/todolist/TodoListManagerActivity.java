package il.ac.huji.todolist;



import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class TodoListManagerActivity extends Activity {
private final String CALL_REGEX= "^call .*";
	private ArrayAdapter<Task> adapter;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list_manager);
        List<Task> tasks = new ArrayList<Task>();
        tasks.add(new Task("Plan your day!", new Date()));
        ListView listTasksView = (ListView)findViewById(R.id.lstTodoItems);
        adapter = new TaskDisplayAdapter(this, tasks);
        listTasksView.setAdapter(adapter);
        registerForContextMenu(listTasksView);
        
    }
           
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
			getMenuInflater().inflate(R.menu.add_new_todo_item, menu);
			
			AdapterContextMenuInfo conMenut = (AdapterContextMenuInfo)menuInfo;
			int selectionIndex =conMenut.position;
			Task task=adapter.getItem(selectionIndex);
			menu.setHeaderTitle(task.get_taskTxt());
			if(task.get_taskTxt().matches(CALL_REGEX)){
					menu.add(0,R.id.menuItemCall, 0, task.get_taskTxt());
			}
}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.todo_list_manager, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	switch (item.getItemId()) {
    		case R.id.menuItemAdd:
    			Intent intent = new Intent(this, AddNewTodoItemActivity.class);
    		     startActivityForResult(intent,1986);
    			break;
    	}
     return true;
    }
    
}
