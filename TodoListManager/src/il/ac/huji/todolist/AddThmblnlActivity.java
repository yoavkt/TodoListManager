package il.ac.huji.todolist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;

import android.R.integer;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

public class AddThmblnlActivity extends Activity {
 
	private ImageView imageMap;
	private Bitmap map;
	private Context thisForm;
	
	ThumbnailDisplayAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_thumbnail);
		thisForm=this;
		findViewById(R.id.btnTHBOK).setOnClickListener(new OnClickListener() {	
			
			public void onClick(View v) {

				String searchString = ((EditText)findViewById(R.id.editTextThumbnailSearch)).getText().toString();
				if (searchString==""){
					((EditText)findViewById(R.id.editTextThumbnailSearch)).setText("Enter a search String");
				}
				else{
					
					ImageView imageView = (ImageView) findViewById(R.id.imageView1);
					flickrHandler fh=new flickrHandler();
					ArrayList<flickrImage> flickrARR=new ArrayList<flickrImage>();
					try {
						flickrARR=fh.getImageArrayListFromFlickr(searchString);
						imageView.setImageBitmap(fh.getImageArrayListFromFlickr("Cat").get(0).getImageAsBitMap());
						imageView.requestLayout();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (JSONException e) {
						e.printStackTrace();
					}

				
					GridView gridview = (GridView) findViewById(R.id.gridViewTHMB);
				    gridview.setAdapter(new ThumbnailDisplayAdapter(thisForm, flickrARR));
				    
				    gridview.setOnItemClickListener(new OnItemClickListener() {
				        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				           // Toast.makeText(HelloGridView.this, "" + position, Toast.LENGTH_SHORT).show();
				        	((EditText)findViewById(R.id.editTextThumbnailSearch)).setText(Integer.toString(   position));
				        }
				    });
					//finish();
				}	
			}
		});
		
		findViewById(R.id.btnTHBCancel).setOnClickListener(new OnClickListener() {
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

}
