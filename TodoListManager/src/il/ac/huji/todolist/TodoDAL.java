package il.ac.huji.todolist;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Environment;

public class TodoDAL {
	Context _context;
	private SQLiteDatabase db;
	boolean delete = true;
	protected boolean update;
	private final String CLASS_NAME = "todo";
	private final String TITLE = "title";
	private final String DUEDATE = "due";
	private final String THUMB= "thumbnail";
	private boolean ParseUpdateFlag;
	private boolean ParseDeleteFlag;

	public TodoDAL(Context context) {
		_context = context;

		Parse.initialize(_context, "NaEEoOzHnxvom1sy64RTMQwzmMXlHpL5XRvvvCoa",
				"pPR7u3HxoPX3ZOYCHR0Aps2a9Q35NZcnsBuqvVsF");

		DBHelper helper = new DBHelper(_context);
		db = helper.getWritableDatabase();
		ParseUser.enableAutomaticUser();
	}

	public boolean insert(ITodoItem todoItem) {
		return (insertParse(todoItem) && insertLocalDB(todoItem));
	}

	private boolean insertParse(ITodoItem todoItem) {
		ParseObject parseObject = new ParseObject(CLASS_NAME);
		parseObject.put(TITLE, todoItem.getTitle());
		if (todoItem.getDueDate() != null)
			parseObject.put(DUEDATE, todoItem.getDueDate().getTime());
		else
			parseObject.put(DUEDATE, JSONObject.NULL);
		parseObject.saveInBackground();
		return true;
	}

	private boolean insertLocalDB(ITodoItem todoItem) {
		ContentValues values = new ContentValues();
		values.put(TITLE, todoItem.getTitle());
		if (todoItem.getDueDate() != null)
			values.put(DUEDATE, todoItem.getDueDate().getTime());
		else
			values.putNull(DUEDATE);
		return (!(db.insert(CLASS_NAME, null, values) == -1));
	}

	public boolean update(ITodoItem todoItem) {
		boolean res=updateLocalDB(todoItem);
		res=res&updateParse(todoItem);
		return update;

	}
	private boolean updateLocalDB(ITodoItem todoItem) {
		ContentValues CV = new ContentValues();
		if (todoItem.getDueDate() != null)
			CV.put(DUEDATE, todoItem.getDueDate().getTime());
		else
			CV.putNull(DUEDATE);
		return (db.update(CLASS_NAME, CV, "title=?",
				new String[] { todoItem.getTitle() }) < 1);
	}
	public boolean insert(TwitterTask t){
		return insert(t.toTask())&&insertIdTable(t);
	}
	public boolean insertIdTable(TwitterTask t) {
		ContentValues values = new ContentValues();
		values.put("twitid", t.get_twitterID());
		return (!(db.insert("twitterId", null, values) == -1));
	}
	

