package com.nextgenbermobileapp.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import com.nextgenbermobileapp.connectioninfo.CheckNetworkConnection;
import com.nextgenbermobileapp.connectioninfo.ConnectionURL;
import com.nextgenbermobileapp.info.MonthInfo;
import com.nextgenbermobileapp.info.WeekDateInfo;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.annotation.TargetApi;

@TargetApi(3)
public class ViewCalendar extends Activity implements OnClickListener {

	private static final String tag = "MyCalendarActivity";

	TextView currentMonth;
	private Button selectedDayMonthYearButton;
	private ImageView prevMonth;
	private ImageView nextMonth;
	private GridView calendarView;
	private GridCellAdapter adapter;
	private Calendar _calendar;
	@SuppressLint("NewApi")
	private int month, year;
	@SuppressWarnings("unused")
	private final DateFormat dateFormatter = new DateFormat();
	private static final String dateTemplate = "MMMM yyyy";

	ArrayList<Integer> store_weekDate;
	int month_count;
	ArrayList<MonthInfo> store_val_month_info;
	ArrayList<MonthInfo> store_result_info;
	ArrayList<WeekDateInfo> store_weekDate_info;

	CheckNetworkConnection cd;
	private Boolean isInternetPresent = false;

	String store_UserID, store_CorpID, store_UserName, store_password,
			store_UserRole, show_year_from_system, show_month_from_system,
			show_date_from_system, var_store_val_from_web_service;

	/*
	 * // JSON Node names private static final String TAG_WeekDates =
	 * "WeekDates"; private static final String TAG_WeekDate = "WeekDate";
	 * private static final String TAG_StartDate = "StartDate"; private static
	 * final String TAG_EndDate = "EndDate"; private static final String
	 * TAG_BERStatus = "BERStatus";
	 */

	// contacts JSONArray
	JSONArray contacts = null;
	ArrayList<Integer> result;

	// Hashmap for ListView
	ArrayList<HashMap<String, String>> result_weekDateInfo;

	private SharedPreferences example;

	private String Calender_save_flag;

	private String get_url;

	private int day;

	private String today;

	public static final String ValueStore = "Collection_Data";

	SimpleDateFormat sm = new SimpleDateFormat("MM-dd-yyyy");

