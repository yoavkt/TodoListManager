package il.ac.huji.todolist;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

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

	public TodoDAL(Context context) {
		_context = context;

		Parse.initialize(_context, "NaEEoOzHnxvom1sy64RTMQwzmMXlHpL5XRvvvCoa", "pPR7u3HxoPX3ZOYCHR0Aps2a9Q35NZcnsBuqvVsF");

		DBHelper helper = new DBHelper(_context);
		db = helper.getWritableDatabase();
		ParseUser.enableAutomaticUser();
	}

	public boolean insert(ITodoItem todoItem) {
		ParseObject parseObject = new ParseObject("todo");
		parseObject.put("title", todoItem.getTitle());
		parseObject.put("due", todoItem.getDueDate().getTime());
		parseObject.saveInBackground();
		ContentValues values = new ContentValues();
		values.put("title", todoItem.getTitle());
		if (!(todoItem.getDueDate().equals(null))) {
			values.put("due", todoItem.getDueDate().getTime());
		} else {
			values.put("due", -1);
		}
		long succ = db.insert("todo", null, values);
		if (succ == -1) {
			return false;
		}
		return true;

	}

	public boolean update(ITodoItem todoItem) {
		return true;
	}

	public boolean delete(ITodoItem todoItem) {
	return true;		
	}

	public List<ITodoItem> all() {
		ParseQuery query = new ParseQuery("todo");
		List<ITodoItem> list = new ArrayList<ITodoItem>();

		try {
			for (ParseObject i : query.find()) {
				list.add(new Task(i.getString("title"), new Date(i.getInt("due"))));

			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}
}
