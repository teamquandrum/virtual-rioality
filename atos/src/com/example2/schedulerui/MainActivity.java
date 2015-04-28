package com.example2.schedulerui;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.quandrum.atos.R;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);
		File file = new File(android.os.Environment.getExternalStorageDirectory()+"/ScheduleNames.txt");
		if(file.exists()){
			Intent i=new Intent(this,DisplaySchedule.class);
			startActivity(i);
			finish();
		}
	}
	
	public void getStarted(View view){
		Intent i=new Intent(this,StartEnd.class);
		startActivity(i);
		finish();
	}
	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}*/

}
