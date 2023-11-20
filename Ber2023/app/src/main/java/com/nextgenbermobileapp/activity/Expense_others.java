package com.nextgenbermobileapp.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.nextgenbermobileapp.R;
import com.nextgenbermobileapp.adapter.MySimpleArrayAdapter_other;
import com.nextgenbermobileapp.connectioninfo.ConnectionURL;

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
import android.os.Handler;
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

public class Expense_others extends Activity implements TextWatcher {

	String var_store_val_from_web_service;

	LinearLayout lv;

	EditText et;

	MySimpleArrayAdapter_other adapters;

	List<String> perdiem_name = new ArrayList<String>();
	List<String> perdiem_id = new ArrayList<String>();
	List<String> perdiem_allownace = new ArrayList<String>();

	ArrayList<HashMap<String, String>> attachmentList;

	List<HashMap<String, String>> expenseList;

	private String date;

	private String store_loc_to_string;

	private String CorpID;

	public static final String ValueStore = "Collection_Data";
	View row;

	private String ett;

	private String get_url;

	private String expStr;

	private String str;

	private String show_BERStatus;

	private TextView expense_type;

	private TextView expense_reset;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.other_expense);

		// attachmentList = (ArrayList<HashMap<String, String>>) getIntent()
		// .getSerializableExtra("AttachmentList");

		SharedPreferences example = getSharedPreferences(ValueStore, 0);
		date = example.getString("Date", "");
		show_BERStatus = example.getString("BERStatus", "");
		store_loc_to_string = example.getString("store_loc_to", "");
		CorpID = example.getString("CorpID", "");
		get_url = example.getString("URL", "");

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
							Expense_others.this);

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
									Intent i = new Intent(Expense_others.this,
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
					Intent i = new Intent(Expense_others.this,
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
					View row = lv.getChildAt(i);
					try {
						String expense = ((EditText) row
								.findViewById(R.id.meal_expense)).getText()
								.toString().trim();
						// ==============change=================================
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

						// ===========change=========================
						bundle.putSerializable("other_" + i, expenseMap);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				// bundle.putSerializable("AttachmentList", attachmentList);
				Intent i = new Intent(Expense_others.this, ExpenseSummary.class);
				i.putExtras(bundle);
				startActivity(i);
			}
		});
	}

	/******** async task for retrive data ***************/

	private class verify_save_data_asynchronously extends
			AsyncTask<Void, Void, Void> {
		ProgressDialog dialog = new ProgressDialog(Expense_others.this);
		private Handler handler;

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
			store_data_for_login();

			for (int i = 0; i < perdiem_name.size(); i++) {
				createRow(i);
			}

			expenseList = (List<HashMap<String, String>>) getIntent()
					.getSerializableExtra("expenseList");

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
											.findViewById(R.id.meal_expense);
									final String val = map.get("Expense");
									et.setText(val);
									// et.post(new Runnable() {
									//
									// @Override
									// public void run() {
									// et.setText(val);
									// }
									// });
								}
							}
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				}
			};
			handler.postDelayed(r, 500);
			// for (int i = 0; i < lv.getChildCount(); i++) {
			// View row = lv.getChildAt(i);
			//
			// for (HashMap<String, String> map : expenseList) {
			// if (map.get("ExpenseTypeID").equals(perdiem_id.get(i))) {
			// final EditText et = (EditText)
			// row.findViewById(R.id.tolls_parking);
			// final String val = map.get("Expense");
			// et.setText(val);
			// // et.post(new Runnable() {
			// //
			// // @Override
			// // public void run() {
			// // et.setText(val);
			// // }
			// // });
			// }
			// }
			// }
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

				if (expenses_RateType.equals("Other")) {

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

	@Override
	public void afterTextChanged(Editable s) {
		float total = 0;

		for (int i = 0; i < lv.getChildCount(); i++) {
			View row = lv.getChildAt(i);
			try {
				// String expense = ((EditText) row
				// .findViewById(R.id.meal_expense)).getText().toString()
				// .trim();
				//
				// ett = ((EditText) row.findViewById(R.id.meal_allowance))
				// .getText().toString().trim();
				// EditText exp = (EditText)
				// row.findViewById(R.id.meal_expense);
				// if (Float.valueOf(ett) > 0) {
				// try {
				//
				// if (Double.parseDouble(expense) > Double
				// .parseDouble(ett)) {
				//
				// exp.setTextColor(Color.RED);
				// } else {
				// exp.setTextColor(Color.BLACK);
				// }
				// } catch (Exception e) {
				// // TODO: handle exception
				// e.printStackTrace();
				// }
				// }
				//
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
			allowance.setText((perdiem_allownace.get(position)));
		} catch (Exception e) {
			// handle exception
		}

		lv.addView(v);
	}
}