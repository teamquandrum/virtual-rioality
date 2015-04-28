package com.example2.schedulerui;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.quandrum.atos.R;

public class StartEnd extends Activity {
	static LatLng startLoc,endLoc;
	static int startTimeHour,endTimeHour,startTimeMinute,endTimeMinute;
	static String startName,endName;
	static StartEnd startEnd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		startEnd=this;
		setContentView(R.layout.activity_start_end);
	}
	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start_end, menu);
		return true;
	}*/
	public void next(View view){
		Intent i=new Intent(this,FixedLocationSelector.class);
		startActivity(i);
	}
	public void start(View view){
		Intent i=new Intent(this,StartEndSelector.class);
		startActivityForResult(i,1);
		}
	public void end(View view){
		Intent i=new Intent(this,StartEndSelector.class);
		startActivityForResult(i,2);
		}
	public static StartEnd getInstance(){
		   return   startEnd;
		 }
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		  if (requestCode == 1) {
		     if(resultCode == RESULT_OK){      
		    	Bundle bundle = data.getParcelableExtra("bundle");
		 		LatLng a = bundle.getParcelable("location");
		 		int h1=bundle.getInt("hour");
		 		int m1=bundle.getInt("minute");
		 		String name=bundle.getString("name_of_location");
		 		startTimeHour=h1;
		 		startTimeMinute=m1;
		 		startLoc=a;
		 		startName=name;
		 		Toast.makeText(this,"Success! "+startLoc.latitude+" "+startLoc.longitude,Toast.LENGTH_SHORT).show();
		 	}
		     if (resultCode == RESULT_CANCELED) {    
		         //Write your code if there's no result		    	 
		     }
		  }
		  if (requestCode == 2) {
			     if(resultCode == RESULT_OK){      
			    	Bundle bundle = data.getParcelableExtra("bundle");
			 		LatLng a = bundle.getParcelable("location");
			 		int h1=bundle.getInt("hour");
			 		int m1=bundle.getInt("minute");
			 		String name=bundle.getString("name_of_location");
			 		endTimeHour=h1;
			 		endTimeMinute=m1;
			 		endLoc=a;
			 		endName=name;
			 		Toast.makeText(this,"Success! "+startLoc.latitude+" "+startLoc.longitude,Toast.LENGTH_SHORT).show();
			 	}
			     if (resultCode == RESULT_CANCELED) {    
			         //Write your code if there's no result		    	 
			     }
			  }
		}//onActivityResult

	
}
