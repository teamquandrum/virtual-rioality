package com.example2.schedulerui;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.quandrum.atos.R;
@SuppressWarnings("unused")
public class Map extends FragmentActivity {
	GoogleMap map;
	private Marker[] placeMarkers;
	private final int MAX_PLACES = 20;
	private MarkerOptions[] places;
	GPSTracker gps; 
	double lat,lon;
	String query;
	EditText et;
	LatLngBounds.Builder b;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		et=(EditText) findViewById(R.id.editText1);
         map = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMap();
         if (map == null) {
             Toast.makeText(this,"Map not available",Toast.LENGTH_SHORT).show();
           
             finish();
         }
         map.setMyLocationEnabled(true);
         MarkerOptions a=new MarkerOptions();
         gps = new GPSTracker(this);
         if(gps.canGetLocation()){
        	 lat=gps.getLatitude();
        	 lon=gps.getLongitude();
        	 LatLng my1=new LatLng(gps.getLatitude(),gps.getLongitude());
        	 CameraPosition myPosition = new CameraPosition.Builder().target(my1).zoom(17).bearing(0).tilt(0).build();
        	 LatLng b=new LatLng(lat,lon);
        	 a.position(b).snippet("Click here to select this location").title("Current Location");
        	 map.addMarker(a);
        	 map.animateCamera(CameraUpdateFactory.newCameraPosition(myPosition));
        	  map.setOnInfoWindowClickListener(new OnInfoWindowClickListener(){
  		        public void onInfoWindowClick(Marker marker){
  		        	Toast.makeText(getApplicationContext(), marker.getTitle(), Toast.LENGTH_SHORT).show();
  		        	Intent returnIntent = new Intent();
  		        	//double[] array={marker.getPosition().latitude,marker.getPosition().longitude};
  		        	//Log.d("before",""+array.length);
  		        	//returnIntent.putExtra("hello", array);
  		        	Bundle args = new Bundle();
  		        	args.putParcelable("position", marker.getPosition());
  		        	//String result=marker.getPosition().toString(); 
  		        	returnIntent.putExtra("bundle",args);
  		        	 setResult(RESULT_OK,returnIntent);     
  		        	 Log.d("tag","reached");
  		        	 finish();
  		        }//close void
  		      }//close listener method
  		    ); //close listener arguement
         } 
	}
	
	public void mapSearch(View view) throws UnsupportedEncodingException{
		InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		String keyword=et.getText().toString().trim();
		Toast.makeText(this, keyword, Toast.LENGTH_LONG).show();
		if(keyword!=""||keyword!=null){
		keyword = URLEncoder.encode(keyword, "UTF-8");
		String base="https://maps.googleapis.com/maps/api/place/textsearch/json?query=";
		base+=keyword;
		base+=("&location="+lat+","+lon+"&radius=50000");
		base+="&sensor=true";
		base+="&key=AIzaSyDM4x2caaRSjzHJJlPMJV7MBs1cy3s5fi4";//ENTER THE BROWSER KEY FOR PLACES API HERE
		new GetPlaces().execute(base);
		}
	}
	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}*/

