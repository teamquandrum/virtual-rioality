package com.example2.schedulerui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.quandrum.atos.R;

public class DisplaySchedule extends Activity {
	ArrayList<String> buildNames,buildLocations;
	ArrayAdapter<String> adp;
	ListView l;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_schedule);
		buildNames=new ArrayList<String>();
		Scanner input;
		try {
			input = new Scanner(new File(android.os.Environment.getExternalStorageDirectory()+"/ScheduleNames.txt"));
			 while(input.hasNextLine()){
			    	buildNames.add(input.nextLine());
			    }
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		buildLocations=new ArrayList<String>();
		Scanner input2;
		try {
			input2 = new Scanner(new File(android.os.Environment.getExternalStorageDirectory()+"/ScheduleLocations.txt"));
			 while(input2.hasNextLine()){
			    	buildLocations.add(input2.nextLine());
			    }
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		l=(ListView)findViewById(R.id.listlist);
		adp=new ArrayAdapter<String> 
		(getBaseContext(),R.layout.list,buildNames);
		l.setAdapter(adp);
	}
	
	@SuppressWarnings("unused")
	public void scheduleNew(View v){
		File file = new File(android.os.Environment.getExternalStorageDirectory()+"/ScheduleNames.txt");
		boolean deleted = file.delete();

		File file2 = new File(android.os.Environment.getExternalStorageDirectory()+"/ScheduleLocations.txt");
		boolean deleted2 = file2.delete();
		
		finish();
	
		//Intent i=new Intent(this,MainActivity.class);
		//startActivity(i);
	}

	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_schedule, menu);
		return true;
	}*/

}
