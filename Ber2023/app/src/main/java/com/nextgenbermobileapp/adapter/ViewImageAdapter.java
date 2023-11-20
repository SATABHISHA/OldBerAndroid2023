package com.nextgenbermobileapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ViewImageAdapter extends BaseAdapter{

	// Declare variables
    private Activity activity;
    private String[] filepath;
    private String[] filename;

    private static LayoutInflater inflater = null;

    public ViewImageAdapter(Activity a, String[] fpath, String[] fname) {
           activity = a;
           filepath = fpath;
           filename = fname;
           inflater = (LayoutInflater) activity
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount() {
           return filepath.length;

    }

    public Object getItem(int position) {
           return position;
    }

    public long getItemId(int position) {
           return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
          
     // TODO Auto-generated method stub
     ImageView i = new ImageView(activity);
     Bitmap bmp = BitmapFactory.decodeFile(filepath[position]);
    //  i.setImageResource(mImageIds[position]);
     /*i.setLayoutParams(new Gallery.LayoutParams(200, 200));*/
     i.setScaleType(ImageView.ScaleType.FIT_XY);
     i.setImageBitmap(bmp);
     return i;
    }
}
