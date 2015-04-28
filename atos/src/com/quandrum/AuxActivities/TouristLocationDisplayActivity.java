package com.quandrum.AuxActivities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.quandrum.Json.FilePathSetter;
import com.quandrum.Json.JSONParser;
import com.quandrum.Json.TouristLocation;
import com.quandrum.atos.R;
import com.quandrum.atos.R.id;
import com.quandrum.atos.R.layout;

/**
 * Requires 'id','lat','lon' (user's position) as intent extras
 * @author Abraham
 *
 */
public class TouristLocationDisplayActivity extends Activity {

	TextView mDesc;
	TextView mName;
	TouristLocation mTouristLocation;
	
	double mLat;
	double mLon;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tourist_location_display);

		FilePathSetter.setFilePaths(this);
		
		Intent i = getIntent();
		int id = i.getIntExtra("id",-1);
		mLat = i.getDoubleExtra("lat", -1);
		mLon = i.getDoubleExtra("lon", -1);
		if(id==-1){
			mName = (TextView)findViewById(R.id.name);
			mName.setText("Location not downloaded");
		}
		else{
			mTouristLocation = JSONParser.getTouristLocationById(id);
			populateTextViews(mTouristLocation);
		}
	}
	public void populateTextViews(TouristLocation tl){
		mName = (TextView)findViewById(R.id.name);
		mDesc = (TextView)findViewById(R.id.desc_tv);
		
		mName.setText(tl.getName());
		
		mDesc.setText(mDesc.getText()+tl.getDescription());
	}
	public void loadDirections(View v){
		String baseURL="http://maps.google.com/maps?d&saddr="+ mLat+","+mLon;
		String mapURL = baseURL+"&daddr="+mTouristLocation.getLat()+","+mTouristLocation.getLon()+"&dirflg=d";
		Intent intent = new Intent(android.content.Intent.ACTION_VIEW,Uri.parse(mapURL));
		intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
		startActivity(intent);
	}
}
