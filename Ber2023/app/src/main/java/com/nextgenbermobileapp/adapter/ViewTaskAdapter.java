package com.nextgenbermobileapp.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.nextgenbermobileapp.R;
import com.nextgenbermobileapp.activity.ShowTask;
import com.nextgenbermobileapp.info.TaskInfo;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ViewTaskAdapter extends BaseAdapter{

	private Activity activity;
    private ArrayList<TaskInfo> data;
    private static LayoutInflater inflater=null;
	
    public ViewTaskAdapter(Activity a, ArrayList<TaskInfo> d) {
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
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
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
        
        // Setting all values in listview
        title.setText(data.get(position).getDescription());
        
        return vi;
    }

}
