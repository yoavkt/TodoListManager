package il.ac.huji.todolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	public DBHelper(Context context) {
		super(context, "todo_db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table todo ( _id integer primary key autoincrement,"
				+ " title text, due long, thumbnail text );");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
		db.execSQL("ALTER TABLE todo ADD thumbnail text AFTER due;");
		
	}

}
