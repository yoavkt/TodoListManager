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
import android.widget.ListView;

public class TodoListManagerActivity extends Activity {

	private ArrayAdapter<Task> adapter;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list_manager);
        List<Task> tasks = new ArrayList<Task>();
        tasks.add(new Task(
         "Introduction to Computer Science"));
        tasks.add(new Task(
         "Object Oriented Programming"));
        tasks.add(new Task(
         "Human Centric Mobile Computing"));
        ListView listCourses =
         (ListView)findViewById(R.id.listCourses);
        adapter = new TaskDisplayAdapter(this, tasks);
        listCourses.setAdapter(adapter);
        findViewById(R.id.buttonAdd).setOnClickListener( new OnClickListener() {
        		@Override
        		public void onClick(View v) {
        				adapter.add(new Task("iPhone Application Development"));
        		}
            });
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
    		case R.id.itemAdd:
    			adapter.add(new Task("First Aid"));
    			break;
    	}
     return true;
    }
    
}
