package com.nextgenbermobileapp.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.nextgenbermobileapp.R;
import com.nextgenbermobileapp.info.LocationDetailsInfo;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ViewLocAdapter extends BaseAdapter{

	private Activity activity;
	private static LayoutInflater inflater=null;
	
	@SuppressWarnings("unused")
	private ArrayList<LocationDetailsInfo> show_Date_Result;
	
	
	public ViewLocAdapter(Activity a,ArrayList<LocationDetailsInfo> d) {
        activity = a;
        show_Date_Result=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return show_Date_Result.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_row_for_location, null);
 
        TextView title = (TextView)vi.findViewById(R.id.ShowDateforText); // title
        String show_result=show_Date_Result.get(position).getName();
        System.out.println("Show Result :"+show_result);
        title.setText(show_result);
        
        return vi;
    }

}
