package il.ac.huji.todolist;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("SimpleDateFormat")
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
	// the twitter web sevice handler
	private TwitterHandler th=new TwitterHandler();
	// a list for the to add 
	private ArrayList<TwitterTask> twittesToAddToTask;
	private  TodoDAL todo;
	private Cursor taskListCursor;
	private Task myTask;

	private SQLiteDatabase myDB;
	TwitterTaskDownloader twitterLoader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_todo_list_manager);
    	ListView listViewTasks = (ListView)findViewById(R.id.lstTodoItems);
    	registerForContextMenu(listViewTasks);
    	//organizing the data bases
    	todo=new TodoDAL(this);
    	DBHelper helper = new DBHelper(this);
    	myDB = helper.getWritableDatabase();
    	twittesToAddToTask=new ArrayList<TwitterTask>();
    	
    	String[] from = { TITLE, DUEDATE, PICTURE};
    	int[] to = { R.id.txtTodoTitle, R.id.txtTodoDueDate, R.id.imageViewTodoThmb};
    	taskListCursor = myDB.query(TODO,new String[] {ID_HEADER, TITLE, DUEDATE ,PICTURE},null, null, null, null, null);
    	//diaplying tasks
    	adapter = new TaskDisplayAdapter(this, taskListCursor, from, to);
    	listViewTasks.setAdapter(adapter);
    	// setting the from twitter thread
    	twitterLoader=new TwitterTaskDownloader(TodoListManagerActivity.this );
    	try {
    		//if we have a default hsh tag will use it if not we will
    		//create a file with a default todoapp tag
    		if (th.defaultTagFileExists())
    			twitterLoader.execute(th.getDefaultHashTag());
    		else{
    			th.setDefaultHashTag(TodoListManagerConstants.DEFAULT_HASH_TAG);
    			//starting the thread
    			twitterLoader.execute(TodoListManagerConstants.DEFAULT_HASH_TAG);
    		}
		} catch (IOException e) {
			// we shouldn't get here only if we have file system problem
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
    	Intent intent;
    	switch (item.getItemId()) {
    		case R.id.menuItemAdd:
    			 intent = new Intent(this, AddNewTodoItemActivity.class);
    		    startActivityForResult(intent,TodoListManagerConstants.ADD_NEW_TASK);
    			break;
    		case R.id.menuItemAddFromTweet:
        			intent = new Intent(this, ChangeHashTagActivity.class);
        		    startActivityForResult(intent,TodoListManagerConstants.HASH_TAG_CHANGED);
        			break;
    	}
     return true;
    }
	@SuppressWarnings("deprecation")
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
				Intent intent = new Intent(this, AddFlickrTHMActivity.class);
   		     	startActivityForResult(intent,TodoListManagerConstants.ADD_NEW_PIC);
			break;	
		}
		return true;
	}
	@SuppressWarnings("deprecation")
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (requestCode == TodoListManagerConstants.ADD_NEW_TASK && resultCode == RESULT_OK) {
			todo.insert(new Task(data.getStringExtra(TITLE),(Date) data.getSerializableExtra(DUEDATE)));
    		taskListCursor.requery();
		}
		if (requestCode == TodoListManagerConstants.ADD_NEW_PIC && resultCode == RESULT_OK) {
			todo.updatePicture(data.getStringExtra(PICTURE),
					data.getStringExtra(TodoListManagerConstants.THUMBNAIL_ACTIVITY_EXTRA_ID),myTask);	
          	taskListCursor.requery();
		}
		if (requestCode == TodoListManagerConstants.HASH_TAG_CHANGED && resultCode == RESULT_OK) {
			(new TwitterTaskDownloader(TodoListManagerActivity.this)).execute(
										data.getStringExtra(TodoListManagerConstants.CHANGE_HSHTAG_EXTRA));
          	taskListCursor.requery();
		}
		if (requestCode == TodoListManagerConstants.ADD_NEW_TASK_FROM_TWITTER && resultCode == RESULT_OK) {
			todo.insert(twittesToAddToTask);
			taskListCursor.requery();
			
		}
		if (requestCode == TodoListManagerConstants.ADD_NEW_TASK_FROM_TWITTER && resultCode == RESULT_CANCELED) {
			twittesToAddToTask=new ArrayList<TwitterTask>();
		}
    }
	protected class TaskDisplayAdapter extends SimpleCursorAdapter {

		private Context _context;
		private final String DUE_DATE_STRING = "No due date";
		private final String STRING_FORMAT = "dd/MM/yyyy";
		@SuppressWarnings("deprecation")
		public TaskDisplayAdapter(Context context, Cursor cursor, String[] from,
				int[] to) {
			super(context, R.layout.row, cursor, from, to);

			_context = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			Cursor cursor = (Cursor) getItem(position);
			cursor.moveToPosition(position);
			LayoutInflater inflater = (LayoutInflater) _context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.row, null);

			TextView txtName = (TextView) view.findViewById(R.id.txtTodoTitle);
			ImageView imgV= (ImageView) view.findViewById(R.id.imageViewTodoThmb);
			TextView txtDate = (TextView) view.findViewById(R.id.txtTodoDueDate);
			//the date handling part, if null write no date if not check if exceeding
			//color accordingly
			if (cursor.isNull(2))
				txtDate.setText(DUE_DATE_STRING);
			else {
				Date taskDate = new Date(cursor.getLong(2));
				txtDate.setText((new SimpleDateFormat(STRING_FORMAT))
						.format(taskDate));
				Date todayDate = new Date();
				if (taskDate.before(todayDate)) {
					txtDate.setTextColor(Color.RED);
					txtName.setTextColor(Color.RED);
				}
			}
			txtName.setText(cursor.getString(1));
			//Handle the task thumnail
			if (!cursor.isNull(3)){ 
				File imgFile = new  File(TodoListManagerConstants.THUMBNAIL_FILE_DIR
											+"/"+cursor.getString(3)+
											TodoListManagerConstants.IMGFILETYPE);
				Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
				imgV.setImageBitmap((myBitmap));
			}
			return view;
		}
	}
	/**
	 * This class is a asynctask that can serach and download tasks form twiiter
	 * @author yoavk_000
	 *
	 */
	protected class TwitterTaskDownloader extends AsyncTask<String, Integer, Integer>{
		
		private ProgressDialog _progressDialog;
		private Context _todoListManagerActivity;
		
		
		public TwitterTaskDownloader(Context todoListManagerActivity) {
			_progressDialog = new ProgressDialog(todoListManagerActivity);
			_progressDialog.setTitle("Twitter");
			_progressDialog.setCancelable(false);
			_todoListManagerActivity=todoListManagerActivity;
		}
		public void onPreExecute() {
			_progressDialog.setMessage("Connecting");
			_progressDialog.show();
			super.onPreExecute();
		}
		@Override
		protected Integer doInBackground(String... params) {
			ArrayList<TwitterTask> fromTwiter=new ArrayList<TwitterTask>();
			try {
				 fromTwiter=th.getArrayListFromWebService(params[0]);
			} catch (Exception e) {
				e.printStackTrace();
			} 
			
			int i;
			//keeping the new tw
			for ( i = 0; i < fromTwiter.size(); i++) {
				if(!todo.seenThisTwiterID(fromTwiter.get(i))){
					twittesToAddToTask.add(fromTwiter.get(i));
					todo.insertIdTable(fromTwiter.get(i));
					publishProgress(i);
			}
			}
			return i;
		}
		@Override
		protected void onPostExecute(Integer result) {
			_progressDialog.dismiss();
			super.onPostExecute(result);
			//once done a an activity will appear either informing the user that there
			//are no tasks or will ask him if he want to add the tasks.
			Intent intent = new Intent(_todoListManagerActivity, AddTaskFromTwitterYesNo.class);
			intent.putExtra(TodoListManagerConstants.TASK_FROM_TWITTER_EXTRA_NUM_OF_TASKS, 
						twittesToAddToTask.size());
		    startActivityForResult(intent,TodoListManagerConstants.ADD_NEW_TASK_FROM_TWITTER);
		    }
		
		protected void onProgressUpdate(Integer... progress) {
			_progressDialog.setMessage("found " +Integer.toString(progress[0])+" new tasks..." );
			super.onProgressUpdate(progress);
		}
	}
	}
