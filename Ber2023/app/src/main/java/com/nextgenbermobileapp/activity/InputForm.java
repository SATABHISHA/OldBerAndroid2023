package com.nextgenbermobileapp.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.nextgenbermobileapp.R;
import com.nextgenbermobileapp.connectioninfo.CheckNetworkConnection;
import com.nextgenbermobileapp.connectioninfo.ConnectionURL;
import com.nextgenbermobileapp.info.ExpensesInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class InputForm extends Activity implements OnClickListener {

	TextView var_ShowDateDay, var_LocationForm, var_ShowSelectTask,
			var_ShowLocationTo, var_AllowanceRate, var_ShowExpType,
			var_ShowExpAllowence, var_ShowExpAmount, label_ExpType,
			label_Allowance, label_Expenses;

	String store_UserID, store_CorpID, store_UserName, store_password,
			store_UserRole, show_Store_Date, store_TaskName, show_Store_Day,
			store_loc_from, store_loc_id_from, store_loc_id_to,
			store_loc_CountryID, store_loc_StateID, store_loc_LocationID,
			store_locExpenseTypeID, store_locStartDate, store_loc_EndDate,
			store_locLocRate, var_store_val_from_web_service, store_loc_to,
			store_Name, encodedImage, store_task_id, show_Purpose, show_Note,
			store_expenses_type_id, show_start_date, show_end_Date,
			show_BERStatus, show_WeekDate, store_second_attachment,
			store_first_attachment, store_attachment, input_amt,
			store_loc_rate_id, var_Allowance;

	JSONArray var_DetailList, var_Attachment;

	EditText var_ExpensesAmt, var_editTextPurpose, var_editTextNote;
	Bundle bd;

	private CheckNetworkConnection cd;
	private Boolean isInternetPresent = false;

	ArrayList<ExpensesInfo> store_expenses;
	ArrayList<HashMap<String, String>> expenseList, attachmentlist;

	Spinner var_ShowRateInSpinner;
	Button var_DeleteCell, var_Save, var_btnCalendar, var_Day, Expense_entry;
	Bitmap bm;

	private byte[] bitmap1, bitmap2, bitmap3, bitmap4;

	ArrayList<HashMap<String, String>> store_result, save_result;

	// Activity request codes
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	public static final int MEDIA_TYPE_IMAGE = 1;

	// directory name to store captured images and videos
	private static final String IMAGE_DIRECTORY_NAME = "CameraImage";
	private Uri fileUri; // file url to store image/video

	TableLayout var_MainTable;
	private int expense_count;
	int count = 0;
	int expense_reportId = 0;
	LinearLayout var_MainButton;

	Map<String, Boolean> addedRows = new HashMap<String, Boolean>();

	TableRow var_ShowData;
	ImageView var_StoreCameraImage;
	final Context context = this;

	String defaultTextForSpinner = "select";

	String var_show_storeDate_month, var_show_storeDate_day,
			var_show_storeDate_year, current_date_day, purpose_name;

	EditText Purpose;

	public static final String ValueStore = "Collection_Data";
	String view_data;

	private String exp;// new

	private String date;

	private String dayno;

	private String new_converted_storeDate;

	private Button SaveButton;

	private String get_url;

	SimpleDateFormat sm = new SimpleDateFormat("MM-dd-yyyy");

	private Date d;

	private int var_show_storeDate_day_new;

	private String next_new_date;

	private Date today_date;

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// new==============================================================
		exp = getIntent().getStringExtra("exp");
		d = Calendar.getInstance().getTime();
		if (exp != null) {
			Log.wtf("sirsho", "if e dhukche");
			if (exp.equalsIgnoreCase("new")) {
				setContentView(R.layout.inputform_same_expense);
				bd = new Bundle();
				initialize_user_interface_sameday();
			} else if (exp.equalsIgnoreCase("few")) {
				setContentView(R.layout.input_form_activity1);
				bd = new Bundle();
				initialize_user_interface_sameday();
			} else {

			}
		} else {

			Log.wtf("sirsho", "else e dhukche");

			setContentView(R.layout.input_form_activity1);

			// new==============================================================
			SaveButton = (Button) findViewById(R.id.SaveButton);
			SaveButton.setEnabled(false);
			bd = new Bundle();
			store_result = new ArrayList<HashMap<String, String>>();
			store_UserID = getIntent().getExtras().getString("UserID");
			store_CorpID = getIntent().getExtras().getString("CorpID");
			store_UserName = getIntent().getExtras().getString("UserName");
			store_password = getIntent().getExtras().getString("Password");
			store_UserRole = getIntent().getExtras().getString("UserRole");
			store_TaskName = getIntent().getExtras().getString("Description");
			show_Store_Date = getIntent().getExtras().getString("StoreDate");
			show_Store_Day = getIntent().getExtras().getString("StoreDay");
			store_loc_from = getIntent().getExtras().getString("LocName");
			store_loc_id_from = getIntent().getExtras().getString("LocID");
			store_loc_CountryID = getIntent().getExtras().getString(
					"LocCountryID");
			store_loc_StateID = getIntent().getExtras().getString("LocStateID");
			store_loc_LocationID = getIntent().getExtras().getString(
					"LocLocationID");
			store_locExpenseTypeID = getIntent().getExtras().getString(
					"LocExpenseTypeID");
			store_locStartDate = getIntent().getExtras().getString(
					"LocStartDate");
			store_loc_EndDate = getIntent().getExtras().getString("LocEndDate");
			store_locLocRate = getIntent().getExtras().getString("LocRate");
			store_loc_to = getIntent().getExtras().getString("LocToName");
			store_task_id = getIntent().getExtras().getString("Id");

			show_start_date = getIntent().getExtras().getString("StartDate");
			show_end_Date = getIntent().getExtras().getString("EndDate");

			store_result = (ArrayList<HashMap<String, String>>) getIntent()
					.getSerializableExtra("LocationInfo");
			show_BERStatus = getIntent().getExtras().getString("BERStatus");
			show_WeekDate = getIntent().getExtras().getString("WeekDate");

			var_DetailList = new JSONArray();
			var_Attachment = new JSONArray();

			cd = new CheckNetworkConnection(getApplicationContext());
			isInternetPresent = cd.isConnectingToInternet();
			store_expenses = new ArrayList<ExpensesInfo>();

			/*** iniatilize ui interface **********/
			initialize_user_interface();

			if (store_TaskName != null && store_TaskName.length() > 0) {
				System.out.println("Task Name :" + store_TaskName);
			} else {
				store_TaskName = "";
			}

			if (store_loc_from != null && store_loc_from.length() > 0) {
				System.out.println("Task Name :" + store_loc_from);
			}
			if (store_loc_to != null && store_loc_to.length() > 0) {
				System.out.println("Task Name :" + store_loc_to);
			}

			load_data_from_url();

			view_data = show_Store_Date + " " + "(" + show_Store_Day + ")";
			var_ShowDateDay.setText(view_data);

			var_LocationForm.setText(store_loc_from);
			var_ShowSelectTask.setText(store_TaskName);
			var_ShowLocationTo.setText(store_loc_to);

			findViewById(R.id.LocationForm).setOnClickListener(this);

			findViewById(R.id.LocationTo).setOnClickListener(this);
			findViewById(R.id.SelectTask).setOnClickListener(this);
			findViewById(R.id.SaveButton).setOnClickListener(this);
			findViewById(R.id.leftimageArrow).setOnClickListener(this);
			findViewById(R.id.RightArrowimage).setOnClickListener(this);
			findViewById(R.id.btnCalendar).setOnClickListener(this);
			findViewById(R.id.btnDay).setOnClickListener(this);
			findViewById(R.id.Expense_entry).setOnClickListener(this);
			if (show_Store_Date.equals(show_start_date)) {
				findViewById(R.id.leftimageArrow).setVisibility(View.INVISIBLE);
			}
			if (show_Store_Date.equals(show_end_Date)) {
				findViewById(R.id.RightArrowimage)
						.setVisibility(View.INVISIBLE);
			}
		}

	}

	private void initialize_user_interface() {
		var_editTextPurpose = (EditText) findViewById(R.id.editTextPurpose);
		var_editTextNote = (EditText) findViewById(R.id.editTextNote);

		var_LocationForm = (TextView) findViewById(R.id.ShowLocationForm);
		var_ShowDateDay = (TextView) findViewById(R.id.ShowDateDay);
		var_ShowSelectTask = (TextView) findViewById(R.id.ShowSelectTask);
		var_ShowLocationTo = (TextView) findViewById(R.id.ShowLocationTo);

		var_editTextPurpose.setOnClickListener(this);
	}

	private void initialize_user_interface_sameday() {

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
		Button SaveButton = (Button) findViewById(R.id.SaveButton);
		SaveButton.setEnabled(false);
		// findViewById(R.id.LocationTo).setOnClickListener(this);
		findViewById(R.id.SelectTask).setOnClickListener(this);
		findViewById(R.id.SaveButton).setOnClickListener(this);
		findViewById(R.id.leftimageArrow).setOnClickListener(this);
		findViewById(R.id.RightArrowimage).setOnClickListener(this);
		findViewById(R.id.btnCalendar).setOnClickListener(this);
		findViewById(R.id.btnDay).setOnClickListener(this);
		findViewById(R.id.Expense_entry).setOnClickListener(this);

		var_ShowDateDay = (TextView) findViewById(R.id.ShowDateDay);

		// shared prefs for next day============================================

		SharedPreferences example = getSharedPreferences(ValueStore, 0);
		date = example.getString("Date", "");
		store_CorpID = example.getString("CorpID", "");
		store_UserID = example.getString("UserID", "");
		show_WeekDate = example.getString("WeekDate", "");
		show_Store_Date = example.getString("ExpenseDate", "");
		store_task_id = String.valueOf(example.getInt("TaskID", 0));
		store_loc_from = example.getString("store_loc_from", "");
		store_loc_to = example.getString("store_loc_to", "");
		store_loc_id_from = String.valueOf(example.getInt("LocFromID", 0));
		store_loc_id_to = String.valueOf(example.getInt("LocToID", 0));
		show_Note = example.getString("Note", "");
		show_Purpose = example.getString("Purpose", "");
		expense_reportId = example.getInt("ExpenseReportID", 0);
		dayno = example.getString("DayNo", "");
		store_UserName = example.getString("UserLoginName", "");
		get_url = example.getString("URL", "");

		Log.wtf("expense_reportId", show_Store_Date + "");

		// shared prefs for next day============================================

		// ======================next date===============================

		/********** working for right arrow ***************/

		System.out.println("Store Date :" + show_Store_Date);
		String[] separated = show_Store_Date.split(" ");
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

			String new_converted_storeDate = var_show_storeDate_month
					.concat("-").concat(var_convertDay).concat("-")
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
				view_data = new_converted_storeDate + " " + "(" + s + ")";
				var_ShowDateDay.setText(view_data);
				// ============arrow click e data
				// change=================================
				show_Store_Date = new_converted_storeDate;
				// ============arrow click e data
				// change=================================

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// ======================next date===============================

		var_editTextPurpose = (EditText) findViewById(R.id.editTextPurpose);
		var_editTextNote = (EditText) findViewById(R.id.editTextNote);

		var_LocationForm = (TextView) findViewById(R.id.ShowLocationForm);
		var_ShowDateDay = (TextView) findViewById(R.id.ShowDateDay);
		var_ShowSelectTask = (TextView) findViewById(R.id.ShowSelectTask);
		var_ShowLocationTo = (TextView) findViewById(R.id.ShowLocationTo);
		var_editTextPurpose.setOnClickListener(this);
		if (exp.equalsIgnoreCase("new")) {
			var_ShowLocationTo.setText(store_loc_to);
		} else {
			findViewById(R.id.LocationForm).setOnClickListener(this);
			findViewById(R.id.LocationTo).setOnClickListener(this);
			var_ShowLocationTo.setText(null);

		}

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.LocationForm:
			Intent in_next = new Intent(InputForm.this, ShowLocationList.class);
			bd.putString("UserID", store_UserID);
			bd.putString("CorpID", store_CorpID);
			bd.putString("UserName", store_UserName);
			bd.putString("Password", store_password);
			bd.putString("UserRole", store_UserRole);
			bd.putString("StoreDate", show_Store_Date);
			bd.putString("StoreDay", show_Store_Day);
			bd.putString("StartDate", show_start_date);
			bd.putString("EndDate", show_end_Date);
			bd.putString("WeekDate", show_WeekDate);
			bd.putString("BERStatus", show_BERStatus);
			bd.putString("Exp_type", show_BERStatus);
			in_next.putExtras(bd);
			in_next.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivityForResult(in_next, 1);
			// overridePendingTransition(R.anim.left_to_right, 0);
			// finish();
			break;
		case R.id.SaveButton:
			Intent in_nextt = new Intent(InputForm.this, Direct_summary.class);
			bd.putString("UserID", store_UserID);
			bd.putString("CorpID", store_CorpID);
			bd.putString("UserName", store_UserName);
			bd.putString("Password", store_password);
			bd.putString("UserRole", store_UserRole);
			bd.putString("StoreDate", show_Store_Date);
			bd.putString("StoreDay", show_Store_Day);
			bd.putString("StartDate", show_start_date);
			bd.putString("EndDate", show_end_Date);
			bd.putString("WeekDate", show_WeekDate);
			bd.putString("BERStatus", show_BERStatus);
			bd.putString("Exp_type", show_BERStatus);
			// in_nextt.putExtra("AttachmentList", attachmentlist);
			in_nextt.putExtras(bd);
			in_nextt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(in_nextt);
			// overridePendingTransition(R.anim.left_to_right, 0);
			// finish();
			break;

		case R.id.LocationTo:

			if (store_loc_from != null && store_loc_from.length() > 0) {

				Intent in_next_one = new Intent(InputForm.this,
						ShowLocationToList.class);
				bd.putString("UserID", store_UserID);
				bd.putString("CorpID", store_CorpID);
				bd.putString("UserName", store_UserName);
				bd.putString("Password", store_password);
				bd.putString("UserRole", store_UserRole);
				bd.putString("StoreDate", show_Store_Date);
				bd.putString("StoreDay", show_Store_Day);
				bd.putString("LocName", store_loc_from);
				bd.putString("StartDate", show_start_date);
				bd.putString("EndDate", show_end_Date);
				bd.putString("WeekDate", show_WeekDate);
				bd.putString("BERStatus", show_BERStatus);
				in_next_one.putExtras(bd);
				in_next_one.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(in_next_one, 2);
				// overridePendingTransition(R.anim.left_to_right, 0);
			} else {

				AlertDialog alertDialog = new AlertDialog.Builder(
						InputForm.this).create();
				// Setting Dialog Title
				alertDialog.setTitle("Info");
				// Setting Dialog Message
				alertDialog.setMessage("Please select Location From");

				// Setting Icon to Dialog
				alertDialog.setIcon(R.drawable.delete);
				// Setting OK Button
				alertDialog.setButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// Write your code here to execute after
								// dialog
								// closed
							}
						});

				// Showing Alert Message
				alertDialog.show();
			}

			break;

		case R.id.SelectTask:
			if (store_loc_from == null || store_loc_from.equals("")) {
				AlertDialog alertDialog = new AlertDialog.Builder(
						InputForm.this).create();
				// Setting Dialog Title
				alertDialog.setTitle("Info");
				// Setting Dialog Message
				alertDialog.setMessage("Please Select Location From");
				// Setting Icon to Dialog
				alertDialog.setIcon(R.drawable.delete);
				// Setting OK Button
				alertDialog.setButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// Write your code here to execute after dialog
								// closed
							}
						});

				// Showing Alert Message
				alertDialog.show();
			} else if (store_loc_to == null || store_loc_to.equals("")) {
				AlertDialog alertDialog = new AlertDialog.Builder(
						InputForm.this).create();
				// Setting Dialog Title
				alertDialog.setTitle("Info");
				// Setting Dialog Message
				alertDialog.setMessage("Please select Location To");

				// Setting Icon to Dialog
				alertDialog.setIcon(R.drawable.delete);
				// Setting OK Button
				alertDialog.setButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// Write your code here to execute after dialog
								// closed
							}
						});

				// Showing Alert Message
				alertDialog.show();
			} else {
				Intent in_select = new Intent(InputForm.this, ShowTask.class);
				bd.putString("UserID", store_UserID);
				bd.putString("CorpID", store_CorpID);
				bd.putString("UserName", store_UserName);
				bd.putString("Password", store_password);
				bd.putString("UserRole", store_UserRole);
				bd.putString("StoreDate", show_Store_Date);
				bd.putString("StoreDay", show_Store_Day);
				bd.putString("LocName", store_loc_from);
				bd.putString("LocToName", store_loc_to);
				bd.putString("LocID", store_loc_id_from);
				bd.putString("StartDate", show_start_date);
				bd.putString("EndDate", show_end_Date);
				bd.putString("WeekDate", show_WeekDate);
				bd.putString("BERStatus", show_BERStatus);
				bd.putString("LocExpenseTypeID", store_locExpenseTypeID);
				in_select.putExtra("LocationInfo", store_result);
				in_select.putExtras(bd);
				in_select.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(in_select, 3);
			}

			break;
		// case R.id.editTextPurpose:
		// if (store_TaskName == null || store_TaskName.equals("")) {
		// // Toast.makeText(InputForm.this, "Please Select Task",
		// // Toast.LENGTH_SHORT).show();
		// AlertDialog alertDialog = new AlertDialog.Builder(
		// InputForm.this).create();
		// // Setting Dialog Title
		// alertDialog.setTitle("Info");
		// // Setting Dialog Message
		// alertDialog.setMessage("Please select Task");
		//
		// // Setting Icon to Dialog
		// alertDialog.setIcon(R.drawable.delete);
		// // Setting OK Button
		// alertDialog.setButton("OK",
		// new DialogInterface.OnClickListener() {
		// public void onClick(DialogInterface dialog,
		// int which) {
		// // Write your code here to execute after dialog
		// // closed
		// }
		// });
		//
		// // Showing Alert Message
		// alertDialog.show();
		// } else {
		// Intent in_select = new Intent(InputForm.this,
		// Show_Purpose.class);
		// startActivityForResult(in_select, 4);
		// }
		// break;
		case R.id.leftimageArrow:

			current_date_day = var_ShowDateDay.getText().toString();
			String[] separated1 = current_date_day.split(" ");
			String current_date1 = separated1[0].trim();
			System.out.println("Store Date new :" + current_date1);

			if (current_date1.length() == 10) {

				var_show_storeDate_month = current_date1.substring(0, 2);
				var_show_storeDate_day = current_date1.substring(3, 5);
				var_show_storeDate_year = current_date1.substring(6, 10);

				// =============arrow manage===========
				if (Integer.parseInt((show_start_date).substring(3, 5)) + 1 == Integer
						.parseInt((current_date_day).substring(3, 5))) {
					findViewById(R.id.leftimageArrow).setVisibility(
							View.INVISIBLE);
					findViewById(R.id.RightArrowimage).setVisibility(
							View.VISIBLE);
				} else {
					findViewById(R.id.RightArrowimage).setVisibility(
							View.VISIBLE);
				}
				// =============arrow manage===========

				if (Integer.parseInt((show_start_date).substring(3, 5)) < Integer
						.parseInt((current_date_day).substring(3, 5))) {

					System.out.println("Store Date new :" + "dhukche");
					change_day_for_left_arrow();
				} else {
					System.out.println("Store Date new :" + "dhukche na");
				}

			} else {

				var_show_storeDate_month = current_date1.substring(0, 2);
				var_show_storeDate_day = current_date1.substring(3, 4);
				var_show_storeDate_year = current_date1.substring(5, 9);

				Log.i("test", show_start_date.substring(3, 4));
				Log.i("test1", current_date_day.substring(3, 4));

				if (Integer.parseInt((show_start_date).substring(3, 5)) < Integer
						.parseInt((current_date_day).substring(3, 4))) {

					change_day_for_left_arrow();
				} else {
					System.out.println("Store Date new :" + "dhukche na");
				}
			}

			break;
		case R.id.RightArrowimage:
			current_date_day = var_ShowDateDay.getText().toString();
			String[] separated2 = current_date_day.split(" ");
			String current_date2 = separated2[0].trim();
			System.out.println("Store Date new :" + current_date2);

			if (current_date2.length() == 10) {

				var_show_storeDate_month = current_date2.substring(0, 2);
				var_show_storeDate_day = current_date2.substring(3, 5);
				var_show_storeDate_year = current_date2.substring(6, 10);

				var_show_storeDate_day_new = Integer
						.parseInt(var_show_storeDate_day) + 01;

				next_new_date = var_show_storeDate_month + "-"
						+ (var_show_storeDate_day_new) + "-"
						+ var_show_storeDate_year;
				try {
					today_date = sm.parse(next_new_date);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (d.before(today_date) || d.equals(today_date)) {
					Alert_dialog();
				} else {
					// =============arrow manage===========
					if (Integer.parseInt((show_end_Date).substring(3, 5)) - 1 == Integer
							.parseInt((current_date_day).substring(3, 5))) {
						findViewById(R.id.RightArrowimage).setVisibility(
								View.INVISIBLE);
						findViewById(R.id.leftimageArrow).setVisibility(
								View.VISIBLE);

					} else {
						findViewById(R.id.leftimageArrow).setVisibility(
								View.VISIBLE);
					}
					// =============arrow manage===========
					if (Integer.parseInt((show_end_Date).substring(3, 5)) > Integer
							.parseInt((current_date_day).substring(3, 5))) {

						System.out.println("Store Date new :" + "dhukche");
						change_day_for_right_arrow();
					} else {
						System.out.println("Store Date new :" + "dhukche na");
					}

				}

			} else {

				var_show_storeDate_month = current_date2.substring(0, 2);
				var_show_storeDate_day = current_date2.substring(3, 4);
				var_show_storeDate_year = current_date2.substring(5, 9);

				Log.i("test", show_start_date.substring(3, 4));
				Log.i("test1", current_date_day.substring(3, 4));
				// =============arrow manage===========
				if (Integer.parseInt((show_end_Date).substring(3, 5)) - 1 == Integer
						.parseInt((current_date_day).substring(3, 4))) {
					findViewById(R.id.RightArrowimage).setVisibility(
							View.INVISIBLE);
				}
				// =============arrow manage===========

				if (Integer.parseInt((show_end_Date).substring(3, 5)) > Integer
						.parseInt((current_date_day).substring(3, 4))) {

					change_day_for_right_arrow();
				} else {
					System.out.println("Store Date new :" + "dhukche na");
				}
			}

			// }

			break;

		case R.id.Expense_entry:
			store_TaskName = var_ShowSelectTask.getText().toString();
			show_Purpose = var_editTextPurpose.getText().toString();
			show_Note = var_editTextNote.getText().toString();

			if (store_loc_from == null || store_loc_to == null
					|| store_TaskName.equals("") || show_Purpose.equals("")) {
				// Toast.makeText(InputForm.this,
				// "Please Enter all mandatory field", Toast.LENGTH_SHORT)
				// .show();
				AlertDialog alertDialog = new AlertDialog.Builder(
						InputForm.this).create();
				// Setting Dialog Title
				alertDialog.setTitle("Info");

				// Setting Dialog Message
				alertDialog.setMessage("Please enter all mandatory fields");

				// Setting Icon to Dialog
				alertDialog.setIcon(R.drawable.delete);
				// Setting OK Button
				alertDialog.setButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// Write your code here to execute after dialog
								// closed
							}
						});

				// Showing Alert Message
				alertDialog.show();

			} else {
				try {

					SharedPreferences example1 = getSharedPreferences(
							ValueStore, 0);
					Editor editor1 = example1.edit();
					editor1.putString("Date", view_data);
					editor1.putString("CorpID", store_CorpID);
					editor1.putString("UserID", store_UserID);
					editor1.putString("WeekDate", show_WeekDate);
					editor1.putString("ExpenseDate", show_Store_Date);
					editor1.putInt("TaskID", Integer.parseInt(store_task_id));
					editor1.putString("store_loc_from", store_loc_from);
					editor1.putInt("LocFromID",
							Integer.parseInt(store_loc_id_from));
					editor1.putString("store_loc_to", store_loc_to);
					editor1.putInt("LocToID", Integer.parseInt(store_loc_id_to));
					editor1.putString("Note", show_Note);
					editor1.putString("Purpose", show_Purpose);
					editor1.putInt("ExpenseReportID", expense_reportId);
					editor1.putString("DayNo", "0");
					editor1.putString("UserLoginName", store_UserName);
					editor1.putString("StartDate", show_start_date);
					editor1.putString("EndDate", show_end_Date);
					if (var_DetailList != null)
						editor1.putString("DetailList",
								var_DetailList.toString());
					editor1.commit();
				} catch (Exception e) {
					// TODO: handle exception
				}

				Intent i = new Intent(InputForm.this, Expense_Travel.class);
				i.putExtra("UserID", store_UserID);
				i.putExtra("CorpID", store_CorpID);
				i.putExtra("UserName", store_UserName);
				i.putExtra("Password", store_password);
				i.putExtra("UserRole", store_UserRole);
				i.putExtra("StoreDate", show_Store_Date);
				i.putExtra("StoreDay", show_Store_Day);
				i.putExtra("LocName", store_loc_from);
				i.putExtra("StartDate", show_start_date);
				i.putExtra("EndDate", show_end_Date);
				i.putExtra("WeekDate", show_WeekDate);
				i.putExtra("BERStatus", show_BERStatus);
				i.putExtra("Loc_To", store_loc_id_to);
				i.putExtra("expenseList", expenseList);
				// i.putExtra("AttachmentList", attachmentlist);
				startActivity(i);
			}
			break;
		case R.id.btnCalendar:
			if (show_BERStatus.equals("0") || show_BERStatus.equals("1")
					|| show_BERStatus.equals("3")) {
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						InputForm.this);

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
								show_calendar();
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
				show_calendar();
			}

			break;
		case R.id.btnDay:
			if (show_BERStatus.equals("0") || show_BERStatus.equals("1")
					|| show_BERStatus.equals("3")) {
				AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(
						InputForm.this);

				// Setting Dialog Title
				alertDialog1.setTitle("Info...");

				// Setting Dialog Message
				alertDialog1.setMessage("Move to Daylist without saving ?");

				// Setting Icon to Dialog
				alertDialog1.setIcon(R.drawable.delete);

				// Setting Positive "Yes" Button
				alertDialog1.setPositiveButton("YES",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {

								// Write your code here to invoke YES
								// event"You clicked on YES",
								// Toast.LENGTH_SHORT).show();
								show_day();
							}
						});

				// Setting Negative "NO" Button
				alertDialog1.setNegativeButton("NO",
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
				alertDialog1.show();
			} else {
				show_day();
			}

			break;
		}
	}

	private void show_day() {
		Intent in_next = new Intent(InputForm.this, ShowCalendarList.class);
		Bundle bd = new Bundle();
		bd.putString("Date", show_Store_Date);
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
	}

	private void show_calendar() {

		SharedPreferences example1 = getSharedPreferences(ValueStore, 0);
		Editor editor1 = example1.edit();
		editor1.putString("Calender_save_flag", "saved").commit();
		Intent in_next = new Intent(InputForm.this, ViewCalendar.class);
		Bundle bd = new Bundle();
		bd.putString("UserID", store_UserID);
		bd.putString("CorpID", store_CorpID);
		bd.putString("UserName", store_UserName);
		bd.putString("Password", store_password);
		bd.putString("UserRole", store_UserRole);
		in_next.putExtras(bd);
		in_next.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(in_next);
	}

	private void change_day_for_right_arrow() {

		/********** working for right arrow ***************/

		System.out.println("Store Date :" + show_Store_Date);
		String current_date_day = var_ShowDateDay.getText().toString();
		String[] separated = current_date_day.split(" ");
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

			// if(var_show_storeDate_day.charAt(0)=='0'){
			// var_show_storeDate_day=var_show_storeDate_day.replace("0", "");
			// }else{
			// Log.i("sirsho", var_show_storeDate_day);
			// }

			if (Integer.valueOf(var_convertDay) <= 9) {

				new_converted_storeDate = var_show_storeDate_month.concat("-")
						.concat("0".concat(var_convertDay)).concat("-")
						.concat(var_show_storeDate_year);
			} else {

				new_converted_storeDate = var_show_storeDate_month.concat("-")
						.concat(var_convertDay).concat("-")
						.concat(var_show_storeDate_year);
			}

			String change_converted_storeDate_inDay = new_converted_storeDate;
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");

			Date convertedDate = new Date();
			try {
				convertedDate = dateFormat
						.parse(change_converted_storeDate_inDay);
				String s = (String) android.text.format.DateFormat.format(
						"EEEE", convertedDate);
				System.out.println("Check Date :" + s);
				view_data = new_converted_storeDate + " " + "(" + s + ")";
				var_ShowDateDay.setText(view_data);
				// ============arrow click e data
				// change=================================
				show_Store_Date = new_converted_storeDate;
				// save_result.clear();
				load_data_from_url();
				// ============arrow click e data
				// change=================================

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void change_day_for_left_arrow() {

		/****** working for left arrow **********/
		System.out.println("Store Date :" + show_Store_Date);
		String current_date_day = var_ShowDateDay.getText().toString();
		String[] separated = current_date_day.split(" ");
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

		int convert_day = Integer.parseInt(var_show_storeDate_day);
		int new_convert_day = convert_day - 1;

		Log.i("newdate", new_convert_day + "");
		String var_convertDay = String.valueOf(new_convert_day);

		if (Integer.valueOf(var_convertDay) <= 9) {

			new_converted_storeDate = var_show_storeDate_month.concat("-")
					.concat("0".concat(var_convertDay)).concat("-")
					.concat(var_show_storeDate_year);
		} else {

			new_converted_storeDate = var_show_storeDate_month.concat("-")
					.concat(var_convertDay).concat("-")
					.concat(var_show_storeDate_year);
		}

		String change_converted_storeDate_inDay = new_converted_storeDate;
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");

		Date convertedDate = new Date();

		try {
			convertedDate = dateFormat.parse(change_converted_storeDate_inDay);
			String s = (String) android.text.format.DateFormat.format("EEEE",
					convertedDate);
			System.out.println("Check Date :" + s);
			view_data = new_converted_storeDate + " " + "(" + s + ")";
			var_ShowDateDay.setText(view_data);

			show_Store_Date = new_converted_storeDate;
			// save_result.clear();
			load_data_from_url();

		} catch (Exception e) {
		}

	}

	/**
	 * Receiving activity result method will be called after closing the camera
	 * */

	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 1:
				store_loc_id_from = data.getStringExtra("LocID");
				store_loc_from = data.getStringExtra("LocName");
				store_result = (ArrayList<HashMap<String, String>>) data
						.getSerializableExtra("LocationInfo");
				var_LocationForm.setText(store_loc_from);
				break;
			case 2:
				store_loc_id_to = data.getStringExtra("LocID");
				store_loc_to = data.getStringExtra("LocToName");
				store_result = (ArrayList<HashMap<String, String>>) data
						.getSerializableExtra("LocationInfo");
				SharedPreferences example1 = getSharedPreferences(ValueStore, 0);
				Editor editor1 = example1.edit();
				editor1.putString("store_loc_to", store_loc_to).commit();
				var_ShowLocationTo.setText(store_loc_to);
				break;
			case 3:
				store_task_id = data.getExtras().getString("Id");
				store_TaskName = data.getStringExtra("Description");
				var_ShowSelectTask.setText(store_TaskName);
				break;
			case 4:

				purpose_name = data.getExtras().getString("Purpose_Name");

				Log.i("gggg", purpose_name);

				if (purpose_name.equals("Meeting with client")) {
					var_editTextPurpose.setText(purpose_name);
				} else {
					var_editTextPurpose.setText("");
					var_editTextPurpose.setClickable(true);
					var_editTextPurpose.setFocusable(true);
					var_editTextPurpose.setFocusableInTouchMode(true);
				}
				break;
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

	/****** Call AsyncTask for threading *************************/

	private class fetch_data_asynchronously extends
			AsyncTask<Void, Void, String> {
		ProgressDialog dialog = new ProgressDialog(InputForm.this);

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
			showResult();
		}
	}

	public String view_details_from_url2() {
		try {

			SharedPreferences example = getSharedPreferences(ValueStore, 0);
			get_url = example.getString("URL", "");

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
			expenseList = new ArrayList<HashMap<String, String>>();
			expenseList.clear();
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

				save_result.add(map);

				JSONArray ExpenseList = show.getJSONArray("ExpenseList");
				expense_count = ExpenseList.length();

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

					String var_chargable_qnt = show_ExpenseList
							.getString("ChargableQty");
					map.put("Expense", var_ExpenseList_Expense);
					map.put("ChargableQty", var_chargable_qnt);
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
					save_result.add(map);
					attachmentlist.add(map);
				}
				Log.d("save_result", save_result + "");
				/* result.add(map); */
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showResult() {

		/********* fetch save data from hash map *************/
		if (save_result != null && save_result.size() > 2) {
			Log.d("save_result", save_result + "");

			SaveButton.setBackgroundColor(Color.parseColor("#596582"));
			SaveButton.setEnabled(true);

			// for (int i = 0; i < save_result.size(); i++) {

			// String decode_image = save_result.get(i).get("ImageCode");
			// System.out.println("Purpose :" + decode_image);

			store_loc_id_from = save_result.get(1).get("LocFrom");

			store_loc_id_to = save_result.get(1).get("LocTo");

			store_task_id = save_result.get(1).get("TaskID");

			store_loc_from = save_result.get(1).get("LocFromName");
			var_LocationForm.setText(store_loc_from);

			store_loc_to = save_result.get(1).get("LocToName");
			var_ShowLocationTo.setText(store_loc_to);

			store_TaskName = save_result.get(1).get("TaskDesc");
			var_ShowSelectTask.setText(store_TaskName);

			String var_Purpose = save_result.get(1).get("Purpose");
			System.out.println("Purpose :" + var_Purpose);
			var_editTextPurpose.setText(var_Purpose);

			String var_Note = save_result.get(1).get("Note");
			System.out.println("Note :" + var_Note);

			var_editTextNote.setText(var_Note);

			for (int i = expense_count + 2; i < save_result.size(); i++) {
				Map<String, String> attachment = save_result.get(i);
				String var_imagecode = attachment.get("ImageCode");

				if (var_imagecode != null) {
					try {
						byte[] decodedString = Base64.decode(var_imagecode,
								Base64.DEFAULT);
						int index = i - 2 - expense_count;
						switch (index) {
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
						attachment.put("ID", "0");
						attachment.put("ExpenseDate", show_start_date);
						attachment.put("ImageURL", "");
						attachment.put("ImageCode", var_imagecode);
						var_Attachment.put(attachmentObject);
					} catch (IllegalArgumentException e) {
						Log.e("image", e.getMessage());
					}
				}
			}
		} else {
			System.out.println("Hay Ram");
			var_LocationForm.setText("");
			var_ShowLocationTo.setText("");
			var_ShowSelectTask.setText("");
			var_editTextPurpose.setText("");
			var_editTextNote.setText("");
			SaveButton.setBackgroundColor(Color.parseColor("#9BA5BF"));
			SaveButton.setEnabled(false);
		}
	}

	public void Alert_dialog() {
		AlertDialog alertDialog = new AlertDialog.Builder(InputForm.this)
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
}