	public boolean seenThisTwiterID(TwitterTask t){
    
		Cursor c = db.query("twitterId",new String[] {"twitid"},null, null, null, null, null);
		if(c.moveToFirst())
			do {
				String id=c.getString(0);
				if (id.matches(t.get_twitterID())){
					c.close();
					return true;
					}
			} while(c.moveToNext());
		c.close();
		return false;
		
	}
	private boolean updateParse(ITodoItem todoItem) {
		ParseUpdateFlag = false;

		final ITodoItem tempTodo = todoItem;
		if (todoItem.getTitle() == null)
			return ParseUpdateFlag;
		ParseQuery pq = new ParseQuery(CLASS_NAME);
		pq.whereEqualTo(TITLE, todoItem.getTitle());
		pq.findInBackground(new FindCallback() {
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				
				if (objects.isEmpty())
					return;
				for (ParseObject po : objects) {
					if (tempTodo.getDueDate() != null) {
						po.put(DUEDATE, tempTodo.getDueDate().getTime());
						po.saveInBackground();
					} else {
						po.put(DUEDATE, JSONObject.NULL);
						po.saveInBackground();
					}
					ParseUpdateFlag = true;
				}

			}
		});
		return ParseUpdateFlag;
	}

	public boolean delete(ITodoItem todoItem) {
		return (deleteParse(todoItem) && deleteLocalDB(todoItem));
	}

	public boolean deleteParse(ITodoItem todoItem) {
		if (todoItem.getTitle() == null)
			return false;
		ParseQuery query = new ParseQuery(CLASS_NAME);
		query.whereEqualTo(TITLE, todoItem.getTitle());
		ParseDeleteFlag = true;
		query.findInBackground(new FindCallback() {

			@Override
			public void done(List<ParseObject> objects, ParseException arg1) {
				if (arg1 != null) {
					arg1.printStackTrace();
				} else {
					if (objects.isEmpty()) {
						ParseDeleteFlag = false;
						return;
					}
					for (ParseObject obj : objects) {
						try {
							obj.delete();
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
		return ParseDeleteFlag;

	}

	public boolean deleteLocalDB(ITodoItem todoItem) {
		return (db.delete("todo", "title=?",
				new String[] { todoItem.getTitle() }) < 1);
	}

	public List<ITodoItem> all() {
		ParseQuery query = new ParseQuery(CLASS_NAME);
		List<ITodoItem> list = new ArrayList<ITodoItem>();
		try {
			for (ParseObject i : query.find())
				if (i.getJSONObject(DUEDATE).equals(JSONObject.NULL))
					list.add(new Task(i, TITLE));
				else
					list.add(new Task(i, TITLE, DUEDATE));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return list;
	}
	public boolean updatePictureParse(Bitmap bm,ITodoItem myTask) throws ParseException{
		ParseUpdateFlag = false;
		final ITodoItem tempTodo = myTask;
		final Bitmap Fbm=bm;
		if (tempTodo.getTitle() == null)
			return ParseUpdateFlag;
		ParseQuery pq = new ParseQuery(CLASS_NAME);
		pq.whereEqualTo(TITLE, tempTodo.getTitle());

		pq.findInBackground(new FindCallback() {
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				
				if (objects.isEmpty())
					return;
				for (ParseObject po : objects) {
						ByteArrayOutputStream stream = new ByteArrayOutputStream();
			
						byte[] byteArray = stream.toByteArray();
						ParseFile file = new ParseFile(byteArray);
						try {
							file.save();
						} catch (ParseException e1) {
							e1.printStackTrace();
						} 
						po.put(THUMB, file);
						po.saveInBackground();
				
					ParseUpdateFlag = true;
				}

			}
		});
		return ParseUpdateFlag;

	}
	public boolean updatePictureLocal(String thubID,ITodoItem myTask){
		ContentValues CV = new ContentValues();
		if (thubID != null)
			CV.put("thumbnail", thubID);
		else
			CV.putNull("thumbnail");
		Boolean flag= (db.update(CLASS_NAME, CV, "title=?",
				new String[] { myTask.getTitle() }) < 1);
		return flag;
	}
	public boolean saveImage(Bitmap bm, String thumbId) throws IOException {
			File direct = new File("/data/data/il.ac.huji.todolist/files");
			if(!direct.exists())
			        direct.mkdir(); 
			File file = new File("/data/data/il.ac.huji.todolist/files" , thumbId + ".png");
		    FileOutputStream fOut = new FileOutputStream(file);
		    bm.compress(Bitmap.CompressFormat.PNG, 100, fOut);
		    fOut.flush();
		    fOut.close();

		return true;
	}
	public boolean updatePicture(String url, String thumbId, ITodoItem myTask) {
		 	FlickrHandler fh=new FlickrHandler();
		 	Bitmap bm=fh.getImageViaURL(url);
		try {	
				updatePictureLocal(thumbId,myTask);
				 updatePictureParse(bm,myTask);
				 saveImage(bm,thumbId);
				 return updatePictureLocal(thumbId,myTask);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void insert(ArrayList<TwitterTask> twittesToAddToTask) {
		for (TwitterTask twitterTask : twittesToAddToTask) {
			this.insert(twitterTask.toTask());
		}
		
	}


}
