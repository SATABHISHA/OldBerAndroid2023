package com.nextgenbermobileapp.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.nextgenbermobileapp.R;
import com.nextgenbermobileapp.adapter.ViewAdapter;
import com.nextgenbermobileapp.adapter.ViewAdapterOne;
import com.nextgenbermobileapp.adapter.ViewAdapterfour;
import com.nextgenbermobileapp.adapter.ViewAdapterthree;
import com.nextgenbermobileapp.adapter.ViewAdaptertwo;
import com.nextgenbermobileapp.connectioninfo.CheckNetworkConnection;
import com.nextgenbermobileapp.connectioninfo.ConnectionURL;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ShowCalendarList extends Activity {

	TextView var_ShowValueofStartDateandEndDate, var_ShowValueofWeekDate;
	ArrayList<String> store_date;
	ArrayList<HashMap<String, String>> songsList;
	ListView var_ShowDataInList;

	String store_UserID, store_CorpID, store_UserName, store_password,
			store_UserRole, show_start_date, show_end_Date, show_WeekDate,
			show_BERStatus, var_store_val_from_web_service, store_Day,
			store_Date;

	CheckNetworkConnection cd;
	private Boolean isInternetPresent = false;

	// Hashmap for ListView
	ArrayList<HashMap<String, String>> result;
	private int expense_count;
	private ArrayList<HashMap<String, String>> attachmentlist;
	private char[] exptotal;
	private int store_start_Date;
	private int store_end_Date;
	private String fetch_date_from_Second;
	private String fetch_date_from_First;
	private String fetch_date_from_startDate;
	private ImageView submit;
	private String get_url;

	public static final String ValueStore = "Collection_Data";

	SimpleDateFormat sm = new SimpleDateFormat("MM-dd-yyyy");
	private Date d;

	@SuppressLint("SimpleDateFormat")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.calender_list_data);

		d = Calendar.getInstance().getTime();

		submit = (ImageView) findViewById(R.id.submit);
		submit.setEnabled(false);

		findViewById(R.id.refresh).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(),
						ShowCalendarList.class);
				startActivity(i);
				finish();
			}
		});

		try {

			SharedPreferences example = getSharedPreferences(ValueStore, 0);
			store_CorpID = example.getString("CorpID", "");
			store_UserID = example.getString("UserID", "");
			show_WeekDate = example.getString("WeekDate", "");
			show_start_date = example.getString("StartDate", "");
			show_end_Date = example.getString("EndDate", "");
			show_BERStatus = example.getString("BERStatus", "");
			get_url = example.getString("URL", "");
			Log.wtf("show_BERStatus", show_BERStatus);

		} catch (Exception e) {
			// TODO: handle exception
		}

		store_date = new ArrayList<String>();
		songsList = new ArrayList<HashMap<String, String>>();
		result = new ArrayList<HashMap<String, String>>();

		var_ShowDataInList = (ListView) findViewById(R.id.ShowDataInList);

		// try {
		//
		// store_UserID = getIntent().getExtras().getString("UserID");
		// store_CorpID = getIntent().getExtras().getString("CorpID");
		// store_UserName = getIntent().getExtras().getString("UserName");
		// store_password = getIntent().getExtras().getString("Password");
		// store_UserRole = getIntent().getExtras().getString("UserRole");
		//
		// show_start_date = getIntent().getExtras().getString("StartDate");
		// show_end_Date = getIntent().getExtras().getString("EndDate");
		//
		// if (show_BERStatus != null) {
		//
		// } else {
		//
		// show_BERStatus = getIntent().getExtras().getString("BERStatus");
		// }
		// show_WeekDate = getIntent().getExtras().getString("WeekDate");
		// } catch (Exception e) {
		//
		// }

		var_ShowValueofStartDateandEndDate = (TextView) findViewById(R.id.ShowValueofStartDateandEndDate);
		var_ShowValueofWeekDate = (TextView) findViewById(R.id.ShowValueofWeekDate);
		var_ShowValueofWeekDate.setText(show_WeekDate);

		String load_left = "(";
		String store_startDate_endDate = load_left.concat(show_start_date)
				.concat(" - ").concat(show_end_Date).concat(")");
		var_ShowValueofStartDateandEndDate.setText(store_startDate_endDate);

		cd = new CheckNetworkConnection(getApplicationContext());

		// fetch_date_from_startDate = show_start_date.substring(3, 5);
		// fetch_date_from_First = show_start_date.substring(0, 2);
		// fetch_date_from_Second = show_start_date.substring(6, 10);
		//
		// String fetch_date_from_EndDate = show_end_Date.substring(3, 5);
		//
		// store_start_Date = Integer.parseInt(fetch_date_from_startDate);
		// store_end_Date = Integer.parseInt(fetch_date_from_EndDate);

		findViewById(R.id.CalendarImage).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						back_to_calendar_screen();
					}
				});

		findViewById(R.id.submit).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				new IdSender().execute();
			}
		});

		if (show_BERStatus.equals("2")) {

			new IdGetter().execute();

		} else if (show_BERStatus.equals("1")) {
			new IdGetter().execute();

		} else {
			new IdGetter().execute();

		}

	}

	public void back_to_calendar_screen() {
		SharedPreferences example1 = getSharedPreferences(ValueStore, 0);
		Editor editor1 = example1.edit();
		editor1.putString("Calender_save_flag", "saved").commit();
		Intent in_next = new Intent(ShowCalendarList.this, ViewCalendar.class);
		Bundle bd = new Bundle();
		bd.putString("UserID", store_UserID);
		bd.putString("CorpID", store_CorpID);
		bd.putString("UserName", store_UserName);
		bd.putString("Password", store_password);
		bd.putString("UserRole", store_UserRole);
		in_next.putExtras(bd);
		in_next.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(in_next);
		// finish();
	}

	private class IdGetter extends AsyncTask<Void, Void, String> {
		ProgressDialog dialog;
		private String test_Date;
		private String s;
		private double var__Expense;
		private double var_Expense;
		private String var_status;

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(ShowCalendarList.this);
			dialog.setMessage("Please wait...");
			dialog.show();
		}

		@Override
		protected String doInBackground(Void... params) {

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
						get_url + ConnectionURL.URL_For_DetailList);
				androidHttpTransport.call(
						ConnectionURL.SOAP_ACTION_For_DetailList, envelope);
				Object result = (Object) envelope.getResponse();
				return result.toString();

			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String resultt) {
			dialog.dismiss();

			try {
				// save_result = new ArrayList<HashMap<String, String>>();
				HashMap<String, String> map = new HashMap<String, String>();

				JSONObject js = new JSONObject(resultt);

				String var_UserID = js.getString("UserID");

				map.put("UserID", var_UserID);

				JSONObject BERDetails = js.getJSONObject("BER");
				var_status = BERDetails.getString("Status");
				if (var_status.equals("0")) {
					show_BERStatus = "0";
				}

				// SharedPreferences example1 = getSharedPreferences(ValueStore,
				// 0);
				// Editor editor1 = example1.edit();
				// editor1.putString("BERStatus", var_status);
				// editor1.commit();

				/*
				 * if we delete all data from total list time instant change of
				 * status====
				 */

				// SharedPreferences example = getSharedPreferences(ValueStore,
				// 0);
				// show_BERStatus = example.getString("BERStatus", "");

				if (show_BERStatus.equals("1")) {
					submit.setImageDrawable(getResources().getDrawable(
							R.drawable.submit_button));
					submit.setEnabled(true);
				}

				JSONArray contacts_BERList = BERDetails.getJSONArray("BERList");

				var_store_val_from_web_service = BERDetails.getString("ID");
				map.put("ID", var_store_val_from_web_service);

				//
				// String var_Status = BERDetails.getString("Status");
				// map.put("Status", var_Status);
				//
				// String var_Advance = BERDetails.getString("Advance");
				// map.put("Advance", var_Advance);
				// save_result.add(map);

				// String[] separated = store_Date.split("-");
				// String first = separated[0];
				// String second = separated[1];
				// String third = separated[2];
				//
				// if (Integer.parseInt(second) <= 9) {
				// String secone = "0".concat(second);
				// store_Date = first.concat("-").concat(secone).concat("-")
				// .concat(third);
				fetch_date_from_startDate = show_start_date.substring(3, 5);
				fetch_date_from_First = show_start_date.substring(0, 2);
				fetch_date_from_Second = show_start_date.substring(6, 10);

				String fetch_date_from_EndDate = show_end_Date.substring(3, 5);

				store_start_Date = Integer.parseInt(fetch_date_from_startDate);
				store_end_Date = Integer.parseInt(fetch_date_from_EndDate);
				// }
				int j = 0;
				while (store_start_Date <= store_end_Date) {
					System.out.print("value of Date : " + store_start_Date);
					String convert_Date = String.valueOf(store_start_Date);
					if (store_start_Date <= 9) {
						test_Date = fetch_date_from_First + '-'
								+ ("0".concat(convert_Date)) + '-'
								+ fetch_date_from_Second;
						store_start_Date++;
					} else {
						test_Date = fetch_date_from_First + '-' + convert_Date
								+ '-' + fetch_date_from_Second;
						store_start_Date++;
					}

					String convert_Date_to_day = test_Date;

					SimpleDateFormat dateFormat = new SimpleDateFormat(
							"MM-dd-yyyy");
					Date convertedDate;

					try {
						convertedDate = dateFormat.parse(convert_Date_to_day);
						s = (String) android.text.format.DateFormat.format(
								"EEEE", convertedDate);
						System.out.println("Check Date :" + s);
						map.put("Day" + j, s);
					} catch (Exception e) {
						e.printStackTrace();
					}

					map.put("Date" + j, test_Date);

					for (int i = 0; i < contacts_BERList.length(); i++) {
						JSONObject show = contacts_BERList.getJSONObject(i);

						String var_BERListExpenseDate = show
								.getString("ExpenseDate");

						// Log.d("show_Store_Date", store_Date);
						if (!convert_Date_to_day
								.equalsIgnoreCase(var_BERListExpenseDate
										.replaceAll("/", "-")))
							continue;
						float total = 0;

						// =======================problem
						// issue====================
						String Total = show.getString("Total");
						map.put("Total" + j, Total);

						JSONArray ExpenseList = show
								.getJSONArray("ExpenseList");
						expense_count = ExpenseList.length();
						ArrayList<HashMap<String, String>> expenseList = new ArrayList<HashMap<String, String>>();
						for (int k = 0; k < ExpenseList.length(); k++) {
							// map = new HashMap<String, String>();
							JSONObject show_ExpenseList = ExpenseList
									.getJSONObject(k);

							String var_ExpenseList_ExpenseTypeID = show_ExpenseList
									.getString("ExpenseTypeID");
							if (var_ExpenseList_ExpenseTypeID.equals("1")) {
								String var_ExpenseList_Expense = show_ExpenseList
										.getString("Expense");
								var_Expense = Float
										.valueOf(var_ExpenseList_Expense);
							} else {
								String var_ExpenseList_Expense = show_ExpenseList
										.getString("Expense");
								var_Expense = Float
										.valueOf(var_ExpenseList_Expense);
							}

							// String var_ExpenseList_Expense = show_ExpenseList
							// .getString("Expense");

							total += (var_Expense);
							map.put("Expense" + j, String.valueOf(String
									.format("%.2f", total)));
							// songsList.add(map);
						}

						attachmentlist = new ArrayList<HashMap<String, String>>();

						JSONArray show_AttatchmentList = show
								.getJSONArray("AttatchmentList");
						for (int k = 0; k < show_AttatchmentList.length(); k++) {
							JSONObject show_Attatchment = show_AttatchmentList
									.getJSONObject(k);
							// map = new HashMap<String, String>();
							map.put("ImageID" + j,
									show_Attatchment.getString("ID"));
							map.put("ImageCode" + j,
									show_Attatchment.getString("ImageCode"));
							attachmentlist.add(map);

						}
						// Log.d("save_result", save_result + "");
						/* result.add(map); */

					}

					j++;
					songsList.add(map);

				}
				if (show_BERStatus.equals("2")) {
					ViewAdapterOne adapter = new ViewAdapterOne(
							ShowCalendarList.this, songsList);
					var_ShowDataInList.setAdapter(adapter);
					var_ShowDataInList
							.setOnItemClickListener(new OnItemClickListener() {

								private Date today_date;
								private String day_total;

								@Override
								public void onItemClick(AdapterView<?> arg0,
										View arg1, int position, long arg3) {
									String store_Date = songsList.get(position)
											.get("Date" + position);
									store_Day = songsList.get(position).get(
											"Day" + position);
									day_total = songsList.get(position).get(
											"Total" + position);
									if (day_total != null) {

										try {
											today_date = sm.parse(store_Date);
										} catch (ParseException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										Log.d("store_Date", store_Date);
										Log.d("store_Day", store_Day);
										Log.d("weekdate", show_WeekDate);

										if (d.after(today_date)
												|| d.equals(today_date)) {

											SharedPreferences example1 = getSharedPreferences(
													ValueStore, 0);
											Editor editor1 = example1.edit();
											editor1.putString("Date",
													store_Date).commit();

											Intent in_next = new Intent(
													ShowCalendarList.this,
													Direct_summary.class);
											Bundle bd = new Bundle();
											bd.putString("StoreDate",
													store_Date);
											bd.putString("StoreDay", store_Day);
											bd.putString("UserID", store_UserID);
											bd.putString("CorpID", store_CorpID);
											bd.putString("UserName",
													store_UserName);
											bd.putString("Password",
													store_password);
											bd.putString("UserRole",
													store_UserRole);
											bd.putString("StartDate",
													show_start_date);
											bd.putString("EndDate",
													show_end_Date);
											bd.putString("WeekDate",
													show_WeekDate);
											bd.putString("BERStatus",
													show_BERStatus);
											in_next.putExtra("Detail", result);
											in_next.putExtras(bd);
											in_next.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
											startActivity(in_next);
										} else {
											Alert_dialog();
										}
									} else {
										Alert_dialog1();
									}
								}

							});
				}

				else if (show_BERStatus.equals("3")) {
					ViewAdapterthree adapter = new ViewAdapterthree(
							ShowCalendarList.this, songsList);
					var_ShowDataInList.setAdapter(adapter);
					var_ShowDataInList
							.setOnItemClickListener(new OnItemClickListener() {

								private Date today_date;

								@Override
								public void onItemClick(AdapterView<?> arg0,
										View arg1, int position, long arg3) {
									store_Date = songsList.get(position).get(
											"Date" + position);
									store_Day = songsList.get(position).get(
											"Day" + position);
									Log.d("weekdate", show_WeekDate);

									try {
										today_date = sm.parse(store_Date);
									} catch (ParseException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									if (d.after(today_date)
											|| d.equals(today_date)) {

										SharedPreferences example1 = getSharedPreferences(
												ValueStore, 0);
										Editor editor1 = example1.edit();
										editor1.putString("Date", store_Date)
												.commit();

										Intent in_next = new Intent(
												ShowCalendarList.this,
												InputForm.class);
										Bundle bd = new Bundle();
										bd.putString("StoreDate", store_Date);
										bd.putString("StoreDay", store_Day);
										bd.putString("UserID", store_UserID);
										bd.putString("CorpID", store_CorpID);
										bd.putString("UserName", store_UserName);
										bd.putString("Password", store_password);
										bd.putString("UserRole", store_UserRole);
										bd.putString("StartDate",
												show_start_date);
										bd.putString("EndDate", show_end_Date);
										bd.putString("WeekDate", show_WeekDate);
										bd.putString("BERStatus",
												show_BERStatus);
										in_next.putExtra("Detail", result);
										in_next.putExtras(bd);
										in_next.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
										startActivity(in_next);
									} else {
										Alert_dialog();
									}
								}

							});
				}

				else if (show_BERStatus.equals("4")) {
					ViewAdapterfour adapter = new ViewAdapterfour(
							ShowCalendarList.this, songsList);
					var_ShowDataInList.setAdapter(adapter);
					var_ShowDataInList
							.setOnItemClickListener(new OnItemClickListener() {

								private Date today_date;
								private String day_total;

								@Override
								public void onItemClick(AdapterView<?> arg0,
										View arg1, int position, long arg3) {
									String store_Date = songsList.get(position)
											.get("Date" + position);
									store_Day = songsList.get(position).get(
											"Day" + position);

									day_total = songsList.get(position).get(
											"Total" + position);
									Log.d("store_Date", store_Date);
									Log.d("store_Day", store_Day);
									Log.d("weekdate", show_WeekDate);
									if (day_total != null) {

										try {
											today_date = sm.parse(store_Date);
										} catch (ParseException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										if (d.after(today_date)
												|| d.equals(today_date)) {

											SharedPreferences example1 = getSharedPreferences(
													ValueStore, 0);
											Editor editor1 = example1.edit();
											editor1.putString("Date",
													store_Date).commit();
											Intent in_next = new Intent(
													ShowCalendarList.this,
													Direct_summary.class);
											Bundle bd = new Bundle();
											bd.putString("StoreDate",
													store_Date);
											bd.putString("StoreDay", store_Day);
											bd.putString("UserID", store_UserID);
											bd.putString("CorpID", store_CorpID);
											bd.putString("UserName",
													store_UserName);
											bd.putString("Password",
													store_password);
											bd.putString("UserRole",
													store_UserRole);
											bd.putString("StartDate",
													show_start_date);
											bd.putString("EndDate",
													show_end_Date);
											bd.putString("WeekDate",
													show_WeekDate);
											bd.putString("BERStatus",
													show_BERStatus);
											in_next.putExtra("Detail", result);
											in_next.putExtras(bd);
											in_next.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
											startActivity(in_next);
										} else {
											Alert_dialog();
										}
									} else {
										Alert_dialog1();
									}
								}

							});
				}

				else if (show_BERStatus.equals("1")) {
					ViewAdaptertwo adapter = new ViewAdaptertwo(
							ShowCalendarList.this, songsList);
					var_ShowDataInList.setAdapter(adapter);

					var_ShowDataInList
							.setOnItemClickListener(new OnItemClickListener() {

								private Date today_date;
								private String day_total;

								@Override
								public void onItemClick(AdapterView<?> arg0,
										View arg1, int position, long arg3) {
									store_Date = songsList.get(position).get(
											"Date" + position);
									store_Day = songsList.get(position).get(
											"Day" + position);
									day_total = songsList.get(position).get(
											"Total" + position);
									Log.d("weekdate", show_WeekDate);

									try {
										today_date = sm.parse(store_Date);
									} catch (ParseException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									if (d.after(today_date)
											|| d.equals(today_date)) {

										SharedPreferences example1 = getSharedPreferences(
												ValueStore, 0);
										Editor editor1 = example1.edit();
										editor1.putString("Date", store_Date)
												.commit();

										Intent in_next = new Intent(
												ShowCalendarList.this,
												InputForm.class);
										Bundle bd = new Bundle();
										bd.putString("StoreDate", store_Date);
										bd.putString("StoreDay", store_Day);
										bd.putString("UserID", store_UserID);
										bd.putString("CorpID", store_CorpID);
										bd.putString("UserName", store_UserName);
										bd.putString("Password", store_password);
										bd.putString("UserRole", store_UserRole);
										bd.putString("StartDate",
												show_start_date);
										bd.putString("EndDate", show_end_Date);
										bd.putString("WeekDate", show_WeekDate);
										bd.putString("BERStatus",
												show_BERStatus);
										in_next.putExtra("Detail", result);
										in_next.putExtras(bd);
										in_next.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
										startActivity(in_next);
									} else {
										Alert_dialog();
									}
								}

							});
				} else {
					ViewAdapter adapter = new ViewAdapter(
							ShowCalendarList.this, songsList);
					var_ShowDataInList.setAdapter(adapter);
					var_ShowDataInList
							.setOnItemClickListener(new OnItemClickListener() {

								private Date today_date;

								@Override
								public void onItemClick(AdapterView<?> arg0,
										View arg1, int position, long arg3) {
									String store_Date = songsList.get(position)
											.get("Date" + position);
									String store_Day = songsList.get(position)
											.get("Day" + position);
									Log.d("store_Date", store_Date);
									Log.d("store_Day", store_Day);
									Log.d("weekdate", show_WeekDate);

									try {
										today_date = sm.parse(store_Date);
									} catch (ParseException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									if (d.after(today_date)
											|| d.equals(today_date)) {

										SharedPreferences example1 = getSharedPreferences(
												ValueStore, 0);
										Editor editor1 = example1.edit();
										editor1.putString("Date", store_Date)
												.commit();
										Intent in_next = new Intent(
												ShowCalendarList.this,
												InputForm.class);
										Bundle bd = new Bundle();
										bd.putString("StoreDate", store_Date);
										bd.putString("StoreDay", store_Day);
										bd.putString("UserID", store_UserID);
										bd.putString("CorpID", store_CorpID);
										bd.putString("UserName", store_UserName);
										bd.putString("Password", store_password);
										bd.putString("UserRole", store_UserRole);
										bd.putString("StartDate",
												show_start_date);
										bd.putString("EndDate", show_end_Date);
										bd.putString("WeekDate", show_WeekDate);
										bd.putString("BERStatus",
												show_BERStatus);
										in_next.putExtra("Detail", result);
										in_next.putExtras(bd);
										in_next.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
										startActivity(in_next);
									} else {
										Alert_dialog();
									}
								}

							});
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private class IdSender extends AsyncTask<Void, Void, String> {
		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(ShowCalendarList.this);
			dialog.setMessage("Please wait...");
			dialog.show();
		}

		@Override
		protected String doInBackground(Void... params) {
			try {

				SoapObject request = new SoapObject(
						ConnectionURL.NAMESPACE_For_SaveList, "Submit");
				request.addProperty("CorpID", store_CorpID);
				request.addProperty("UserID", store_UserID);
				request.addProperty("WeekDate", show_WeekDate);
				request.addProperty("ExpenseReportID",
						var_store_val_from_web_service);
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);
				HttpTransportSE androidHttpTransport = new HttpTransportSE(
						get_url + ConnectionURL.URL_For_SaveList);
				androidHttpTransport
						.call("http://tempuri.org/Submit", envelope);
				return envelope.getResponse().toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			Log.d("submit result", result);
			dialog.dismiss();
			if (result != null && result.toString().contentEquals("1")) {
				// for (int i = 0; i < var_ShowDataInList.getChildCount(); i++)
				// {
				// Log.d("child " + i, var_ShowDataInList.getChildAt(i)
				// .toString());
				// var_ShowDataInList.getChildAt(i).setBackgroundColor(
				// Color.parseColor("#FF69B4"));
				// }

				AlertDialog alertDialog = new AlertDialog.Builder(
						ShowCalendarList.this).create();

				// Setting Dialog Title
				alertDialog.setTitle("Info...");

				// Setting Dialog Message
				alertDialog.setMessage("Data Submitted Successfully");

				// Setting Icon to Dialog
				alertDialog.setIcon(R.drawable.delete);

				// Setting OK Button
				alertDialog.setButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// Write your code here to execute after dialog
								// closed
								SharedPreferences example1 = getSharedPreferences(
										ValueStore, 0);
								Editor editor1 = example1.edit();
								editor1.putString("BERStatus", "2");
								editor1.commit();
								Intent in_next = new Intent(
										ShowCalendarList.this,
										ShowCalendarList.class);
								Bundle bd = new Bundle();
								bd.putString("Date", store_Day);
								bd.putString("StartDate", show_start_date);
								bd.putString("EndDate", show_end_Date);
								bd.putString("BERStatus", show_BERStatus);
								bd.putString("WeekDate", show_WeekDate);
								bd.putString("UserID", store_UserID);
								bd.putString("CorpID", store_CorpID);
								bd.putString("UserName", store_UserName);
								bd.putString("Password", store_password);
								bd.putString("UserRole", store_UserRole);
								in_next.putExtras(bd);
								in_next.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(in_next);
								finish();
							}
						});

				// Showing Alert Message
				alertDialog.show();

			}
		}
	}

	public void Alert_dialog() {
		AlertDialog alertDialog = new AlertDialog.Builder(ShowCalendarList.this)
				.create();

		// Setting Dialog Title
		alertDialog.setTitle("Info...");

		// Setting Dialog Message
		alertDialog.setMessage("Invalid Expense Date");

		// Setting Icon to Dialog
		alertDialog.setIcon(R.drawable.delete);

		// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

			}
		});

		// Showing Alert Message
		alertDialog.show();
	}

	public void Alert_dialog1() {
		AlertDialog alertDialog = new AlertDialog.Builder(ShowCalendarList.this)
				.create();

		// Setting Dialog Title
		alertDialog.setTitle("Info...");

		// Setting Dialog Message
		alertDialog.setMessage("There is no expense saved");

		// Setting Icon to Dialog
		alertDialog.setIcon(R.drawable.delete);

		// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

			}
		});

		// Showing Alert Message
		alertDialog.show();
	}
}