package com.quandrum.AuxActivities;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.quandrum.Json.FilePathSetter;
import com.quandrum.Json.JSONParser;
import com.quandrum.Json.LeaderboardUser;
import com.quandrum.Json.TouristLocation;
import com.quandrum.atos.LatLonCalculator;
import com.quandrum.atos.LeaderboardUserLayout;
import com.quandrum.atos.NewUserActivity;
import com.quandrum.atos.PublishingAsyncTask;
import com.quandrum.atos.R;

public class LeaderboardActivity extends Activity {

	LinearLayout mLl;
	Handler mHandler;
	final String GET_LEADERBOARD = "GET_LEADERBOARD";
	ArrayList<TouristLocation> tll;
	
	ArrayList<LeaderboardUser> lbl = new ArrayList<LeaderboardUser>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_leaderboard);
		mLl = (LinearLayout)findViewById(R.id.ll);
		
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg){
				Bundle b = msg.getData();
				String function = b.getString("function");
				if(function.equals(GET_LEADERBOARD)){
					leaderboardReceived(b);
				}
			}
		};
		FilePathSetter.setFilePaths(this);
		tll = JSONParser.getAllTouristLocations();
		refresh();
	}
	public void leaderboardReceived(Bundle b){
		try{
			String json = b.getString("json");
			Log.d("json",json);
			JSONObject obj = new JSONObject(json);
			JSONArray result = obj.getJSONArray("result");
			lbl.clear();
			for(int i=0;i<result.length();i++){
				JSONObject jUser = result.getJSONObject(i);
				int id = jUser.getInt("id");
				String name = jUser.getString("name");
				String iemi = jUser.getString("iemi");
				int points = jUser.getInt("points");
				LeaderboardUser lbu = new LeaderboardUser(name, id, iemi, points);
				lbl.add(lbu);
			}
			displayBoard();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public void displayBoard(){
		mLl.removeAllViews();
		for(int i =0;i<lbl.size();i++){
			LeaderboardUserLayout lbu = getLeaderboardUserLayout(lbl.get(i));
			if(lbl.get(i).getIemi().equals(((TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId()))
			{
				lbu.nameTv.setTextColor(getResources().getColor(R.color.teal));
				lbu.pointsTv.setTextColor(getResources().getColor(R.color.teal));
			}
			
			mLl.addView(lbu);
		}
	}
	public LeaderboardUserLayout getLeaderboardUserLayout(LeaderboardUser user){
		return new LeaderboardUserLayout(this, user.getName(), user.getPoints()+"");
	}
	public void refresh(){
		String name = "";
		String iemi = ((TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
		if(userExists()){
			name = getUserName();
			HttpPost hp = new HttpPost("http://atosquandrum.net78.net/ci/");
			List<BasicNameValuePair> param = new ArrayList<BasicNameValuePair>();
			param.add(new BasicNameValuePair("controller","leaderboarduser"));
			param.add(new BasicNameValuePair("action","newEntry"));
			param.add(new BasicNameValuePair("name",name));
			param.add(new BasicNameValuePair("iemi", iemi));
			param.add(new BasicNameValuePair("points", "0"));
			
			if(isWithinTouristLocation()){
				param.add(new BasicNameValuePair("increment", "0"));
			}
			try {
				hp.setEntity(new UrlEncodedFormEntity(param));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ProgressDialog pd = new ProgressDialog(this);
			pd.setCancelable(false);
			pd.setMessage("Refreshing");
			
			PublishingAsyncTask pb = new PublishingAsyncTask(GET_LEADERBOARD, new DefaultHttpClient(), hp, mHandler, pd);
			pb.execute();
		}
		else{
			newUser();
		}
	}
	public void refresh(View v){
		String name = "";
		String iemi = ((TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
		if(userExists()){
			name = getUserName();
			HttpPost hp = new HttpPost("http://atosquandrum.net78.net/ci/");
			List<BasicNameValuePair> param = new ArrayList<BasicNameValuePair>();
			param.add(new BasicNameValuePair("controller","leaderboarduser"));
			param.add(new BasicNameValuePair("action","newEntry"));
			param.add(new BasicNameValuePair("name",name));
			param.add(new BasicNameValuePair("iemi", iemi));
			param.add(new BasicNameValuePair("points", "0"));
			
			if(isWithinTouristLocation()){
				param.add(new BasicNameValuePair("increment", "0"));
			}
			try {
				hp.setEntity(new UrlEncodedFormEntity(param));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ProgressDialog pd = new ProgressDialog(this);
			pd.setCancelable(false);
			pd.setMessage("Refreshing");
			
			PublishingAsyncTask pb = new PublishingAsyncTask(GET_LEADERBOARD, new DefaultHttpClient(), hp, mHandler, pd);
			pb.execute();
		}
		else{
			newUser();
		}
	}
	public boolean isWithinTouristLocation(){
		final double MAX_DISTANCE = 200;//metres
		LocationManager locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);     
		
		Location location = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		if(location==null){//TODO
			locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		}
		//double lat = location.getLatitude();
		//double lon = location.getLongitude();
		
		double lat = 13.7693212;
		double lon = 77.7693212;
		
		for(int i=0;i<tll.size();i++){
			TouristLocation tl = tll.get(i);
			double d = LatLonCalculator.getDistance(lat, lon, tl.getLatitude(), tl.getLongitude());
			if(d<MAX_DISTANCE){
				tll.remove(i);
				return true;
			}
		}
		return false;
		
	}
	
	
	public String getUserName(){
		try{
		File dir = this.getDir("user", 0);
		File f= new File(dir,"name");
		Scanner sc = new Scanner(f);
		String name = sc.nextLine();
		sc.close();
		return name;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	public boolean userExists(){
		File dir = this.getDir("user", 0);
		File f= new File(dir,"name");
		if(f.exists()){
			return true;
		}
		else
			return false;
	}
	public void newUser(){
		Intent i = new Intent(this,NewUserActivity.class);
		startActivityForResult(i,1);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode ,Intent data){
		refresh();
	}
}

