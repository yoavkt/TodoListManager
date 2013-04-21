package il.ac.huji.todolist;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;

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
	private TwitterHandler th=new TwitterHandler();
	private ArrayList<TwitterTask> twittesToAddToTask;
	private  TodoDAL todo;
	private Cursor taskListCursor;
	private Task myTask;
	private boolean Testing=false;
	private SQLiteDatabase myDB;
	TwitterTaskDownloader twitterLoader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_todo_list_manager);
    	ListView listViewTasks = (ListView)findViewById(R.id.lstTodoItems);
    	registerForContextMenu(listViewTasks);
    	todo=new TodoDAL(this);
    	DBHelper helper = new DBHelper(this);
    	myDB = helper.getWritableDatabase();
    	twittesToAddToTask=new ArrayList<TwitterTask>();
    	String[] from = { TITLE, DUEDATE, PICTURE};
    	int[] to = { R.id.txtTodoTitle, R.id.txtTodoDueDate, R.id.imageViewTodoThmb};
    	taskListCursor = myDB.query(TODO,new String[] {ID_HEADER, TITLE, DUEDATE ,PICTURE},null, null, null, null, null);
    	
    	adapter = new TaskDisplayAdapter(this, taskListCursor, from, to);
    	listViewTasks.setAdapter(adapter);
    	twitterLoader=new TwitterTaskDownloader(TodoListManagerActivity.this );
       
    	
    	
    	try {
			twitterLoader.execute(th.getDefaultHashTag());
		} catch (IOException e) {
			
		}
    	 if (Testing) toTest();
        
    }
	public void toTest(){
		String s="";
		try {
			th.setDefaultHashTag("yoavtodoapp");
			 s=th.getDefaultHashTag();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		s=s+"dfg";
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
        			intent = new Intent(this, ChangeHashTagActivity.class);
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
				Intent intent = new Intent(this, AddFlickrTHMActivity.class);
   		     	startActivityForResult(intent,1987);
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
		if (requestCode == 1987 && resultCode == RESULT_OK) {
			todo.updatePicture(data.getStringExtra(PICTURE),data.getStringExtra("thumbnailId"),myTask);	
          	taskListCursor.requery();
		}
		if (requestCode == 1988 && resultCode == RESULT_OK) {
			(new TwitterTaskDownloader(TodoListManagerActivity.this)).execute(data.getStringExtra("hashTag"));
          	taskListCursor.requery();
		}
		if (requestCode == 1989 && resultCode == RESULT_OK) {
			todo.insert(twittesToAddToTask);
			taskListCursor.requery();
			
		}
    }
	protected class TaskDisplayAdapter extends SimpleCursorAdapter {

		private Context _context;
		private final String DUE_DATE_STRING = "No due date";
		private final String STRING_FORMAT = "dd/MM/yyyy";
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
			if (!cursor.isNull(3)){ 
				File imgFile = new  File("/data/data/il.ac.huji.todolist/files/"+cursor.getString(3)+".png");
				Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
				imgV.setImageBitmap((myBitmap));
			}
			return view;
		}
	}
	protected class TwitterTaskDownloader extends AsyncTask<String, Integer, Integer>{
		
		private ProgressDialog _progressDialog;
		private Context _todoListManagerActivity;
		
		public TwitterTaskDownloader(Context todoListManagerActivity) {
			_progressDialog = new ProgressDialog(todoListManagerActivity);
			_progressDialog.setTitle("Searching Twitter");
			_progressDialog.setCancelable(false);
			_todoListManagerActivity=todoListManagerActivity;
		}
		public void onPreExecute() {
			_progressDialog.setMessage("Connecting to Twitter");
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
			if(twittesToAddToTask.size()>0){
			Intent intent = new Intent(_todoListManagerActivity, AddTaskFromTwitterYesNo.class);
			intent.putExtra("NumOfTasks", twittesToAddToTask.size());
		    startActivityForResult(intent,1989);}
		}
		protected void onProgressUpdate(Integer... progress) {
			_progressDialog.setMessage("found " +Integer.toString(progress[0])+" new tasks so far" );
			super.onProgressUpdate(progress);
		}
	}
	}
