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
import com.nextgenbermobileapp.adapter.SpinnerAdapter;
import com.nextgenbermobileapp.connectioninfo.CheckNetworkConnection;
import com.nextgenbermobileapp.connectioninfo.ConnectionURL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class NewInputForm extends Activity implements OnClickListener {

	TextView var_ShowDateDay, var_LocationForm, var_ShowSelectTask,
			var_ShowLocationTo, var_AllowanceRate, var_ShowExpType,
			var_ShowExpAllowence, var_ShowExpAmount, label_ExpType,
			label_Allowance, label_Expenses;

	Button var_DeleteCell, var_Save, var_btnCalendar, var_Day;

	String store_UserID, store_CorpID, store_UserName, show_start_date,
			show_end_Date, show_BERStatus, show_WeekDate,
			var_store_val_from_web_service;
	String var_BERListLocTo;

	private CheckNetworkConnection cd;
	private Boolean isInternetPresent = false;

	Spinner var_ShowRateInSpinner;

	EditText var_ExpensesAmt, var_editTextPurpose, var_editTextNote;

	TableLayout var_MainTable;
	int count = 0;
	int expense_reportId = 0;
	LinearLayout var_MainButton;

	TableRow var_ShowData;
	ImageView var_StoreCameraImage;
	final Context context = this;

	private Bitmap mBitmapCamera;
	private Bitmap resized;
	private String EncodedBase64;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.input_form_activity);

		initialize_user_interface();

		cd = new CheckNetworkConnection(getApplicationContext());
		isInternetPresent = cd.isConnectingToInternet();

		// if (show_BERStatus.equalsIgnoreCase("1")) {

		// } else {
		// System.out.println("Hay Ram");
		// }

		Bundle bd = new Bundle();

		// store_result = new ArrayList<HashMap<String, String>>();
		store_UserID = getIntent().getExtras().getString("UserID");
		store_CorpID = getIntent().getExtras().getString("CorpID");
		store_UserName = getIntent().getExtras().getString("UserName");

		show_start_date = getIntent().getExtras().getString("StartDate");
		show_end_Date = getIntent().getExtras().getString("EndDate");

		show_BERStatus = getIntent().getExtras().getString("BERStatus");
		show_WeekDate = getIntent().getExtras().getString("WeekDate");
		Log.i("sirshendu", store_UserID + "" + show_start_date);

		load_data_from_url();
		// var_ShowLocationTo.setText(var_BERListLocTo);

		findViewById(R.id.LocationForm).setOnClickListener(this);
		findViewById(R.id.LocationTo).setOnClickListener(this);
		findViewById(R.id.SelectTask).setOnClickListener(this);
		findViewById(R.id.AddCell).setOnClickListener(this);
		findViewById(R.id.Attachment).setOnClickListener(this);
		findViewById(R.id.SaveButton).setOnClickListener(this);
		findViewById(R.id.leftimageArrow).setOnClickListener(this);
		findViewById(R.id.RightArrowimage).setOnClickListener(this);
		findViewById(R.id.btnCalendar).setOnClickListener(this);
		findViewById(R.id.btnDay).setOnClickListener(this);

	}

	private void initialize_user_interface() {

		var_ShowRateInSpinner = (Spinner) findViewById(R.id.ShowRateInSpinner);
		var_AllowanceRate = (TextView) findViewById(R.id.AllowanceRate);

		var_ExpensesAmt = (EditText) findViewById(R.id.ExpensesAmt);
		var_DeleteCell = (Button) findViewById(R.id.DeleteCell);

		var_ShowExpType = (TextView) findViewById(R.id.ShowExpType);
		var_ShowExpAllowence = (TextView) findViewById(R.id.ShowExpAllowence);
		var_ShowExpAmount = (TextView) findViewById(R.id.ShowExpAmount);

		var_editTextPurpose = (EditText) findViewById(R.id.editTextPurpose);
		var_editTextNote = (EditText) findViewById(R.id.editTextNote);

		var_MainTable = (TableLayout) findViewById(R.id.MainTable);
		var_MainButton = (LinearLayout) findViewById(R.id.MainButton);

		var_LocationForm = (TextView) findViewById(R.id.ShowLocationForm);
		var_ShowDateDay = (TextView) findViewById(R.id.ShowDateDay);
		var_ShowSelectTask = (TextView) findViewById(R.id.ShowSelectTask);
		var_ShowLocationTo = (TextView) findViewById(R.id.ShowLocationTo);
		var_ShowData = (TableRow) findViewById(R.id.ShowData);

		var_StoreCameraImage = (ImageView) findViewById(R.id.StoreCameraImage);
	}

	private void load_data_from_url() {
		isInternetPresent = cd.isConnectingToInternet();
		if (isInternetPresent) {

			fetch_data_asynchronously task1 = new fetch_data_asynchronously();
			task1.execute(new String[] { "" });
		} else {
			Toast.makeText(NewInputForm.this, "Check Your Internet Connection",
					Toast.LENGTH_SHORT).show();
		}

	}

	/****** Call AsyncTask for threading *************************/

	private class fetch_data_asynchronously extends
			AsyncTask<String, Void, Boolean> {
		ProgressDialog dialog = new ProgressDialog(NewInputForm.this);

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
			if (NewInputForm.this != null && !NewInputForm.this.isFinishing())
				dialog.dismiss();
			store_data_for_url();
		}
	}

	public void view_details_from_url() {

		try {

			SoapObject request = new SoapObject(
					ConnectionURL.NAMESPACE_For_DetailList,
					ConnectionURL.METHOD_NAME_For_DetailList);
			request.addProperty("CorpID", store_CorpID);
			request.addProperty("UserID", store_UserID);
			request.addProperty("WeekDate", show_WeekDate);
			request.addProperty("StartDate", show_start_date);
			request.addProperty("EndDate", show_end_Date);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					ConnectionURL.URL_For_DetailList);
			androidHttpTransport.call(ConnectionURL.SOAP_ACTION_For_DetailList,
					envelope);
			Object result = (Object) envelope.getResponse();
			var_store_val_from_web_service = result.toString();
			// System.out.println("Result :" + var_store_val_from_web_service);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void store_data_for_url() {

		try {

			HashMap<String, String> map = new HashMap<String, String>();

			JSONObject js = new JSONObject(var_store_val_from_web_service);

			String var_UserID = js.getString("UserID");
			map.put("UserID", var_UserID);

			String var_BER = js.getString("BER");

			JSONObject BERDetails = new JSONObject(var_BER);

			String var_Id = BERDetails.getString("ID");
			map.put("ID", var_Id);

			String var_WeekDate = BERDetails.getString("WeekDate");
			map.put("WeekDate", var_WeekDate);

			String var_Status = BERDetails.getString("Status");
			map.put("Status", var_Status);

			String var_Advance = BERDetails.getString("Advance");
			map.put("Advance", var_Advance);
			// result.add(map);

			JSONArray contacts_BERList = BERDetails.getJSONArray("BERList");

			for (int i = 0; i < contacts_BERList.length(); i++) {

				JSONObject show = contacts_BERList.getJSONObject(i);

				String var_BERListExpenseDate = show.getString("ExpenseDate");
				map.put("ExpenseDate", var_BERListExpenseDate);

				String var_BERListDayNo = show.getString("DayNo");
				map.put("DayNo", var_BERListDayNo);

				String var_BERListLocFrom = show.getString("LocFrom");
				map.put("LocFrom", var_BERListLocFrom);

				var_BERListLocTo = show.getString("LocTo");
				map.put("LocTo", var_BERListLocTo);

				String var_BERListTaskID = show.getString("TaskID");
				map.put("TaskID", var_BERListTaskID);

				String var_BERListPurpose = show.getString("Purpose");
				map.put("Purpose", var_BERListPurpose);

				String var_BERListNote = show.getString("Note");
				map.put("Note", var_BERListNote);

				String var_BERListStatus = show.getString("Status");
				map.put("Status", var_BERListStatus);

				String var_BERListLocFromName = show.getString("LocFromName");
				map.put("LocFromName", var_BERListLocFromName);

				String var_BERListLocToName = show.getString("LocToName");
				map.put("LocToName", var_BERListLocToName);
				Log.i("sirshendu", var_BERListLocToName);

				String var_BERListTaskDesc = show.getString("TaskDesc");
				map.put("TaskDesc", var_BERListTaskDesc);

				// result.add(map);

				JSONArray ExpenseList = show.getJSONArray("ExpenseList");
				for (int j = 0; j < ExpenseList.length(); j++) {

					JSONObject show_ExpenseList = ExpenseList.getJSONObject(j);

					String var_ExpenseList_ID = show_ExpenseList
							.getString("ID");
					map.put("ExpenseListID", var_ExpenseList_ID);

					String var_ExpenseList_ExpenseTypeID = show_ExpenseList
							.getString("ExpenseTypeID");
					map.put("ExpenseTypeID", var_ExpenseList_ExpenseTypeID);

					String var_ExpenseList_LocRateID = show_ExpenseList
							.getString("LocRateID");
					map.put("LocRateID", var_ExpenseList_LocRateID);

					String var_ExpenseList_Allowance = show_ExpenseList
							.getString("Allowance");
					map.put("Allowance", var_ExpenseList_Allowance);

					String var_ExpenseList_Expense = show_ExpenseList
							.getString("Expense");
					map.put("Expense", var_ExpenseList_Expense);
					// result.add(map);
				}

				JSONArray show_AttatchmentList = show
						.getJSONArray("AttatchmentList");
				for (int k = 0; k < show_AttatchmentList.length(); k++) {

					JSONObject show_Attatchment = show_AttatchmentList
							.getJSONObject(k);
					String show_image_id = show_Attatchment.getString("ID");
					map.put("ImageID", show_image_id);
					String show_ImageCode = show_Attatchment
							.getString("ImageCode");

					map.put("ImageCode", show_ImageCode);
					// result.add(map);
				}

				/* result.add(map); */
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
