package il.ac.huji.todolist;

import java.io.File;

import android.os.Environment;

public class TodoListManagerConstants {
	
	
	//Thumbnail Strings 
	public static String THUMBNAIL_ACTIVITY_PROGRESS_HEADER="Searching Flickr";
	public static String THUMBNAIL_ACTIVITY_PROGRESS_INITIAL_MESSAGE="Image will start to appear when\n" +
																	 "I will finish searching";
	public static String THUMBNAIL_ACTIVITY_TEXTVIEW_DEFAULT="Enter a search String";
	public static String THUMBNAIL_ACTIVITY_EXTRA_URL="thumbnail";
	public static String THUMBNAIL_ACTIVITY_EXTRA_ID="thumbnailId";
	public static String THUMBNAIL_FILE_DIR="/data/data/il.ac.huji.todolist/files";
	public static final String IMGFILETYPE = ".png";
	//New todo item Strings
	public static String NEWTODO_ACTIVITY_EXTRA_TITLE="title";
	public static String NEWTODO_ACTIVITY_EXTRA_DATE="due";
	
	//Add Task From Twitter Tasks Strings
	public static String TASK_FROM_TWITTER_EXTRA_NUM_OF_TASKS="NumOfTasks";
	public static String TASK_FROM_TWITTER_TEXT="Do you wish to add ";
	public static final String NO_TASK_FROM_TWITTER_TEXT = "No tasks from twitter"; 
	
	//Change hash tag activity	
	public static String CHANGE_HSHTAG_EXTRA="hashTag";
	
	//MAIN ACTIVITY
	public static final int ADD_NEW_TASK=1986;
	public static final int ADD_NEW_PIC=1987; 
	public static final int HASH_TAG_CHANGED=1988; 
	public static final int ADD_NEW_TASK_FROM_TWITTER=1989;
	public static final String DEFAULT_HASH_TAG = "todoapp";
	public static final String DATA_FILE = TodoListManagerConstants.THUMBNAIL_FILE_DIR+"/dat.dat";
	public static final String DATA_FILE_DIR = TodoListManagerConstants.THUMBNAIL_FILE_DIR;

}