	private Date d;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.calendar_view_activity);

		findViewById(R.id.refresh).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(),
						ViewCalendar.class);
				startActivity(i);
				finish();
			}
		});
		example = getSharedPreferences(ValueStore, 0);

		_calendar = Calendar.getInstance(Locale.getDefault());
		month = _calendar.get(Calendar.MONTH) + 1;
		year = _calendar.get(Calendar.YEAR);
		day = _calendar.get(Calendar.DATE);

		d = Calendar.getInstance().getTime();
		today = month + "-" + day + "-" + year;
		Log.d(tag, "Calendar Instance:= " + "Month: " + month + " " + "Year: "
				+ year);

		store_val_month_info = new ArrayList<MonthInfo>();
		store_result_info = new ArrayList<MonthInfo>();
		result = new ArrayList<Integer>();
		store_weekDate_info = new ArrayList<WeekDateInfo>();
		result_weekDateInfo = new ArrayList<HashMap<String, String>>();

		prevMonth = (ImageView) this.findViewById(R.id.prevMonth);
		prevMonth.setOnClickListener(this);

		currentMonth = (TextView) this.findViewById(R.id.currentMonth);
		currentMonth.setText(DateFormat.format(dateTemplate,
				_calendar.getTime()));// ===change

		nextMonth = (ImageView) this.findViewById(R.id.nextMonth);
		nextMonth.setOnClickListener(this);

		calendarView = (GridView) this.findViewById(R.id.calendar);

		cd = new CheckNetworkConnection(getApplicationContext());

		final Calendar c = Calendar.getInstance();

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate = df.format(c.getTime());
		System.out.println("Date Format :" + formattedDate);

		show_year_from_system = formattedDate.substring(0, 4);
		show_month_from_system = formattedDate.substring(5, 7);
		show_date_from_system = formattedDate.substring(8, 10);

		store_CorpID = example.getString("CorpID", "");
		store_UserID = example.getString("UserID", "");
		if (store_CorpID.equals("") && store_UserID.equals("")) {
			store_UserID = getIntent().getExtras().getString("UserID");
			store_CorpID = getIntent().getExtras().getString("CorpID");
			store_UserName = getIntent().getExtras().getString("UserName");
			store_password = getIntent().getExtras().getString("Password");
			store_UserRole = getIntent().getExtras().getString("UserRole");
		} else {

			store_CorpID = example.getString("CorpID", "");
			store_UserID = example.getString("UserID", "");

			Calender_save_flag = example.getString("Calender_save_flag", "");

			try {
				if (Calender_save_flag.equals("saved")) {
					show_month_from_system = example
							.getString("saved_mnth", "");
					month = Integer.parseInt(show_month_from_system);
				} else {
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

		isInternetPresent = cd.isConnectingToInternet();
		if (isInternetPresent) {

			// Editor editor1 = example.edit();
			// editor1.putString("show_year_from_system",
			// show_year_from_system);

			verify_user_fetch_data_asynchronously task1 = new verify_user_fetch_data_asynchronously();
			task1.execute();
		} else {
			Toast.makeText(ViewCalendar.this, "Check Your Internet Connection",
					Toast.LENGTH_SHORT).show();
		}

		findViewById(R.id.Logout).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// Intent i=new Intent(ViewCalendar.this,LoginActivity.class);
				// startActivity(i);
				final Calendar c = Calendar.getInstance();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String formattedDate = df.format(c.getTime());
				System.out.println("Date Format :" + formattedDate);

				show_year_from_system = formattedDate.substring(0, 4);
				show_month_from_system = formattedDate.substring(5, 7);
				show_date_from_system = formattedDate.substring(8, 10);

				Editor editor1 = example.edit();
				editor1.putString("saved_mnth", show_month_from_system);
				editor1.commit();
				finish();
			}
		});
	}

	/**
	 * 
	 * @param month
	 * @param year
	 */
	private void setGridCellAdapterToDate(int month, int year) {
		adapter = new GridCellAdapter(getApplicationContext(),
				R.id.calendar_day_gridcell);
		_calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
		currentMonth.setText(DateFormat.format(dateTemplate,
				_calendar.getTime()));
		adapter.notifyDataSetChanged();
		calendarView.setAdapter(adapter);
		calendarView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View pos, int arg2,
					long arg3) {
				System.out.println("Pos" + pos);
			}
		});
	}

	@Override
	public void onClick(View v) {
		if (v == prevMonth) {
			if (month <= 1) {
				month = 12;
				year--;

				if (!show_year_from_system.equals(year)) {
					if (isInternetPresent) {

						// Editor editor1 = example.edit();
						// editor1.putString("show_year_from_system",
						// show_year_from_system);
						show_year_from_system = String.valueOf(year);
						verify_user_fetch_data_asynchronously task1 = new verify_user_fetch_data_asynchronously();
						task1.execute();
					} else {
						Toast.makeText(ViewCalendar.this,
								"Check Your Internet Connection",
								Toast.LENGTH_SHORT).show();
					}
				}
			} else {
				month--;
			}
			Log.d(tag, "Setting Prev Month in GridCellAdapter: " + "Month: "
					+ month + " Year: " + year);

			show_year_from_system = String.valueOf(year);
			show_month_from_system = String.valueOf(month);
			view_data_for_contactList();

			/*********** Initialize Adapter **************/
			setGridCellAdapterToDate(month, year);
		}
		if (v == nextMonth) {
			if (month > 11) {
				month = 1;
				year++;
				if (!show_year_from_system.equals(year)) {
					if (isInternetPresent) {

						// Editor editor1 = example.edit();
						// editor1.putString("show_year_from_system",
						// show_year_from_system);
						show_year_from_system = String.valueOf(year);
						verify_user_fetch_data_asynchronously task1 = new verify_user_fetch_data_asynchronously();
						task1.execute();
					} else {
						Toast.makeText(ViewCalendar.this,
								"Check Your Internet Connection",
								Toast.LENGTH_SHORT).show();
					}
				}

			} else {
				month++;
			}
			Log.d(tag, "Setting Next Month in GridCellAdapter: " + "Month: "
					+ month + " Year: " + year);

			show_year_from_system = String.valueOf(year);
			show_month_from_system = String.valueOf(month);
			view_data_for_contactList();

			/*********** Initialize Adapter **************/
			setGridCellAdapterToDate(month, year);

		}

	}

	@Override
	public void onDestroy() {
		Log.d(tag, "Destroying View ...");
		super.onDestroy();
	}

	// Inner Class
	public class GridCellAdapter extends BaseAdapter implements OnClickListener {
		private static final String tag = "GridCellAdapter";
		private final Context _context;
		private ArrayList<Integer> result;
		private final List<String> list;
		private static final int DAY_OFFSET = 1;
		private final String[] weekdays = new String[] { "Sun", "Mon", "Tue",
				"Wed", "Thu", "Fri", "Sat" };
		private final String[] months = { "January", "February", "March",
				"April", "May", "June", "July", "August", "September",
				"October", "November", "December" };
		private final int[] daysOfMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30,
				31, 30, 31 };
		private int daysInMonth;
		private int currentDayOfMonth, currentDay, thirdDay, next_currentDay,
				next_thirdDay, thirdDayOfMonth, next_currentDayOne,
				next_thirdDayOne, next_currentDayTwo, next_thirdDayTwo,
				next_currentDayThree, SavecurrentDayOfMonth, SavecurrentDay,
				ReturncurrentDay, ApprovecurrentDay, Savenext_currentDay,
				Returnnext_currentDay, Approvenext_currentDay,
				Savenext_currentDayOne, Returnnext_currentDayOne,
				Approvenext_currentDayOne, ReturncurrentDayOfMonth,
				ApprovecurrentDayOfMonth, next_SavecurrentDayTwo,
				next_ReturncurrentDayTwo, next_ApprovecurrentDayTwo;
		private int currentWeekDay;
		private Button gridcell;
		private TextView num_events_per_day;
		private final HashMap<String, Integer> eventsPerMonthMap;

		HashMap<Integer, String> remainingDates;

		int start_weekDate;

		private final SimpleDateFormat dateFormatter = new SimpleDateFormat(
				"dd-MMM-yyyy");
		private Date start_date;
		private Date end_date;

		// Days in Current Month
		public GridCellAdapter(Context context, int textViewResourceId) {
			super();
			this._context = context;
			this.list = new ArrayList<String>();
			Log.d(tag, "==> Passed in Date FOR Month: " + month + " "
					+ "Year: " + year);

			this.result = new ArrayList<Integer>();
			Calendar calendar = Calendar.getInstance();

			remainingDates = new HashMap<Integer, String>();
			try {

				String store_berStatusFirst = result_weekDateInfo.get(0).get(
						"BERStatus");
				String store_berStatusSecond = result_weekDateInfo.get(1).get(
						"BERStatus");
				String store_berStatusThird = result_weekDateInfo.get(2).get(
						"BERStatus");
				String store_berStatusFourth = result_weekDateInfo.get(3).get(
						"BERStatus");
				String store_berStatusFifth = result_weekDateInfo.get(4).get(
						"BERStatus");

				String s = result_weekDateInfo.get(0).get("Day");
				String s1 = result_weekDateInfo.get(1).get("Day");
				String s2 = result_weekDateInfo.get(2).get("Day");
				String s3 = result_weekDateInfo.get(3).get("Day");
				String s4 = result_weekDateInfo.get(4).get("Day");

				// TODO added third else if
				if (store_berStatusFirst.equalsIgnoreCase("1")) {
					setSaveCurrentDayOfMonth(Integer.parseInt(s));
				} else if (store_berStatusFirst.equalsIgnoreCase("0")) {
					setCurrentDayOfMonth(Integer.parseInt(s));
				} else if (store_berStatusFirst.equalsIgnoreCase("2")) {
					setThirdDayOfMonth(Integer.parseInt(s));
				} else if (store_berStatusFirst.equalsIgnoreCase("3")) {
					setReturnCurrentDayOfMonth(Integer.parseInt(s));
				} else if (store_berStatusFirst.equalsIgnoreCase("4")) {
					setApproveCurrentDayOfMonth(Integer.parseInt(s));
				}

				// TODO added third else if
				if (store_berStatusSecond.equalsIgnoreCase("1")) {
					setSaveCurrentDay(Integer.parseInt(s1));
				} else if (store_berStatusSecond.equalsIgnoreCase("0")) {
					setCurrentDay(Integer.parseInt(s1));
				} else if (store_berStatusSecond.equalsIgnoreCase("2")) {
					setThirdDay(Integer.parseInt(s1));
				} else if (store_berStatusSecond.equalsIgnoreCase("3")) {
					setReturnCurrentDay(Integer.parseInt(s1));
				} else if (store_berStatusSecond.equalsIgnoreCase("4")) {
					setApproveCurrentDay(Integer.parseInt(s1));
				}

				// TODO added third else if
				if (store_berStatusThird.equalsIgnoreCase("1")) {
					setSaveNextCurrentDay(Integer.parseInt(s2));
				} else if (store_berStatusThird.equalsIgnoreCase("0")) {
					setNextCurrentDay(Integer.parseInt(s2));
				} else if (store_berStatusThird.equalsIgnoreCase("2")) {
					setThirdCurrentDay(Integer.parseInt(s2));
				} else if (store_berStatusThird.equalsIgnoreCase("3")) {
					setReturnNextCurrentDay(Integer.parseInt(s2));
				} else if (store_berStatusThird.equalsIgnoreCase("4")) {
					setApproveNextCurrentDay(Integer.parseInt(s2));
				}

				// TODO added third else if
				if (store_berStatusFourth.equalsIgnoreCase("1")) {
					setSaveNextCurrentDayOne(Integer.parseInt(s3));
				} else if (store_berStatusFourth.equalsIgnoreCase("0")) {
					setNextCurrentDayOne(Integer.parseInt(s3));
				} else if (store_berStatusFourth.equalsIgnoreCase("2")) {
					setThirdCurrentDayOne(Integer.parseInt(s3));
				} else if (store_berStatusFourth.equalsIgnoreCase("3")) {
					setReturnNextCurrentDayOne(Integer.parseInt(s3));
				} else if (store_berStatusFourth.equalsIgnoreCase("4")) {
					setApproveNextCurrentDayOne(Integer.parseInt(s3));
				}

				// TODO added third else if
				if (store_berStatusFifth.equalsIgnoreCase("1")) {
					setSaveNextCurrentDayTwo(Integer.parseInt(s4));
				} else if (store_berStatusFifth.equalsIgnoreCase("0")) {
					setNextCurrentDayTwo(Integer.parseInt(s4));
				} else if (store_berStatusFifth.equalsIgnoreCase("2")) {
					setThirdCurrentDayTwo(Integer.parseInt(s4));
				} else if (store_berStatusFifth.equalsIgnoreCase("3")) {
					setReturnNextCurrentDayTwo(Integer.parseInt(s4));
				} else if (store_berStatusFifth.equalsIgnoreCase("4")) {
					setApproveNextCurrentDayTwo(Integer.parseInt(s4));
				}

				for (int i = 5; i < result_weekDateInfo.size(); i++) {
					String day = result_weekDateInfo.get(i).get("Day");
					String berStatus = result_weekDateInfo.get(i).get(
							"BERStatus");
					if (berStatus.equalsIgnoreCase("0"))
						remainingDates.put(Integer.parseInt(day), "BLUE");
					else if (berStatus.equalsIgnoreCase("1"))
						remainingDates.put(Integer.parseInt(day), "Yellow");
					else if (berStatus.equalsIgnoreCase("2"))
						remainingDates.put(Integer.parseInt(day), "Pink");
					else if (berStatus.equalsIgnoreCase("3"))
						remainingDates.put(Integer.parseInt(day), "orange");
					else if (berStatus.equalsIgnoreCase("4"))
						remainingDates.put(Integer.parseInt(day), "lightPink");
				}

			} catch (Exception e) {
				// TODO: handle exception
			}

			setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));

			// Print Month
			printMonth(month, year);

			_calendar
					.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
			currentMonth.setText(DateFormat.format(dateTemplate,
					_calendar.getTime()));

			// Find Number of Events
			eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
		}

		private String getMonthAsString(int i) {
			return months[i];
		}

		private String getWeekDayAsString(int i) {
			return weekdays[i];
		}

		private int getNumberOfDaysOfMonth(int i) {
			return daysOfMonth[i];
		}

		public String getItem(int position) {
			return list.get(position);
		}

		@Override
		public int getCount() {
			return list.size();
		}

		/**
		 * Prints Month
		 * 
		 * @param mm
		 * @param yy
		 */
		private void printMonth(int mm, int yy) {
			Log.d(tag, "==> printMonth: mm: " + mm + " " + "yy: " + yy);
			int trailingSpaces = 0;
			int daysInPrevMonth = 0;
			int prevMonth = 0;
			int prevYear = 0;
			int nextMonth = 0;
			int nextYear = 0;

			int currentMonth = mm - 1;
			String currentMonthName = getMonthAsString(currentMonth);
			daysInMonth = getNumberOfDaysOfMonth(currentMonth);

			Log.d(tag, "Current Month: " + currentMonthName + " having "
					+ daysInMonth + " days.");

			GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);
			Log.d(tag, "Gregorian Calendar:= " + cal.getTime().toString());

			if (currentMonth == 11) {
				prevMonth = currentMonth - 1;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				nextMonth = 0;
				prevYear = yy;
				nextYear = yy + 1;
				Log.d(tag, "*->PrevYear: " + prevYear + " PrevMonth:"
						+ prevMonth + " NextMonth: " + nextMonth
						+ " NextYear: " + nextYear);
			} else if (currentMonth == 0) {
				prevMonth = 11;
				prevYear = yy - 1;
				nextYear = yy;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				nextMonth = 1;
				Log.d(tag, "**--> PrevYear: " + prevYear + " PrevMonth:"
						+ prevMonth + " NextMonth: " + nextMonth
						+ " NextYear: " + nextYear);
			} else {
				prevMonth = currentMonth - 1;
				nextMonth = currentMonth + 1;
				nextYear = yy;
				prevYear = yy;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				Log.d(tag, "***---> PrevYear: " + prevYear + " PrevMonth:"
						+ prevMonth + " NextMonth: " + nextMonth
						+ " NextYear: " + nextYear);
			}

			int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
			trailingSpaces = currentWeekDay;

			Log.d(tag, "Week Day:" + currentWeekDay + " is "
					+ getWeekDayAsString(currentWeekDay));
			Log.d(tag, "No. Trailing space to Add: " + trailingSpaces);
			Log.d(tag, "No. of Days in Previous Month: " + daysInPrevMonth);

			if (cal.isLeapYear(cal.get(Calendar.YEAR))) {
				if (mm == 2)
					++daysInMonth;
				else if (mm == 3)
					++daysInPrevMonth;
			}

			// Trailing Month days
			for (int i = 0; i < trailingSpaces; i++) {
				Log.d(tag,
						"PREV MONTH:= "
								+ prevMonth
								+ " => "
								+ getMonthAsString(prevMonth)
								+ " "
								+ String.valueOf((daysInPrevMonth
										- trailingSpaces + DAY_OFFSET)
										+ i));
				list.add(String
						.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET)
								+ i)
						+ "-GREY"
						+ "-"
						+ getMonthAsString(prevMonth)
						+ "-"
						+ prevYear);
			}

			// Current Month Days
			for (int i = 1; i <= daysInMonth; i++) {
				Log.d(currentMonthName, String.valueOf(i) + " "
						+ getMonthAsString(currentMonth) + " " + yy);

				if (i == getCurrentDayOfMonth()) {
					list.add(String.valueOf(i) + "-BLUE" + "-"
							+ getMonthAsString(currentMonth) + "-" + yy);
				} else if (i == getSaveCurrentDayOfMonth()) {
					list.add(String.valueOf(i) + "-Yellow" + "-"
							+ getMonthAsString(currentMonth) + "-" + yy);
				} else if (i == getReturnCurrentDayOfMonth()) {
					list.add(String.valueOf(i) + "-orange" + "-"
							+ getMonthAsString(currentMonth) + "-" + yy);
				} else if (i == getApproveCurrentDayOfMonth()) {
					list.add(String.valueOf(i) + "-lightPink" + "-"
							+ getMonthAsString(currentMonth) + "-" + yy);
				} else if (i == getThirdDayOfMonth()) {
					list.add(String.valueOf(i) + "-Pink" + "-"
							+ getMonthAsString(currentMonth) + "-" + yy);
				} else if (i == getCurrentDay()) {
					list.add(String.valueOf(i) + "-BLUE" + "-"
							+ getMonthAsString(currentMonth) + "-" + yy);
				} else if (i == getSaveCurrentDay()) {
					list.add(String.valueOf(i) + "-Yellow" + "-"
							+ getMonthAsString(currentMonth) + "-" + yy);
				} else if (i == getReturnCurrentDay()) {
					list.add(String.valueOf(i) + "-orange" + "-"
							+ getMonthAsString(currentMonth) + "-" + yy);
				} else if (i == getApproveCurrentDay()) {
					list.add(String.valueOf(i) + "-lightPink" + "-"
							+ getMonthAsString(currentMonth) + "-" + yy);
				} else if (i == getThirdDay()) {
					list.add(String.valueOf(i) + "-Pink" + "-"
							+ getMonthAsString(currentMonth) + "-" + yy);
				}

				else if (i == getNextCurrentDay()) {
					list.add(String.valueOf(i) + "-BLUE" + "-"
							+ getMonthAsString(currentMonth) + "-" + yy);
				} else if (i == getSaveNextCurrentDay()) {
					list.add(String.valueOf(i) + "-Yellow" + "-"
							+ getMonthAsString(currentMonth) + "-" + yy);
				} else if (i == getReturnNextCurrentDay()) {
					list.add(String.valueOf(i) + "-orange" + "-"
							+ getMonthAsString(currentMonth) + "-" + yy);
				} else if (i == getApproveNextCurrentDay()) {
					list.add(String.valueOf(i) + "-lightPink" + "-"
							+ getMonthAsString(currentMonth) + "-" + yy);
				} else if (i == getThirdCurrentDay()) {
					list.add(String.valueOf(i) + "-Pink" + "-"
							+ getMonthAsString(currentMonth) + "-" + yy);
				}

				else if (i == getNextCurrentDayOne()) {
					list.add(String.valueOf(i) + "-BLUE" + "-"
							+ getMonthAsString(currentMonth) + "-" + yy);
				} else if (i == getSaveNextCurrentDayOne()) {
					list.add(String.valueOf(i) + "-Yellow" + "-"
							+ getMonthAsString(currentMonth) + "-" + yy);
				} else if (i == getReturnNextCurrentDayOne()) {
					list.add(String.valueOf(i) + "-orange" + "-"
							+ getMonthAsString(currentMonth) + "-" + yy);
				} else if (i == getApproveNextCurrentDayOne()) {
					list.add(String.valueOf(i) + "-lightPink" + "-"
							+ getMonthAsString(currentMonth) + "-" + yy);
				} else if (i == getThirdCurrentDayOne()) {
					list.add(String.valueOf(i) + "-Pink" + "-"
							+ getMonthAsString(currentMonth) + "-" + yy);
				}

				else if (i == getNextCurrentDayTwo()) {
					list.add(String.valueOf(i) + "-BLUE" + "-"
							+ getMonthAsString(currentMonth) + "-" + yy);
				}

				else if (i == getSaveNextCurrentDayTwo()) {
					list.add(String.valueOf(i) + "-Yellow" + "-"
							+ getMonthAsString(currentMonth) + "-" + yy);
				} else if (i == getReturnNextCurrentDayTwo()) {
					list.add(String.valueOf(i) + "-orange" + "-"
							+ getMonthAsString(currentMonth) + "-" + yy);
				} else if (i == getApproveNextCurrentDayTwo()) {
					list.add(String.valueOf(i) + "-lightPink" + "-"
							+ getMonthAsString(currentMonth) + "-" + yy);
				}

				else if (i == getThirdCurrentDayTwo()) {
					list.add(String.valueOf(i) + "-Pink" + "-"
							+ getMonthAsString(currentMonth) + "-" + yy);
				} else if (remainingDates.containsKey(i)) {
					list.add(String.valueOf(i) + "-" + remainingDates.get(i)
							+ "-" + getMonthAsString(currentMonth) + "-" + yy);
				}

				else {
					list.add(String.valueOf(i) + "-WHITE" + "-"
							+ getMonthAsString(currentMonth) + "-" + yy);
				}
				Log.d("BOOL", remainingDates.containsKey(i) + "");
			}
			// Leading Month days
			for (int i = 0; i < list.size() % 7; i++) {
				Log.d(tag, "NEXT MONTH:= " + getMonthAsString(nextMonth));
				list.add(String.valueOf(i + 1) + "-GREY" + "-"
						+ getMonthAsString(nextMonth) + "-" + nextYear);
			}
		}

		/**
		 * NOTE: YOU NEED TO IMPLEMENT THIS PART Given the YEAR, MONTH, retrieve
		 * ALL entries from a SQLite database for that month. Iterate over the
		 * List of All entries, and get the dateCreated, which is converted into
		 * day.
		 * 
		 * @param year
		 * @param month
		 * @return
		 */
		private HashMap<String, Integer> findNumberOfEventsPerMonth(int year,
				int month) {
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			return map;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View row = convertView;
			if (row == null) {
				LayoutInflater inflater = (LayoutInflater) _context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = inflater.inflate(R.layout.screen_gridcell, parent, false);
			}

			// Get a reference to the Day gridcell
			gridcell = (Button) row.findViewById(R.id.calendar_day_gridcell);
			gridcell.setOnClickListener(this);
			// gridcell.setId(position);*/

			// ACCOUNT FOR SPACING
			Log.d(tag, "Current Day: " + getCurrentDayOfMonth());
			Log.d("color", list.get(position));
			String[] day_color = list.get(position).split("-");
			String theday = day_color[0];
			String themonth = day_color[2];
			String theyear = day_color[3];

			if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null)) {
				if (eventsPerMonthMap.containsKey(theday)) {
					num_events_per_day = (TextView) row
							.findViewById(R.id.num_events_per_day);
					Integer numEvents = (Integer) eventsPerMonthMap.get(theday);
					num_events_per_day.setText(numEvents.toString());
				}
			}

			// Set the Day GridCell
			gridcell.setText(theday);
			gridcell.setTag(theday + "-" + themonth + "-" + theyear);
			Log.d(tag, "Setting GridCell " + theday + "-" + themonth + "-"
					+ theyear);
			Log.d(tag, "Setting color " + day_color[1]);

			if (day_color[1].equals("GREY")) {
				gridcell.setTextColor(getResources()
						.getColor(R.color.lightgray));
			}
			if (day_color[1].equals("WHITE")) {

				gridcell.setTextColor(getResources().getColor(R.color.blue));
				/* gridcell.setBackgroundColor(Color.parseColor("#0000FF")); */
			}
			if (day_color[1].equals("BLUE")) {
				gridcell.setBackgroundColor(Color.parseColor("#0000FF"));
				gridcell.setTextColor(getResources().getColor(R.color.white));
			}

			if (day_color[1].equals("Yellow")) {
				gridcell.setBackgroundColor(Color.parseColor("#FDEA93"));
				gridcell.setTextColor(getResources().getColor(R.color.white));
			}

			if (day_color[1].equals("Pink")) {
				gridcell.setBackgroundColor(Color.parseColor("#FFD9FF"));
				gridcell.setTextColor(Color.WHITE);
			}
			if (day_color[1].equals("orange")) {
				gridcell.setBackgroundColor(Color.parseColor("#F9B58E"));
				gridcell.setTextColor(Color.WHITE);
			}
			if (day_color[1].equals("lightPink")) {
				gridcell.setBackgroundColor(Color.parseColor("#97E8F9"));
				gridcell.setTextColor(Color.WHITE);
			}
			return row;
		}

		@Override
		public void onClick(View view) {

			/* String pos=store_val_month_info.get(view).getWeekDate(); */
			String date_month_year = (String) view.getTag();
			Log.e("Selected date", date_month_year);

			String fetch_Date = date_month_year.substring(0, 2)
					.replace("-", "");
			System.out.println("Fetch Date :" + fetch_Date);

			for (int j = 0; j < result_weekDateInfo.size(); j++) {

				String show_Day = result_weekDateInfo.get(j).get("Day");
				if (fetch_Date.equals(show_Day)) {

					String start_Date = result_weekDateInfo.get(j).get(
							"StartDateInfo");
					try {
						start_date = sm.parse(start_Date);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("Start Date :" + start_Date);

					String end_Date = result_weekDateInfo.get(j).get(
							"EndtDateInfo");
					System.out.println("End Date :" + end_Date);
					try {
						end_date = sm.parse(end_Date);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					String ber_Status = result_weekDateInfo.get(j).get(
							"BERStatus");
					System.out.println("BER Status :" + ber_Status);

					String week_Date = result_weekDateInfo.get(j).get(
							"WeekDateInfo");
					System.out.println("BER Status :" + week_Date);

					if (d.after(start_date) || start_Date.equals(today)) {
						Log.i("pp", "no problm");

						Intent in_next = new Intent(ViewCalendar.this,
								ShowCalendarList.class);

						SharedPreferences example1 = getSharedPreferences(
								ValueStore, 0);
						Editor editor1 = example1.edit();
						editor1.putString("BERStatus", ber_Status);
						editor1.putString("StartDate", start_Date);
						editor1.putString("EndDate", end_Date);
						editor1.putString("WeekDate", week_Date);
						editor1.putString("saved_mnth", show_month_from_system);
						editor1.commit();
						Bundle bd = new Bundle();
						bd.putString("Date", date_month_year);
						bd.putString("StartDate", start_Date);
						bd.putString("EndDate", end_Date);
						bd.putString("BERStatus", ber_Status);
						bd.putString("WeekDate", week_Date);
						bd.putString("UserID", store_UserID);
						bd.putString("CorpID", store_CorpID);
						bd.putString("UserName", store_UserName);
						bd.putString("Password", store_password);
						bd.putString("UserRole", store_UserRole);
						in_next.putExtras(bd);
						in_next.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(in_next);
					} else {
						AlertDialog alertDialog = new AlertDialog.Builder(
								ViewCalendar.this).create();

						// Setting Dialog Title
						alertDialog.setTitle("Info...");

						// Setting Dialog Message
						alertDialog.setMessage("Invalid Expense Date");

						// Setting Icon to Dialog
						alertDialog.setIcon(R.drawable.delete);

						// Setting OK Button
						alertDialog.setButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										
									}
								});

						// Showing Alert Message
						alertDialog.show();
					}

				}

			}
		}

		public int getCurrentDayOfMonth() {
			return currentDayOfMonth;
		}

		private void setCurrentDayOfMonth(int currentDayOfMonth) {
			this.currentDayOfMonth = currentDayOfMonth;
		}

		private void setThirdDayOfMonth(int currentDayOfMonth) {
			this.thirdDayOfMonth = currentDayOfMonth;
		}

		private int getThirdDayOfMonth() {
			return thirdDayOfMonth;
		}

		public int getSaveCurrentDayOfMonth() {
			return SavecurrentDayOfMonth;
		}

		private void setSaveCurrentDayOfMonth(int SavecurrentDayOfMonth) {
			this.SavecurrentDayOfMonth = SavecurrentDayOfMonth;
		}

		public int getReturnCurrentDayOfMonth() {
			return ReturncurrentDayOfMonth;
		}

		private void setReturnCurrentDayOfMonth(int ReturncurrentDayOfMonth) {
			this.ReturncurrentDayOfMonth = ReturncurrentDayOfMonth;
		}

		public int getApproveCurrentDayOfMonth() {
			return ApprovecurrentDayOfMonth;
		}

		private void setApproveCurrentDayOfMonth(int ApprovecurrentDayOfMonth) {
			this.ApprovecurrentDayOfMonth = ApprovecurrentDayOfMonth;
		}

		public int getCurrentDay() {
			return currentDay;
		}

		private void setCurrentDay(int currentDay) {
			this.currentDay = currentDay;
		}

		public int getSaveCurrentDay() {
			return SavecurrentDay;
		}

		private void setSaveCurrentDay(int SavecurrentDay) {
			this.SavecurrentDay = SavecurrentDay;
		}

		public int getReturnCurrentDay() {
			return ReturncurrentDay;
		}

		private void setReturnCurrentDay(int ReturncurrentDay) {
			this.ReturncurrentDay = ReturncurrentDay;
		}

		public int getApproveCurrentDay() {
			return ApprovecurrentDay;
		}

		private void setApproveCurrentDay(int ApprovecurrentDay) {
			this.ApprovecurrentDay = ApprovecurrentDay;
		}

		public int getNextCurrentDay() {
			return next_currentDay;
		}

		private void setNextCurrentDay(int next_currentDay) {
			this.next_currentDay = next_currentDay;
		}

		public int getSaveNextCurrentDay() {
			return Savenext_currentDay;
		}

		private void setSaveNextCurrentDay(int Savenext_currentDay) {
			this.Savenext_currentDay = Savenext_currentDay;
		}

		public int getReturnNextCurrentDay() {
			return Returnnext_currentDay;
		}

		private void setReturnNextCurrentDay(int Returnnext_currentDay) {
			this.Returnnext_currentDay = Returnnext_currentDay;
		}

		public int getApproveNextCurrentDay() {
			return Approvenext_currentDay;
		}

		private void setApproveNextCurrentDay(int Approvenext_currentDay) {
			this.Approvenext_currentDay = Approvenext_currentDay;
		}

		public void setThirdCurrentDay(int parseInt) {
			next_thirdDay = parseInt;
		}

		public int getThirdCurrentDay() {
			return next_thirdDay;
		}

		public void setThirdDay(int parseInt) {
			this.thirdDay = parseInt;
		}

		public int getThirdDay() {
			return thirdDay;
		}

		private void setThirdCurrentDayTwo(int parseInt) {
			this.next_thirdDayTwo = parseInt;
		}

		private int getThirdCurrentDayTwo() {
			return next_thirdDayTwo;
		}

		private void setThirdCurrentDayOne(int parseInt) {
			this.next_thirdDayOne = parseInt;
		}

		private int getThirdCurrentDayOne() {
			return next_thirdDayOne;
		}

		public int getNextCurrentDayOne() {
			return next_currentDayOne;
		}

		private void setNextCurrentDayOne(int next_currentDayOne) {
			this.next_currentDayOne = next_currentDayOne;
		}

		public int getSaveNextCurrentDayOne() {
			return Savenext_currentDayOne;
		}

		private void setSaveNextCurrentDayOne(int Savenext_currentDayOne) {
			this.Savenext_currentDayOne = Savenext_currentDayOne;
		}

		public int getReturnNextCurrentDayOne() {
			return Returnnext_currentDayOne;
		}

		private void setReturnNextCurrentDayOne(int Returnnext_currentDayOne) {
			this.Returnnext_currentDayOne = Returnnext_currentDayOne;
		}

		public int getApproveNextCurrentDayOne() {
			return Approvenext_currentDayOne;
		}

		private void setApproveNextCurrentDayOne(int Approvenext_currentDayOne) {
			this.Approvenext_currentDayOne = Approvenext_currentDayOne;
		}

		public int getNextCurrentDayTwo() {
			return next_currentDayTwo;
		}

		private void setNextCurrentDayTwo(int next_currentDayTwo) {
			this.next_currentDayTwo = next_currentDayTwo;
		}

		public int getSaveNextCurrentDayTwo() {
			return next_SavecurrentDayTwo;
		}

		private void setSaveNextCurrentDayTwo(int next_SavecurrentDayTwo) {
			this.next_SavecurrentDayTwo = next_SavecurrentDayTwo;
		}

		public int getReturnNextCurrentDayTwo() {
			return next_ReturncurrentDayTwo;
		}

		private void setReturnNextCurrentDayTwo(int next_ReturncurrentDayTwo) {
			this.next_ReturncurrentDayTwo = next_ReturncurrentDayTwo;
		}

		public int getApproveNextCurrentDayTwo() {
			return next_ApprovecurrentDayTwo;
		}

		private void setApproveNextCurrentDayTwo(int next_ApprovecurrentDayTwo) {
			this.next_ApprovecurrentDayTwo = next_ApprovecurrentDayTwo;
		}

		public void setCurrentWeekDay(int currentWeekDay) {
			this.currentWeekDay = currentWeekDay;
		}

		public int getCurrentWeekDay() {
			return currentWeekDay;
		}
	}

	/******** async task for calendar data fetching ***************/

	private class verify_user_fetch_data_asynchronously extends
			AsyncTask<String, Void, Boolean> {
		ProgressDialog dialog = new ProgressDialog(ViewCalendar.this);

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
			if (ViewCalendar.this != null && !ViewCalendar.this.isFinishing())
				dialog.dismiss();
			view_data_for_contactList();

			/*********** Initialize Adapter **************/
			adapter = new GridCellAdapter(getApplicationContext(),
					R.id.calendar_day_gridcell);
			adapter.notifyDataSetChanged();
			calendarView.setAdapter(adapter);

		}
	}

	public void view_details_from_url() {
		try {

			SharedPreferences example = getSharedPreferences(ValueStore, 0);
			get_url = example.getString("URL", "");

			SoapObject request = new SoapObject(
					ConnectionURL.NAMESPACE_For_WeekDateList,
					ConnectionURL.METHOD_NAME_For_WeekDateList);
			// ********** for login**************//*
			System.out.println("Year of System :" + show_year_from_system);
			System.out.println("Month of System :" + show_month_from_system);

			request.addProperty("CorpID", store_CorpID);
			request.addProperty("UserID", store_UserID);
			request.addProperty("Year", show_year_from_system);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(get_url
					+ ConnectionURL.URL_For_WeekDateList);
			androidHttpTransport.call(
					ConnectionURL.SOAP_ACTION_For_WeekDateList, envelope);
			Object result = (Object) envelope.getResponse();
			// tv.setText(result.toString());
			var_store_val_from_web_service = result.toString();
			System.out.println("Print Result :"
					+ var_store_val_from_web_service);

			/* store_data_for_login(); */

			store_yearwise_data();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void store_yearwise_data() {
		try {

			JSONObject js = new JSONObject(var_store_val_from_web_service);
			String var_Year = js.getString("Year");
			System.out.println(var_Year);

			store_val_month_info.clear();

			// Getting Array of Contacts
			JSONArray contacts_months = js.getJSONArray("Months");

			for (int i = 0; i < contacts_months.length(); i++) {

				JSONObject c = contacts_months.getJSONObject(i);
				String month = c.getString("Month");
				System.out.println("Month :" + month);

				/*
				 * // creating new HashMap HashMap<String, String> map = new
				 * HashMap<String, String>(); map.put("month",month);
				 */

				JSONArray contacts_weeks = c.getJSONArray("WeekDates");

				for (int j = 0; j < contacts_weeks.length(); j++) {

					JSONObject show = contacts_weeks.getJSONObject(j);

					String var_Day = show.getString("Day");
					String var_WeekDate = show.getString("WeekDate");
					String var_StartDate = show.getString("StartDate");
					String var_EndDate = show.getString("EndDate");
					String var_BERStatus = show.getString("BERStatus");

					MonthInfo month_info_name = new MonthInfo(month, var_Day,
							var_WeekDate, var_StartDate, var_EndDate,
							var_BERStatus);
					store_val_month_info.add(month_info_name);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void view_data_for_contactList() {

		if (result_weekDateInfo.size() > 0)
			result_weekDateInfo.clear();

		for (int i = 0; i < store_val_month_info.size(); i++) {

			String show_month = store_val_month_info.get(i).getMonth();

			/*
			 * please check this section========
			 * if(Integer.valueOf(show_month)<=9){
			 * show_month="0".concat(show_month); }else{ show_month=show_month;
			 * }
			 */
			System.out.println("Show Month :" + show_month);
			System.out.println("Show Month From System :"
					+ show_month_from_system);

			if (show_month_from_system.equals(show_month)) {

				// tmp hashmap for single contact
				HashMap<String, String> result_info = new HashMap<String, String>();

				String Day = store_val_month_info.get(i).getDay();
				result_info.put("Day", Day);

				String week_date = store_val_month_info.get(i).getWeekDate();
				result_info.put("WeekDateInfo", week_date);

				String StartDate = store_val_month_info.get(i).getStartDate();
				result_info.put("StartDateInfo", StartDate);

				String EndDate = store_val_month_info.get(i).getEndDate();
				result_info.put("EndtDateInfo", EndDate);

				String BERStatus = store_val_month_info.get(i).getBERStatus();
				result_info.put("BERStatus", BERStatus);

				result_weekDateInfo.add(result_info);

			}

		}
	}

	@Override
	public void onBackPressed() {
		final Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate = df.format(c.getTime());
		System.out.println("Date Format :" + formattedDate);

		show_year_from_system = formattedDate.substring(0, 4);
		show_month_from_system = formattedDate.substring(5, 7);
		show_date_from_system = formattedDate.substring(8, 10);

		Editor editor1 = example.edit();
		editor1.putString("saved_mnth", show_month_from_system);
		editor1.commit();
		finish();
	}
}