//Inner AsyncTask Private Class
	private class GetPlaces extends AsyncTask<String, Void, String> {

	@Override
	protected String doInBackground(String... placesURL) {
		// fetch places
		StringBuilder placesBuilder = new StringBuilder();
		//process search parameter string(s)
		for (String placeSearchURL : placesURL) {
		//execute search
			HttpClient placesClient = new DefaultHttpClient();
			try {
			    //try to fetch the data
				HttpGet placesGet = new HttpGet(placeSearchURL);
				HttpResponse placesResponse = placesClient.execute(placesGet);
				StatusLine placeSearchStatus = placesResponse.getStatusLine();
				if (placeSearchStatus.getStatusCode() == 200) {
					//we have an OK response
					HttpEntity placesEntity = placesResponse.getEntity();
					InputStream placesContent = placesEntity.getContent();
					InputStreamReader placesInput = new InputStreamReader(placesContent);
					BufferedReader placesReader = new BufferedReader(placesInput);
					String lineIn;
					while ((lineIn = placesReader.readLine()) != null) {
					    placesBuilder.append(lineIn);
					}
				}
			}
			catch(Exception e){
			    e.printStackTrace();
			}
		}//close-for
		return placesBuilder.toString();
	}
		//fetch and parse place data
	protected void onPostExecute(String res) {
		//Toast.makeText(getBaseContext(), "onPostExecute", Toast.LENGTH_SHORT).show();
	    //parse place data returned from Google Places
		if(placeMarkers!=null){
		    for(int pm=0; pm<placeMarkers.length; pm++){
		        if(placeMarkers[pm]!=null)
		            placeMarkers[pm].remove();
		    }
		}
		try {
			//Toast.makeText(getBaseContext(), "Enter Try Block", Toast.LENGTH_SHORT).show();
		    //parse JSON
			JSONObject resultObject = new JSONObject(res);
			JSONArray placesArray = resultObject.getJSONArray("results");
			places = new MarkerOptions[placesArray.length()];
			//Toast.makeText(getApplicationContext(), placesArray.length()+" places found", Toast.LENGTH_SHORT).show();
			//loop through places
			for (int p=0; p<placesArray.length(); p++) {
			    //parse each place
			boolean missingValue=false;
				LatLng placeLL=null;
				String placeName="";
				String vicinity="";
				//String icon;
				double rating = 0;
				try{
				    //attempt to retrieve place data values
				missingValue=false;
					JSONObject placeObject = placesArray.getJSONObject(p);
					JSONObject loc = placeObject.getJSONObject("geometry").getJSONObject("location");
					placeLL = new LatLng(
						    Double.valueOf(loc.getString("lat")),
						    Double.valueOf(loc.getString("lng")));
					vicinity = placeObject.getString("formatted_address");
					placeName = placeObject.getString("name");
					//icon = placeObject.getString("icon");
					//rating = placeObject.getDouble("rating");
					
				}
				catch(JSONException jse){
				  //  missingValue=true;
				    jse.printStackTrace();
				}
				vicinity="Click here to select this location";
				if(missingValue)    places[p]=null;
				else
				    places[p]=new MarkerOptions()
				    .position(placeLL)
				    .title(placeName)
				    .snippet(vicinity);
			}
		}
		catch (Exception e) {
		    e.printStackTrace();
		}
		
	if(places!=null){
		//Toast.makeText(getApplicationContext(), places.length+" places built", Toast.LENGTH_SHORT).show();
		map.clear();
		placeMarkers=new Marker[places.length];
		  b = new LatLngBounds.Builder();
		    for(int p=0; p<places.length; p++){
		        //will be null if a value was missing
		       if(places[p]!=null){
		    placeMarkers[p]=map.addMarker(places[p]);
		       b.include(places[p].getPosition());
		    //        map.addMarker(places[p]);
		       
		    }//close for
		    LatLngBounds bounds = b.build();
		  //Change the padding as per needed
		  CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 0);
		  map.animateCamera(cu);
		  map.setOnInfoWindowClickListener(new OnInfoWindowClickListener(){
		        public void onInfoWindowClick(Marker marker){
		        	Toast.makeText(getApplicationContext(), marker.getTitle(), Toast.LENGTH_SHORT).show();
		        	Intent returnIntent = new Intent();
		        	//double[] array={marker.getPosition().latitude,marker.getPosition().longitude};
		        	//Log.d("before",""+array.length);
		        	//returnIntent.putExtra("hello", array);
		        	Bundle args = new Bundle();
		        	args.putParcelable("position", marker.getPosition());
		        	//String result=marker.getPosition().toString(); 
		        	returnIntent.putExtra("bundle",args);
		        	 setResult(RESULT_OK,returnIntent);     
		        	 Log.d("tag","reached");
		        	 finish();
		        	 
		        }//close void
		      }//close listener method
		    ); //close listener argument
		    }

		}
	}//Close onPostExecute
		}
//Close the AsyncTask class	
	public void cancel(View view){
		Intent returnIntent = new Intent();
		setResult(RESULT_CANCELED, returnIntent);        
		finish();
	}
}
