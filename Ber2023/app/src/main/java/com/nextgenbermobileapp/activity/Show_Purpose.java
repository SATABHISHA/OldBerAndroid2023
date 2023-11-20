package com.nextgenbermobileapp.activity;

import java.util.ArrayList;
import java.util.List;

import com.nextgenbermobileapp.R;
import com.nextgenbermobileapp.adapter.ViewTaskAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Show_Purpose extends Activity {

	List<String> add_taskList = new ArrayList<String>();

	ListView var_ViewTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_purpose_list_activity);

		add_taskList.add("Meeting with client");
		add_taskList.add("Other");

		var_ViewTask = (ListView) findViewById(R.id.ViewTask);

		MySimpleArrayAdapter view_adapter = new MySimpleArrayAdapter(this,
				add_taskList);
		var_ViewTask.setAdapter(view_adapter);

		var_ViewTask.setOnItemClickListener(new OnItemClickListener() {

			private String show_Name;

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				show_Name = add_taskList.get(arg2);
				Log.i("ami", show_Name);
				Intent in_next = new Intent();
				Bundle bd = new Bundle();
				bd.putString("Purpose_Name", show_Name);

				in_next.putExtras(bd);
				setResult(RESULT_OK, in_next);
				finish();
			}
		});
		
		findViewById(R.id.MainBack).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				finish();
			}
			
		});
	}

}

class MySimpleArrayAdapter extends ArrayAdapter<String> {

	private Context context;
	private List<String> perdiem_name, perdiem_id;
	View rowView;

	public MySimpleArrayAdapter(Context context, List<String> perdiem_name) {
		super(context, R.layout.list_row_for_location, perdiem_name);
		this.context = context;
		this.perdiem_name = perdiem_name;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.list_row_for_location, parent,
					false);
		} else {
			rowView = convertView;
		}
		try {

			TextView projectic = (TextView) rowView
					.findViewById(R.id.ShowDateforText);
			projectic.setText((perdiem_name.get(position)));
			// currentemail=lsEmail.get(position);

		} catch (Exception e) {
			// TODO: handle exception
		}
		return rowView;

	}

}
