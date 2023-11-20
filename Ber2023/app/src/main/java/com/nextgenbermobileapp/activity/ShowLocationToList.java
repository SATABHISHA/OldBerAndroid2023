package com.nextgenbermobileapp.activity;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.nextgenbermobileapp.R;
import com.nextgenbermobileapp.connectioninfo.CheckNetworkConnection;
import com.nextgenbermobileapp.connectioninfo.ConnectionURL;
import com.nextgenbermobileapp.info.LocationDetailsInfo;
import com.nextgenbermobileapp.info.LocationInfo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ShowLocationToList extends Activity {

	EditText search;
	ListView var_ViewLocation;

	private CheckNetworkConnection cd;
	private Boolean isInternetPresent = false;

	String store_UserID, store_CorpID, store_UserName, store_password,
			store_UserRole, var_store_val_from_web_service, store_WeekDate,
			show_Store_Date, show_Store_Day, show_check_form,
			show_check_form_locto, store_loc_form, show_start_date,
			show_end_Date, show_WeekDate, show_BERStatus;

	ArrayList<LocationDetailsInfo> store_data, check_data;
	ArrayList<LocationInfo> store_loc_info;
	/* ArrayList<String> result; */

	// Hashmap for ListView
	ArrayList<HashMap<String, String>> result;
	private String get_url;
	public static final String ValueStore = "Collection_Data";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.location_list_view_activity);

		SharedPreferences example = getSharedPreferences(ValueStore, 0);
		get_url = example.getString("URL", "");

		search = (EditText) findViewById(R.id.search);
		var_ViewLocation = (ListView) findViewById(R.id.ViewLocation);

		store_UserID = getIntent().getExtras().getString("UserID");
		store_CorpID = getIntent().getExtras().getString("CorpID");
		store_UserName = getIntent().getExtras().getString("UserName");
		store_password = getIntent().getExtras().getString("Password");
		store_UserRole = getIntent().getExtras().getString("UserRole");
		/* store_WeekDate=getIntent().getExtras().getString("WeekDate"); */

		show_Store_Date = getIntent().getExtras().getString("StoreDate");
		show_Store_Day = getIntent().getExtras().getString("StoreDay");
		store_loc_form = getIntent().getExtras().getString("LocName");
		// show_check_form_locto=getIntent().getExtras().getString("LocationTo");*/

		show_start_date = getIntent().getExtras().getString("StartDate");
		show_end_Date = getIntent().getExtras().getString("EndDate");

		show_BERStatus = getIntent().getExtras().getString("BERStatus");
		show_WeekDate = getIntent().getExtras().getString("WeekDate");

		findViewById(R.id.MainBack).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		cd = new CheckNetworkConnection(getApplicationContext());

		store_data = new ArrayList<LocationDetailsInfo>();
		store_loc_info = new ArrayList<LocationInfo>();
		check_data = new ArrayList<LocationDetailsInfo>();
		result = new ArrayList<HashMap<String, String>>();

		isInternetPresent = cd.isConnectingToInternet();
		if (isInternetPresent) {

			verify_user_login_asynchronously task1 = new verify_user_login_asynchronously();
			task1.execute(new String[] { "" });
		} else {
			Toast.makeText(ShowLocationToList.this,
					"Check Your Internet Connection", Toast.LENGTH_SHORT)
					.show();
		}
	}

	/******* call asynctask for location list ******************/

	private class verify_user_login_asynchronously extends
			AsyncTask<String, Void, Boolean> {
		ProgressDialog dialog = new ProgressDialog(ShowLocationToList.this);

		// @Override
		protected void onPreExecute() {
			dialog.setMessage("Please wait...");
			dialog.setCancelable(true);
			dialog.show();
		}

		// @Override
		protected Boolean doInBackground(String... params) {
			view_details_from_url();
			return null;
		}

		protected void onPostExecute(Boolean is_authenticated) {
			if (ShowLocationToList.this != null
					&& !ShowLocationToList.this.isFinishing())
				dialog.dismiss();
			view_data_in_List();
		}
	}

	public void view_details_from_url() {
		try {

			SoapObject request = new SoapObject(
					ConnectionURL.NAMESPACE_For_LocationList,
					ConnectionURL.METHOD_NAME_For_LocationList);

			// ********** for login**************//*

			request.addProperty("CorpID", store_CorpID);
			request.addProperty("UserName", store_UserID);
			request.addProperty("WeekDate", show_Store_Date);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(get_url+
					ConnectionURL.URL_For_LocationList);
			androidHttpTransport.call(
					ConnectionURL.SOAP_ACTION_For_LocationList, envelope);
			Object result = (Object) envelope.getResponse();
			// tv.setText(result.toString());
			var_store_val_from_web_service = result.toString();
			System.out.println("Print Result :"
					+ var_store_val_from_web_service);

			store_data_for_login();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void view_data_in_List() {
		final ArrayAdapter<LocationDetailsInfo> store_adapter_data = new ArrayAdapter<LocationDetailsInfo>(
				this, android.R.layout.simple_list_item_1, store_data);
		var_ViewLocation.setAdapter(store_adapter_data);

		search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				store_adapter_data.getFilter().filter(s);
			}
		});

		var_ViewLocation.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {

				String show_loc_id = store_adapter_data.getItem(pos).getID();
				String show_Name = store_adapter_data.getItem(pos).getName();

				for (int i = 0; i < check_data.size(); i++) {

					String show_id = check_data.get(i).getID();

					if (show_loc_id.equalsIgnoreCase(show_id)) {
						String show_LocationID = store_loc_info.get(i)
								.getLoc_ID();
						String show_ExpenseTypeID = store_loc_info.get(i)
								.getExpenseTypeID();
						String show_StartDate = store_loc_info.get(i)
								.getStartDate();
						String show_EndDate = store_loc_info.get(i)
								.getStartDate();
						String show_Rate = store_loc_info.get(i).getRate();

						// tmp hashmap for single contact
						HashMap<String, String> result_info = new HashMap<String, String>();
						result_info.put("LocationID", show_LocationID);
						result_info.put("ExpenseTypeID", show_ExpenseTypeID);
						result_info.put("StartDate", show_StartDate);
						result_info.put("EndDate", show_EndDate);
						result_info.put("Rate", show_Rate);
						result.add(result_info);
					}
				}

				Intent in_next = new Intent();
				Bundle bd = new Bundle();
				bd.putString("StoreDate", show_Store_Date);
				bd.putString("StoreDay", show_Store_Day);
				bd.putString("LocID", show_loc_id);
				bd.putString("LocToName", show_Name);
				bd.putString("UserID", store_UserID);
				bd.putString("CorpID", store_CorpID);
				bd.putString("UserName", store_UserName);
				bd.putString("Password", store_password);
				bd.putString("UserRole", store_UserRole);
				bd.putString("LocName", store_loc_form);
				bd.putString("StartDate", show_start_date);
				bd.putString("EndDate", show_end_Date);
				bd.putString("WeekDate", show_WeekDate);
				bd.putString("BERStatus", show_BERStatus);
				/* bd.putParcelableArrayList("LocationInfo", store_loc_info); */
				in_next.putExtra("LocationInfo", result);

				in_next.putExtras(bd);
				setResult(RESULT_OK, in_next);
				finish();
			}
		});
	}

	private void store_data_for_login() {
		try {

			JSONObject js = new JSONObject(var_store_val_from_web_service);
			String var_WeekDate = js.getString("WeekDate");
			System.out.println(var_WeekDate);

			// Getting Array of Contacts
			JSONArray contacts_List = js.getJSONArray("List");

			for (int i = 0; i < contacts_List.length(); i++) {

				JSONObject c = contacts_List.getJSONObject(i);

				String var_ID = c.getString("ID");
				String var_Name = c.getString("Name");
				String var_CountryID = c.getString("CountryID");
				String var_StateID = c.getString("StateID");

				LocationDetailsInfo var_store_loc_info = new LocationDetailsInfo(
						var_ID, var_Name, var_CountryID, var_StateID);

				store_data.add(var_store_loc_info);

				if (c.isNull("LocationRate"))
					continue;

				JSONArray contacts_LocationRate = c
						.getJSONArray("LocationRate");

				for (int j = 0; j < contacts_LocationRate.length(); j++) {

					JSONObject show = contacts_LocationRate.getJSONObject(j);

					String var_LocationID = show.getString("ID");
					String var_ExpenseTypeID = show.getString("ExpenseTypeID");
					String var_StartDate = show.getString("StartDate");
					String var_EndDate = show.getString("EndDate");
					String var_Rate = show.getString("Rate");

					/*
					 * LocationDetailsInfo store_loc_info=new
					 * LocationDetailsInfo
					 * (var_ID,var_Name,var_CountryID,var_StateID
					 * ,var_LocationID,
					 * var_ExpenseTypeID,var_StartDate,var_EndDate,var_Rate);
					 * store_data.add(store_loc_info);
					 */
					LocationInfo store_loc = new LocationInfo(var_LocationID,
							var_ExpenseTypeID, var_StartDate, var_EndDate,
							var_Rate);
					store_loc_info.add(store_loc);

					LocationDetailsInfo store_loc_info = new LocationDetailsInfo(
							var_ID, var_Name, var_CountryID, var_StateID,
							var_LocationID, var_ExpenseTypeID, var_StartDate,
							var_EndDate, var_Rate);
					check_data.add(store_loc_info);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
