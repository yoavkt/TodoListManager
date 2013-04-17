package il.ac.huji.todolist;

import java.util.ArrayList;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class FlickrDisplayAdapter extends BaseAdapter  {

	private Context _mContext;
	private ArrayList<FlickrImage> _flickrARR;
    
	public FlickrDisplayAdapter(Context c,ArrayList<FlickrImage> flickrARR) {
		_mContext = c;
		_flickrARR=flickrARR;
    }

	public int getCount() {
		return _flickrARR.size();
	}
	public Object getItem(int arg0) {
		
		return _flickrARR.get(arg0);
	}
	public long getItemId(int arg0) {
		return 0;
	}
	public View getView(int position, View convertView, ViewGroup parent) {
	       ImageView imageView;
	        if (convertView == null) {  // if it's not recycled, initialize some attributes
	            imageView = new ImageView(_mContext);
	            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
	            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	            imageView.setPadding(8, 8, 8, 8);
	        } else {
	            imageView = (ImageView) convertView;
	        }
	        	imageView.setImageBitmap(_flickrARR.get(position).getImageAsBitMap());
	        return imageView;
	}





}
