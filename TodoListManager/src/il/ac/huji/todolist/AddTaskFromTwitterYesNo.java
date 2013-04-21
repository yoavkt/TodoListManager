package il.ac.huji.todolist;

import org.w3c.dom.Text;

import il.ac.huji.todolist.R.id;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class AddTaskFromTwitterYesNo extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_task_from_twitter_yes_no);
		TextView myText=(TextView)findViewById(R.id.textViewAddFromTwiter);
		Bundle extras = getIntent().getExtras();
		String value="No num";
		if (extras != null) {
		     value = Integer.toString(extras.getInt("NumOfTasks"));
		}
		myText.setText("Do you wish to add "+value);
		findViewById(R.id.btnYesAddFromTwitter).setOnClickListener(new OnClickListener() {
		
			public void onClick(View v) {
				setResult(RESULT_OK);
				finish();
				
			}
		});
		findViewById(R.id.btnNoAddFromTwitter).setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
				
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.add_task_from_twitter_yes_no, menu);
		return true;
	}

}
