package com.example2.schedulerui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;
import com.quandrum.atos.R;

public class TouristSelector extends FragmentActivity {
	LatLng a;
	String placename="";
	EditText eh2,em2,en;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tourist_selector);
	}
	
	
		
	public void selectLocation(View view){
		startActivityForResult((new Intent(this,Map.class)),1);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		  if (requestCode == 1) {
		     if(resultCode == RESULT_OK){      
		    	 Bundle bundle = data.getParcelableExtra("bundle");
		    	 a = bundle.getParcelable("position");
		     }
		     if (resultCode == RESULT_CANCELED) {    
		         //Write your code if there's no result
		    	 a = null;
		     }
		  }
		}//onActivityResult
	
	public void done(View view){
		//eh2=(EditText)findViewById(R.id.hours);
		//em2=(EditText)findViewById(R.id.minutes);
		en=(EditText)findViewById(R.id.name);
		placename=en.getText().toString();
		//h2=Integer.parseInt(eh2.getText().toString());
		//m2=Integer.parseInt(em2.getText().toString());
		Intent returnIntent = new Intent();
    	Bundle args = new Bundle();
    	args.putParcelable("location", a);
    	args.putString("name_of_location",placename);
    	
    	returnIntent.putExtra("bundle",args);
    	 setResult(RESULT_OK,returnIntent);     
    	 finish();
	}
	
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.selector, menu);
		return true;
	}*/

}
