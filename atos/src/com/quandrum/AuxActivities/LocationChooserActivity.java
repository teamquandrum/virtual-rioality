package com.quandrum.AuxActivities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quandrum.Json.Business;
import com.quandrum.Json.FilePathSetter;
import com.quandrum.Json.JSONParser;
import com.quandrum.Json.TouristLocation;
import com.quandrum.atos.R;

/**
 * Accepts 'lat','lon' (user's current coordinates) and 'bids' (an array of business ids) and 'tlids' an array of touristlocationids
 * @author Abraham
 *
 */
public class LocationChooserActivity extends Activity {

	double mLat;
	double mLon;
	int[] mBids;
	int[] mTlids;
	LinearLayout mScll;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location_chooser);
		
		FilePathSetter.setFilePaths(this);
		
		Intent i = getIntent();
		mLat = i.getDoubleExtra("lat", -1);
		mLon = i.getDoubleExtra("lon", -1);
		
		mBids = i.getIntArrayExtra("bids");
		mTlids = i.getIntArrayExtra("tlids");
		
		Log.e("LocationChooserActivity","bids "+mBids.length);
		Log.e("LocationChooserActivity","tlids "+mTlids.length);
		
		mScll = (LinearLayout)findViewById(R.id.scll);
		
		addBusinesses(mBids,mScll);
		
		addTouristLocations(mTlids,mScll);
		
	}
	public void addBusinesses(int[] bids, LinearLayout ll){
		if(bids.length==0)
			return;
		TextView tv = new TextView(this);
		tv.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		tv.setText("Businesses: ");
		tv.setTextColor(getResources().getColor(R.color.White));
		tv.setTextSize(20);
		ll.addView(tv);
		for(int id:bids){
			final Business b = JSONParser.getBusinessById(id);
			
			Button btv = new Button(this);
			btv.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
			btv.setTextSize(20);
			btv.setTextColor(getResources().getColor(R.color.white));
			btv.setBackgroundResource(R.drawable.blackflatbuttonxml);
			
			btv.setText(b.getName());
			btv.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v){
					Intent i = getIntentForBusiness(b.getId());
					startActivity(i);
				}
			});
			ll.addView(btv);
		}
		
	}
	public void addTouristLocations(int tlids[], LinearLayout ll){
		if(tlids.length==0)
			return;
		TextView tv = new TextView(this);
		tv.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		tv.setText("Tourist Attractions: ");
		tv.setTextColor(getResources().getColor(R.color.White));
		tv.setTextSize(20);
		ll.addView(tv);
		for(int id:tlids){
			final TouristLocation tl = JSONParser.getTouristLocationById(id);
			
			Button btv = new Button(this);
			btv.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
			btv.setTextSize(20);
			btv.setTextColor(getResources().getColor(R.color.white));
			btv.setBackgroundResource(R.drawable.blackflatbuttonxml);
			
			btv.setText(tl.getName());
			btv.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v){
					Intent i = getIntentForTouristLocation(tl.getId());
					startActivity(i);
				}
			});
			ll.addView(btv);
		}
	}
	public Intent getIntentForBusiness(int id){
		Intent i = new Intent(this, BusinessDisplayActivity.class);
		i.putExtra("lon", mLon);
		i.putExtra("lat", mLat);
		i.putExtra("id",id);
		return i;
	}
	public Intent getIntentForTouristLocation(int id){
		Intent i = new Intent(this,TouristLocationDisplayActivity.class);
		i.putExtra("lon", mLon);
		i.putExtra("lat", mLat);
		i.putExtra("id", id);
		return i;
	}
}
