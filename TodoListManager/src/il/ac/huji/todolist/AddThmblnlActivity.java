package il.ac.huji.todolist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;

import android.R.integer;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class AddThmblnlActivity extends Activity {

	private Context thisForm;
	private String selePicURL;
	private ArrayList<FlickrImage> flickrARR = new ArrayList<FlickrImage>();
	private ArrayList<Bitmap> imageFromFlickr = new ArrayList<Bitmap>();
	ImageAdapter FIA;
	FlickrDisplayAdapter adapter;

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
					// thread attempt
					FlickrHandler fh=new FlickrHandler();
					try {
						flickrARR=fh.getImageArrayListFromFlickr(searchString);
					} catch (Exception e) {
						e.printStackTrace();
					} 
					FIA = new ImageAdapter(thisForm);
					GridView gridview = (GridView) findViewById(R.id.gridViewTHMB);
					gridview.setAdapter(FIA);
					new  ImageDownloader().execute();
					//new FlickrDisplayAsyncTask().execute();
					
					// (new FlickrDisplayAsyncTask(AddThmblnlActivity.this,
					// gridview)).execute(searchString);

					// bl

					/*
					 * FlickrHandler fh=new FlickrHandler(); try {
					 * flickrARR=fh.getImageArrayListFromFlickr(searchString); }
					 * catch (IOException e) { e.printStackTrace(); } catch
					 * (JSONException e) { e.printStackTrace(); }
					 * 
					 * 
					 * gridview.setAdapter(new FlickrDisplayAdapter(thisForm,
					 * flickrARR));
					 */
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
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_thumbnail, menu);
		return true;
	}

	private class ImageDownloader extends AsyncTask<Integer, Integer, String> {
		@Override
		public void onPreExecute() {
			// prg=ProgressDialog.show(LazyLoadActivity.this, null,
			// "Loading...");
		}

		protected void onPostExecute(String result) {
			// prg.hide();

		}

		protected void onProgressUpdate(Integer... progress) {
			FIA.notifyDataSetChanged();
		}

		protected String doInBackground(Integer... a) {
			for (int i = 0; i < flickrARR.size(); i++) {
				imageFromFlickr.set(i, flickrARR.get(i).getImageAsBitMap());
				this.publishProgress(i);
			}
			return "";
		}
	}


	public class ImageAdapter extends BaseAdapter {
		private Context mContext;

		// Keep all Images in array

		// Constructor
		public ImageAdapter(Context c) {
			mContext = c;
		}

		public int getCount() {
			return flickrARR.size();
		}

		public Object getItem(int position) {
			return imageFromFlickr.get(position);
		}

		public long getItemId(int position) {
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView = new ImageView(mContext);
			if (position<imageFromFlickr.size())
				imageView.setImageBitmap(imageFromFlickr.get(position));
			else{
				imageFromFlickr.add(null);
				imageView.setImageBitmap(imageFromFlickr.get(position));
			}
				
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setLayoutParams(new GridView.LayoutParams(70, 70));
			return imageView;
		}

	}
}
