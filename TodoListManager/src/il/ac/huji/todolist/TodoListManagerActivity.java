package il.ac.huji.todolist;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.PushService;

public class TodoListManagerActivity extends Activity {
	private static final String TITLE = "title";
	private static final String DUEDATE = "due";
	private static final String ID_HEADER="_id";
	private final String CALL_REGEX= "^Call .*";
	private final String CALL_STRING= "Call ";
	private final String TELE_STRING= "tel:";
	private final String TODO="todo";
	TaskDisplayAdapter adapter;
	private  TodoDAL todo;
	private Cursor taskListCursor;
	private boolean Testing=false;
	//private ArrayAdapter<Task> adapter;
    
	private SQLiteDatabase myDB;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_todo_list_manager);
    	ListView listViewTasks = (ListView)findViewById(R.id.lstTodoItems);
    	registerForContextMenu(listViewTasks);
    	DBHelper helper = new DBHelper(this);
    	myDB = helper.getWritableDatabase();
    	taskListCursor = myDB.query(TODO,	new String[] {ID_HEADER, TITLE, DUEDATE }, null, null, null, null, null);
    	String[] from = { TITLE, DUEDATE };
    	int[] to = { R.id.txtTodoTitle, R.id.txtTodoDueDate };
    	adapter = new TaskDisplayAdapter(this,
    			taskListCursor, from, to);
    	listViewTasks.setAdapter(adapter);

    	todo=new TodoDAL(this);
        if (Testing){
        	Date d= new Date();
        	d.setDate(5);
        	//todo.insert(new Task(d.toLocaleString(),d));
        	todo.delete(new Task("Hello", new Date(-1)));
        	todo.delete(new Task("Hello4"));
        	todo.delete(new Task("Hello2"));
        	taskListCursor.requery(); 
        }

    }
 
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
			
		getMenuInflater().inflate(R.menu.add_new_todo_item, menu);	
		AdapterContextMenuInfo conMenut = (AdapterContextMenuInfo)menuInfo;
			int selectionIndex =conMenut.position;
			Cursor taskCursor = (Cursor)adapter.getItem(selectionIndex);
			menu.setHeaderTitle(taskCursor.getString(1));
			if(taskCursor.getString(1).matches(CALL_REGEX)){
					menu.add(0,R.id.menuItemCall, 0, taskCursor.getString(1));
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
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
	
		Cursor taskCursor = (Cursor)adapter.getItem(info.position);
		Task myTask= new Task(taskCursor);
		switch (item.getItemId()){
			case R.id.menuItemCall:
				Intent dial = new Intent(Intent.ACTION_DIAL,Uri.parse(myTask.getTitle().replace(CALL_STRING, TELE_STRING)));
				startActivity(dial);
			break;
			case R.id.menuItemDelete:
				taskCursor.moveToPosition(info.position);
				todo.delete(myTask);
				taskListCursor.requery();
			break;
		}
		return true;
	}
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     if (requestCode == 1986 && resultCode == RESULT_OK) {
    	todo.insert(new Task(data.getStringExtra(TITLE),(Date) data.getSerializableExtra(DUEDATE)));
    	taskListCursor.requery();
     }
    }
	
}
