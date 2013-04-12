package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class TodoDAL {
	Context _context;
	private SQLiteDatabase db;
	boolean delete = true;
	protected boolean update;
	private final String CLASS_NAME = "todo";
	private final String TITLE = "title";
	private final String DUEDATE = "due";
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
}
