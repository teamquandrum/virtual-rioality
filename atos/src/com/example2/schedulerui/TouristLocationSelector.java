package com.example2.schedulerui;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.quandrum.atos.LatLonCalculator;
import com.quandrum.atos.R;

public class TouristLocationSelector extends Activity {
	static TouristLocationSelector touristLocationSelector;
	static ArrayList<FixedLocation> scheduled;
	static ArrayList<TouristLocation> touristLocations;
	ArrayList<String> names;
	ListView l;
	ArrayAdapter<String> adp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		names=new ArrayList<String>();
		touristLocations=new ArrayList<TouristLocation>();
		setContentView(R.layout.activity_tourist_location_selector);
		l=(ListView)findViewById(R.id.listed);
		adp=new ArrayAdapter<String> 
		(getBaseContext(),R.layout.list,names);
		l.setAdapter(adp);
	}
	
	public void addLocation(View view){
		Intent i=new Intent(this,TouristSelector.class);
		startActivityForResult(i,1);
		}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		  if (requestCode == 1) {
		     if(resultCode == RESULT_OK){      
		    	Bundle bundle = data.getParcelableExtra("bundle");
		 		LatLng a = bundle.getParcelable("location");
		 		String name=bundle.getString("name_of_location");
		 		touristLocations.add(new TouristLocation(a,name));
		 		update(name);
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
	
	public static TouristLocationSelector getInstance(){
		   return   touristLocationSelector;
		 }
	
	public void generate(View v){
		
		ArrayList<FixedLocation> fl = FixedLocationSelector.fixedLocations;
		if(fl==null){
			fl = new ArrayList<FixedLocation>();
		}
		FixedLocation f2 = new FixedLocation(StartEnd.endLoc,StartEnd.endTimeHour,StartEnd.endTimeMinute,0,0,StartEnd.endName);
		FixedLocation f1 = new FixedLocation(StartEnd.startLoc,0,0,StartEnd.startTimeHour,StartEnd.startTimeMinute,StartEnd.startName);
		fl.add(0,f1);
		fl.add(f2);
		
		//The FixedLocationSelector.fixedLocations has everything now
		for(int i=0;i<fl.size()-1;i++){		
			ArrayList<Double> lat = new ArrayList<Double>();
			ArrayList<Double> lon = new ArrayList<Double>();
			for(int j=0;j<touristLocations.size();j++){
				Log.d("tl added","round "+i+" tl index " + j);
				lat.add(touristLocations.get(j).loc.latitude);
				lon.add(touristLocations.get(j).loc.longitude);
			}
			ArrayList<Integer> r = returnLocations(fl.get(i).end_hour+fl.get(i).end_minute/60.0, fl.get(i).loc.latitude, fl.get(i).loc.longitude, fl.get(i+1).start_hour+fl.get(i+1).start_minute/60.0, fl.get(i+1).loc.latitude, fl.get(i+1).loc.longitude, lat, lon);
			Log.d("r size",r.size()+"");
			for(int j=0;j<r.size();j++){
				fl.add(i+1+j,new FixedLocation(touristLocations.get(r.get(j)).loc,0,0,0,0,touristLocations.get(r.get(j)).name));
			}
			i=i+r.size();
			for(int j=0;j<r.size();j++){
				Log.e("Removing tl","at "+j);
				touristLocations.remove((int)r.get(j)-j);
			}
			Log.e("tl size",""+touristLocations.size());
		}

		for(int i=0;i<fl.size();i++){
			Log.d("fixedLoc",fl.get(i).name);
		}
		scheduled=fl;

		try {
	 		BufferedWriter outputWriter = null;
	 		outputWriter = new BufferedWriter(new FileWriter(android.os.Environment.getExternalStorageDirectory()+"/ScheduleNames.txt"));
	 		for (int i = 0; i < scheduled.size(); i++) {
					String s=scheduled.get(i).name;
	 				outputWriter.write(s);
					outputWriter.newLine();
				}
	 		outputWriter.flush();  
	 		  outputWriter.close();  
	 		}
	 			catch (IOException e) {
					e.printStackTrace();
				}
		try {
	 		BufferedWriter outputWriter = null;
	 		outputWriter = new BufferedWriter(new FileWriter(android.os.Environment.getExternalStorageDirectory()+"/ScheduleLocations.txt"));
	 		for (int i = 0; i < scheduled.size(); i++) {
					String s=("&daddr="+scheduled.get(i).loc.latitude+","+scheduled.get(i).loc.longitude+"&dirflg=d");
	 				outputWriter.write(s);
					outputWriter.newLine();
				}
	 		outputWriter.flush();  
	 		  outputWriter.close();  
	 		}
	 			catch (IOException e) {
					e.printStackTrace();
				}
		Intent i=new Intent(this,DisplaySchedule.class);
		//TouristLocationSelector.getInstance().finish();
		FixedLocationSelector.getInstance().finish();
		StartEnd.getInstance().finish();
		startActivity(i);
		finish();
	}
	/**
	 * returns indexes of chosen locations in passed array
	 * @param t1//in hours 
	 * @param lat1
	 * @param lon1
	 * @param t2//in hours
	 * @param lat2
	 * @param lon2
	 * @param lat
	 * @param lon
	 * @return
	 */
	public ArrayList<Integer> returnLocations(double t1, double lat1, double lon1, double t2, double lat2, double lon2, ArrayList<Double> lat, ArrayList<Double> lon){
		final double SPEED = 20000;//m/h
		double maxTime = Math.abs(t2-t1);//in hours
		double cTime = 0;
		ArrayList<Integer> r = new ArrayList<Integer>();
		double llat = 0;
		double llon = 0;
		for(int i=0;i<lat.size();i++)
		{
			double cLat = lat.get(i);
			double cLon = lon.get(i);
			Log.d("lat 1 lon 1",lat1+" "+lon1);
			Log.d("lat 2 lon 2",lat2+" "+lon2);
			Log.d("lat lon",cLat+ " "+cLon);
			double time=0;
			if(llat==0&llon==0)
			{
				time = (LatLonCalculator.getDistance(cLat, cLon, lat1, lon1)+LatLonCalculator.getDistance(cLat, cLon, lat2, lon2))/SPEED;
			}
			else{
				time =(LatLonCalculator.getDistance(cLat, cLon, llat, llon)+LatLonCalculator.getDistance(cLat, cLon, lat2, lon2))/SPEED;
			}
			Log.d("time",""+time);
			cTime+=time;
			Log.d("cTime maxTime",""+cTime+" M "+maxTime);
			if(cTime<maxTime){
				llat=cLat;
				llon=cLon;
				r.add(i);
			}
		}
		return r;
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fixed_location_selector, menu);
		return true;
	}*/

}
