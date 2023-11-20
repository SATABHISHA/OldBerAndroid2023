package com.nextgenbermobileapp.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.nextgenbermobileapp.R;
import com.nextgenbermobileapp.adapter.MySimpleArrayAdapter;
import com.nextgenbermobileapp.connectioninfo.ConnectionURL;
import com.nextgenbermobileapp.info.LocationDetailsInfo;
import com.nextgenbermobileapp.info.LocationInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class Expense_typte extends Activity implements TextWatcher {

	String var_store_val_from_web_service;

	LinearLayout lv;

	EditText et;

	MySimpleArrayAdapter adapters;

	List<String> perdiem_name = new ArrayList<String>();
	List<String> perdiem_id = new ArrayList<String>();

	String store_UserID, store_CorpID, store_UserName, store_password,
			store_UserRole, store_WeekDate, show_Store_Date, show_Store_Day,
			show_check_form, show_check_form_locto, store_loc_form,
			show_start_date, show_end_Date, show_WeekDate, show_BERStatus;

	ArrayList<LocationDetailsInfo> store_data, check_data;
	ArrayList<LocationInfo> store_loc_info;

	String store_loc_to;

	ArrayList<HashMap<String, String>> result;
	ArrayList<HashMap<String, String>> attachmentList;

	private String date;

	public static final String ValueStore = "Collection_Data";

	View row;

	private String store_loc_to_string;

	private List<HashMap<String, String>> expenseList;

	private String CorpID;

	private String get_url;

	private String expStr;

	private String str;

	private TextView expense_type;

	private TextView expense_reset;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.perdiem_expense);

		// attachmentList = (ArrayList<HashMap<String, String>>) getIntent()
		// .getSerializableExtra("AttachmentList");

		SharedPreferences example = getSharedPreferences(ValueStore, 0);
		date = example.getString("Date", "");
		store_loc_to_string = example.getString("store_loc_to", "");
		CorpID = example.getString("CorpID", "");
		get_url = example.getString("URL", "");
		show_BERStatus = example.getString("BERStatus", "");

		TextView Date = (TextView) findViewById(R.id.ShowDateDay);
		Date.setText(date);
		
		expense_type = (TextView) findViewById(R.id.Exptype);
		expense_reset = (TextView) findViewById(R.id.textView12);

		TextView store_loc_to_textview = (TextView) findViewById(R.id.tolocation);
		store_loc_to_textview.setText(store_loc_to_string);

		lv = (LinearLayout) findViewById(R.id.listView1);

		et = (EditText) findViewById(R.id.editText1);

		verify_save_data_asynchronously task1 = new verify_save_data_asynchronously();
		task1.execute();

		findViewById(R.id.back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});

		findViewById(R.id.calender).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (show_BERStatus.equals("0") || show_BERStatus.equals("1")
						|| show_BERStatus.equals("3")) {
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(
							Expense_typte.this);

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
									Intent i = new Intent(Expense_typte.this,
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
					Intent i = new Intent(Expense_typte.this,
							ViewCalendar.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
					finish();
				}

			}
		});

		findViewById(R.id.textView12).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for (int i = 0; i < lv.getChildCount(); i++) {
					row = lv.getChildAt(i);

					((EditText) row.findViewById(R.id.meal_expense))
							.setText("");

				}
			}
		});
		findViewById(R.id.next).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle(getIntent().getExtras());

				for (int i = 0; i < lv.getChildCount(); i++) {
					row = lv.getChildAt(i);
					try {
						String expense = ((EditText) row
								.findViewById(R.id.meal_expense)).getText()
								.toString().trim();
						// ========================change=================
						String meal_allowance = ((EditText) row
								.findViewById(R.id.meal_allowance)).getText()
								.toString().trim();

						HashMap<String, String> expenseMap = new HashMap<String, String>();
						expenseMap.put("id", perdiem_id.get(i));
						expenseMap.put("label", ((TextView) row
								.findViewById(R.id.perdiem_name)).getText()
								.toString());
						expenseMap.put("value", expense.length() > 0 ? expense
								: "0");

						expenseMap.put("value_meal_allowance", meal_allowance
								.length() > 0 ? meal_allowance : "0");
						expenseMap.put("ChargableQty", "0");
						// ==========================change============
						bundle.putSerializable("perdiem_" + i, expenseMap);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				// bundle.putSerializable("AttachmentList", attachmentList);
				Intent i = new Intent(Expense_typte.this, Expense_others.class);
				i.putExtras(bundle);
				startActivity(i);
			}
		});

		store_data = new ArrayList<LocationDetailsInfo>();
		store_loc_info = new ArrayList<LocationInfo>();
		check_data = new ArrayList<LocationDetailsInfo>();
		result = new ArrayList<HashMap<String, String>>();

		store_loc_to = getIntent().getStringExtra("Loc_To");

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
	}

	/******** async task for retrive data ***************/

	private class verify_save_data_asynchronously extends
			AsyncTask<Void, Void, Void> {
		ProgressDialog dialog = new ProgressDialog(Expense_typte.this);

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

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(Void param) {
			dialog.dismiss();
			// Log.d("response", var_store_val_from_web_service);
			//
			// if (var_store_val_from_web_service.equalsIgnoreCase("-1"))
			// Toast.makeText(InputForm.this, "Error Occured",
			// Toast.LENGTH_SHORT).show();
			// else if (var_store_val_from_web_service.equalsIgnoreCase("0"))
			// Toast.makeText(InputForm.this, "Error Occured",
			// Toast.LENGTH_SHORT).show();
			// else
			// Toast.makeText(InputForm.this, "Data Saved", Toast.LENGTH_SHORT)
			// .show();
			store_data_for_login1();

			// adapters = new MySimpleArrayAdapter(Expense_typte.this,
			// perdiem_name, perdiem_id);
			// adapters.setTextWatcher(Expense_typte.this);
			// lv.setAdapter(adapters);
			for (int i = 0; i < perdiem_name.size(); i++)
				createRow(i);
		}
	}

	public void view_save_url() {

		try {

			SoapObject request = new SoapObject(
					ConnectionURL.NAMESPACE_For_GroupWiseExpenseTypeList,
					ConnectionURL.METHOD_NAME_For_GroupWiseExpenseTypeList);
			request.addProperty("CorpID", CorpID);

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

	/******* call asynctask for location list ******************/

	private class verify_user_login_asynchronously extends
			AsyncTask<String, Void, Boolean> {
		ProgressDialog dialog = new ProgressDialog(Expense_typte.this);

		@Override
		protected void onPreExecute() {
			dialog.setMessage("Please wait...");
			dialog.setCancelable(true);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			view_details_from_url();
			return null;
		}

		@Override
		protected void onPostExecute(Boolean is_authenticated) {
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
			HttpTransportSE androidHttpTransport = new HttpTransportSE(get_url
					+ ConnectionURL.URL_For_LocationList);
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

		for (int j = 0; j < check_data.size(); j++) {
			String show_id = check_data.get(j).getID();

			if (store_loc_to.equalsIgnoreCase(show_id)) {

				String show_LocationID = store_loc_info.get(j).getLoc_ID();
				String show_ExpenseTypeID = store_loc_info.get(j)
						.getExpenseTypeID();
				String show_StartDate = store_loc_info.get(j).getStartDate();
				String show_EndDate = store_loc_info.get(j).getStartDate();
				String show_Rate = store_loc_info.get(j).getRate();

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

		Log.d("result", result + "");
	}

	private void store_data_for_login1() {

		try {

			JSONObject js = new JSONObject(var_store_val_from_web_service);
			String var_LocationID = js.getString("LocationID");
			System.out.println(var_LocationID);

			// Getting Array of Contacts
			JSONArray contacts_List = js.getJSONArray("List");
			for (int i = 0; i < contacts_List.length(); i++) {

				JSONObject c = contacts_List.getJSONObject(i);
				String expenses_RateType = c.getString("RateCategory");

				if (expenses_RateType.equals("Meal & Loging")) {
					
					expense_type.setText(expenses_RateType);
					
					expense_reset.setText(expense_reset.getText().toString()
							.replace("This", expenses_RateType));

					JSONArray contacts_List_one = c.getJSONArray("List");
					Log.i("storedataone", contacts_List_one + "");
					for (int j = 0; j < contacts_List_one.length(); j++) {

						JSONObject d = contacts_List_one.getJSONObject(j);

						String expenses_Id = d.getString("ID");
						String expenses_Name = d.getString("Name");
						String expenses_CostTypeID = d.getString("CostTypeID");
						String expenses_CostType = d.getString("CostType");
						String expenses_RateType_inner = d
								.getString("RateType");
						String expenses_RateEntryText = d
								.getString("RateEntryText");
						String expenses_UseRate = d.getString("UseRate");

						Log.i("storedata", expenses_Id + "   " + expenses_Name);

						perdiem_name.add(expenses_Name);
						perdiem_id.add(expenses_Id);
					}
				}

			}

			verify_user_login_asynchronously task2 = new verify_user_login_asynchronously();
			task2.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
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

				if (!var_ID.equals(store_loc_to))
					continue;

				LocationDetailsInfo var_store_loc_info = new LocationDetailsInfo(
						var_ID, var_Name, var_CountryID, var_StateID);
				Log.d("loc_current", var_Name);

				store_data.add(var_store_loc_info);

				if (c.isNull("LocationRate"))
					continue;

				JSONArray contacts_LocationRate = c
						.getJSONArray("LocationRate");

				Log.d("show", contacts_LocationRate.toString());

				for (int j = 0; j < lv.getChildCount(); j++) {
					try {
						String id = perdiem_id.get(j);

						for (int k = 0; k < contacts_LocationRate.length(); k++) {
							final JSONObject show = contacts_LocationRate
									.getJSONObject(k);
							if (show.getString("ExpenseTypeID").equals(id)) {
								final EditText text = (EditText) lv.getChildAt(
										j).findViewById(R.id.meal_allowance);
								text.post(new Runnable() {

									@Override
									public void run() {
										try {
											text.setText(show.getString("Rate"));
										} catch (JSONException e) {
											e.printStackTrace();
										}
									}
								});
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			expenseList = (List<HashMap<String, String>>) getIntent()
					.getSerializableExtra("expenseList");

			for (int i = 0; i < lv.getChildCount(); i++) {
				row = lv.getChildAt(i);
				for (HashMap<String, String> map : expenseList) {
					if (map.get("ExpenseTypeID").equals(perdiem_id.get(i))) {

						final EditText text = (EditText) lv.getChildAt(i)
								.findViewById(R.id.meal_allowance);
						final EditText et = (EditText) row
								.findViewById(R.id.meal_expense);
						final String val = map.get("Expense");
						et.post(new Runnable() {

							@Override
							public void run() {
								et.setText(val);
							}
						});
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		float total = 0;

		for (int i = 0; i < lv.getChildCount(); i++) {
			row = lv.getChildAt(i);
			try {
				// String expense = ((EditText) row
				// .findViewById(R.id.meal_expense)).getText().toString()
				// .trim();
				//
				// // for (HashMap<String, String> map : expenseList) {
				// // if (map.get("ExpenseTypeID").equals(perdiem_id.get(i))) {
				// String ett = ((EditText)
				// row.findViewById(R.id.meal_allowance))
				// .getText().toString().trim();
				//
				// EditText exp = (EditText)
				// row.findViewById(R.id.meal_expense);
				// // final String val = map.get("Expense");
				//
				// try {
				//
				// if (Double.parseDouble(expense) > Double.parseDouble(ett)) {
				//
				// exp.setTextColor(Color.RED);
				// } else {
				// exp.setTextColor(Color.BLACK);
				// }
				// } catch (Exception e) {
				// // TODO: handle exception
				// e.printStackTrace();
				// }
				// // }
				// // }
				//
				// // String meal_allowance = ((EditText) row
				// // .findViewById(R.id.meal_allowance)).getText()
				// // .toString().trim();
				// // if (expense.length() > 0) {
				// // if (Integer.parseInt(expense) <= Integer
				// // .parseInt(meal_allowance)) {
				// // ((EditText) row.findViewById(R.id.meal_allowance))
				// // .setTextColor(Color.RED);
				// // }
				// // }
				// if (expense.length() > 0) {
				// total += Float.parseFloat(expense);
				// }

				EditText exp = (EditText) row.findViewById(R.id.meal_expense);
				expStr = exp.getText().toString().trim();

				if (expStr.equals(".")) {
					exp.setText(null);
					return;
				}

				String ett = ((EditText) row.findViewById(R.id.meal_allowance))
						.getText().toString().trim();

				if (expStr.length() > 0) {
					exp.removeTextChangedListener(this);
					int selection = exp.getSelectionStart();

					int pos = 0, count = 0, lastIndex = 0;
					while (pos != -1) {
						pos = (count == 0 && pos == 0) ? expStr.indexOf(".")
								: expStr.indexOf(".", pos + 1);
						if (pos != -1) {
							lastIndex = pos;
							count++;
						}
					}

					if (count > 1) {
						if (selection - 1 >= 0
								&& selection + 1 < expStr.length()) {
							if (!".".equals(expStr.substring(selection - 1,
									selection))
									|| !".".equals(expStr.substring(selection,
											selection + 1))) {
								selection--;
							}
						}

						if (selection <= lastIndex) {
							expStr = expStr.replaceFirst("\\.", "");
						} else {
							String startFragment = expStr.substring(0,
									lastIndex);
							String endFragment = expStr.substring(
									lastIndex + 1, expStr.length());
							expStr = startFragment + endFragment;
							selection--;
						}
					} else if (count == 0 && expStr.length() > 1) {
						String startFragment = expStr.substring(0, selection);
						String endFragment = expStr.substring(selection,
								expStr.length());
						expStr = startFragment + "." + endFragment;
					}

					if (expStr.length() - expStr.indexOf(".") - 1 > 2) {
						expStr = expStr.substring(0, expStr.length() - 1);
					}

					float value = Float.parseFloat(expStr);
					if (value == 0) {
						exp.setText("");
					} else {
						str = String.format(Locale.US, "%.2f", value);
						exp.setText(str);
					}

					int len = str.length();
					exp.setSelection(selection <= len ? selection : len);
					if (ett.length() > 0) {

						if (Float.valueOf(ett) > 0) {
							exp.setTextColor((value > Float.parseFloat(ett)) ? Color.RED
									: Color.BLACK);
						}
					}

					exp.addTextChangedListener(this);
				}

				if (expStr.length() > 0) {
					total += Float.parseFloat(expStr);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		et.setText(String.format("%.2f", total));
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	// TODO add this function
	private void createRow(int position) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v;
		v = inflater.inflate(R.layout.perdiem_expense_row, lv, false);
		((EditText) v.findViewById(R.id.meal_expense))
				.addTextChangedListener(this);
		((EditText) v.findViewById(R.id.meal_allowance))
				.addTextChangedListener(this);

		try {
			TextView projectic = (TextView) v.findViewById(R.id.perdiem_name);
			EditText allowance = (EditText) v.findViewById(R.id.meal_allowance);
			projectic.setText((perdiem_name.get(position)));

			// allowance.setText((perdiem_allownace.get(position)));
		} catch (Exception e) {
			// handle exception
		}

		lv.addView(v);
	}
}