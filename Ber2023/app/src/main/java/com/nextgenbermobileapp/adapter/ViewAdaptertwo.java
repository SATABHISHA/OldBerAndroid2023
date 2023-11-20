package com.nextgenbermobileapp.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.nextgenbermobileapp.R;
import com.nextgenbermobileapp.activity.InputForm;
import com.nextgenbermobileapp.activity.ShowCalendarList;
import com.nextgenbermobileapp.connectioninfo.ConnectionURL;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ViewAdaptertwo extends BaseAdapter {

	private Activity activity;
	private static LayoutInflater inflater = null;
	@SuppressWarnings("unused")
	/* private ArrayList<DateInfo> show_Date_Result; */
	private ArrayList<HashMap<String, String>> data;
	private String store_CorpID;
	private String store_UserID;
	private String show_WeekDate;
	private String show_start_date;
	private String show_end_Date;
	private String show_BERStatus;
	private String store_User_ID;
	private HashMap<String, String> song;

	private String date_position;
	private String day_position;
	private String get_url;

	public static final String ValueStore = "Collection_Data";

	public ViewAdaptertwo(Activity a, ArrayList<HashMap<String, String>> d) {
		activity = a;
		data = d;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		SharedPreferences example = activity
				.getSharedPreferences(ValueStore, 0);
		store_CorpID = example.getString("CorpID", "");
		store_UserID = example.getString("UserID", "");
		store_User_ID = example.getString("UserName", "");
		show_WeekDate = example.getString("WeekDate", "");
		show_start_date = example.getString("StartDate", "");
		show_end_Date = example.getString("EndDate", "");
		show_BERStatus = example.getString("BERStatus", "");
		get_url = example.getString("URL", "");
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

	public View getView(final int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.list_row_two, null);

		TextView title = (TextView) vi.findViewById(R.id.ShowDateforText); // title
		TextView artist = (TextView) vi.findViewById(R.id.ShowDayforText);

		TextView expense = (TextView) vi.findViewById(R.id.expense);// artist
																	// name

		ImageView mv = (ImageView) vi.findViewById(R.id.img);
		ImageView trash = (ImageView) vi.findViewById(R.id.trash);

		LinearLayout delete = (LinearLayout) vi.findViewById(R.id.delete);
		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				date_position = song.get("Date" + position);
				day_position = song.get("Day" + position);
				String date_day = date_position + " (" + day_position + ")";
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						activity);

				// Setting Dialog Title
				alertDialog.setTitle("Confirm Delete...");

				// Setting Dialog Message
				alertDialog
						.setMessage("Do you really want to delete the expense for "
								+ date_day + " ?");

				// Setting Icon to Dialog
				alertDialog.setIcon(R.drawable.delete);

				// Setting Positive "Yes" Button
				alertDialog.setPositiveButton("YES",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								Delete_data_asynchronously task1 = new Delete_data_asynchronously();
								task1.execute();

							}
						});

				// Setting Negative "NO" Button
				alertDialog.setNegativeButton("NO",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// Write your code here to invoke NO event;
								dialog.cancel();
							}
						});

				// Showing Alert Message
				alertDialog.show();

			}
		});

		song = new HashMap<String, String>();
		song = data.get(position);

		String s = song.get("Date" + position);
		String t = song.get("Day" + position);
		String v = song.get("Total" + position);
		String attch = song.get("ImageCode" + position);
		/*
		 * artist.setText(song.get("Day")); String temp=s+" "+t; String
		 * result=song.get("Date").toString()+song.get("Day").toString();
		 */
		title.setText(s);
		artist.setText(t);
		if (v != null) {
			expense.setText("$" + v);
			delete.setVisibility(View.VISIBLE);
			// trash.setVisibility(View.VISIBLE);
		} else {
			expense.setText(v);
			delete.setVisibility(View.INVISIBLE);
			// trash.setVisibility(View.INVISIBLE);

		}
		if (attch != null) {
			mv.setVisibility(View.VISIBLE);
		} else {
			mv.setVisibility(View.INVISIBLE);
		}
		return vi;
	}

	/****** Call AsyncTask for threading *************************/

	private class Delete_data_asynchronously extends
			AsyncTask<Void, Void, String> {
		ProgressDialog dialog = new ProgressDialog(activity);

		@Override
		protected void onPreExecute() {
			dialog.setMessage("Please wait...");
			dialog.setCancelable(true);
			dialog.show();
		}

		@Override
		protected String doInBackground(Void... params) {
			return view_details_from_url2();
		}

		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
			// store_data_for_url(result);
			// showResult();

			if (result.equals("1")) {

				AlertDialog alertDialog = new AlertDialog.Builder(activity)
						.create();

				// Setting Dialog Title
				// alertDialog.setTitle("Alert Dialog");

				// Setting Dialog Message
				alertDialog.setMessage("Expense deleted successfully");

				// Setting Icon to Dialog
				// alertDialog.setIcon(R.drawable.tick);

				// Setting OK Button
				alertDialog.setButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// Write your code here to execute after dialog
								// closed
								Intent i = new Intent(activity, ShowCalendarList.class);
								activity.startActivity(i);
								activity.finish();
							}
						});

				// Showing Alert Message
				alertDialog.show();
				

			}
		}
	}

	public String view_details_from_url2() {
		try {
			SoapObject request = new SoapObject(
					ConnectionURL.NAMESPACE_For_delete,
					ConnectionURL.METHOD_NAME_For_delete);
			request.addProperty("strCorpID", store_CorpID);
			request.addProperty("lngPersoinId", store_UserID);
			request.addProperty("strWeekDate", show_WeekDate);
			request.addProperty("strExpenseDate", date_position);
			request.addProperty("strUserId", store_User_ID);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(get_url+
					ConnectionURL.URL_For_SaveList);
			androidHttpTransport.call(ConnectionURL.SOAP_ACTION_For_delete,
					envelope);
			Object result = (Object) envelope.getResponse();
			return result.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
