package com.nextgenbermobileapp.activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nextgenbermobileapp.R;
import com.nextgenbermobileapp.config.Config;
import com.nextgenbermobileapp.connectioninfo.CheckNetworkConnection;
import com.nextgenbermobileapp.connectioninfo.ConnectionURL;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LoginActivity extends Activity {

	EditText var_CorpId, var_UserId, var_Password;
//	ImageView var_LoginImage;
	ImageView var_LoginImage_modified;

	private CheckNetworkConnection cd;
	private Boolean isInternetPresent = false;

	String var_store_val_from_web_service, store_UserID, store_CorpID,
			store_UserName, store_password, store_UserRole, store_Msg, corp_id,
			user_id, password;

	public static final String ValueStore = "Collection_Data";

	CheckBox remember_me;
	private String get_CorpID;
	private String get_UserID;
	private String get_pass;
	private String get_status;
	private String get_url;
	private SharedPreferences example;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_activity);

		findViewById(R.id.exit).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		cd = new CheckNetworkConnection(getApplicationContext());

		/***** initialize view *************/

		var_CorpId = (EditText) findViewById(R.id.CorpId);
		var_UserId = (EditText) findViewById(R.id.UserId);
		var_Password = (EditText) findViewById(R.id.Password);
//		var_LoginImage = (ImageView) findViewById(R.id.LoginImage);
		var_LoginImage_modified = (ImageView) findViewById(R.id.LoginImage);
		remember_me = (CheckBox) findViewById(R.id.remember);

		example = getSharedPreferences(ValueStore, 0);
		get_CorpID = example.getString("CorpID", "");
		get_UserID = example.getString("login_User_ID", "");
		get_pass = example.getString("login_User_pass", "");
		get_status = example.getString("chk_flag", "");

		if (get_status.equals("1")) {
			var_CorpId.setText(get_CorpID);
			var_UserId.setText(get_UserID);
			var_Password.setText(get_pass);
			remember_me.setChecked(true);
		}

		// remember_me.setOnCheckedChangeListener(new OnCheckedChangeListener()
		// {
		//
		// @Override
		// public void onCheckedChanged(CompoundButton buttonView, boolean
		// isChecked) {
		// // TODO Auto-generated method stub
		// if(isChecked){
		// // Toast.makeText(getApplicationContext(), "chk", 2000).show();
		//
		// SharedPreferences example1 = getSharedPreferences(
		// ValueStore, 0);
		// Editor editor1 = example1.edit();
		// editor1.putString("UserID", store_UserID);
		// editor1.commit();
		// }
		//
		// }
		// });

		/*var_LoginImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				corp_id = var_CorpId.getText().toString();
				user_id = var_UserId.getText().toString();
				password = var_Password.getText().toString();
				if (remember_me.isChecked()) {
					SharedPreferences example1 = getSharedPreferences(
							ValueStore, 0);
					Editor editor1 = example1.edit();
					editor1.putString("chk_flag", "1");
					editor1.putString("CorpID", corp_id);
					editor1.putString("login_User_ID", user_id);
					editor1.putString("login_User_pass", password);
					editor1.commit();
				} else {
					SharedPreferences example1 = getSharedPreferences(
							ValueStore, 0);
					Editor editor1 = example1.edit();
					editor1.putString("chk_flag", "0");
					editor1.commit();
				}

				if (corp_id.length() < 1) {
					*//*
					 * CustomDialogClass cdd=new CustomDialogClass(Login.this,
					 * "Please enter user name." ,"Okay"); cdd.show();
					 *//*
					// display in long period of time
					Toast.makeText(getApplicationContext(),
							"Please Enter Corp ID", Toast.LENGTH_LONG).show();
					return;
				} else if (user_id.length() < 1) {
					*//*
					 * CustomDialogClass cdd=new CustomDialogClass(Login.this,
					 * "Please enter password." ,"Okay"); cdd.show();
					 *//*
					Toast.makeText(getApplicationContext(),
							"Please Enter User ID", Toast.LENGTH_LONG).show();
					return;
				} else if (password.length() < 1) {

					Toast.makeText(getApplicationContext(),
							"Please Enter Password", Toast.LENGTH_LONG).show();
					*//*
					 * CustomDialogClass cdd=new CustomDialogClass(Login.this,
					 * "Please enter password." ,"Okay"); cdd.show();
					 *//*
					return;
				}

				isInternetPresent = cd.isConnectingToInternet();
				if (isInternetPresent) {

					if (example.getString("URL", "").length()>0) {
						verify_user_login_asynchronously task1 = new verify_user_login_asynchronously();
						task1.execute(new String[] { "" });
					} else {

						verify_user_url_asynchronously task0 = new verify_user_url_asynchronously();
						task0.execute();
					}
				} else {
					Toast.makeText(LoginActivity.this,
							"Check Your Internet Connection",
							Toast.LENGTH_SHORT).show();
				}
				}

			

		});*/

		var_LoginImage_modified.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getLogin();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	//---volley method for login, code starts (added on 20th Nov-2023
	public void getLogin(){
		final ProgressDialog loading = ProgressDialog.show(LoginActivity.this, "Authenticating", "Please wait while logging", false, false);
		corp_id = var_CorpId.getText().toString();
		user_id = var_UserId.getText().toString();
		password = var_Password.getText().toString();
		if (remember_me.isChecked()) {
			SharedPreferences example1 = getSharedPreferences(
					ValueStore, 0);
			Editor editor1 = example1.edit();
			editor1.putString("chk_flag", "1");
			editor1.putString("CorpID", corp_id);
			editor1.putString("login_User_ID", user_id);
			editor1.putString("login_User_pass", password);
			editor1.commit();
		} else {
			SharedPreferences example1 = getSharedPreferences(
					ValueStore, 0);
			Editor editor1 = example1.edit();
			editor1.putString("chk_flag", "0");
			editor1.commit();
		}

//		String url = Config.BASEURL + "ValidateLogin/" + corp_id + "/" + user_id + "/" + password;
		String url = Config.BASEURL + "ValidateLogin";
		Log.d("LoginUrl-=>", url);
		StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						loading.dismiss();
						JSONObject jsonObj = null;
						try{
							jsonObj = XML.toJSONObject(response);
							String responseData = jsonObj.toString();
							String val = "";
							JSONObject resobj = new JSONObject(responseData);
							Iterator<?> keys = resobj.keys();
							while(keys.hasNext() ) {
								String key = (String)keys.next();
								if ( resobj.get(key) instanceof JSONObject ) {
									JSONObject xx = new JSONObject(resobj.get(key).toString());
									val = xx.getString("content");
									Log.d("res1",xx.getString("content"));
									JSONObject jsonObject = new JSONObject(val);
									String status = jsonObject.getString("Msg");
									if(status.equalsIgnoreCase("Success")){
										store_UserID = jsonObject.getString("UserID");
										store_CorpID = jsonObject.getString("CorpID");
										store_UserName = jsonObject.getString("UserName");
										store_password = jsonObject.getString("Password");
										store_UserRole = jsonObject.getString("UserRole");
										store_Msg = jsonObject.getString("Msg");

										Intent in_next = new Intent(LoginActivity.this, ViewCalendar.class);
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

								}
							}
							Log.d("logintest",responseData);
						}catch (Exception e){
							e.printStackTrace();
						}

					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				loading.dismiss();


				String message = "Could not connect server";
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
                /*int color = Color.parseColor("#ffffff");
                Snackbar snackbar = Snackbar.make(findViewById(R.id.relativeLayout), message, 4000);

                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(color);
                snackbar.show();*/

				/*View v = findViewById(R.id.relativeLayout);
				new org.arb.timesheet_demo.config.Snackbar(message,v);*/


			}
		}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<>();
				params.put("CorpID", corp_id);
				params.put("UserName", user_id);
				params.put("Password",password);
				return params;
			}
		};

		RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
		requestQueue.add(stringRequest);
	}
	//---volley method for login, code ends

	/******** start async task for login screen *******/

	private class verify_user_login_asynchronously extends
			AsyncTask<String, Void, Boolean> {
		ProgressDialog dialog = new ProgressDialog(LoginActivity.this);

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
			if (LoginActivity.this != null && !LoginActivity.this.isFinishing())
				dialog.dismiss();
			try {

				if (store_Msg.equalsIgnoreCase("Success")) {

					SharedPreferences example1 = getSharedPreferences(
							ValueStore, 0);
					Editor editor1 = example1.edit();
					editor1.putString("CorpID", corp_id);
					editor1.putString("UserID", store_UserID);
					editor1.putString("UserName", store_UserName);
					editor1.commit();
					send_data_to_next_screen();
				} else {
					Toast.makeText(LoginActivity.this, store_Msg,
							Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	public void view_details_from_url() {
		try {

			SharedPreferences example = getSharedPreferences(ValueStore, 0);
//			get_url = example.getString("URL", "");
			get_url = "http://14.99.211.60:9012/";
			Log.d("Url=>", get_url);

			SoapObject request = new SoapObject(
					ConnectionURL.NAMESPACE_For_Login,
					ConnectionURL.METHOD_NAME_For_Login);

			// ********** for login**************//*
			request.addProperty("CorpID", corp_id);
			request.addProperty("UserName", user_id);
			request.addProperty("Password", password);

			/*SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(get_url
					+ ConnectionURL.URL_For_Login, 6000000);
			androidHttpTransport.call(ConnectionURL.SOAP_ACTION_For_Login,
					envelope);
			Object result = (Object) envelope.getResponse();*/

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					get_url
							+ ConnectionURL.URL_For_Login, 6000000);
			androidHttpTransport.call(ConnectionURL.SOAP_ACTION_For_Login,
					envelope);
			Object result = (Object) envelope.getResponse();

			var_store_val_from_web_service = result.toString();
			Log.d("status-=>", var_store_val_from_web_service);
			if (var_store_val_from_web_service != null) {

			} else {
				Toast.makeText(getApplicationContext(), "Network Problem", 2000)
						.show();
			}
			store_data_for_login();

		} catch (Exception e) {
			e.printStackTrace();
			// Toast.makeText(getApplicationContext(), "problm",2000).show();
		}
	}

	public void store_data_for_login() {

		try {

			JSONObject js = new JSONObject(var_store_val_from_web_service);
			Log.d("testData-=>", js.getString("UserID"));

			store_UserID = js.getString("UserID");
			store_CorpID = js.getString("CorpID");
			store_UserName = js.getString("UserName");
			store_password = js.getString("Password");
			store_UserRole = js.getString("UserRole");
			store_Msg = js.getString("Msg");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void send_data_to_next_screen() {

		if (remember_me.isChecked()) {

		} else {
			var_CorpId.setText("");
			var_Password.setText("");
			var_UserId.setText("");
			remember_me.setChecked(false);
		}

		Intent in_next = new Intent(LoginActivity.this, ViewCalendar.class);
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

	/******** start async task for login screen *******/

	private class verify_user_url_asynchronously extends
			AsyncTask<String, Void, Boolean> {
		ProgressDialog dialog = new ProgressDialog(LoginActivity.this);

		// @Override
		protected void onPreExecute() {
			dialog.setMessage("Please wait...");
			dialog.setCancelable(true);
			dialog.show();
		}

		// @Override
		protected Boolean doInBackground(String... params) {
			view_urlstring_from_url();
			return null;
		}

		protected void onPostExecute(Boolean is_authenticated) {
			if (LoginActivity.this != null && !LoginActivity.this.isFinishing())
				dialog.dismiss();
			try {

				Log.d("statusTest-=>", var_store_val_from_web_service);
				if (!var_store_val_from_web_service.equals("0")) {

					SharedPreferences example1 = getSharedPreferences(
							ValueStore, 0);
					Editor editor1 = example1.edit();
					editor1.putString("URL", var_store_val_from_web_service);
					editor1.commit();

					verify_user_login_asynchronously task1 = new verify_user_login_asynchronously();
					task1.execute(new String[] { "" });
				} else {
					Toast.makeText(LoginActivity.this, "Invalid CorpID",
							Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	public void view_urlstring_from_url() {
		try {

			SharedPreferences example = getSharedPreferences(ValueStore, 0);
			get_url = example.getString("URL", "");

			SoapObject request = new SoapObject(
					ConnectionURL.NAMESPACE_For_url,
					ConnectionURL.METHOD_NAME_For_url);

			// ********** for login**************//*
			request.addProperty("CorpID", corp_id);
			 request.addProperty("UserName", user_id);
			 request.addProperty("Password", password);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					ConnectionURL.URL_For_url, 6000000);
			androidHttpTransport.call(ConnectionURL.SOAP_ACTION_For_url,
					envelope);
			Object result = (Object) envelope.getResponse();

			var_store_val_from_web_service = result.toString();
			if (var_store_val_from_web_service != null) {

			} else {
				Toast.makeText(getApplicationContext(), "Network Problem", 2000)
						.show();
			}
			 store_data_for_login();

		} catch (Exception e) {
			e.printStackTrace();
			// Toast.makeText(getApplicationContext(), "problm",2000).show();
		}
	}
}
