package com.nextgenbermobileapp.activity;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
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
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ExpenseSummary extends Activity implements OnClickListener {

	private byte[] bitmap1, bitmap2, bitmap3, bitmap4;
	JSONArray var_DetailList, var_Attachment;

	String store_UserID, store_CorpID, store_UserName, store_password,
			store_UserRole, var_store_val_from_web_service, store_WeekDate,
			show_Store_Date, show_Store_Day, show_check_form,
			show_check_form_locto, store_loc_form, show_start_date,
			show_end_Date, show_WeekDate, show_BERStatus;

	ArrayList<LocationDetailsInfo> store_data, check_data;
	ArrayList<LocationInfo> store_loc_info;

	String store_loc_to;

	ArrayList<HashMap<String, String>> result;

	private Boolean isInternetPresent = false;
	private CheckNetworkConnection cd;

	public static final String ValueStore = "Collection_Data";

	String show_Note, show_Purpose, dayno, date;

	int store_task_id, store_loc_id_from, store_loc_id_to, expense_reportId;

	JSONArray jArray;
	// ArrayList<HashMap<String, String>> attachmentList;
	private String store_loc_to_string;
	private String var_show_storeDate_month;
	private String var_show_storeDate_day;
	private String var_show_storeDate_year;
	private int expense_count;
	private String var_BERListLocFrom;
	private String new_converted_storeDate;
	private String get_url;
	private ArrayList<HashMap<String, String>> attachmentlist;

	private ArrayList<HashMap<String, String>> save_result;

	SimpleDateFormat sm = new SimpleDateFormat("MM-dd-yyyy");
	private Date d;
	ArrayList<String> image_list;
	ArrayList<String> image_listt = new ArrayList<String>();
	ArrayList<String> paths;
	ArrayList<Integer> deleteYnStateList;
	private JSONObject attachment;
	private ArrayList<Byte[]> image;
	private String image_size;
	private String id_afterdate_save;
	private String attachment_response;

	private JSONArray var_Attachment1;
	private JSONObject attachment1;

	Boolean save_image_flag = true;

	ArrayList<String> image_attachment = new ArrayList<String>();
	private String attached_jsonobject;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.expense_summary);

		attachmentlist = new ArrayList<HashMap<String, String>>();

		d = Calendar.getInstance().getTime();

		// attachmentList = (ArrayList<HashMap<String, String>>) getIntent()
		// .getSerializableExtra("AttachmentList");

		// try {

		SharedPreferences example = getSharedPreferences(ValueStore, 0);
		date = example.getString("Date", "");
		store_CorpID = example.getString("CorpID", "");
		store_UserID = example.getString("UserID", "");
		show_WeekDate = example.getString("WeekDate", "");
		show_Store_Date = example.getString("ExpenseDate", "");
		store_task_id = example.getInt("TaskID", 0);
		store_loc_id_from = example.getInt("LocFromID", 0);
		store_loc_id_to = example.getInt("LocToID", 0);
		show_Note = example.getString("Note", "");
		show_Purpose = example.getString("Purpose", "");
		expense_reportId = example.getInt("ExpenseReportID", 0);
		dayno = example.getString("DayNo", "");
		store_UserName = example.getString("UserLoginName", "");
		store_loc_to_string = example.getString("store_loc_to", "");
		get_url = example.getString("URL", "");

		Log.i("sirshendu", store_loc_id_from + "");

		TextView Date = (TextView) findViewById(R.id.ShowDateDay);
		Date.setText(date);

		TextView store_loc_to_textview = (TextView) findViewById(R.id.tolocation);
		store_loc_to_textview.setText(store_loc_to_string);

		// } catch (Exception e) {
		// // TODO: handle exception
		// }

		cd = new CheckNetworkConnection(getApplicationContext());
		isInternetPresent = cd.isConnectingToInternet();

		var_DetailList = new JSONArray();
		var_Attachment = new JSONArray();

		findViewById(R.id.attach_receipt).setOnClickListener(this);
		findViewById(R.id.save_daily_expenses).setOnClickListener(this);

		ListView listView = (ListView) findViewById(R.id.listView1);
		listView.setAdapter(new SummaryAdapter(this, getIntent().getExtras()));

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

		float total = 0;

		Bundle extras = getIntent().getExtras();

		Set<String> keySet = extras.keySet();
		for (String key : keySet) {
			try {
				if (key.contains("travel")) {
					Map<String, String> map = (Map<String, String>) extras
							.getSerializable(key);
					total += Float.parseFloat(map.get(map
							.containsKey("value_exp") ? "value_exp" : "value"));
				} else if (key.contains("perdiem") || key.contains("other")) {
					Map<String, String> map = (Map<String, String>) extras
							.getSerializable(key);
					total += Float.parseFloat(map.get("value"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		((EditText) findViewById(R.id.editText1)).setText(String.format("%.2f",
				total));
		load_data_from_url1();

		verify_user_login_asynchronously task1 = new verify_user_login_asynchronously();
		task1.execute();

		findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// Write your code here to invoke YES event
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						ExpenseSummary.this);

				// Setting Dialog Title
				alertDialog.setTitle("Info...");

				// Setting Dialog Message
				alertDialog.setMessage("Cancel without saving?");

				// Setting Icon to Dialog
				alertDialog.setIcon(R.drawable.delete);

				// Setting Positive "Yes" Button
				alertDialog.setPositiveButton("YES",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {

								// Write your code here to invoke YES event
								Intent i = new Intent(ExpenseSummary.this,
										ShowCalendarList.class);
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

								dialog.cancel();
							}
						});

				// Showing Alert Message
				alertDialog.show();

			}
		});

		findViewById(R.id.calender).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (show_BERStatus.equals("0") || show_BERStatus.equals("1")
						|| show_BERStatus.equals("3")) {
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(
							ExpenseSummary.this);

					// Setting Dialog Title
					// alertDialog.setTitle("Confirm Delete...");

					// Setting Dialog Message
					alertDialog.setMessage("Move to calendar without saving ?");

					// Setting Icon to Dialog
					// alertDialog.setIcon(R.drawable.delete);

					// Setting Positive "Yes" Button
					alertDialog.setPositiveButton("YES",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {

									// Write your code here to invoke YES
									// event"You clicked on YES",
									// Toast.LENGTH_SHORT).show();
									Intent i = new Intent(ExpenseSummary.this,
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
					Intent i = new Intent(ExpenseSummary.this,
							ViewCalendar.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
					finish();
				}

			}
		});

		findViewById(R.id.back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
	}

	/******* call asynctask for location list ******************/

	private class verify_user_login_asynchronously extends
			AsyncTask<String, Void, Boolean> {
		ProgressDialog dialog = new ProgressDialog(ExpenseSummary.this);

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
		for (int i = 0; i < check_data.size(); i++) {
			String show_id = check_data.get(i).getID();

			if (store_loc_to.equalsIgnoreCase(show_id)) {
				String show_LocationID = store_loc_info.get(i).getLoc_ID();
				String show_ExpenseTypeID = store_loc_info.get(i)
						.getExpenseTypeID();
				String show_StartDate = store_loc_info.get(i).getStartDate();
				String show_EndDate = store_loc_info.get(i).getStartDate();
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

		Log.d("result", result + "");
	}

	private void store_data_for_login() {
		try {
			Log.d("store_loc_to", store_loc_to);

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

				for (int j = 0; j < contacts_LocationRate.length(); j++) {

					JSONObject show = contacts_LocationRate.getJSONObject(j);

					String var_LocationID = show.getString("ID");
					String var_ExpenseTypeID = show.getString("ExpenseTypeID");
					String var_StartDate = show.getString("StartDate");
					String var_EndDate = show.getString("EndDate");
					String var_Rate = show.getString("Rate");

					Log.d("show", show.toString());

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

	@SuppressLint("NewApi")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			switch (requestCode) {

			case 4:
				try {
					if (var_Attachment == null) {
						var_Attachment = new JSONArray();
					}

					ArrayList<String> pathList = data
							.getStringArrayListExtra("paths");
					if (paths != null) {
						paths.addAll(pathList);
					} else {
						paths = pathList;
					}

					deleteYnStateList = data
							.getIntegerArrayListExtra("delete_yn");
					Log.d("delete_yn", deleteYnStateList + "");
					// for (byte[] bitmap : bitmaps) {
					// if (bitmap == null)
					// continue;

					// String encodedBase64 = new String(Base64.encode(bitmap,
					// Base64.DEFAULT));
					// attachment = new JSONObject();
					// attachment.put("ID", "0");
					// attachment.put("ExpenseDate", show_Store_Date);
					//
					// attachment.put("ImageCode", encodedBase64);
					// var_Attachment.put(attachment);

					for (String path : pathList) {
						Bitmap bitmap = BitmapFactory.decodeFile(path);

						ByteArrayOutputStream stream = new ByteArrayOutputStream();
						bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
						stream.flush();
						byte[] bytes = stream.toByteArray();
						stream.close();

						String encodedBase64 = Base64.encodeToString(bytes,
								Base64.DEFAULT);
						attachment = new JSONObject();
						attachment.put("ID", "0");
						attachment.put("ExpenseDate", show_Store_Date);

						// attachment.put("ImageCode",
						// encodedBase64);============
						// var_Attachment.put(attachment);

						// attachment.put("ImageURL", image_list.get(i));

						HashMap<String, String> map = new HashMap<String, String>();
						map.put("ImageCode", encodedBase64);
						map.put("ImageID", "0");
						map.put("ExpenseDate", show_Store_Date);
						map.put("ImageURL",
								path.substring(path.lastIndexOf("/") + 1));
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
							image_size = (bitmap.getAllocationByteCount() / 1024)
									+ " kB";
							map.put("ImageSize", image_size);
						} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
							image_size = bitmap.getByteCount() / 1024 + " kB";
							map.put("ImageSize", image_size);
						} else {
							image_size = (bitmap.getRowBytes()
									* bitmap.getHeight() / 1024)
									+ " kB";
							map.put("ImageSize", image_size);
						}

						attachment.put("ImageSize", image_size);
						// save_result.add(map);
						attachmentlist.add(map);
						var_Attachment.put(attachment);
					}

					Log.wtf("sirshendu attached", attachmentlist + "");
					// }
					// for (int i = 0; i < image_list.size(); i++) {
					// attachment.put("ImageURL", image_list.get(i));
					// var_Attachment.put(attachment);
					// }
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.attach_receipt:
			Intent attachmentIntent = new Intent(this, ShowImage.class);
			attachmentIntent.putExtra("AttachmentList", attachmentlist);
			attachmentIntent.putStringArrayListExtra("paths", paths);
			attachmentIntent.putIntegerArrayListExtra("delete_yn",
					deleteYnStateList);
			startActivityForResult(attachmentIntent, 4);
			break;
		case R.id.save_daily_expenses:
			jArray = new JSONArray();

			Bundle bundle = getIntent().getExtras();
			Set<String> keySet = bundle.keySet();
			for (String key : keySet) {
				if (key.contains("travel") || key.contains("perdiem")
						|| key.contains("other")) {
					Map<String, String> map = (Map<String, String>) bundle
							.getSerializable(key);
					String value = map.get("value");
					String value_allowance = map.get("value_meal_allowance");
					String value_ChargableQty = map.get("ChargableQty");
					if (!"0".equals(value)) {
						String id = map.get("id");
						Log.d("key", key);
						Log.d("id", id);
						Log.d("value", value);
						try {
							JSONObject jObject = new JSONObject();
							jObject.put("ID", "0");
							jObject.put("LocationRateID", "0");
							jObject.put("ExpenseTypeID", id);
							jObject.put("Rate", value);
							jObject.put("Perdiem", value_allowance);
							jObject.put("ChargableQty", value_ChargableQty);
							for (Map<String, String> loc_map : result) {
								if (loc_map.get("ExpenseTypeID").equals(id)) {
									jObject.put("LocationRateID",
											loc_map.get("LocationID"));
								}
							}

							Log.d("built", jObject.toString());
							jArray.put(jObject);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}
			}

			// show_Purpose = var_editTextPurpose.getText().toString();
			// show_Note = var_editTextNote.getText().toString();
			// String allowance_Rate = var_AllowanceRate.getText().toString();

			save_data_in_database();
			break;
		}

	}

	private void save_data_in_database() {

		if (isInternetPresent) {
			verify_save_data_asynchronously task1 = new verify_save_data_asynchronously();
			task1.execute();
		} else {
			Toast.makeText(ExpenseSummary.this,
					"Check Your Internet Connection", Toast.LENGTH_SHORT)
					.show();
		}
	}

	/******** async task for save data ***************/

	private class verify_save_data_asynchronously extends
			AsyncTask<Void, Void, Void> {
		ProgressDialog dialog = new ProgressDialog(ExpenseSummary.this);
		private String imageid;
		private Integer delete_yn;

		private String imageurl;
		private String imagecode;
		private String imagesize;

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
			Log.d("response", id_afterdate_save);

			if (id_afterdate_save.equalsIgnoreCase("-1"))
				Toast.makeText(ExpenseSummary.this, "Some Problem Occured",
						Toast.LENGTH_SHORT).show();
			else if (id_afterdate_save.equalsIgnoreCase("0"))
				Toast.makeText(ExpenseSummary.this,
						"Expense cannot be saved with zero amount",
						Toast.LENGTH_SHORT).show();
			else {
				if (deleteYnStateList != null) {

					// ==========================issue=======================
					for (int i = 0; i < attachmentlist.size(); i++) {

						imageid = attachmentlist.get(i).get("ImageID");

						if (imageid.equals("0")) {
							if (deleteYnStateList.size() > 0) {
								delete_yn = deleteYnStateList.get(i);
								if (delete_yn == 0) {

									if (var_Attachment1 == null) {
										var_Attachment1 = new JSONArray();
									}

									imageurl = attachmentlist.get(i).get(
											"ImageURL");
									imagecode = attachmentlist.get(i).get(
											"ImageCode");
									imagesize = attachmentlist.get(i).get(
											"ImageSize");

									try {
										try {
											attachment1 = new JSONObject();
											attachment1.put("ID", imageid);
											attachment1.put("ExpenseDate",
													show_Store_Date.replace(
															"-", "/"));
											attachment1.put("ImageURL",
													imageurl);
											 attachment1.put("ImageCode",
											 imagecode);
											attachment1.put("Size", imagesize);
											attachment1.put("DeleteYN",
													delete_yn);
											Log.i("sirshoattached2",
													attachment1.toString());
											image_attachment.add(attachment1
													.toString());
											// var_Attachment1.put(attachment);
										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

									// Attachment_save_data_asynchronously task1
									// = new
									// Attachment_save_data_asynchronously();
									// task1.execute();
								}

							}
						} else {
							delete_yn = deleteYnStateList.get(i);
							if (delete_yn == 1) {
								try {
									{

										imageurl = attachmentlist.get(i).get(
												"ImageURL");
										imagecode = attachmentlist.get(i).get(
												"ImageCode");
										imagesize = attachmentlist.get(i).get(
												"ImageSize");

										// attachment1 = new JSONObject();
										try {
											attachment1 = new JSONObject();
											attachment1.put("ID", imageid);
											attachment1.put("ExpenseDate",
													show_Store_Date.replace(
															"-", "/"));
											attachment1.put("ImageURL",
													imageurl);
											attachment1.put("ImageCode", "");
											attachment1.put("Size", imagesize);
											attachment1.put("DeleteYN",
													delete_yn);
											var_Attachment.put(attachment);
											image_attachment.add(attachment1
													.toString());
										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}

										// Attachment_save_data_asynchronously
										// task1 = new
										// Attachment_save_data_asynchronously();
										// task1.execute();
									}
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							//
						}

					}

					Attachment_save_data_asynchronously task1 = new Attachment_save_data_asynchronously();
					task1.execute();
					// }

				}
				//

				if (save_image_flag || Integer.parseInt(id_afterdate_save) > 0) {
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(
							ExpenseSummary.this);

					// Setting Dialog Title
					alertDialog.setTitle("Message");

					// Setting Dialog Message
					alertDialog
							.setMessage("Expense saved successfully.Enter expense for next day?");

					// Setting Icon to Dialog
					alertDialog.setIcon(R.drawable.delete);

					// Setting Positive "Yes" Button
					alertDialog.setPositiveButton("YES",
							new DialogInterface.OnClickListener() {
								// private String current_date_day;
								private String var_show_storeDate_month;
								private String var_show_storeDate_day;
								private String var_show_storeDate_year;
								private String next_new_date;
								private int var_show_storeDate_day_new;
								private Date today_date;

								public void onClick(DialogInterface dialog,
										int which) {

									SharedPreferences example1 = getSharedPreferences(
											ValueStore, 0);
									Editor editor1 = example1.edit();
									editor1.putString("BERStatus", "1");
									editor1.commit();

									// current_date_day =
									// var_ShowDateDay.getText().toString();
									String[] separated2 = date.split(" ");
									String current_date2 = separated2[0].trim();
									System.out.println("Store Date new :"
											+ current_date2);

									if (current_date2.length() == 10) {

										var_show_storeDate_month = current_date2
												.substring(0, 2);
										var_show_storeDate_day = current_date2
												.substring(3, 5);
										var_show_storeDate_year = current_date2
												.substring(6, 10);
										var_show_storeDate_day_new = Integer
												.parseInt(var_show_storeDate_day) + 01;
										// =========================future date
										// data
										// not shown============================
										next_new_date = var_show_storeDate_month
												+ "-"
												+ "0".concat(String
														.valueOf(var_show_storeDate_day_new))
												+ "-" + var_show_storeDate_year;
										try {
											today_date = sm
													.parse(next_new_date);
										} catch (ParseException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										if (d.after(today_date)
												|| d.equals(today_date)) {
											if (Integer
													.parseInt((show_end_Date)
															.substring(3, 5)) > Integer
													.parseInt((date).substring(
															3, 5))) {

												System.out
														.println("Store Date new :"
																+ "dhukche");
												change_day_for_right_arrow();
											} else {
												System.out
														.println("Store Date new :"
																+ "dhukche na");
												AlertDialog alertDialog = new AlertDialog.Builder(
														ExpenseSummary.this)
														.create();

												// Setting Dialog Title
												alertDialog.setTitle("Info");

												// Setting Dialog Message
												alertDialog
														.setMessage("You cannot enter expense for the next day as you are already on the last day of this period");

												// Setting Icon to Dialog
												alertDialog
														.setIcon(R.drawable.delete);

												// Setting OK Button
												alertDialog
														.setButton(
																"OK",
																new DialogInterface.OnClickListener() {
																	public void onClick(
																			DialogInterface dialog,
																			int which) {

																		Intent i = new Intent(
																				ExpenseSummary.this,
																				ShowCalendarList.class);
																		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
																		startActivity(i);
																		finish();
																	}
																});

												// Showing Alert Message
												alertDialog.show();
											}
										} else {
											Alert_dialog();
										}

									} else {

										var_show_storeDate_month = current_date2
												.substring(0, 2);
										var_show_storeDate_day = current_date2
												.substring(3, 4);
										var_show_storeDate_year = current_date2
												.substring(5, 9);

										Log.i("test",
												show_start_date.substring(3, 4));
										Log.i("test1", date.substring(3, 4));

										if (Integer.parseInt((show_end_Date)
												.substring(3, 5)) > Integer
												.parseInt((date)
														.substring(3, 4))) {

											change_day_for_right_arrow();
										} else {
											System.out
													.println("Store Date new :"
															+ "dhukche na");

											AlertDialog alertDialog = new AlertDialog.Builder(
													ExpenseSummary.this)
													.create();

											// Setting Dialog Title
											alertDialog.setTitle("Info");

											// Setting Dialog Message
											alertDialog
													.setMessage("You cannot enter expense for the next day as you are already on the last day of this period");

											// Setting Icon to Dialog
											alertDialog
													.setIcon(R.drawable.delete);

											// Setting OK Button
											alertDialog
													.setButton(
															"OK",
															new DialogInterface.OnClickListener() {
																public void onClick(
																		DialogInterface dialog,
																		int which) {

																	Intent i = new Intent(
																			ExpenseSummary.this,
																			ShowCalendarList.class);
																	i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
																	startActivity(i);
																	finish();
																}
															});

											// Showing Alert Message
											alertDialog.show();
										}
									}

								}
							});

					// Setting Negative "NO" Button
					alertDialog.setNegativeButton("NO",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// Write your code here to invoke NO event
									Intent i = new Intent(ExpenseSummary.this,
											ViewCalendar.class);
									i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									startActivity(i);
									finish();
									dialog.cancel();
								}
							});

					// Showing Alert Message
					alertDialog.show();
				}

			}
		}
	}

	public void view_save_url() {

		try {

			System.out.println("Expenses Date :" + show_start_date);

			/*
			 * String var_store_date_month=show_start_date.substring(0, 2);
			 * String var_store_date_day=show_start_date.substring(3, 5); String
			 * var_store_date_Year=show_start_date.substring(6,10);
			 * show_start_date
			 * =var_store_date_day.concat("-").concat(var_store_date_month
			 * ).concat("-").concat(var_store_date_Year);
			 */

			System.out.println("Expenses Date :" + show_start_date);

			/*
			 * String var_show_weekDate_month=show_WeekDate.substring(0,2);
			 * String var_show_weekDate_day=show_WeekDate.substring(3,5); String
			 * var_show_weekDate_year=show_WeekDate.substring(6,10);
			 * show_WeekDate
			 * =var_show_weekDate_day.concat("-").concat(var_show_weekDate_month
			 * ).concat("-").concat(var_show_weekDate_year);
			 */

			System.out.println("DetailList :" + jArray.toString());
			System.out.println("Attachment List" + var_DetailList);
			System.out.println("Loc Id :" + store_loc_id_from);

			SoapObject request = new SoapObject(
					ConnectionURL.NAMESPACE_For_SaveList,
					ConnectionURL.METHOD_NAME_For_SaveList);
			request.addProperty("CorpID", store_CorpID);
			request.addProperty("UserID", store_UserID);
			request.addProperty("WeekDate", show_WeekDate);
			request.addProperty("ExpenseDate", show_Store_Date);
			request.addProperty("TaskID", store_task_id);
			request.addProperty("LocFromID", store_loc_id_from);
			request.addProperty("LocToID", store_loc_id_to);
			request.addProperty("Note", show_Note);
			request.addProperty("Purpose", show_Purpose);
			request.addProperty("ExpenseReportID", expense_reportId);
			request.addProperty("DayNo", 0);
			request.addProperty("UserLoginName", store_UserName);
			request.addProperty("DetailList", jArray.toString());
			request.addProperty("Attachments", var_Attachment.toString());

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(get_url
					+ ConnectionURL.URL_For_SaveList);
			androidHttpTransport.call(ConnectionURL.SOAP_ACTION_For_SaveList,
					envelope);
			Object result = (Object) envelope.getResponse();

			id_afterdate_save = result.toString();
			System.out.println("Print Result :"
					+ var_store_val_from_web_service);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void change_day_for_right_arrow() {

		/********** working for right arrow ***************/

		System.out.println("Store Date :" + show_Store_Date);
		// String current_date_day = date.getText().toString();
		String[] separated = date.split(" ");
		String current_date = separated[0].trim();
		System.out.println("Store Date new :" + current_date);

		if (current_date.length() == 10) {

			var_show_storeDate_month = current_date.substring(0, 2);
			var_show_storeDate_day = current_date.substring(3, 5);
			var_show_storeDate_year = current_date.substring(6, 10);

		} else {

			var_show_storeDate_month = current_date.substring(0, 2);
			var_show_storeDate_day = current_date.substring(3, 4);
			var_show_storeDate_year = current_date.substring(5, 9);
		}

		if (var_show_storeDate_day.contains("31")) {

			ImageView show_right_arrowImgae = (ImageView) findViewById(R.id.RightArrowimage);
			show_right_arrowImgae.setVisibility(View.GONE);
		} else {

			int convert_day = Integer.parseInt(var_show_storeDate_day);
			int new_convert_day = convert_day + 1;
			String var_convertDay = String.valueOf(new_convert_day);

			new_converted_storeDate = var_show_storeDate_month.concat("-")
					.concat(var_convertDay).concat("-")
					.concat(var_show_storeDate_year);

			String change_converted_storeDate_inDay = new_converted_storeDate;
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");

			Date convertedDate = new Date();
			try {
				convertedDate = dateFormat
						.parse(change_converted_storeDate_inDay);
				String s = (String) android.text.format.DateFormat.format(
						"EEEE", convertedDate);
				System.out.println("Check Date :" + s);
				String view_data = new_converted_storeDate + " " + "(" + s
						+ ")";
				// var_ShowDateDay.setText(view_data);
				// ============arrow click e data
				// change=================================
				show_Store_Date = new_converted_storeDate;
				load_data_from_url();
				// ============arrow click e data
				// change=================================

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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

	private void load_data_from_url1() {
		isInternetPresent = cd.isConnectingToInternet();
		if (isInternetPresent) {
			fetch_image_asynchronously task = new fetch_image_asynchronously();
			task.execute();
		} else {
			Toast.makeText(this, "Check Your Internet Connection",
					Toast.LENGTH_SHORT).show();
		}
	}

	/****** Call AsyncTask for threading *************************/

	private class fetch_image_asynchronously extends
			AsyncTask<Void, Void, String> {
		ProgressDialog dialog = new ProgressDialog(ExpenseSummary.this);

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
			store_data_for_url1(result);

		}
	}

	/****** Call AsyncTask for threading *************************/

	private class fetch_data_asynchronously extends
			AsyncTask<Void, Void, String> {
		ProgressDialog dialog = new ProgressDialog(ExpenseSummary.this);

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

			if (var_BERListLocFrom != null) {
				AlertDialog alertDialog = new AlertDialog.Builder(
						ExpenseSummary.this).create();

				// Setting Dialog Title
				alertDialog.setTitle("Info");

				// Setting Dialog Message
				alertDialog.setMessage("Expense already saved for "
						+ show_Store_Date);

				// Setting OK Button
				alertDialog.setButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// Write your code here to execute after dialog
								// closed
								Intent i = new Intent(ExpenseSummary.this,
										ShowCalendarList.class);
								i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(i);
								finish();

							}
						});

				// Showing Alert Message
				alertDialog.show();
			} else {
				// Write your code here to invoke YES event
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						ExpenseSummary.this);

				// Setting Dialog Title
				alertDialog.setTitle("Message");

				// Setting Dialog Message
				alertDialog.setMessage("Same location?");

				// Setting Icon to Dialog
				alertDialog.setIcon(R.drawable.delete);

				// Setting Positive "Yes" Button
				alertDialog.setPositiveButton("YES",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {

								// Write your code here to
								// invoke YES event
								Intent i = new Intent(ExpenseSummary.this,
										InputForm.class);
								i.putExtra("exp", "new");
								i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(i);
							}
						});

				// Setting Negative "NO" Button
				alertDialog.setNegativeButton("NO",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// Write your code here to
								// invoke NO event
								Intent i = new Intent(ExpenseSummary.this,
										InputForm.class);
								i.putExtra("exp", "few");
								i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(i);

								dialog.cancel();
							}
						});

				// Showing Alert Message
				alertDialog.show();
			}
			// showResult();
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

	public void store_data_for_url1(String result) {
		try {
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

			String[] separated = show_Store_Date.split("-");
			String first = separated[0];
			String second = separated[1];
			String third = separated[2];

			for (int i = 0; i < contacts_BERList.length(); i++) {
				JSONObject show = contacts_BERList.getJSONObject(i);

				String var_BERListExpenseDate = show.getString("ExpenseDate");
				Log.d("expense date", var_BERListExpenseDate);
				Log.d("show_Store_Date", show_Store_Date);
				if (!show_Store_Date.equalsIgnoreCase(var_BERListExpenseDate
						.replaceAll("/", "-")))
					continue;

				JSONArray ExpenseList = show.getJSONArray("ExpenseList");
				expense_count = ExpenseList.length();
				ArrayList<HashMap<String, String>> expenseList = new ArrayList<HashMap<String, String>>();

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
					image_listt.add(show_Attatchment.getString("ImageName"));
					// save_result.add(map);
					attachmentlist.add(map);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void store_data_for_url(String result) {
		try {
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

			String[] separated = show_Store_Date.split("-");
			String first = separated[0];
			String second = separated[1];
			String third = separated[2];

			if (Integer.parseInt(second) <= 9) {
				String secone = "0".concat(second);
				show_Store_Date = first.concat("-").concat(secone).concat("-")
						.concat(third);
			}

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

				var_BERListLocFrom = show.getString("LocFrom");
				map.put("LocFrom", var_BERListLocFrom);

				Log.i("sirshendu", var_BERListLocFrom);

				String var_BERListLocTo = show.getString("LocTo");
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

				String var_BERListTaskDesc = show.getString("TaskDesc");
				map.put("TaskDesc", var_BERListTaskDesc);

				// save_result.add(map);

				JSONArray ExpenseList = show.getJSONArray("ExpenseList");
				expense_count = ExpenseList.length();
				ArrayList<HashMap<String, String>> expenseList = new ArrayList<HashMap<String, String>>();
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
					// save_result.add(map);
					expenseList.add(map);
				}

				// attachmentlist = new ArrayList<HashMap<String, String>>();
				//
				// JSONArray show_AttatchmentList = show
				// .getJSONArray("AttatchmentList");
				// for (int j = 0; j < show_AttatchmentList.length(); j++) {
				// JSONObject show_Attatchment = show_AttatchmentList
				// .getJSONObject(j);
				// map = new HashMap<String, String>();
				// map.put("ImageID", show_Attatchment.getString("ID"));
				// map.put("ImageCode",
				// show_Attatchment.getString("ImageCode"));
				// // save_result.add(map);
				// attachmentlist.add(map);
				// }

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class SummaryAdapter extends BaseAdapter {
		private static final String category_travel = "travel";
		private static final String category_perdiem = "perdiem";
		private static final String category_other = "other";

		private Context context;
		private Bundle bundle;

		private List<String> travelKeys, perdiemKeys, otherKeys;

		public SummaryAdapter(Context context, Bundle bundle) {
			this.context = context;
			this.bundle = bundle;

			travelKeys = new ArrayList<String>();
			perdiemKeys = new ArrayList<String>();
			otherKeys = new ArrayList<String>();

			Set<String> keySet = bundle.keySet();
			for (String key : keySet) {
				if (key.contains(category_travel))
					travelKeys.add(key);
				else if (key.contains(category_perdiem))
					perdiemKeys.add(key);
				else if (key.contains(category_other))
					otherKeys.add(key);
			}
		}

		@Override
		public int getCount() {
			return travelKeys.size() + perdiemKeys.size() + otherKeys.size()
					+ 3;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@SuppressWarnings("unchecked")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.summary_row, parent,
						false);
			}

			TextView heading = (TextView) convertView
					.findViewById(R.id.heading);
			View detail = convertView.findViewById(R.id.detail);
			TextView label = (TextView) convertView.findViewById(R.id.label);
			EditText field = (EditText) convertView.findViewById(R.id.field);

			if (position == 0) {
				heading.setVisibility(View.VISIBLE);
				detail.setVisibility(View.GONE);

				heading.setText("Travel");
			} else if (position == travelKeys.size() + 1) {
				heading.setVisibility(View.VISIBLE);
				detail.setVisibility(View.GONE);

				heading.setText("Perdiem Related / Limited");
			} else if (position == (travelKeys.size() + perdiemKeys.size() + 2)) {
				heading.setVisibility(View.VISIBLE);
				detail.setVisibility(View.GONE);

				heading.setText("Other Expenses");
			} else if (position < travelKeys.size() + 1) {
				heading.setVisibility(View.GONE);
				detail.setVisibility(View.VISIBLE);

				int index = position - 1;
				Map<String, String> map = (Map<String, String>) bundle
						.getSerializable(travelKeys.get(index));

				label.setText(map.get("label"));
				field.setText(map.get("value"));
			} else if (position < travelKeys.size() + perdiemKeys.size() + 2) {
				heading.setVisibility(View.GONE);
				detail.setVisibility(View.VISIBLE);

				int index = position - travelKeys.size() - 2;
				Map<String, String> map = (Map<String, String>) bundle
						.getSerializable(perdiemKeys.get(index));

				label.setText(map.get("label"));
				field.setText(map.get("value"));
			} else {
				heading.setVisibility(View.GONE);
				detail.setVisibility(View.VISIBLE);

				int index = position - travelKeys.size() - perdiemKeys.size()
						- 3;
				Map<String, String> map = (Map<String, String>) bundle
						.getSerializable(otherKeys.get(index));

				label.setText(map.get("label"));
				field.setText(map.get("value"));
			}

			return convertView;
		}
	}

	public void Alert_dialog() {
		AlertDialog alertDialog = new AlertDialog.Builder(ExpenseSummary.this)
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

	/******** async attachment ***************/

	private class Attachment_save_data_asynchronously extends
			AsyncTask<Void, Void, Void> {
		ProgressDialog dialog = new ProgressDialog(ExpenseSummary.this);

		@Override
		protected void onPreExecute() {
			dialog.setMessage("Please wait...");
			dialog.setCancelable(true);
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {

			for (int i = 0; i < image_attachment.size(); i++) {
				attached_jsonobject = image_attachment.get(i);
				view_save_url_attachment();
			}
			return null;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(Void param) {
			dialog.dismiss();
			//

		}
	}

	public void view_save_url_attachment() {

		try {
			Log.i("sirshoattached", attached_jsonobject);

			SoapObject request = new SoapObject(
					ConnectionURL.NAMESPACE_For_attachment_save,
					ConnectionURL.METHOD_NAME_For_attachment_save);
			request.addProperty("CorpID", store_CorpID);
			request.addProperty("ExpenseReportID", id_afterdate_save);
			request.addProperty("Attachments", attached_jsonobject);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(get_url
					+ ConnectionURL.URL_For_SaveList);
			androidHttpTransport.call(
					ConnectionURL.SOAP_ACTION_For_attachment_save, envelope);
			Object result = (Object) envelope.getResponse();

			attachment_response = result.toString();
			if (Integer.parseInt(attachment_response) == 0) {
				save_image_flag = false;
			}
			System.out.println("Image save Result :" + attachment_response);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/******* attachment save ******************/
}