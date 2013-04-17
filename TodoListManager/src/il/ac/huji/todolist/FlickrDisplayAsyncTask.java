package il.ac.huji.todolist;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.GridView;

public class FlickrDisplayAsyncTask extends AsyncTask<String, Void, Void> {

	private Context _context;
	private GridView _gridView;
	public FlickrDisplayAsyncTask(Context context, GridView gridView)
	{
		_context=context;
		_gridView=gridView;
	}
	
	@Override
	protected Void doInBackground(String... arg0) {
		FlickrHandler fh=new FlickrHandler();
		ArrayList<FlickrImage> flickrARR= new ArrayList<FlickrImage>();
		try {
			flickrARR=fh.getImageArrayListFromFlickr(arg0[0]);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		FlickrDisplayAdapter flickrDA = new FlickrDisplayAdapter(_context, flickrARR);
		_gridView.setAdapter(flickrDA);
		return null;
	}
	

}
