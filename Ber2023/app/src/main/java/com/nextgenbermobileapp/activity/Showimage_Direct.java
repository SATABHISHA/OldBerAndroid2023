package com.nextgenbermobileapp.activity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import com.nextgenbermobileapp.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class Showimage_Direct extends Activity implements View.OnClickListener {

	private ImageView img1, img2, img3, img4;
	private byte[] bitmap1, bitmap2, bitmap3, bitmap4;

	private int empty = -1;
	private String date;
	private String store_loc_to_string;
	private byte[] bitmap_1;
	private byte[] bitmap_2;
	private byte[] bitmap_3;
	private byte[] bitmap_4;
	public static final String ValueStore = "Collection_Data";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.showimage_direct);

		SharedPreferences example = getSharedPreferences(ValueStore, 0);
		date = example.getString("Date", "");
		store_loc_to_string = example.getString("store_loc_to", "");

		TextView Date = (TextView) findViewById(R.id.ShowDateDay);
		Date.setText(date);

		TextView store_loc_to_textview = (TextView) findViewById(R.id.tolocation);
		store_loc_to_textview.setText(store_loc_to_string);

		img1 = (ImageView) findViewById(R.id.img1);
		img2 = (ImageView) findViewById(R.id.img2);
		img3 = (ImageView) findViewById(R.id.img3);
		img4 = (ImageView) findViewById(R.id.img4);

		// findViewById(R.id.capture).setOnClickListener(this);
		// findViewById(R.id.done).setOnClickListener(this);
		findViewById(R.id.back).setOnClickListener(this);
		img1.setOnClickListener(this);
		img2.setOnClickListener(this);
		img3.setOnClickListener(this);
		img4.setOnClickListener(this);

		Intent i = getIntent();
		bitmap1 = i.getByteArrayExtra("bitmap1");
		bitmap2 = i.getByteArrayExtra("bitmap2");
		bitmap3 = i.getByteArrayExtra("bitmap3");
		bitmap4 = i.getByteArrayExtra("bitmap4");
		Checkit();

		// if (bitmap1 != null)
		// img1.setImageBitmap(BitmapFactory.decodeByteArray(bitmap1, 0,
		// bitmap1.length));
		// else if (empty == -1)
		// empty = 0;
		//
		// if (bitmap2 != null)
		// img2.setImageBitmap(BitmapFactory.decodeByteArray(bitmap2, 0,
		// bitmap2.length));
		// else if (empty == -1)
		// empty = 1;
		//
		// if (bitmap3 != null)
		// img3.setImageBitmap(BitmapFactory.decodeByteArray(bitmap3, 0,
		// bitmap3.length));
		// else if (empty == -1)
		// empty = 2;
		//
		// if (bitmap4 != null)
		// img4.setImageBitmap(BitmapFactory.decodeByteArray(bitmap4, 0,
		// bitmap4.length));
		// else if (empty == -1)
		// empty = 3;
		//
		// if (empty == -1)
		// empty = 0;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/*
		 * case R.id.capture:
		 * 
		 * if (bitmap1 != null && bitmap2 != null && bitmap3 != null && bitmap4
		 * != null) {
		 * 
		 * } else { Intent cameraIntent = new Intent(
		 * MediaStore.ACTION_IMAGE_CAPTURE);
		 * startActivityForResult(cameraIntent, 1); } break;
		 */
		/*
		 * case R.id.done: endActivity(); break;
		 */
		case R.id.back:
			finish();
			break;
		case R.id.img1:
			if (bitmap1 == null) {
			} else {
				Intent i1 = new Intent(this, FullScreen.class);
				i1.putExtra("bitmap", bitmap1);
				i1.putExtra("flag", "false");
				// startActivity(i1);
				startActivityForResult(i1, 5);
			}
			break;
		case R.id.img2:
			if (bitmap2 == null) {
			} else {

				Intent i2 = new Intent(this, FullScreen.class);
				i2.putExtra("bitmap", bitmap2);
				i2.putExtra("flag", "false");
				startActivityForResult(i2, 6);
			}
			break;
		case R.id.img3:
			if (bitmap3 == null) {
			} else {
				Intent i3 = new Intent(this, FullScreen.class);
				i3.putExtra("bitmap", bitmap3);
				i3.putExtra("flag", "false");
				startActivityForResult(i3, 7);
			}
			break;
		case R.id.img4:
			if (bitmap4 == null) {
			} else {
				Intent i4 = new Intent(this, FullScreen.class);
				i4.putExtra("bitmap", bitmap4);
				i4.putExtra("flag", "false");
				startActivityForResult(i4, 8);
			}
			break;
		}
	}

	@Override
	public void onBackPressed() {
		endActivity();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1 && resultCode == RESULT_OK) {
			try {
				Bitmap mBitmapCamera = (Bitmap) data.getExtras().get("data");
				// Bitmap resized = Bitmap.createScaledBitmap(mBitmapCamera,
				// 100,
				// 100, true);

				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				mBitmapCamera.compress(Bitmap.CompressFormat.PNG, 100, stream);
				byte[] bitmap = stream.toByteArray();
				stream.close();

				ImageView view = null;

				switch (empty) {
				case 0:
					view = img1;
					bitmap1 = bitmap;
					break;
				case 1:
					view = img2;
					bitmap2 = bitmap;
					break;
				case 2:
					view = img3;
					bitmap3 = bitmap;
					break;
				case 3:
					view = img4;
					bitmap4 = bitmap;
					break;

				}

				view.setImageBitmap(mBitmapCamera);
				if (++empty > 3)
					empty = 0;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (resultCode == RESULT_OK) {
			switch (requestCode) {

			case 5:
				// ImageView view1 = null;
				bitmap_1 = data.getByteArrayExtra("BitmapImage");
				// view1 = img4;
				bitmap1 = bitmap_1;
				if (bitmap1 == null) {
					Bitmap icon = BitmapFactory.decodeResource(getResources(),
							R.drawable.noimage);
					img1.setImageBitmap(icon);
				}
				Checkit();
				// Intent i=new Intent(getApplicationContext(),ShowImage.class);
				// i.putExtra("bitmap1", bitmap1);
				// i.putExtra("bitmap2", bitmap2);
				// i.putExtra("bitmap3", bitmap3);
				// i.putExtra("bitmap4", bitmap4);
				// startActivityForResult(i, 4);
				// finish();
				break;
			case 6:
				// ImageView view1 = null;
				bitmap_2 = data.getByteArrayExtra("BitmapImage");
				// view1 = img4;
				bitmap2 = bitmap_2;
				if (bitmap2 == null) {
					Bitmap icon = BitmapFactory.decodeResource(getResources(),
							R.drawable.noimage);
					img2.setImageBitmap(icon);
				}
				Checkit();
				// Intent ii=new
				// Intent(getApplicationContext(),ShowImage.class);
				// ii.putExtra("bitmap1", bitmap1);
				// ii.putExtra("bitmap2", bitmap2);
				// ii.putExtra("bitmap3", bitmap3);
				// ii.putExtra("bitmap4", bitmap4);
				// startActivityForResult(ii, 4);
				// finish();
				break;
			case 7:
				// ImageView view1 = null;
				bitmap_3 = data.getByteArrayExtra("BitmapImage");
				// view1 = img4;
				bitmap3 = bitmap_3;
				if (bitmap3 == null) {
					Bitmap icon = BitmapFactory.decodeResource(getResources(),
							R.drawable.noimage);
					img3.setImageBitmap(icon);
				}
				Checkit();
				// Intent i2=new
				// Intent(getApplicationContext(),ShowImage.class);
				// i2.putExtra("bitmap1", bitmap1);
				// i2.putExtra("bitmap2", bitmap2);
				// i2.putExtra("bitmap3", bitmap3);
				// i2.putExtra("bitmap4", bitmap4);
				// startActivityForResult(i2, 4);
				// finish();
				break;
			case 8:
				// ImageView view1 = null;
				bitmap_4 = data.getByteArrayExtra("BitmapImage");
				// view1 = img4;
				bitmap4 = bitmap_4;
				if (bitmap4 == null) {
					Bitmap icon = BitmapFactory.decodeResource(getResources(),
							R.drawable.noimage);
					img4.setImageBitmap(icon);
				}
				Checkit();
				// Intent i3=new
				// Intent(getApplicationContext(),ShowImage.class);
				// i3.putExtra("bitmap1", bitmap1);
				// i3.putExtra("bitmap2", bitmap2);
				// i3.putExtra("bitmap3", bitmap3);
				// i3.putExtra("bitmap4", bitmap4);
				// startActivityForResult(i3, 4);
				// finish();
				break;

			}
		}
	}

	private void endActivity() {
		Intent i = new Intent();
		i.putExtra("bitmap1", bitmap1);
		i.putExtra("bitmap2", bitmap2);
		i.putExtra("bitmap3", bitmap3);
		i.putExtra("bitmap4", bitmap4);
		setResult(RESULT_OK, i);
		finish();
	}

	private void Checkit() {
		if (bitmap1 != null)
			img1.setImageBitmap(BitmapFactory.decodeByteArray(bitmap1, 0,
					bitmap1.length));
		else if (empty == -1)
			empty = 0;

		if (bitmap2 != null)
			img2.setImageBitmap(BitmapFactory.decodeByteArray(bitmap2, 0,
					bitmap2.length));
		else if (empty == -1)
			empty = 1;

		if (bitmap3 != null)
			img3.setImageBitmap(BitmapFactory.decodeByteArray(bitmap3, 0,
					bitmap3.length));
		else if (empty == -1)
			empty = 2;

		if (bitmap4 != null)
			img4.setImageBitmap(BitmapFactory.decodeByteArray(bitmap4, 0,
					bitmap4.length));
		else if (empty == -1)
			empty = 3;

		if (empty == -1)
			empty = 0;
	}
}