package il.ac.huji.todolist;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
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
	private static final String PICTURE="thumbnail";
	
	private final String CALL_REGEX= "^Call .*";
	private final String CALL_STRING= "Call ";
	private final String TELE_STRING= "tel:";
	private final String TODO="todo";

	TaskDisplayAdapter adapter;
	private  TodoDAL todo;
	private Cursor taskListCursor;
	private Task myTask;
	private boolean Testing=true;
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
    	String[] from = { TITLE, DUEDATE, PICTURE};
    	int[] to = { R.id.txtTodoTitle, R.id.txtTodoDueDate, R.id.imageViewTodoThmb};
    	taskListCursor = myDB.query(TODO,new String[] {ID_HEADER, TITLE, DUEDATE ,PICTURE},null, null, null, null, null);

    	adapter = new TaskDisplayAdapter(this, taskListCursor, from, to);
    	listViewTasks.setAdapter(adapter);
    	populateFromTweets();
    	todo=new TodoDAL(this);
        if (Testing){
        	try {
				toTest();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
        }

    }
    private void populateFromTweets() {
		// TODO Auto-generated method stub
		
	}
	public void toTest() throws IOException, JSONException{
    	TwitterHandler th=new TwitterHandler();
    	Boolean c;
    	ArrayList<TwitterTask> t=th.getArrayListFromWebService("todolist");
    	String s1=t.get(0).getTitle();
    	String s2=t.get(1).getTitle();
    	int num=t.size();
    	s2=s2+"";
    	TwitterTask tw;
    	TodoDAL td=new TodoDAL(this);
    	boolean insertFalg=td.insertIdTable(tw=new TwitterTask("Hello",new Date(),"11"));
    	boolean existFlag=td.seenThisTwiterID(tw);
    	c=existFlag&insertFalg;	
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
    	Intent intent;
    	switch (item.getItemId()) {
    		case R.id.menuItemAdd:
    			 intent = new Intent(this, AddNewTodoItemActivity.class);
    		    startActivityForResult(intent,1986);
    			break;
    		case R.id.menuItemAddFromTweet:
        			intent = new Intent(this, TwittHashTagActivity.class);
        		    startActivityForResult(intent,1988);
        			break;
    	}
     return true;
    }
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
	
		Cursor taskCursor = (Cursor)adapter.getItem(info.position);
		myTask= new Task(taskCursor);
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
			case R.id.menuItemThumbnail:
				taskCursor.moveToPosition(info.position);
				Intent intent = new Intent(this, AddThmblnlActivity.class);
   		     	startActivityForResult(intent,1987);
			break;	
		}
		return true;
	}
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (requestCode == 1986 && resultCode == RESULT_OK) {
			Date d=(Date) data.getSerializableExtra(DUEDATE);
			todo.insert(new Task(data.getStringExtra(TITLE),(Date) data.getSerializableExtra(DUEDATE)));
    		taskListCursor.requery();
		}
		if (requestCode == 1987 && resultCode == RESULT_OK) {
			FlickrHandler fh=new FlickrHandler();
			todo.updatePicture(data.getStringExtra(PICTURE),data.getStringExtra("thumbnailId"),myTask);	
          	taskListCursor.requery();
		}
		if (requestCode == 1988 && resultCode == RESULT_OK) {
			populateFromTweets();
		}
    }
	
}
