package com.quandrum.atos;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nineoldandroids.view.ViewHelper;
import com.quandrum.AuxActivities.BusinessDisplayActivity;
import com.quandrum.AuxActivities.LocationChooserActivity;
import com.quandrum.AuxActivities.TouristLocationDisplayActivity;
import com.quandrum.Json.AugmentedRealityLocation;
import com.quandrum.Json.Business;
import com.quandrum.Json.FilePathSetter;
import com.quandrum.Json.JSONParser;
import com.quandrum.Json.LocationRetriever;
import com.quandrum.Json.TouristLocation;
import com.quandrum.atos.R;

public class AugmentedRealityActivity extends Activity implements SensorEventListener, OnClickListener{
	private SensorManager mSensorManager;
	private Sensor mCompass;
	private TextView mTextView;
	private RelativeLayout mMrl;
	private Camera mCamera;
	private CameraViewer mCameraViewer;	
	ARManager mArManager;
	
	private final static float FOV = 80.0f;//degrees
	
	private float mAzimuth;
	
	private boolean paused = true;
	
	private final static int REFRESH_RATE = 40;//milliseconds
	
	Handler mHandler;

	final static String UPDATE_UI = "UPDATE_UI";
	
	int w;
	
	//Location requirements
	final double MAX_RADIUS =1000;//in m
	final double RECALC_DISTANCE = 0.1;//100 metres
	final double LOC_UPDATE_DISTANCE = 10;//in metres
	private boolean mGpsEnabled = false;
	private boolean mNetworkEnabled = false;
	//String provider;
	LocationManager mLocManager;
	List<AugmentedRealityLocation> mLocList;
	List<AugmentedLayout> mAugLayoutList;//list of layouts being displayed
	int mAlCtr;
	
	final long LOCATION_REFRESH_TIME= 10000;
	
