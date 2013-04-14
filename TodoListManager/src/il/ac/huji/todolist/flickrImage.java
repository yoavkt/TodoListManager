package il.ac.huji.todolist;

import java.io.IOException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class flickrImage {
	private String _photoID;
	private String _farmID;
	private String _serverID;
	private String _secret;
	private String _title;

	flickrImage(String photoID, String farmID, String serverID, String secret,
			String title) {
		_photoID = photoID;
		_title = title;
		_farmID = farmID;
		_serverID = serverID;
		_secret = secret;
	}

	public flickrImage(JSONObject jsonObject) throws JSONException {
		_photoID=jsonObject.getString("id");
		_farmID=jsonObject.getString("farm");
		_title=jsonObject.getString("title");
		_secret=jsonObject.getString("secret");
		_serverID=jsonObject.getString("server");
		
				
	}

	public String get_flickrID() {
		return _photoID;
	}

	public String get_title() {
		return _title;
	}

	public void set_flickrID(String _flickrID) {
		this._photoID = _flickrID;
	}

	public void set_title(String _title) {
		this._title = _title;
	}

	public String get_farmID() {
		return _farmID;
	}

	public String get_photoID() {
		return _photoID;
	}

	public String get_secret() {
		return _secret;
	}

	public String get_serverID() {
		return _serverID;
	}

	public void set_farmID(String _farmID) {
		this._farmID = _farmID;
	}

	public void set_photoID(String _photoID) {
		this._photoID = _photoID;
	}

	public void set_secret(String _secret) {
		this._secret = _secret;
	}

	public void set_serverID(String _serverID) {
		this._serverID = _serverID;
	}

	public String getStaticLocation() {
		return "http://farm"+_farmID+".staticflickr.com/"+_serverID+"/"+_photoID+"_"+_secret+"_m.jpg";
		}

	public Bitmap getImageAsBitMap() {
		try {
			String s=getStaticLocation();
			URL newurl = new URL(getStaticLocation());
			Bitmap bitmap = BitmapFactory.decodeStream(newurl.openConnection()
					.getInputStream());
			return bitmap;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
