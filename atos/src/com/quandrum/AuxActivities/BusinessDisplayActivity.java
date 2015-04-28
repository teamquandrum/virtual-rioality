package com.quandrum.AuxActivities;

import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.quandrum.Json.Business;
import com.quandrum.Json.ContactDetails;
import com.quandrum.Json.FilePathSetter;
import com.quandrum.Json.JSONParser;
import com.quandrum.atos.R;

/**
 * Requires 'id','lat' and 'lon' extras in intent data
 * @author Abraham
 *
 */
public class BusinessDisplayActivity extends Activity {

	TextView mName;
	TextView mType;
	TextView mSdesc;
	TextView mContactDetails;
	TextView mRating;
	double mLon;
	double mLat;
	
	final String REFRESH = "REFRESH";
	
	Handler mHandler;
	
	int mId;
	Business mBusiness;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_business_display);
	
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg){
				Bundle b = msg.getData();
				String f = b.getString("function");
				
				if(f.equals(REFRESH)){
					handleRefresh(b);
				}
			}
		};
		
		FilePathSetter.setFilePaths(this);
		
		mName = (TextView)findViewById(R.id.name);
		
		Intent i = getIntent();
		mId = i.getIntExtra("id", -1);
		mLon = i.getDoubleExtra("lon", -1);
		mLat = i.getDoubleExtra("lat", -1);
		if(mId==-1){
			mName = (TextView)findViewById(R.id.name);
			mName.setText("Business not downloaded");
			Log.e("BusinessDisplayActivity","ID NOT SET!");
		}
		else{
			mBusiness = JSONParser.getBusinessById(mId);
			populateTextViews(mBusiness);
		}
		refresh();
	}
	public void populateTextViews(Business b){		
		mName = (TextView)findViewById(R.id.name);
		mType = (TextView)findViewById(R.id.type_tv);
		mSdesc = (TextView)findViewById(R.id.sdesc_tv);
		mContactDetails = (TextView)findViewById(R.id.contact_tv);
		mRating = (TextView)findViewById(R.id.rating_tv);
		
		mName.setText(b.getName());
		mType.setText(mType.getText()+b.getType());
		mSdesc.setText(mSdesc.getText()+b.getSdesc());
		List<ContactDetails> cdl = JSONParser.getContactDetailsByBusinessId(b.getId());
		String cDetails ="";
		for(int i=0;i<cdl.size();i++){
			ContactDetails cd = cdl.get(i);
			cDetails+=cd.getType()+": "+cd.getValue()+"\n";
		}
		mContactDetails.setText(cDetails);
		mRating.setText(mRating.getText().toString()+b.getRating());
	}
	public void loadMenu(View v){
		Intent i = new Intent(this,WebViewActivity.class);
		i.putExtra("url", mBusiness.getPricelist());
		startActivity(i);
	}
	public void loadBrochure(View v){
		Intent i = new Intent(this,WebViewActivity.class);
		i.putExtra("url", mBusiness.getBrochure());
		startActivity(i);
	}
	public void loadDirections(View v){
		String baseURL="http://maps.google.com/maps?d&saddr="+ mLat+","+mLon;
		String mapURL = baseURL+"&daddr="+mBusiness.getLat()+","+mBusiness.getLon()+"&dirflg=d";
		Intent intent = new Intent(android.content.Intent.ACTION_VIEW,Uri.parse(mapURL));
		intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
		startActivity(intent);
	}
	public void handleRefresh(Bundle b){
		try{String json = b.getString("json");
		JSONObject obj = new JSONObject(json);
		JSONObject result = obj.getJSONObject("result");
		
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public void refresh(){
		
	}
	public void loadDiscounts(View v){
		Intent i = new Intent(this,DiscountActivity.class);
		i.putExtra("id",mId);
		startActivity(i);
	}
}
