package com.example.infomap;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.quandrum.Json.TouristLocation;
import com.quandrum.atos.R;
@SuppressWarnings("unused")
public class InfoMap extends FragmentActivity {
	
	private Marker[] placeMarkers;
	private   MarkerOptions[] games;
	private   Marker[] gamification;
	private final int MAX_PLACES = 20;
	private   MarkerOptions[] places;
	  GoogleMap map;
	GPSTracker gps;
	double lat=0.0,lon=0.0;
	  LatLngBounds.Builder b;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_map);
		 
		map = ((SupportMapFragment)getSupportFragmentManager()
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
        	 a.position(b);
        	 map.addMarker(a);
        	 map.animateCamera(CameraUpdateFactory.newCameraPosition(myPosition));
         }
        try{ 
         com.quandrum.Json.FilePathSetter.setFilePaths(this);
         ArrayList<TouristLocation> locs=new ArrayList<TouristLocation>();
         locs=com.quandrum.Json.JSONParser.getAllTouristLocations();
         LatLng b[]=new LatLng[locs.size()];
         for(int i=0;i<locs.size();i++){
        	double latit=locs.get(i).getLat();
        	double longi=locs.get(i).getLon();
        	b[i]=new LatLng(latit, longi);
         }
         gameLocations(b);
        }
        catch(Exception e){
        	System.out.println("Whoops");
        }
        
	}
	
public void selectFilters(View view){
	startActivityForResult((new Intent(this,FilterSelector.class)),1);
}

protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	
	  if (requestCode == 1) {

	     if(resultCode == RESULT_OK){      
	        String result=data.getStringExtra("result");  
	        Toast.makeText(this,result,Toast.LENGTH_SHORT).show();
	        String base="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=";
	 		base+=(lat+","+lon+"&radius=10000");
	 		base+="&sensor=true";
	 		base+=("&types="+result);
	 		base+="&key=AIzaSyDM4x2caaRSjzHJJlPMJV7MBs1cy3s5fi4";//ENTER THE BROWSER KEY FOR PLACES API HERE
	 		new GetPlaces().execute(base);
	         
	     }
	     if (resultCode == RESULT_CANCELED) {    
	         //Write your code if there's no result
	    	 
	     }
	  }
	}//onActivityResult

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
				//	vicinity = placeObject.getString("formatted_address");
					placeName = placeObject.getString("name");
					//icon = placeObject.getString("icon");
					rating = placeObject.getDouble("rating");
					
				}
				catch(JSONException jse){
				  //  missingValue=true;
				    jse.printStackTrace();
				}
				vicinity="Click here to navigate to this location";
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
		//map.clear();
		placeMarkers=new Marker[places.length];
		fitMap();
	
		    for(int p=0; p<places.length; p++){
		        //will be null if a value was missing
		       if(places[p]!=null)
		    placeMarkers[p]=map.addMarker(places[p]);
		       
		    //        map.addMarker(places[p]);
		       
		    }//close for
		    
		  //Change the padding as per needed
		  
		  map.setOnInfoWindowClickListener(new OnInfoWindowClickListener(){
		        public void onInfoWindowClick(Marker marker){
		        	Toast.makeText(getApplicationContext(), marker.getTitle(), Toast.LENGTH_SHORT).show();
		        	LatLng l=marker.getPosition();
		        	double lat2=l.latitude;
		        	double lon2=l.longitude;
		        	String baseURL="http://maps.google.com/maps?d&saddr="+lat+","+lon;
					String mapURL = baseURL+"&daddr="+lat2+","+lon2+"&dirflg=d";
						Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(mapURL));
						startActivity(intent);
		        }//close void
		      }//close listener method
		    ); //close listener argument
		  


		}
	}//Close onPostExecute
		}
//Close the AsyncTask class	

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.infomain, menu);
		return true;
	}*/
	
	public   void fitMap() {
		  b = new LatLngBounds.Builder();
		if(places != null){
			for(int p=0;p<places.length;p++){
			b.include(places[p].getPosition());  
		    }
		}
			if(games != null){
				for(int p=0;p<games.length;p++){
				b.include(games[p].getPosition());  
			    }
			}
			if((places!=null)||(games!=null)){
			LatLngBounds bounds = b.build();
			CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 100);
			//CameraPosition cameraPosition = new CameraPosition.Builder().target(map.getCameraPosition().target).zoom(map.getCameraPosition().zoom-1).build();     
			map.animateCamera(cu);
			//CameraUpdate c= CameraUpdateFactory.zoomTo((map.getCameraPosition().zoom)-1);
			//map.animateCamera(c);
			//map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		}
	}
	public void close(View v){
		finish();
	}
	
	

	public   void gameLocations(LatLng[] Locations){
		gamification=new Marker[Locations.length];
		games=new MarkerOptions[Locations.length];
		for(int i=0;i<Locations.length;i++){
		games[i]=new MarkerOptions().position(Locations[i]).title("Game Location!").snippet("Click here to navigate to this location").icon(BitmapDescriptorFactory.fromResource(R.drawable.mapicon));
		gamification[i]=map.addMarker(games[i]);
		}
		map.setOnInfoWindowClickListener(new OnInfoWindowClickListener(){
	        public void onInfoWindowClick(Marker marker){
	        	Toast.makeText(getApplicationContext(), marker.getTitle(), Toast.LENGTH_SHORT).show();
	        	LatLng l=marker.getPosition();
	        	double lat2=l.latitude;
	        	double lon2=l.longitude;
	        	String baseURL="http://maps.google.com/maps?d&saddr="+lat+","+lon;
				String mapURL = baseURL+"&daddr="+lat2+","+lon2+"&dirflg=d";
					Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(mapURL));
					startActivity(intent);
	        }//close void
	      }//close listener method
	    ); //close listener argument
		fitMap();
	}
}
