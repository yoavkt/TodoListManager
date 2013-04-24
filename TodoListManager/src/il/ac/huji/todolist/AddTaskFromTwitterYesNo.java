package il.ac.huji.todolist;

import org.w3c.dom.Text;

import il.ac.huji.todolist.R.id;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddTaskFromTwitterYesNo extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_task_from_twitter_yes_no);
		TextView myText=(TextView)findViewById(R.id.textViewAddFromTwiter);
		Button noBTN=(Button)findViewById(R.id.btnNoAddFromTwitter);
		Button yesBTN=(Button)findViewById(R.id.btnYesAddFromTwitter);
		Bundle extras = getIntent().getExtras();
		String value="0";
		// The logic here checks if there are any new tasks and modify the display the display accordingly
		if (extras != null) {
		     value = Integer.toString(extras.getInt(TodoListManagerConstants.TASK_FROM_TWITTER_EXTRA_NUM_OF_TASKS));
		}
		if (!value.equals("0")){
			myText.setText(TodoListManagerConstants.TASK_FROM_TWITTER_TEXT+value);
			noBTN.setVisibility(View.VISIBLE);
			yesBTN.setText("Yes");
		}
			else
			{
				noBTN.setVisibility(View.INVISIBLE);
			yesBTN.setText("OK");
			myText.setText(TodoListManagerConstants.NO_TASK_FROM_TWITTER_TEXT);
			}
		
		final String forBTN=value;
		findViewById(R.id.btnYesAddFromTwitter).setOnClickListener(new OnClickListener() {
		
			public void onClick(View v) {
				//If there are no new tasks to add it's like a cancell
				if (!forBTN.equals("0"))
					setResult(RESULT_OK);
				else
					setResult(RESULT_CANCELED);
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
