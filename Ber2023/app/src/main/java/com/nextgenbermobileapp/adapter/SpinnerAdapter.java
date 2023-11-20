package com.nextgenbermobileapp.adapter;

import java.util.ArrayList;

import com.nextgenbermobileapp.R;
import com.nextgenbermobileapp.info.ExpensesInfo;
import com.nextgenbermobileapp.info.LocationDetailsInfo;
import com.nextgenbermobileapp.info.TaskInfo;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SpinnerAdapter extends BaseAdapter{

	
	private Activity activity;
	private static LayoutInflater inflater=null;
	
	@SuppressWarnings("unused")
	private ArrayList<ExpensesInfo> show_Date_Result;
	
	
	public SpinnerAdapter(Activity a,ArrayList<ExpensesInfo> d) {
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
            vi = inflater.inflate(R.layout.list_row_for_spinner, null);
 
        TextView title = (TextView)vi.findViewById(R.id.ShowDateforText); // title
        String show_result=show_Date_Result.get(position).getName();
        title.setText(show_result);
        
        return vi;
    }

}
