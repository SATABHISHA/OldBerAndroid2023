package com.nextgenbermobileapp.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.nextgenbermobileapp.R;
import com.nextgenbermobileapp.adapter.MySimpleArrayAdapter_summary;
import com.nextgenbermobileapp.adapter.MySimpleArrayAdapter_travel;
import com.nextgenbermobileapp.connectioninfo.CheckNetworkConnection;
import com.nextgenbermobileapp.connectioninfo.ConnectionURL;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Direct_summary extends Activity implements TextWatcher {

	private String var_store_val_from_web_service;

	private String store_CorpID;

	private String store_UserID;

	private String show_WeekDate;

	public static final String ValueStore = "Collection_Data";

	private CheckNetworkConnection cd;
	private Boolean isInternetPresent = false;

	private String store_UserName;

	private String store_password;

	private String store_UserRole;

	private String show_start_date;

	private String show_BERStatus;

	private String show_Store_Date;

	private String show_Store_Day;

	private ArrayList<HashMap<String, String>> save_result;

	private int expense_count;

	private ArrayList<HashMap<String, String>> attachmentlist;

	private String show_end_Date;
	MySimpleArrayAdapter_summary adapters;

	List<String> perdiem_name = new ArrayList<String>();
	List<String> perdiem_id = new ArrayList<String>();

	private LinearLayout lv;

	private View row;

	private ArrayList<HashMap<String, String>> expenseList;

	private Handler handler;

	private EditText et;

	private String view_data;

	private String var_BERListLocTo;

	private String var_BERListLocToName;

	private String get_url;

	private JSONArray var_Attachment;

	private byte[] bitmap1, bitmap2, bitmap3, bitmap4;

	private String date;

	private String store_loc_to_string;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.expense_summary_direct);

		SharedPreferences example = getSharedPreferences(ValueStore, 0);
		show_WeekDate = example.getString("WeekDate", "");
		date = example.getString("Date", "");
		get_url = example.getString("URL", "");
		store_loc_to_string = example.getString("store_loc_to", "");

		lv = (LinearLayout) findViewById(R.id.listView1);
		et = (EditText) findViewById(R.id.editText1);

		TextView store_loc_to_textview = (TextView) findViewById(R.id.tolocation);
		store_loc_to_textview.setText(store_loc_to_string);

		cd = new CheckNetworkConnection(getApplicationContext());
		isInternetPresent = cd.isConnectingToInternet();

		store_UserID = getIntent().getExtras().getString("UserID");
		store_CorpID = getIntent().getExtras().getString("CorpID");
		store_UserName = getIntent().getExtras().getString("UserName");
		store_password = getIntent().getExtras().getString("Password");
		store_UserRole = getIntent().getExtras().getString("UserRole");
		show_start_date = getIntent().getExtras().getString("StartDate");
		show_end_Date = getIntent().getExtras().getString("EndDate");
		show_BERStatus = getIntent().getExtras().getString("BERStatus");
		show_WeekDate = getIntent().getExtras().getString("WeekDate");
		show_Store_Date = getIntent().getExtras().getString("StoreDate");
		show_Store_Day = getIntent().getExtras().getString("StoreDay");

		view_data = show_Store_Date + " " + "(" + show_Store_Day + ")";

		TextView Date = (TextView) findViewById(R.id.ShowDateDay);
		Date.setText(date);

		load_data_from_url();

		findViewById(R.id.back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});

		findViewById(R.id.receipt).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Intent attachmentIntent = new Intent(getApplicationContext(),
				// Showimage_Direct.class);
				Intent attachmentIntent = new Intent(getApplicationContext(),
						ShowImage.class);
				attachmentIntent.putExtra("AttachmentList", attachmentlist);
				attachmentIntent.putExtra("Flag_button_disable", "yes");
				// startActivityForResult(attachmentIntent, 4);
				startActivity(attachmentIntent);

			}
		});

		findViewById(R.id.calender).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (show_BERStatus.equals("0") || show_BERStatus.equals("1")
						|| show_BERStatus.equals("3")) {
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(
							Direct_summary.this);

					// Setting Dialog Title
					alertDialog.setTitle("Info...");

					// Setting Dialog Message
					alertDialog.setMessage("Move to calendar without saving ?");

					// Setting Icon to Dialog
					alertDialog.setIcon(R.drawable.delete);

					// Setting Positive "Yes" Button
					alertDialog.setPositiveButton("YES",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {

									// Write your code here to invoke YES
									// event"You clicked on YES",
									// Toast.LENGTH_SHORT).show();
									Intent i = new Intent(Direct_summary.this,
											ViewCalendar.class);
									i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									startActivity(i);
									finish();
								}
							});

					// Setting Negative "NO" Button
					alertDialog.setNegativeButton("NO",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// Write your code here to invoke NO event
									// "You clicked on NO",
									// Toast.LENGTH_SHORT).show();
									dialog.cancel();
								}
							});

					// Showing Alert Message
					alertDialog.show();
				} else {
					Intent i = new Intent(Direct_summary.this,
							ViewCalendar.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
					finish();
				}

			}
		});
	}

	private void load_data_from_url() {
		isInternetPresent = cd.isConnectingToInternet();
		if (isInternetPresent) {
			fetch_data_asynchronously task1 = new fetch_data_asynchronously();
			task1.execute();
		} else {
			Toast.makeText(this, "Check Your Internet Connection",
					Toast.LENGTH_SHORT).show();
		}
	}

	/****** Call AsyncTask for threading *************************/

	private class fetch_data_asynchronously extends
			AsyncTask<Void, Void, String> {
		ProgressDialog dialog = new ProgressDialog(Direct_summary.this);

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
			store_data_for_url(result);
			// showResult();
			// TextView store_loc_to_textview = (TextView)
			// findViewById(R.id.tolocation);
			// store_loc_to_textview.setText(var_BERListLocToName);

			verify_save_data_asynchronously task1 = new verify_save_data_asynchronously();
			task1.execute();
		}
	}

	public String view_details_from_url2() {
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
			HttpTransportSE androidHttpTransport = new HttpTransportSE(get_url
					+ ConnectionURL.URL_For_DetailList);
			androidHttpTransport.call(ConnectionURL.SOAP_ACTION_For_DetailList,
					envelope);
			Object result = (Object) envelope.getResponse();
			return result.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public void store_data_for_url(String result) {
		try {
			save_result = new ArrayList<HashMap<String, String>>();
			HashMap<String, String> map = new HashMap<String, String>();

			JSONObject js = new JSONObject(result);

			String var_UserID = js.getString("UserID");
			map.put("UserID", var_UserID);

			JSONObject BERDetails = js.getJSONObject("BER");

			JSONArray contacts_BERList = BERDetails.getJSONArray("BERList");

			String var_Id = BERDetails.getString("ID");
			map.put("ID", var_Id);

			String var_WeekDate = BERDetails.getString("WeekDate");
			map.put("WeekDate", var_WeekDate);

			String var_Status = BERDetails.getString("Status");
			map.put("Status", var_Status);

			String var_Advance = BERDetails.getString("Advance");
			map.put("Advance", var_Advance);
			save_result.add(map);

			String[] separated = show_Store_Date.split("-");
			String first = separated[0];
			String second = separated[1];
			String third = separated[2];

			// if (Integer.parseInt(second) <= 9) {
			// String secone = "0".concat(second);
			// show_Store_Date = first.concat("-").concat(secone).concat("-")
			// .concat(third);
			// }

			for (int i = 0; i < contacts_BERList.length(); i++) {
				JSONObject show = contacts_BERList.getJSONObject(i);

				String var_BERListExpenseDate = show.getString("ExpenseDate");
				Log.d("expense date", var_BERListExpenseDate);
				Log.d("show_Store_Date", show_Store_Date);
				if (!show_Store_Date.equalsIgnoreCase(var_BERListExpenseDate
						.replaceAll("/", "-")))
					continue;

				map = new HashMap<String, String>();
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

				var_BERListLocToName = show.getString("LocToName");
				map.put("LocToName", var_BERListLocToName);

				String var_BERListTaskDesc = show.getString("TaskDesc");
				map.put("TaskDesc", var_BERListTaskDesc);

				save_result.add(map);

				JSONArray ExpenseList = show.getJSONArray("ExpenseList");

				Log.i("sirsho expense", ExpenseList + "");
				expense_count = ExpenseList.length();
				expenseList = new ArrayList<HashMap<String, String>>();
				for (int j = 0; j < ExpenseList.length(); j++) {
					map = new HashMap<String, String>();
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
					save_result.add(map);
					expenseList.add(map);
				}

				attachmentlist = new ArrayList<HashMap<String, String>>();

				JSONArray show_AttatchmentList = show
						.getJSONArray("AttatchmentList");
				for (int j = 0; j < show_AttatchmentList.length(); j++) {
					JSONObject show_Attatchment = show_AttatchmentList
							.getJSONObject(j);
					map = new HashMap<String, String>();
					map.put("ImageID", show_Attatchment.getString("ID"));
					map.put("ImageCode",
							show_Attatchment.getString("ImageCode"));
					map.put("ImageURL", show_Attatchment.getString("ImageName"));
					map.put("ImageSize", show_Attatchment.getString("Size"));
					map.put("ImageStatus",
							show_Attatchment.getString("DeleteYN"));
					// save_result.add(map);
					attachmentlist.add(map);
				}

				try {

					var_Attachment = new JSONArray();

					for (int k = 0; k < attachmentlist.size(); k++) {
						Map<String, String> attachment = attachmentlist.get(i);
						String var_imagecode = attachment.get("ImageCode");

						if (var_imagecode != null) {
							try {
								byte[] decodedString = Base64.decode(
										var_imagecode, Base64.DEFAULT);
								int index = k;
								switch (i) {
								case 0:
									bitmap1 = decodedString;
									break;
								case 1:
									bitmap2 = decodedString;
									break;
								case 2:
									bitmap3 = decodedString;
									break;
								case 3:
									bitmap4 = decodedString;
									break;
								}

								JSONObject attachmentObject = new JSONObject();
								attachmentObject.put("ID", "0");
								attachmentObject.put("ExpenseDate",
										show_start_date);
								attachmentObject.put("ImageURL", "recipt.png");
								attachmentObject
										.put("ImageCode", var_imagecode);
								var_Attachment.put(attachmentObject);
							} catch (IllegalArgumentException e) {
								Log.e("image", e.getMessage());
							}
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				Log.d("save_result", save_result + "");
				/* result.add(map); */
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/******** async task for retrive data ***************/

	private class verify_save_data_asynchronously extends
			AsyncTask<Void, Void, Void> {
		ProgressDialog dialog = new ProgressDialog(Direct_summary.this);

		@Override
		protected void onPreExecute() {
			dialog.setMessage("Please wait...");
			dialog.setCancelable(true);
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			view_save_url();
			return null;
		}

		@Override
		protected void onPostExecute(Void param) {
			dialog.dismiss();
			store_data_for_login();

			// adapters = new MySimpleArrayAdapter_summary(Direct_summary.this,
			// perdiem_name, perdiem_id);
			// adapters.setTextWatcher(Direct_summary.this);
			// lv.setAdapter(adapters);

			for (int i = 0; i < perdiem_name.size(); i++)
				createRow(i);

			handler = new Handler();
			Runnable r = new Runnable() {
				public void run() {

					for (int i = 0; i < lv.getChildCount(); i++) {
						View row = lv.getChildAt(i);

						try {

							for (HashMap<String, String> map : expenseList) {
								if (map.get("ExpenseTypeID").equals(
										perdiem_id.get(i))) {
									final EditText et = (EditText) row
											.findViewById(R.id.tolls_parking);
									final String val = map.get("Expense");
									et.setText(val);
								}
							}
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				}
			};
			handler.postDelayed(r, 200);
		}
	}

	public void view_save_url() {
		try {
			SoapObject request = new SoapObject(
					ConnectionURL.NAMESPACE_For_GroupWiseExpenseTypeList,
					ConnectionURL.METHOD_NAME_For_GroupWiseExpenseTypeList);
			request.addProperty("CorpID", store_CorpID);
			request.addProperty("WeekDate", show_WeekDate);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(get_url
					+ ConnectionURL.URL_For_SaveList);
			androidHttpTransport.call(
					ConnectionURL.SOAP_ACTION_For_GroupWiseExpenseTypeList,
					envelope);
			Object result = (Object) envelope.getResponse();

			var_store_val_from_web_service = result.toString();
			System.out.println("Print Result :"
					+ var_store_val_from_web_service);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void store_data_for_login() {
		try {
			JSONObject js = new JSONObject(var_store_val_from_web_service);
			String var_LocationID = js.getString("LocationID");
			System.out.println(var_LocationID);

			// Getting Array of Contacts
			JSONArray contacts_List = js.getJSONArray("List");
			for (int i = 0; i < contacts_List.length(); i++) {

				JSONObject c = contacts_List.getJSONObject(i);
				String expenses_RateType = c.getString("RateCategory");

				// if (expenses_RateType.equals("Travel")
				// || expenses_RateType.equals("Personal Auto")) {

				JSONArray contacts_List_one = c.getJSONArray("List");
				Log.i("storedataone", contacts_List_one + "");
				for (int j = 0; j < contacts_List_one.length(); j++) {

					JSONObject d = contacts_List_one.getJSONObject(j);

					String expenses_Id = d.getString("ID");
					String expenses_Name = d.getString("Name");
					String expenses_CostTypeID = d.getString("CostTypeID");
					String expenses_CostType = d.getString("CostType");
					String expenses_RateType_inner = d.getString("RateType");
					String expenses_RateEntryText = d
							.getString("RateEntryText");
					String expenses_UseRate = d.getString("UseRate");

					Log.i("storedata", expenses_Id + "   " + expenses_Name);

					perdiem_name.add(expenses_Name);
					perdiem_id.add(expenses_Id);

				}
			}
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub

		float total = 0;

		for (int i = 0; i < lv.getChildCount(); i++) {
			View row = lv.getChildAt(i);
			try {

				String tollsParking = ((EditText) row
						.findViewById(R.id.tolls_parking)).getText().toString()
						.trim();
				if (tollsParking.length() > 0)
					total += Float.parseFloat(tollsParking);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		et.setText(String.format("%.2f", total));

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

	}

	// TODO add this function
	private void createRow(int position) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v;

		v = inflater.inflate(R.layout.other_expense_row, lv, false);
		((EditText) v.findViewById(R.id.tolls_parking))
				.addTextChangedListener(this);

		try {
			TextView projectic = (TextView) v.findViewById(R.id.textView6);
			EditText allowance = (EditText) v.findViewById(R.id.tolls_parking);
			allowance.setFocusable(false);
			projectic.setText((perdiem_name.get(position)));
			// allowance.setText((perdiem_name.get(position)));
		} catch (Exception e) {
			// handle exception
		}

		lv.addView(v);
	}

}
