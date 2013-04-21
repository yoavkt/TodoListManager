package il.ac.huji.todolist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;

import android.R.integer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class AddFlickrTHMActivity extends Activity {

	private Context thisForm;
	private String selePicURL;
	GridView gridview;
	protected ArrayList<FlickrImage> flickrARR = new ArrayList<FlickrImage>();
	ImageAdapter FIA;
	FlickrDisplayAdapter adapter;
	FlickrHandler fh=new FlickrHandler();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_thumbnail);
		thisForm = this;
		findViewById(R.id.btnTHBOK).setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				String searchString = ((EditText) findViewById(R.id.editTextThumbnailSearch))
						.getText().toString();
				if (searchString == "") {
					((EditText) findViewById(R.id.editTextThumbnailSearch))
							.setText("Enter a search String");
				} else {

					gridview = (GridView) findViewById(R.id.gridViewTHMB);
					FIA = new ImageAdapter(thisForm);
					gridview.setAdapter(FIA);
					final ImageDownloader flickrLoader=new  ImageDownloader(AddFlickrTHMActivity.this, searchString);
					flickrLoader.execute();
					gridview.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> parent, View v,
								int position, long id) {
							selePicURL = flickrARR.get(position)
									.getStaticLocation();
							Intent resultIntent = new Intent();
							resultIntent.putExtra("thumbnail", selePicURL);
							resultIntent.putExtra("thumbnailId",
									flickrARR.get(position).get_flickrID());
							resultIntent.putExtra("thumbnailPic", flickrARR
									.get(position).getImageAsBitMap());
							setResult(RESULT_OK, resultIntent);
							flickrLoader.cancel(false);
							finish();
						}
					});

				}
			}
		});

		findViewById(R.id.btnTHBCancel).setOnClickListener(
				new OnClickListener() {
					public void onClick(View v) {
						setResult(RESULT_CANCELED);
						finish();
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	
		getMenuInflater().inflate(R.menu.add_thumbnail, menu);
		return true;
	}

	private class ImageDownloader extends AsyncTask<Integer, Integer, String> {
	
		private ProgressDialog _progressDialog;
		private String _searchString;
		
		public ImageDownloader(Context con, String searchString) {
			_progressDialog=new ProgressDialog(con);
			_progressDialog.setTitle("Searching Flickr");
			_progressDialog.setMessage("Image will start to appear when \n I will finish searching");
			_progressDialog.setCancelable(false);
			
		}
		@Override
		public void onPreExecute() {
			_progressDialog.show();
			try {
				flickrARR=fh.getArrayListFromWebService(_searchString);
			} catch (Exception e) {
				e.printStackTrace();
			}
			super.onPreExecute();
		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);
		}

		protected void onProgressUpdate(Integer... progress) {
			if (progress[0]==0)
				_progressDialog.dismiss();
			FIA.notifyDataSetChanged();
			super.onProgressUpdate(progress);
		}

		protected String doInBackground(Integer... a) {
			
			for (int i = 0; i < flickrARR.size(); i++) {
				flickrARR.get(i).get_thmb(true);	
				this.publishProgress(i);
			}
			return "";
		}
	}
	public class ImageAdapter extends BaseAdapter {
		private Context mContext;
		public ImageAdapter(Context c) {
			mContext = c;
		}

		public int getCount() {
			return flickrARR.size();
		}

		public Object getItem(int position) {
			return flickrARR.get(position).get_thmb(false);
		}

		public long getItemId(int position) {
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView = new ImageView(mContext);
			imageView.setImageBitmap(flickrARR.get(position).get_thmb(false));
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setLayoutParams(new GridView.LayoutParams(70, 70));
			return imageView;
		}

	}
}
