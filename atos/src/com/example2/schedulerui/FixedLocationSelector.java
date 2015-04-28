package com.example2.schedulerui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.quandrum.atos.R;

public class FixedLocationSelector extends Activity {
	static ArrayList<FixedLocation> fixedLocations;
	static FixedLocationSelector fixedLocationSelector;
	ArrayList<String> names;
	ListView l;
	ArrayAdapter<String> adp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fixedLocationSelector=this;
		names=new ArrayList<String>();
		fixedLocations=new ArrayList<FixedLocation>();
		setContentView(R.layout.activity_fixed_location_selector);
		l=(ListView)findViewById(R.id.listed);
		adp=new ArrayAdapter<String> 
		(getBaseContext(),R.layout.list,names);
		l.setAdapter(adp);
	}
	
	public void addLocation(View view){
		Intent i=new Intent(this,Selector.class);
		startActivityForResult(i,1);
		}
	
	public void next(View view){
		Intent j=new Intent(this,TouristLocationSelector.class);
		startActivity(j);
	}
	
	public static FixedLocationSelector getInstance(){
		   return   fixedLocationSelector;
		 }
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		  if (requestCode == 1) {
		     if(resultCode == RESULT_OK){      
		    	Bundle bundle = data.getParcelableExtra("bundle");
		 		LatLng a = bundle.getParcelable("location");
		 		int h1=bundle.getInt("start_hour");
		 		int m1=bundle.getInt("start_minute");
		 		int h2=bundle.getInt("end_hour");
		 		int m2=bundle.getInt("end_minute");
		 		String name=bundle.getString("name_of_location");
		 		fixedLocations.add(new FixedLocation(a,h1,m1,h2,m2,name));
		 		update(name+"- "+h1+":"+m1+" to "+h2+":"+m2);
		 		Toast.makeText(this,"Success!",Toast.LENGTH_SHORT).show();
		 	}
		     if (resultCode == RESULT_CANCELED) {    
		         //Write your code if there's no result		    	 
		     }
		  }
	}
	
	public void update(String s){
		names.add(s);
		adp.notifyDataSetChanged();
	}
	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fixed_location_selector, menu);
		return true;
	}*/

}
