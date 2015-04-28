package com.quandrum.atos;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.quandrum.Json.FilePathSetter;
import com.quandrum.atos.R;

public class MainActivity extends Activity {
	String mUrl="http://atosquandrum.net78.net/ci/";
	
	Handler mHandler;
	
	ProgressDialog mPd;
	
	final String BUSINESS_DOWNLOADED = "BUSINESS_DOWNLOADED";
	final String TOURISTLOCATIONS_DOWNLOADED="TOURISTLOCATIONS_DOWNLOADED";
	final String CONTACTDETAILS_DOWNLOADED="CONTACTDETAILS_DOWNLOADED";
	final String DISCOUNTS_DOWNLOADED = "DISCOUNTS_DOWNLOADED";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		FilePathSetter.setFilePaths(this);
		//Intent i = new Intent(this, AugmentedRealityActivity.class);
		//startActivity(i);
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg)
			{
				Bundle b = msg.getData();
				String function = b.getString("function");
				try{
				if(function.equals(BUSINESS_DOWNLOADED))
				{
					businessesDownloaded(b);
				}
				else if(function.equals(TOURISTLOCATIONS_DOWNLOADED)){
					touristLocationsDownloaded(b);
				}
				else if(function.equals(CONTACTDETAILS_DOWNLOADED)){
					contactDetailsDownloaded(b);
				}
				else if(function.equals(DISCOUNTS_DOWNLOADED)){
					discountsDownloaded(b);
				}
				}
				catch(Exception e){
					e.printStackTrace();
					Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
				}
			}
		};
		if(/*!isDownloadCompleted()*/true){//Remove comments
			AlertDialog.Builder adb = new AlertDialog.Builder(this);
			adb.setCancelable(false).setTitle("Download").setMessage("During the demonstration, this download will take place every time.");
			adb.setPositiveButton("Download", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					startDownload();					
				}
			});
			adb.create().show();
		}
		else{
			startAugmentedRealityActivity();
		}
	}
	public boolean isDownloadCompleted(){
		File f = new File(FilePathSetter.getDoneCheckFilePath(this));
		if(f.exists()){
			return true;
		}
		else{
			return false;
		}
	}
	public void startDownload(){
		mPd = getTailoredProgressDialog("Downloading Businesses", 0);
		mPd.setProgress(0);
		mPd.show();
		downloadBusiness();
	}
	public void businessesDownloaded(Bundle b){
		mPd.setProgress(1);
		mPd.setMessage("Downloading Tourist Attractions");
		saveToBusinessFile(b.getString("json"));
		downloadTouristLocations();
	}
	public void touristLocationsDownloaded(Bundle b){
		mPd.setProgress(2);
		mPd.setMessage("Downloading Contact Details");
		saveToTouristLocationsFile(b.getString("json"));
		downloadContactDetails();
	}
	public void contactDetailsDownloaded(Bundle b){
		mPd.setProgress(3);
		mPd.setMessage("Downloading Discounts");
		saveToContactDetailsFile(b.getString("json"));
		downloadDiscounts();
	}
	public void discountsDownloaded(Bundle b){
		saveToDiscountsFile(b.getString("json"));
		downloadsFinished();
	}
	public void saveToFile(String data, String path){
		try{
			File f =new File(path);
			PrintWriter pw = new PrintWriter(f);
			pw.print(data);
			pw.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public void saveToBusinessFile(String json){
		Log.d("Business",json);
		saveToFile(json,FilePathSetter.getBusinessFilePath(this));
	}
	public void saveToTouristLocationsFile(String json){
		saveToFile(json,FilePathSetter.getTouristLocationFilePath(this));
	}
	public void saveToContactDetailsFile(String json){
		saveToFile(json,FilePathSetter.getContactDetailsFilePath(this));
	}
	public void saveToDiscountsFile(String json){
		saveToFile(json,FilePathSetter.getDiscountsFilePath(this));
	}
	public void downloadDiscounts(){
		HttpPost hp = new HttpPost(mUrl);
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("controller","discountmodel"));
		params.add(new BasicNameValuePair("action", "getEntry"));
		try {
			hp.setEntity(new UrlEncodedFormEntity(params));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//ProgressDialog pd = getTailoredProgressDialog("Downloading Discounts", 3);
		
		PublishingAsyncTask pb = new PublishingAsyncTask(DISCOUNTS_DOWNLOADED,new DefaultHttpClient(), hp,mHandler, null);
		pb.execute();
	}
	public void downloadContactDetails(){
		HttpPost hp = new HttpPost(mUrl);
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("controller","contactmodel"));
		params.add(new BasicNameValuePair("action", "getEntry"));
		try {
			hp.setEntity(new UrlEncodedFormEntity(params));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//ProgressDialog pd = getTailoredProgressDialog("Downloading Contact Details", 2);
		
		PublishingAsyncTask pb = new PublishingAsyncTask(CONTACTDETAILS_DOWNLOADED,new DefaultHttpClient(), hp,mHandler, null);
		pb.execute();	
	}
	public void downloadBusiness(){
		HttpPost hp = new HttpPost(mUrl);
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("controller","businessmodel"));
		params.add(new BasicNameValuePair("action", "getEntry"));
		try {
			hp.setEntity(new UrlEncodedFormEntity(params));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//ProgressDialog pd = getTailoredProgressDialog("Downloading Businesses", 0);
		
		PublishingAsyncTask pb = new PublishingAsyncTask(BUSINESS_DOWNLOADED,new DefaultHttpClient(), hp,mHandler, null);
		pb.execute();
	}
	public void downloadTouristLocations(){
		HttpPost hp = new HttpPost(mUrl);
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("controller","touristmodel"));
		params.add(new BasicNameValuePair("action", "getEntry"));
		try {
			hp.setEntity(new UrlEncodedFormEntity(params));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// pd = getTailoredProgressDialog("Downloading Tourist Attractions", 1);
		
		PublishingAsyncTask pb = new PublishingAsyncTask(TOURISTLOCATIONS_DOWNLOADED,new DefaultHttpClient(), hp,mHandler, null);
		pb.execute();
	}
	public void downloadsFinished(){
		mPd.dismiss();
		showDownloadFinishedDialog();
		saveToFile("done", FilePathSetter.getDoneCheckFilePath(this));
		try {
			Log.d("Business",new Scanner(new File(FilePathSetter.getBusinessFilePath(this))).nextLine());
			Log.d("TouristLocations",new Scanner(new File(FilePathSetter.getTouristLocationFilePath(this))).nextLine());
			Log.d("ContactDetails",new Scanner(new File(FilePathSetter.getContactDetailsFilePath(this))).nextLine());
			Log.d("Discounts",new Scanner(new File(FilePathSetter.getDiscountsFilePath(this))).nextLine());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		startMainMenuActivity();
		//startAugmentedRealityActivity();
	}
	public void showDownloadFinishedDialog(){
		//AlertDialog.Builder adb = new AlertDialog.Builder(this);
		//adb.setTitle("Download Complete").setMessage("You are now ready to experience Rio on a whole new level!").setPositiveButton("Ok", null);
		//adb.show();
	}
	public ProgressDialog getTailoredProgressDialog(String message, int done){
		ProgressDialog pd = new ProgressDialog(this);
		pd.setIndeterminate(false);
		pd.setCancelable(false);
		pd.setTitle("Downloading data");
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setProgress(done);
		pd.setMax(4);
		pd.setMessage(message);
		return pd;
	}
	
	public void startMainMenuActivity(){
		Intent i = new Intent(this,MainMenu2.class);
		startActivity(i);
		finish();
	}
	public void startAugmentedRealityActivity(){
		Intent i = new Intent(this,AugmentedRealityActivity.class);
		startActivity(i);
	}
	
}