package com.nextgenbermobileapp.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import com.nextgenbermobileapp.R;
import com.nextgenbermobileapp.activity.ShowCalendarList;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ViewAdapter extends BaseAdapter{

	private Activity activity;
	private static LayoutInflater inflater=null;
	@SuppressWarnings("unused")
	/*private ArrayList<DateInfo> show_Date_Result;*/
	
	private ArrayList<HashMap<String, String>> data;
	
	
	
	public ViewAdapter(Activity a,ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_row, null);
 
        TextView title = (TextView)vi.findViewById(R.id.ShowDateforText); // title
        TextView artist = (TextView)vi.findViewById(R.id.ShowDayforText); // artist name
      
        HashMap<String, String> song = new HashMap<String, String>();
        song = data.get(position);
    
       String s=song.get("Date"+position);
       String t= song.get("Day"+position);
        /*artist.setText(song.get("Day"));
        String temp=s+" "+t;
        String result=song.get("Date").toString()+song.get("Day").toString();*/
        title.setText(s);
        artist.setText(t);
        return vi;
    }

}
