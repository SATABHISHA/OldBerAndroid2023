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
import com.nextgenbermobileapp.adapter.ViewTaskAdapter;
import com.nextgenbermobileapp.connectioninfo.CheckNetworkConnection;
import com.nextgenbermobileapp.connectioninfo.ConnectionURL;
import com.nextgenbermobileapp.info.LocationDetailsInfo;
import com.nextgenbermobileapp.info.TaskInfo;

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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ShowTask extends Activity {

	String store_UserID, store_CorpID, store_UserName, store_password,
			store_UserRole, store_WeekDate, var_store_val_from_web_service,
			show_Store_Date, show_Store_Day, store_loc_form, show_Name,
			show_ExpenseTypeID, show_start_date, show_end_Date, show_BERStatus,
			show_WeekDate, store_loc_id;

	private CheckNetworkConnection cd;
	private Boolean isInternetPresent = false;

	String KEY_UserID = "UserID"; // parent node
	String KEY_ID = "ID";
	String KEY_Code = "Code";
	String KEY_ContractNo = "ContractNo";
	String KEY_Description = "Description";

	ArrayList<TaskInfo> add_taskList;

	ListView var_ViewTask;

	ArrayList<HashMap<String, String>> store_result;
	public static final String ValueStore = "Collection_Data";

	private EditText search;

	private String get_url;

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.view_task_list_activity);

		SharedPreferences example = getSharedPreferences(ValueStore, 0);
		get_url = example.getString("URL", "");

		search = (EditText) findViewById(R.id.search);

		store_UserID = getIntent().getExtras().getString("UserID");
		store_CorpID = getIntent().getExtras().getString("CorpID");
		store_UserName = getIntent().getExtras().getString("UserName");
		store_password = getIntent().getExtras().getString("Password");
		store_UserRole = getIntent().getExtras().getString("UserRole");
		/* store_WeekDate=getIntent().getExtras().getString("WeekDate"); */
		show_Store_Date = getIntent().getExtras().getString("StoreDate");
		show_Store_Day = getIntent().getExtras().getString("StoreDay");
		store_loc_form = getIntent().getExtras().getString("LocName");
		show_Name = getIntent().getExtras().getString("LocToName");

		show_start_date = getIntent().getExtras().getString("StartDate");
		show_end_Date = getIntent().getExtras().getString("EndDate");

		show_BERStatus = getIntent().getExtras().getString("BERStatus");
		show_WeekDate = getIntent().getExtras().getString("WeekDate");

		store_loc_id = getIntent().getExtras().getString("LocID");

		show_ExpenseTypeID = getIntent().getExtras().getString(
				"LocExpenseTypeID");
		store_result = (ArrayList<HashMap<String, String>>) getIntent()
				.getSerializableExtra("LocationInfo");

		cd = new CheckNetworkConnection(getApplicationContext());
		add_taskList = new ArrayList<TaskInfo>();

		var_ViewTask = (ListView) findViewById(R.id.ViewTask);

		isInternetPresent = cd.isConnectingToInternet();
		if (isInternetPresent) {
			verify_user_login_asynchronously task1 = new verify_user_login_asynchronously();
			task1.execute(new String[] { "" });
		} else {
			Toast.makeText(ShowTask.this, "Check Your Internet Connection",
					Toast.LENGTH_SHORT).show();
		}

		findViewById(R.id.MainBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}

		});

	}

	/******* call asynctask for location list ******************/

	private class verify_user_login_asynchronously extends
			AsyncTask<String, Void, Boolean> {
		ProgressDialog dialog = new ProgressDialog(ShowTask.this);

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
			if (ShowTask.this != null && !ShowTask.this.isFinishing())
				dialog.dismiss();
			view_data_in_List();
		}
	}

	public void view_details_from_url() {
		try {

			Integer store_id = Integer.parseInt(store_UserID);

			SoapObject request = new SoapObject(
					ConnectionURL.NAMESPACE_For_TaskList,
					ConnectionURL.METHOD_NAME_For_TaskList);
			// ********** for login**************//*
			request.addProperty("CorpID", store_CorpID);
			request.addProperty("UserID", store_id);
			request.addProperty("WeekDate", show_Store_Date);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(get_url+
					ConnectionURL.URL_For_TaskList);
			androidHttpTransport.call(ConnectionURL.SOAP_ACTION_For_TaskList,
					envelope);
			Object result = (Object) envelope.getResponse();
			var_store_val_from_web_service = result.toString();
			System.out.println("Print Result :"
					+ var_store_val_from_web_service);

			store_data_for_login();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void view_data_in_List() {

		// ViewTaskAdapter view_adapter=new ViewTaskAdapter(this,add_taskList);
		// var_ViewTask.setAdapter(view_adapter);

		final ArrayAdapter<TaskInfo> store_adapter_data = new ArrayAdapter<TaskInfo>(
				this, android.R.layout.simple_list_item_1, add_taskList);
		var_ViewTask.setAdapter(store_adapter_data);

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

		var_ViewTask.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				
				String pass_id = store_adapter_data.getItem(pos).getID();
				String pass_Description = store_adapter_data.getItem(pos).getDescription();

				String pass_userid = add_taskList.get(pos).getUserID();
//				String pass_id = add_taskList.get(pos).getID();
				String pass_code = add_taskList.get(pos).getCode();
				String pass_contractNo = add_taskList.get(pos).getContractNo();
//				String pass_Description = add_taskList.get(pos)
//						.getDescription();

				Intent in_back = new Intent();
				Bundle bd = new Bundle();
				bd.putString("UserId", pass_userid);
				bd.putString("Id", pass_id);
				bd.putString("Code", pass_code);
				bd.putString("ContractNo", pass_contractNo);
				bd.putString("Description", pass_Description);
				bd.putString("StoreDate", show_Store_Date);
				bd.putString("StoreDay", show_Store_Day);
				bd.putString("UserID", store_UserID);
				bd.putString("CorpID", store_CorpID);
				bd.putString("UserName", store_UserName);
				bd.putString("Password", store_password);
				bd.putString("UserRole", store_UserRole);
				bd.putString("LocName", store_loc_form);
				bd.putString("LocToName", show_Name);
				bd.putString("StartDate", show_start_date);
				bd.putString("EndDate", show_end_Date);
				bd.putString("WeekDate", show_WeekDate);
				bd.putString("BERStatus", show_BERStatus);
				bd.putString("LocID", store_loc_id);
				bd.putString("LocExpenseTypeID", show_ExpenseTypeID);
				in_back.putExtra("LocationInfo", store_result);

				in_back.putExtras(bd);
				setResult(RESULT_OK, in_back);
				finish();

			}

		});

	}

	public void store_data_for_login() {

		try {

			@SuppressWarnings("unused")
			HashMap<String, String> map = new HashMap<String, String>();

			JSONObject js = new JSONObject(var_store_val_from_web_service);
			String var_UserID = js.getString("UserID");

			// Getting Array of Contacts
			JSONArray contacts_TaskList = js.getJSONArray("TaskList");

			for (int i = 0; i < contacts_TaskList.length(); i++) {
				JSONObject c = contacts_TaskList.getJSONObject(i);

				String var_ID = c.getString("ID");
				String var_Code = c.getString("Code");
				String var_ContractNo = c.getString("ContractNo");
				String var_Description = c.getString("Description");

				TaskInfo store_info = new TaskInfo(var_UserID, var_ID,
						var_Code, var_ContractNo, var_Description);
				add_taskList.add(store_info);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

}
