package com.nextgenbermobileapp.activity;

import java.io.Flushable;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.nextgenbermobileapp.R;
import com.nextgenbermobileapp.connectioninfo.ConnectionURL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class FullScreen extends Activity {

	ImageView img;
	private String CorpID;
	private String image_id;
	private String get_url;
	public static final String ValueStore = "Collection_Data";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.full_image_screen);

		SharedPreferences example = getSharedPreferences(ValueStore, 0);
		CorpID = example.getString("CorpID", "");
		get_url = example.getString("URL", "");

		image_id = getIntent().getStringExtra("Image_id");

		img = (ImageView) findViewById(R.id.img);

		/* Attachment data retrive asynck task */

		if (image_id != null) {
			Attachment_retrive_asynchronously task1 = new Attachment_retrive_asynchronously();
			task1.execute();
		} else {
			img.setImageBitmap(BitmapFactory.decodeFile(getIntent()
					.getStringExtra("Image_path")));
		}

		/* Image data retrive asynck task */

		// byte[] bytes = getIntent().getByteArrayExtra("bitmap");
		// String flag = getIntent().getStringExtra("flag");
		// if (flag.length() > 0) {
		// if (flag.equals("true")) {
		// delete.setEnabled(true);
		// delete.setBackgroundColor(Color.parseColor("#596582"));
		// }
		// }
		// Bitmap b = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

		// ImageView view = new ImageView(this);
		// img.setImageBitmap(b);

		findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();
			}
		});
	}

	/******** async task for retrive data ***************/

	private class Attachment_retrive_asynchronously extends
			AsyncTask<Void, Void, Void> {
		ProgressDialog dialog = new ProgressDialog(FullScreen.this);
		private String var_store_val_from_web_service;
		private byte[] bitmap;

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

			byte[] decodedString = Base64.decode(
					var_store_val_from_web_service, Base64.DEFAULT);

			bitmap = decodedString;
			Bitmap b = BitmapFactory.decodeByteArray(decodedString, 0,
					decodedString.length);
			img.setImageBitmap(b);

		}

		public void view_save_url() {

			try {
				SoapObject request = new SoapObject(
						ConnectionURL.NAMESPACE_For_attachment,
						ConnectionURL.METHOD_NAME_For_attachment);
				request.addProperty("CorpID", CorpID);
				request.addProperty("FileID", image_id);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);
				HttpTransportSE androidHttpTransport = new HttpTransportSE(
						get_url + ConnectionURL.URL_For_SaveList);
				androidHttpTransport.call(
						ConnectionURL.SOAP_ACTION_For_attachment, envelope);
				Object result = (Object) envelope.getResponse();

				var_store_val_from_web_service = result.toString().replace(" ", "+");
				System.out.println("Print Result :"
						+ var_store_val_from_web_service);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
}