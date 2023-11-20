package com.nextgenbermobileapp.activity;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

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
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
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

import com.nextgenbermobileapp.R;
import com.nextgenbermobileapp.adapter.MySimpleArrayAdapter_travel;
import com.nextgenbermobileapp.connectioninfo.ConnectionURL;

public class Expense_Travel extends Activity implements TextWatcher {

	String var_store_val_from_web_service;

	// TODO change this to linearlayout
	LinearLayout lv;

	EditText et;

	MySimpleArrayAdapter_travel adapters;

	List<String> perdiem_name = new ArrayList<String>();
	List<String> perdiem_id = new ArrayList<String>();
	List<String> perdiem_allownace = new ArrayList<String>();

	JSONArray var_Attachment;

	View row;
	ArrayList<HashMap<String, String>> attachmentList;

	private String date;

	private String store_loc_to_string;

	private String CorpID;

	private String show_WeekDate;

	private float exp;

	private JSONArray contacts_List_one;

	private int personalauto_length;

	private int last;

	private int travel_length;

	private String get_url;

	private String str;

	private String show_BERStatus;

	private TextView expense_type;

	private TextView expense_reset;
	public static final String ValueStore = "Collection_Data";

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.travel);

		SharedPreferences example = getSharedPreferences(ValueStore, 0);
		date = example.getString("Date", "");
		store_loc_to_string = example.getString("store_loc_to", "");
		CorpID = example.getString("CorpID", "");
		show_WeekDate = example.getString("WeekDate", "");
		show_BERStatus = example.getString("BERStatus", "");
		get_url = example.getString("URL", "");

		TextView Date = (TextView) findViewById(R.id.ShowDateDay);
		Date.setText(date);

		expense_type = (TextView) findViewById(R.id.Exptype);

		expense_reset = (TextView) findViewById(R.id.textView12);

		TextView store_loc_to_textview = (TextView) findViewById(R.id.tolocation);
		store_loc_to_textview.setText(store_loc_to_string);

		lv = (LinearLayout) findViewById(R.id.listView1);
		et = (EditText) findViewById(R.id.editText1);

		// attachmentList = (ArrayList<HashMap<String, String>>) getIntent()
		// .getSerializableExtra("AttachmentList");

		verify_save_data_asynchronously task1 = new verify_save_data_asynchronously();
		task1.execute();

		findViewById(R.id.textView12).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				for (int i = 0; i < lv.getChildCount(); i++) {
					row = lv.getChildAt(i);

					if (i == 0) {
						// ((EditText) row.findViewById(R.id.Rates_meals))
						// .setText("");
						((EditText) row.findViewById(R.id.exp_amt)).setText("");
						((EditText) row.findViewById(R.id.mile_travels))
								.setText("");
					} else {
						((EditText) row.findViewById(R.id.meal_expense))
								.setText("");
					}

				}
			}
		});

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
							Expense_Travel.this);

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
									Intent i = new Intent(Expense_Travel.this,
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
					Intent i = new Intent(Expense_Travel.this,
							ViewCalendar.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
					finish();
				}

			}
		});

		findViewById(R.id.next).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle(getIntent().getExtras());

				for (int i = 0; i < lv.getChildCount(); i++) {
					View row = lv.getChildAt(i);
					try {
						if (i == 0) {
							String expAmt = ((EditText) row
									.findViewById(R.id.exp_amt)).getText()
									.toString().trim();

							String miles = ((EditText) row
									.findViewById(R.id.mile_travels)).getText()
									.toString().trim();

							String meal_allowance = ((EditText) row
									.findViewById(R.id.Rates_meals)).getText()
									.toString().trim();

							HashMap<String, String> exp = new HashMap<String, String>();
							exp.put("id", perdiem_id.get(0));
							exp.put("label", ((TextView) row
									.findViewById(R.id.textView6)).getText()
									.toString());
							exp.put("value", expAmt.length() > 0 ? expAmt : "0");
							exp.put("value_exp", expAmt.length() > 0 ? expAmt
									: "0");

							exp.put("value_meal_allowance", meal_allowance
									.length() > 0 ? meal_allowance : "0");
							exp.put("ChargableQty", miles.length() > 0 ? miles
									: "0");
							bundle.putSerializable("travel_0", exp);
						} else {
							String tollsParking = ((EditText) row
									.findViewById(R.id.meal_expense)).getText()
									.toString().trim();
							// ============change==================================
							String meal_allowance = ((EditText) row
									.findViewById(R.id.meal_allowance))
									.getText().toString().trim();

							HashMap<String, String> tolls = new HashMap<String, String>();
							tolls.put("id", perdiem_id.get(i));
							tolls.put("label", ((TextView) row
									.findViewById(R.id.perdiem_name)).getText()
									.toString());
							tolls.put("value",
									tollsParking.length() > 0 ? tollsParking
											: "0");
							tolls.put("value_meal_allowance", meal_allowance
									.length() > 0 ? meal_allowance : "0");
							tolls.put("ChargableQty", "0");

							// ============change==================================
							bundle.putSerializable("travel_" + i, tolls);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				// bundle.putSerializable("AttachmentList", attachmentList);
				Intent i = new Intent(Expense_Travel.this, Expense_typte.class);
				i.putExtras(bundle);
				startActivity(i);
			}
		});
	}

	/******** async task for retrive data ***************/

	private class verify_save_data_asynchronously extends
			AsyncTask<Void, Void, Void> {
		ProgressDialog dialog = new ProgressDialog(Expense_Travel.this);

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
			// TODO disable the adapter and add the for loop
			// adapters = new MySimpleArrayAdapter_travel(Expense_Travel.this,
			// perdiem_name, perdiem_id);
			// adapters.setTextWatcher(Expense_Travel.this);
			// lv.setAdapter(adapters);
			for (int i = 0; i < perdiem_name.size(); i++)
				createRow(i);

			Mileperrate_data_asynchronously task2 = new Mileperrate_data_asynchronously();
			task2.execute();
		}
	}

	// TODO add this function
	private void createRow(int position) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = null;

		if (position == 0) {
			v = inflater.inflate(R.layout.travel_expense_row, lv, false);
			((EditText) v.findViewById(R.id.Rates_meals))
					.setText(perdiem_allownace.get(position));
			((EditText) v.findViewById(R.id.mile_travels))
					.addTextChangedListener(this);
			((EditText) v.findViewById(R.id.exp_amt))
					.addTextChangedListener(this);
		} else {
			v = inflater.inflate(R.layout.perdiem_expense_row, lv, false);
			((EditText) v.findViewById(R.id.meal_expense))
					.addTextChangedListener(this);
			((EditText) v.findViewById(R.id.meal_allowance))
					.addTextChangedListener(this);

		}

		// if (position == 0) {
		// for (position = 0; position <= personalauto_length; position++) {
		// v = inflater.inflate(R.layout.travel_expense_row, lv, false);
		// ((EditText) v.findViewById(R.id.Rates_meals))
		// .setText(perdiem_allownace.get(position));
		// ((EditText) v.findViewById(R.id.mile_travels))
		// .addTextChangedListener(this);
		// ((EditText) v.findViewById(R.id.exp_amt))
		// .addTextChangedListener(this);
		//
		// TextView projectic = (TextView) v.findViewById(R.id.perdiem_name);
		// EditText allowance = (EditText) v.findViewById(R.id.meal_allowance);
		// projectic.setText((perdiem_name.get(position)));
		// allowance.setText((perdiem_allownace.get(position)));
		//
		// last = position;
		// }
		// } else {
		// for (position = last; position <= travel_length; position++) {
		// v = inflater.inflate(R.layout.perdiem_expense_row, lv, false);
		// ((EditText) v.findViewById(R.id.meal_expense))
		// .addTextChangedListener(this);
		// ((EditText) v.findViewById(R.id.meal_allowance))
		// .addTextChangedListener(this);
		//
		// TextView projectic = (TextView) v.findViewById(R.id.perdiem_name);
		// EditText allowance = (EditText) v.findViewById(R.id.meal_allowance);
		// projectic.setText((perdiem_name.get(position)));
		// allowance.setText((perdiem_allownace.get(position)));
		// }
		// }

		try {
			TextView projectic = (TextView) v.findViewById(R.id.perdiem_name);
			EditText allowance = (EditText) v.findViewById(R.id.meal_allowance);
			projectic.setText((perdiem_name.get(position)));
			allowance.setText((perdiem_allownace.get(position)));
		} catch (Exception e) {
			// handle exception
		}

		lv.addView(v);
	}

	public void view_save_url() {
		try {
			SoapObject request = new SoapObject(
					ConnectionURL.NAMESPACE_For_GroupWiseExpenseTypeList,
					ConnectionURL.METHOD_NAME_For_GroupWiseExpenseTypeList);
			request.addProperty("CorpID", CorpID);
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

				// expense_type.setText(expenses_RateType);

				if (expenses_RateType.equals("Transportation")) {
					expense_type.setText(expenses_RateType);

					expense_reset.setText(expense_reset.getText().toString()
							.replace("This", expenses_RateType));

					contacts_List_one = c.getJSONArray("List");
					Log.i("storedataone", contacts_List_one + "");

					travel_length = contacts_List_one.length();
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
						String Allowance = d.getString("Allowance");

						Log.i("storedata", expenses_Id + "   " + expenses_Name);

						perdiem_name.add(expenses_Name);
						perdiem_id.add(expenses_Id);
						perdiem_allownace.add(Allowance);
					}
				}

				if (expenses_RateType.equals("Personal Auto")) {
					contacts_List_one = c.getJSONArray("List");
					Log.i("storedataone1234", contacts_List_one.length() + "");

					personalauto_length = contacts_List_one.length() - 1;
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
						String Allowance = d.getString("Allowance");

						Log.i("storedata", expenses_Id + "   " + expenses_Name);

						perdiem_name.add(expenses_Name);
						perdiem_id.add(expenses_Id);
						perdiem_allownace.add(Allowance);
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/******** async task for mile/hr retrive data ***************/

	private class Mileperrate_data_asynchronously extends
			AsyncTask<Void, Void, Void> {
		ProgressDialog dialog = new ProgressDialog(Expense_Travel.this);

		@Override
		protected void onPreExecute() {
			dialog.setMessage("Please wait...");
			dialog.setCancelable(true);
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {

			try {
				SoapObject request = new SoapObject(
						ConnectionURL.NAMESPACE_For_ratepermileList,
						ConnectionURL.METHOD_NAME_For_ratepermileList);
				request.addProperty("CorpID", CorpID);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);
				HttpTransportSE androidHttpTransport = new HttpTransportSE(
						get_url + ConnectionURL.URL_For_SaveList);
				androidHttpTransport
						.call(ConnectionURL.SOAP_ACTION_For_ratepermileList,
								envelope);
				Object result = (Object) envelope.getResponse();

				var_store_val_from_web_service = result.toString();
				System.out.println("Print Result :"
						+ var_store_val_from_web_service);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(Void param) {
			dialog.dismiss();

			try {
				JSONObject js = new JSONObject(var_store_val_from_web_service);

				String rate_per_mile = js.getString("Rate");
				Log.i("ratepermile", rate_per_mile);

				List<HashMap<String, String>> expenseList = (List<HashMap<String, String>>) getIntent()
						.getSerializableExtra("expenseList");

				for (int i = 0; i < lv.getChildCount(); i++) {
					row = lv.getChildAt(i);

					// if (i == 0) {
					// ((EditText) row.findViewById(R.id.Rates_meals))
					// .setText(rate_per_mile);
					// }

					try {

						for (HashMap<String, String> map : expenseList) {
							if (map.get("ExpenseTypeID").equals(
									perdiem_id.get(i))) {
								((EditText) row
										.findViewById(i == 0 ? R.id.exp_amt
												: R.id.meal_expense))
										.setText(map.get("Expense"));

								((EditText) row
										.findViewById(i == 0 ? R.id.mile_travels
												: R.id.mile_travels))
										.setText(map.get("ChargableQty"));
							}
						}
					} catch (Exception e) {
						// handle exception
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	@Override
	public void afterTextChanged(Editable s) {
		float total = 0;

		for (int i = 0; i < lv.getChildCount(); i++) {
			View row = lv.getChildAt(i);
			try {
				String expStr = null;
				if (i == 0) {
					String ratesMeal = ((EditText) row
							.findViewById(R.id.Rates_meals)).getText()
							.toString().trim();
					String mileTravels = ((EditText) row
							.findViewById(R.id.mile_travels)).getText()
							.toString().trim();
					String exp_amt = ((EditText) row.findViewById(R.id.exp_amt))
							.getText().toString().trim();
					EditText expEt = ((EditText) row.findViewById(R.id.exp_amt));

					if (mileTravels.length() > 0) {

						if (mileTravels.length() > 0) {
							exp = Float.parseFloat(ratesMeal)
									* Float.parseFloat(mileTravels);
						} else {
							exp = Float.parseFloat(exp_amt);

						}
						total += exp;

						expStr = String.format(Locale.US, "%.2f", exp);

						String prevExpStr = expEt.getText().toString();

						if (!expStr.equals(prevExpStr)) {
							expEt.setText(String.format(Locale.US, "%.2f", exp));
						}
					} else {
						expEt.removeTextChangedListener(this);
						expEt.setText("");
						expEt.addTextChangedListener(this);
					}
				} else {
					EditText exp = (EditText) row
							.findViewById(R.id.meal_expense);
					expStr = exp.getText().toString().trim();

					if (expStr.equals(".")) {
						exp.setText(null);
						return;
					}

					String ett = ((EditText) row
							.findViewById(R.id.meal_allowance)).getText()
							.toString().trim();

					if (expStr.length() > 0) {
						exp.removeTextChangedListener(this);
						int selection = exp.getSelectionStart();

						int pos = 0, count = 0, lastIndex = 0;
						while (pos != -1) {
							pos = (count == 0 && pos == 0) ? expStr
									.indexOf(".") : expStr
									.indexOf(".", pos + 1);
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
										|| !".".equals(expStr.substring(
												selection, selection + 1))) {
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
							String startFragment = expStr.substring(0,
									selection);
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

						if (Float.valueOf(ett) > 0) {
							exp.setTextColor((value > Float.parseFloat(ett)) ? Color.RED
									: Color.BLACK);
						}

						exp.addTextChangedListener(this);
					}

					if (expStr.length() > 0) {
						total += Float.parseFloat(expStr);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		et.setText(String.format("%1.2f", total));
	}
}