	LocationListener mLocListener = new LocationListener(){
		Location oldLoc;
		final int MAX_SUPER_OLD_LOC_TIME = 30000;
		final int MAX_OLD_LOC_TIME=10000;
		final double MIN_SUPER_ACCURACY = 200;
		final double MIN_ACCURACY_DIFF=100;
		@Override
		public void onLocationChanged(Location newLoc) {
			
			if(oldLoc==null)
			{
				oldLoc=newLoc;
			}
			if(oldLoc.getTime()-newLoc.getTime()>MAX_SUPER_OLD_LOC_TIME)
			{
				if(newLoc.getAccuracy()<MIN_SUPER_ACCURACY)
				{
					oldLoc=newLoc;
				}
			}
			if(oldLoc.getTime()-newLoc.getTime()>MAX_OLD_LOC_TIME)
			{
				if(newLoc.getAccuracy()<MIN_SUPER_ACCURACY)
				{
					if(newLoc.getAccuracy()-oldLoc.getAccuracy()<MIN_ACCURACY_DIFF)
					oldLoc=newLoc;
				}
			}
			locationUpdated(oldLoc);
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			
		}

		@Override
		public void onProviderDisabled(String provider) {
			
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    setContentView(R.layout.activity_deprecated_compass);

		FilePathSetter.setFilePaths(this);
		
	    mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
	    mCompass = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
	    mTextView = (TextView) findViewById(R.id.hello);
	    
	    initialiseHandler();
	    
	    Display display = getWindowManager().getDefaultDisplay();
	    Point p = new Point();
	    w = display.getWidth();
	    
	    //getting locations
	    mLocList = JSONParser.getAllLocations();
	    //finished getting locations
	    
	    //setting up location	  
	    mAugLayoutList = new LinkedList<AugmentedLayout>();
	    mLocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    String provider = mLocManager.getBestProvider(new Criteria(), true);
	    if(provider==null)
	    {
	    	provider = LocationManager.NETWORK_PROVIDER;
	    }
	    else{
	 	  Location lastLoc = mLocManager.getLastKnownLocation(provider);
	 	  if(lastLoc==null)
	 		  System.out.println("LAST LOC NULL");
	 	  else
	 		  mArManager = new ARManager(lastLoc.getLatitude(),lastLoc.getLongitude(),MAX_RADIUS,RECALC_DISTANCE,mLocList);
	    }
	    //finished setting up location
	    //Camera initialised in onResume()
	}
	public void registerLocationListeners()//TODO check if GPS enabled and allow GPS
	{
		if(mLocManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
		{
			mLocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME, (float)LOC_UPDATE_DISTANCE, mLocListener);
			mGpsEnabled=true;
		}
		String provider = mLocManager.getBestProvider(new Criteria(), true);
		if(provider==null)
		{
			//Toast.makeText(this, "Please enable Network/GPS", Toast.LENGTH_LONG).show();
		}
		else
		{
			//Log.d("provider",provider);
			mLocManager.requestLocationUpdates(provider, LOCATION_REFRESH_TIME, (float)LOC_UPDATE_DISTANCE, mLocListener);

			mNetworkEnabled=true;
		}
	}
	public void initialiseHandler()
	{
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg)
			{
				Bundle b = msg.getData();
				String function = b.getString("function");
				
				if(function.equals(UPDATE_UI))
				{
					if(paused)
						return;
					updateUI();
					refreshHandlerMessage();
				}
			}
		};
	}
	public void updateUI()//TODO modify to allow real working
	{
		Log.d("mAugLayoutSize","At updateUI: "+mAugLayoutList.size());
		mAlCtr=0;
		if(mArManager==null)
		{
			Log.e("ARManager","NULL at updateUI()");
			registerLocationListeners();
			return;
		}
		//else
			//Log.d("Current Location","lat: "+mArManager.mCurrentLat+" lon: "+mArManager.mCurrentLon);
					
		List<AugmentedRealityLocation> closeLocs = mArManager.getCloseLocations();
		//Log.d("closeLocs",""+closeLocs.size());
		LinkedList<AugmentedRealityLocation> visibleLocs = new LinkedList<AugmentedRealityLocation>();
		for(int i=0;i<closeLocs.size();i++)
		{
			double tposition = getPosition(closeLocs.get(i));
			//Log.d("updateUI","index: "+i+" "+getDistance(closeLocs.get(i)));
			Log.d("updateUI loc","lat: "+mArManager.getCurrentLatitude()+"long"+mArManager.getCurrentLongitude());
			if(isLocationVisible(tposition))//TODO can be improved for performance by checking only for difference in angle lesser than FOV/2
			{
				/*We will populate all visible locations into a new List and order them according to decreasing distance*/
				AugmentedRealityLocation currentLoc = closeLocs.get(i);
				Log.d("loc","visible id: "+currentLoc.type);
				double d = getDistance(currentLoc);
				boolean added = false;
				for(int j=0;j<visibleLocs.size();j++)
				{
					Log.d("insideAddingLoop","visible id: "+currentLoc.type+" visibleLocsSize: "+visibleLocs.size());
					AugmentedRealityLocation oldLoc = visibleLocs.get(j);
					double d1 = getDistance(oldLoc);
					if(d>d1)
					{
						visibleLocs.add(j, currentLoc);
						added=true;
						break;
					}
				}
				if(!added)
					visibleLocs.add(currentLoc);
			}			
		}
		for(int i =0;i<visibleLocs.size();i++)
		{
			Log.d("visible being displayed",""+visibleLocs.size());
			displayNewLocationLayout(visibleLocs.get(i),getDistance(visibleLocs.get(i)));
		}
		hideUnusedAugmentedLayouts();
	}
	public void displayNewLocationLayout(AugmentedRealityLocation loc, double distance)
	{
		Log.d("alCtr",""+mAlCtr);
		Log.d("auglayoutlist",mAugLayoutList.size()+"");
		distance = Math.round(distance);
		if(loc.type==AugmentedRealityLocation.TYPE_BUSINESS)//TODO display actual details
		{
			Business b = (Business)loc;
			String args[] = {"Type: "+b.getType(),"Facilities: "+b.getSdesc(),"Rating: "+Math.round(b.getRating()*100)/100.0};
			AugmentedLayout layout = getAugmentedLayout(mAugLayoutList, loc, mAlCtr);
			if(layout==null){
				Log.e("layoutOptimisation","Layout created business");
				layout = new AugmentedLayout(this, b.getId(),b.getName(),distance,args,null,loc);
				mMrl.addView(layout);
				layout.setOnClickListener(this);
				RelativeLayout.LayoutParams ll = new RelativeLayout.LayoutParams((int)(w*2/3.0), RelativeLayout.LayoutParams.WRAP_CONTENT);
				ll.addRule(RelativeLayout.CENTER_VERTICAL);
				int padding = SizeManager.getDip(20, getResources().getDisplayMetrics());
				layout.setPadding(padding, padding, padding, padding);
				layout.setLayoutParams(ll);
				mAugLayoutList.add(layout);
				mAlCtr++;
			}
			else{
				//Log.d("layoutOptimisation","Layout reused");
				layout.reset(b.getId(), b.getName(), distance, args, null,loc);
				layout.setVisibility(View.VISIBLE);
				mAlCtr++;
			}			
			layout.bringToFront();
			ViewHelper.setX(layout, (float) (getPosition(loc)-w/3.0));
			Log.d("displayNewLocationLayout","Business at "+distance+"m");
		}
		else if(loc.type==AugmentedRealityLocation.TYPE_TOURIST_LOCATION)
		{
			TouristLocation tl = (TouristLocation)loc;
			String args[] = {"Description:"+tl.getDescription()};
			AugmentedLayout layout = getAugmentedLayout(mAugLayoutList, loc, mAlCtr);
			if(layout==null){
				Log.e("layoutOptimisation","Layout created tourist location");
				layout = new AugmentedLayout(this, tl.getId(),tl.getName(),distance,args,null,loc);
				layout.setOnClickListener(this);
				mMrl.addView(layout);
				RelativeLayout.LayoutParams ll = new RelativeLayout.LayoutParams((int)(w*2/3.0), RelativeLayout.LayoutParams.WRAP_CONTENT);
				ll.addRule(RelativeLayout.CENTER_VERTICAL);
				int padding = SizeManager.getDip(20, getResources().getDisplayMetrics());
				layout.setPadding(padding, padding, padding, padding);
				layout.setLayoutParams(ll);
				mAugLayoutList.add(layout);
				mAlCtr++;
			}
			else{
				//Log.e("layoutOptimisation","Layout reused");
				layout.reset(tl.getId(), tl.getName(), distance, args, null,loc);
				layout.setVisibility(View.VISIBLE);
				mAlCtr++;
			}
			layout.bringToFront();
			//Log.d("TOURIST POS","lat: "+loc.getLatitude()+" lon: "+loc.getLongitude());
			ViewHelper.setX(layout, (float) (getPosition(loc)-w/3.0));
			//Log.d("Tourist location",""+getPosition(loc));
		}
	}
	/**
	 * returns an existing layout if possible, else creates
	 * @param loc
	 * @param ctr number of al already created
	 * @return
	 */
	public AugmentedLayout getAugmentedLayout(List<AugmentedLayout> alList,AugmentedRealityLocation loc, int ctr){
		if(ctr==alList.size()){
			return null;
		}
		else{
			return alList.get(ctr);
		}
	}
	public void hideUnusedAugmentedLayouts(){
		for(int i=mAlCtr;i<mAugLayoutList.size();i++){
			mAugLayoutList.get(i).setVisibility(View.GONE);
		}
	}
	/*public void removeAllAugmentedLayouts()
	{
		for(int i =0;i<mAugLayoutList.size();i++)
		{
			mMrl.removeView(mAugLayoutList.get(i));
			mAugLayoutList.remove(i);
		}
	}*/
	public double getLocationBearing(AugmentedRealityLocation loc)
	{
		return LatLonCalculator.getBearing(mArManager.getCurrentLatitude(), mArManager.getCurrentLongitude(), loc.getLatitude(), loc.getLongitude());
	}
	public boolean isLocationVisible(double position)//TODO make it work with relativelayout params
	{
		if(position<0||position>w)
		{
			return false;
		}
		return true;
	}
	public double getPosition(AugmentedRealityLocation loc)
	{
		double objAngle = LatLonCalculator.getBearing(mArManager.getCurrentLatitude(), mArManager.getCurrentLongitude(), loc.getLatitude(), loc.getLongitude());
		
		float tAzimuth = mAzimuth;
		double angleDiff = objAngle - tAzimuth;
		//TODO CALCULATION ISSUE
		if(angleDiff<-180){
			angleDiff = angleDiff+360;
		}
		else if(angleDiff>180){
			angleDiff = angleDiff-360;
		}
		double center = w/2;
		double pixelPerDegree = w/FOV;
		double position = angleDiff*pixelPerDegree+center;
		Log.d("Tourist location","pos: "+position+" angleDiff: "+angleDiff+ "azimuth"+mAzimuth+"objAngle"+objAngle);
		return position;
	}	
	public double getDistance(AugmentedRealityLocation loc)
	{
		return LatLonCalculator.getDistance(mArManager.getCurrentLatitude(), mArManager.getCurrentLongitude(), loc.getLatitude(), loc.getLongitude());
	}
	public void refreshHandlerMessage()
	{
		Message msg = mHandler.obtainMessage();
		Bundle b = new Bundle();
		b.putString("function",UPDATE_UI);
		msg.setData(b);
		mHandler.sendMessageDelayed(msg, REFRESH_RATE);
	}
	public void locationUpdated(Location loc)
	{
		if(mArManager==null)
		{
			mArManager = new ARManager(loc.getLatitude(),loc.getLongitude(),MAX_RADIUS,RECALC_DISTANCE,mLocList);
		}
		else{
			mArManager.setCurrentLocation(loc.getLatitude(), loc.getLongitude());
		}
	}
	// The following method is required by the SensorEventListener interface;
	public void onAccuracyChanged(Sensor sensor, int accuracy) {    
	}

	// The following method is required by the SensorEventListener interface;
	// Hook this event to process updates;
	public void onSensorChanged(SensorEvent event) {
	    mAzimuth = Math.round(event.values[0]);
	    if(mAzimuth>180)
	    {
	    	mAzimuth-=360;
	    }
	    // The other values provided are: 
	    //  float pitch = event.values[1];
	    //  float roll = event.values[2];
	    mTextView.setText("Azimuth: " + Float.toString(mAzimuth));
	}
	Camera getCameraInstance()
	{
		Camera c = null;
		try{
			c = Camera.open();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return c;//if camera opening failed, returns null
	}
	@Override
	protected void onPause() {
	    // Unregister the listener on the onPause() event to preserve battery life;
	    super.onPause();
	    paused = true;
	    mSensorManager.unregisterListener(this);
	    mLocManager.removeUpdates(mLocListener);
	    if(mCameraViewer!=null)
	    {
	    	mMrl.removeView(mCameraViewer);
	    }
	}

	@Override
	protected void onResume() {
	    super.onResume();
	    paused = false;
	    refreshHandlerMessage();
	    mSensorManager.registerListener(this, mCompass, SensorManager.SENSOR_DELAY_NORMAL);
	    registerLocationListeners();
	    mCamera = getCameraInstance();
	    if(mCamera==null)
	    {
	    	Toast.makeText(this, "Camera in user", Toast.LENGTH_LONG).show();
	    }
	    else{
		    mCamera.setDisplayOrientation(90);
	    	mCameraViewer = new CameraViewer(this,mCamera);
	    	mMrl = (RelativeLayout)findViewById(R.id.container);
	    	mMrl.addView(mCameraViewer);
	    }
	}
	@Override
	public void onClick(View v){
		if(mAlCtr==1){
			Intent i=null;
			AugmentedLayout al = mAugLayoutList.get(0);
			if(al.getAugmentedRealityLocationType()==AugmentedRealityLocation.TYPE_BUSINESS){
				i = new Intent(AugmentedRealityActivity.this,BusinessDisplayActivity.class);
			}
			else if(al.getAugmentedRealityLocationType()==AugmentedRealityLocation.TYPE_TOURIST_LOCATION){
				i = new Intent(AugmentedRealityActivity.this,TouristLocationDisplayActivity.class);
			}
			i.putExtra("id", al.getId());
			i.putExtra("lat", mArManager.getCurrentLatitude());
			i.putExtra("lon", mArManager.getCurrentLongitude());
			startActivity(i);
		}
		else if(mAlCtr>1){
			Intent i = new Intent(AugmentedRealityActivity.this,LocationChooserActivity.class);
			i.putExtra("bids", getDisplayedBusinessIds());
			i.putExtra("tlids", getDisplayedTouristLocationIds());
			i.putExtra("lat", mArManager.getCurrentLatitude());
			i.putExtra("lon", mArManager.getCurrentLongitude());
			startActivity(i);
		}
	}
	public int[] getDisplayedBusinessIds(){
		ArrayList<Integer> bidsl = new ArrayList<Integer>();
		
		for(int i=0;i<mAlCtr;i++){
			AugmentedLayout al = mAugLayoutList.get(i);
			if(al.getAugmentedRealityLocationType()==AugmentedRealityLocation.TYPE_BUSINESS){
				bidsl.add(al.getId());
			}
		}
		
		int[] bids = new int[bidsl.size()];
		for(int i=0;i<bids.length;i++){
			bids[i]=bidsl.get(i);
		}
		Log.e("bids length",""+bids.length);
		return bids;
	}
	public int[] getDisplayedTouristLocationIds(){
		ArrayList<Integer> tlidsl = new ArrayList<Integer>();
		
		for(int i=0;i<mAlCtr;i++){
			AugmentedLayout al = mAugLayoutList.get(i);
			if(al.getAugmentedRealityLocationType()==AugmentedRealityLocation.TYPE_TOURIST_LOCATION){
				tlidsl.add(al.getId());
			}
		}
		
		int[] tlids = new int[tlidsl.size()];
		for(int i=0;i<tlids.length;i++){
			tlids[i]=tlidsl.get(i);
		}
		Log.e("tlids length",""+tlids.length);
		return tlids;
	}
}
