package il.ac.huji.todolist;

import java.io.IOException;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class ChangeHashTagActivity extends Activity  {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_hash_tag);
		TwitterHandler th=new TwitterHandler();
		EditText hashTagName= (EditText)findViewById(R.id.editTextTweetTag);
		try {
			hashTagName.setText( th.getDefaultHashTag());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
				findViewById(R.id.btnTwitChange).setOnClickListener(new OnClickListener() {
					
					public void onClick(View v) {
						TwitterHandler th=new TwitterHandler();
						String hashTagVal = ((EditText)findViewById(R.id.editTextTweetTag)).getText().toString();
							try {
								th.setDefaultHashTag(hashTagVal);
								Intent resultIntent = new Intent();
								resultIntent.putExtra("hashTag", hashTagVal);
								setResult(RESULT_OK,resultIntent);
								finish();
								
							} catch (IOException e) {
								e.printStackTrace();
								setResult(RESULT_CANCELED);
								finish();
							}
						}
				});

				findViewById(R.id.btnTwitCancel).setOnClickListener(new OnClickListener() {
					
					public void onClick(View v) {
						setResult(RESULT_CANCELED);
						finish();	
					}
				});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.change_hash_tag, menu);
		return true;
	}

}
