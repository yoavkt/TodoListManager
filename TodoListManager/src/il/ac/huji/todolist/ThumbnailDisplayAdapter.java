package il.ac.huji.todolist;

import java.util.ArrayList;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ThumbnailDisplayAdapter extends BaseAdapter  {

	private Context _mContext;
	private ArrayList<flickrImage> _flickrARR;
    
	public ThumbnailDisplayAdapter(Context c,ArrayList<flickrImage> flickrARR) {
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
	        if(position<30)
	        	imageView.setImageBitmap(_flickrARR.get(position).getImageAsBitMap());
	        return imageView;
	}





}
