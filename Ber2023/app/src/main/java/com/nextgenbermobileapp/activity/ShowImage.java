package com.nextgenbermobileapp.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import com.nextgenbermobileapp.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

public class ShowImage extends Activity implements View.OnClickListener {

	private String date;
	private String store_loc_to_string;
	private byte[] bitmap;
	private Bitmap mBitmapCamera;
	public static final String ValueStore = "Collection_Data";

	// ArrayList<String> image_list = new ArrayList<String>();
	private ArrayList<HashMap<String, String>> AttachmentList;
	private String image_item;
	private String image_Name;
	private String image_Size;
	private Integer image_Status;
	List<String> image_Name_list = new ArrayList<String>();
	List<String> image_size_list = new ArrayList<String>();
	List<String> image_size_id = new ArrayList<String>();
	List<String> image_size_status = new ArrayList<String>();
	// List<String> perdiem_name = new ArrayList<String>();

	LinearLayout lv;
	private String img_type;
	private String image_id;
	private Integer position1;
	private int prev_image_list_length;
	private String value;

	private ArrayList<String> paths, existingPaths;
	private ArrayList<Integer> deleteYnStateList;
	private ImageView image_show;
	private ToggleButton image_delete_undo;

	int flag = 0;
	private String btn_disable_flag;
	private LinearLayout lin;
	private View v;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.showimage);

		lv = (LinearLayout) findViewById(R.id.list);

		SharedPreferences example = getSharedPreferences(ValueStore, 0);
		date = example.getString("Date", "");
		store_loc_to_string = example.getString("store_loc_to", "");

		TextView Date = (TextView) findViewById(R.id.ShowDateDay);
		Date.setText(date);

		TextView store_loc_to_textview = (TextView) findViewById(R.id.tolocation);
		store_loc_to_textview.setText(store_loc_to_string);

		findViewById(R.id.capture).setOnClickListener(this);
		findViewById(R.id.done).setOnClickListener(this);

		Intent i = getIntent();
		AttachmentList = (ArrayList<HashMap<String, String>>) i
				.getSerializableExtra("AttachmentList");

		paths = new ArrayList<String>();
		existingPaths = i.getStringArrayListExtra("paths");
		deleteYnStateList = i.getIntegerArrayListExtra("delete_yn");

		btn_disable_flag = getIntent().getStringExtra("Flag_button_disable");
		if (btn_disable_flag != null) {
			if (btn_disable_flag.equals("yes")) {
				findViewById(R.id.capture).setVisibility(View.INVISIBLE);
				Button back = (Button) findViewById(R.id.done);
				back.setText("Back");
			}
		}
		try {
			for (int j = 0; j < AttachmentList.size(); j++) {
				image_Name = AttachmentList.get(j).get("ImageURL");
				image_Size = AttachmentList.get(j).get("ImageSize");
				// image_Status = AttachmentList.get(j).get("ImageStatus");
				try {
					image_Status = deleteYnStateList.get(j);
				} catch (Exception e) {
					// TODO: handle exception
				}

				image_id = AttachmentList.get(j).get("ImageID");
				image_Name_list.add(image_Name);
				image_size_list.add(image_Size);
				image_size_id.add(image_id);
				image_size_status.add(String.valueOf(image_Status));
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		if (deleteYnStateList == null) {
			deleteYnStateList = new ArrayList<Integer>();
		}

		if (image_Name_list.size() > 0) {

			for (int k = 0; k < image_Name_list.size(); k++) {
				createRow(k);
			}
		} else {
			findViewById(R.id.txt).setVisibility(View.VISIBLE);
			findViewById(R.id.list).setVisibility(View.INVISIBLE);
			findViewById(R.id.scrollView1).setVisibility(View.INVISIBLE);
		}

		// image_name=AttachmentList.
		// if (image_listt.size() > 0) {
		// image_list.addAll(image_listt);
		// }
		//
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.capture:
			Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(cameraIntent, 1);
			break;
		case R.id.done:
			endActivity();
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
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyyMMddHHmmss", Locale.getDefault());
					String currentDateandTime = sdf.format(new Date());

					File sdCard = Environment.getExternalStorageDirectory();
					File dir = new File(sdCard.getAbsolutePath() + "/NextGen");
					dir.mkdirs();

					String image_name = "doc_" + currentDateandTime + ".png";
					prev_image_list_length = image_Name_list.size();
					image_Name_list.add(image_name);

					// Log.i("ddd", image_list.size() + "");
					File file = new File(dir, image_name);
					mBitmapCamera = (Bitmap) data.getExtras().get("data");
					// Bitmap resized = Bitmap.createScaledBitmap(mBitmapCamera,
					// 100,
					// 100, true);

					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					mBitmapCamera.compress(Bitmap.CompressFormat.PNG, 100,
							stream);

					paths.add(file.toString());

					FileOutputStream f = null;
					f = new FileOutputStream(file);

					if (f != null) {
						f.write(stream.toByteArray());
						f.flush();
						f.close();
					}

					bitmap = stream.toByteArray();
					long lengthbmp = bitmap.length / 1024;

					if (lengthbmp >= 1024)
						value = lengthbmp / 1024 + " Mb";
					else
						value = lengthbmp + " Kb";
					image_size_list.add(value);
					stream.close();

					/* after capture pic from camera creat new row */

					for (int k = prev_image_list_length; k < image_Name_list
							.size(); k++) {
						createRow(k);
					}

					/* after capture pic from camera creat new row */
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void endActivity() {
		Intent i = new Intent();
		i.putStringArrayListExtra("paths", paths);
		i.putIntegerArrayListExtra("delete_yn", deleteYnStateList);
		// i.putString("Image_id", image_size_id);
		// i.putExtra("image_list", image_list);
		setResult(RESULT_OK, i);
		finish();
	}

	private void createRow(final int position) {

		findViewById(R.id.txt).setVisibility(View.GONE);
		findViewById(R.id.list).setVisibility(View.VISIBLE);
		findViewById(R.id.scrollView1).setVisibility(View.VISIBLE);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = inflater.inflate(R.layout.image_row, lv, false);
		// ((EditText) v.findViewById(R.id.meal_expense))
		// .addTextChangedListener(this);
		// ((EditText) v.findViewById(R.id.meal_allowance))
		// .addTextChangedListener(this);

		try {
			TextView image_name_textview = (TextView) v
					.findViewById(R.id.image_name);
			ImageView image_type = (ImageView) v.findViewById(R.id.image_type);
			image_show = (ImageView) v.findViewById(R.id.view);

			image_delete_undo = (ToggleButton) v.findViewById(R.id.trash);
			if(btn_disable_flag!=null){
			try {
				if (btn_disable_flag.equals("yes")) {
					image_delete_undo.setVisibility(View.GONE);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			}
			
			TextView image_size_textview = (TextView) v.findViewById(R.id.image_size);
			image_name_textview.setText((image_Name_list.get(position)));
			image_size_textview.setText((image_size_list.get(position)));

			lin = (LinearLayout) v.findViewById(R.id.linrow);

			/* Image name reversing */
			StringBuffer buffer = new StringBuffer(
					image_Name_list.get(position));
			String st = buffer.reverse().toString();
			/* Image name reversing */

			/* Reverse Image Name split */
			StringTokenizer tokens = new StringTokenizer(st, ".");
			String first = tokens.nextToken();

			/* Reverse Image Name split */
			if (first.equalsIgnoreCase("gnp")) {
				image_type.setBackgroundResource(R.drawable.png_icon);
			} else if (first.equalsIgnoreCase("GEPJ")
					|| first.equalsIgnoreCase("GPJ")) {
				image_type.setBackgroundResource(R.drawable.jpg_icon);
			} else if (first.equalsIgnoreCase("fig")) {
				image_type.setBackgroundResource(R.drawable.gif_icon);
			} else {
				image_type.setBackgroundResource(R.drawable.no_format);
			}

			if (position >= deleteYnStateList.size()) {
				deleteYnStateList.add(0);
				image_delete_undo.setChecked(false);
			}
			else {
				image_delete_undo.setChecked(deleteYnStateList.get(position) > 0);
			}
			
			

			// if (btn_disable_flag.equals("yes")) {
			// } else {

			image_delete_undo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

						private CompoundButton btn_view;

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								final boolean isChecked) {
							btn_view = buttonView;
							// check=true;
							if (isChecked) {
								Log.i("msg", "true");

								AlertDialog.Builder alertDialog = new AlertDialog.Builder(
										ShowImage.this);

								// Setting Dialog Title
								alertDialog.setTitle("Info...");

								// Setting Dialog Message
								alertDialog
										.setMessage("Sure you want to delete ?");

								// Setting Icon to Dialog
								alertDialog.setIcon(R.drawable.delete);

								// Setting Positive "Yes" Button
								alertDialog.setPositiveButton("YES",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												((View) btn_view.getParent()
														.getParent())
														.setBackgroundColor(Color
																.parseColor("#fedfcd"));

												// lin.setBackgroundColor(Color.GREEN);
												btn_view.setBackgroundResource(R.drawable.undo_icon);
												// Write your code
												// here to
												// invoke YES event
												deleteYnStateList
														.set(position,
																deleteYnStateList
																		.get(position) > 0 ? 0
																		: 1);
//												check = false;
											}
										});

								// Setting Negative "NO" Button
								alertDialog.setNegativeButton("NO",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
//												check = true;
												Log.i("msg1", isChecked + "");
												// btn_view.setBackgroundResource(R.drawable.trash);
												dialog.cancel();
											}
										});

								// Showing Alert Message
								alertDialog.show();
							} else {
								Log.i("msg", "false");
								AlertDialog.Builder alertDialog = new AlertDialog.Builder(
										ShowImage.this);

								// Setting Dialog Title
								alertDialog.setTitle("Info...");

								// Setting Dialog Message
								alertDialog.setMessage("Undo delete ?");

								// Setting Icon to Dialog
								alertDialog.setIcon(R.drawable.delete);

								// Setting Positive "Yes" Button
								alertDialog.setPositiveButton("YES",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												btn_view.setBackgroundResource(R.drawable.trash);
												((View) btn_view.getParent()
														.getParent())
														.setBackgroundResource(R.drawable.rectborder);
												// Write your code
												// here to
												// invoke YES event
												deleteYnStateList
														.set(position,
																deleteYnStateList
																		.get(position) > 0 ? 0
																		: 1);
//												check = true;
											}
										});

								// Setting Negative "NO" Button
								alertDialog.setNegativeButton("NO",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
//												check = false;
												// btn_view.setBackgroundResource(R.drawable.undo_icon);
												dialog.cancel();
											}
										});

								// Showing Alert Message
								alertDialog.show();
							}
							// deleteYnStateList.set(position,
							// deleteYnStateList.get(position) > 0 ? 0 : 1);
						}
					});
			// }

			// image_delete_undo.setOnTouchListener(new OnTouchListener() {
			//
			// @Override
			// public boolean onTouch(View v, MotionEvent event) {
			// // TODO Auto-generated method stub
			// return true;
			// }
			// });

			image_show.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					System.out.println("position=" + position);
					Intent in = new Intent(getApplicationContext(),
							FullScreen.class);
					// =======================================================================================================issue
					Log.i("golmalvai", existingPaths+"");
					if (existingPaths == null ||existingPaths.size()==0) {
						if (position < image_size_id.size()) {
							in.putExtra("Image_id", image_size_id.get(position));
						} else {
							in.putExtra("Image_path",
									paths.get(position - image_size_id.size()));
						}
					} else {
						int actualSize = image_size_id.size()
								- existingPaths.size();
						if (position < actualSize) {
							in.putExtra("Image_path",
									existingPaths.get(position - actualSize));
						} else if (position - actualSize < existingPaths.size()) {
							in.putExtra("Image_path",
									existingPaths.get(position - actualSize));
						} else {
							in.putExtra(
									"Image_path",
									paths.get(position - actualSize
											- existingPaths.size()));
						}
					}
					startActivity(in);
				}
			});
			
			if ((image_size_status.get(position).equals("1"))) {
				image_delete_undo.setBackgroundResource(R.drawable.undo_icon);
				lin.setBackgroundColor((Color.parseColor("#fedfcd")));
			}
			
		} catch (Exception e) {
			// handle exception
			e.printStackTrace();
		}

		lv.addView(v);
	}
}