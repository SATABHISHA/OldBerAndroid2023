package com.nextgenbermobileapp.adapter;

import java.util.List;

import com.nextgenbermobileapp.R;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MySimpleArrayAdapter extends ArrayAdapter<String> {

	private Context context;
	private List<String> perdiem_name, perdiem_id;
	View rowView;
	
	private TextWatcher textWatcher;

	public MySimpleArrayAdapter(Context context, List<String> perdiem_name,List<String> perdiem_id) {
		super(context, R.layout.perdiem_expense_row, perdiem_name);
		this.context = context;
		this.perdiem_name = perdiem_name;
		this.perdiem_id = perdiem_id;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.perdiem_expense_row, parent, false);
			if (textWatcher != null) {
			    ((EditText) rowView.findViewById(R.id.meal_expense)).addTextChangedListener(textWatcher);
			}
		} else {
			rowView = convertView;
		}
		
		try {
			TextView projectic = (TextView) rowView
					.findViewById(R.id.perdiem_name);
			projectic.setText((perdiem_name.get(position)));
			// currentemail=lsEmail.get(position);

		} catch (Exception e) {
			// TODO: handle exception
		}
		return rowView;

	}
	
	public void setTextWatcher(TextWatcher textWatcher) {
        this.textWatcher = textWatcher;
    }
}