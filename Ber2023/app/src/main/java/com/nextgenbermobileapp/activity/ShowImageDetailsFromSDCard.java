package com.nextgenbermobileapp.activity;

import java.io.File;

import com.nextgenbermobileapp.R;
import com.nextgenbermobileapp.adapter.ViewImageAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.Window;
import android.widget.Gallery;
import android.widget.ListView;
import android.widget.Toast;

public class ShowImageDetailsFromSDCard extends Activity{
	
	private String[] FilePathStrings;
    private String[] FileNameStrings;
    private File[] listFile;
    
    ViewImageAdapter adapter;
    
    File file;
	ListView show_image;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.show_image_in_list);
     // Check for SD Card
        if (!Environment.getExternalStorageState().equals(
                     Environment.MEDIA_MOUNTED)) {
               Toast.makeText(this, "Error! No SDCARD Found!", Toast.LENGTH_LONG)
                            .show();
        } else {
               file = new File(Environment.getExternalStorageDirectory()
                            + File.separator + "here_your_specific_folder");
               file.mkdirs();
        }

        if (file.isDirectory()) {
               
        	   listFile = file.listFiles();
               FilePathStrings = new String[listFile.length];
               FileNameStrings = new String[listFile.length];
               for (int i = 0; i < listFile.length; i++) {
                     FilePathStrings[i] = listFile[i].getAbsolutePath();
                     FileNameStrings[i] = listFile[i].getName();
               }
        }

        show_image = (ListView) findViewById(R.id.ShowImageInList);
        adapter = new ViewImageAdapter(this, FilePathStrings, FileNameStrings);
       
        show_image.setAdapter(adapter);

	}
	
}
