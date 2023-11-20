package com.nextgenbermobileapp.adapter;

import java.util.List;

import com.nextgenbermobileapp.R;
import com.nextgenbermobileapp.activity.Expense_Travel;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

public class MySimpleArrayAdapter_summary extends ArrayAdapter<String> {

	private Context context;
	private List<String> perdiem_name, perdiem_id;
	View rowView;

	TextWatcher textWatcher;
	
	public MySimpleArrayAdapter_summary(Context context,
			List<String> perdiem_name, List<String> perdiem_id) {
		super(context, R.layout.direct_expense_row, perdiem_name);
		this.context = context;
		this.perdiem_name = perdiem_name;
		this.perdiem_id = perdiem_id;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			if (position == 0) {
//				rowView = inflater.inflate(R.layout.travel_expense_row, parent,
//						false);
//				
//				if (textWatcher != null) {
//					((EditText) rowView.findViewById(R.id.Rates_meals)).addTextChangedListener(textWatcher);
//					((EditText) rowView.findViewById(R.id.mile_travels)).addTextChangedListener(textWatcher);
//					((EditText) rowView.findViewById(R.id.exp_amt)).addTextChangedListener(textWatcher);
//				 }
//			} else {
				rowView = inflater.inflate(R.layout.direct_expense_row, parent, false);
				if (textWatcher != null)
					((EditText) rowView.findViewById(R.id.tolls_parking)).addTextChangedListener(textWatcher);
//			}
		} else {
			rowView = convertView;
		}
		
		try {

			TextView projectic = (TextView) rowView
					.findViewById(R.id.textView6);
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