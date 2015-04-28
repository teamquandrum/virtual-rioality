package com.quandrum.atos;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.quandrum.AuxActivities.LeaderboardActivity;

public class MainMenu2 extends Activity {
Context context;
AlertDialog alert;
Intent i;
GridView gridView;
 ArrayList<Item> gridArray = new ArrayList<Item>();
  CustomGridViewAdapter customGridAdapter;
 @SuppressWarnings("deprecation")
@Override
 protected void onCreate(Bundle savedInstanceState) {
	 
	 
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_main_menu2);
  
  final LocationManager manager= (LocationManager) getSystemService( Context.LOCATION_SERVICE );
  
  this.context=this;
  //set grid view item
  Bitmap ar = BitmapFactory.decodeResource(this.getResources(), R.drawable.ar1);
  Bitmap di = BitmapFactory.decodeResource(this.getResources(), R.drawable.di1);
  Bitmap ds = BitmapFactory.decodeResource(this.getResources(), R.drawable.ds1);
  Bitmap im = BitmapFactory.decodeResource(this.getResources(), R.drawable.im1);
  Bitmap lb = BitmapFactory.decodeResource(this.getResources(), R.drawable.lb1);
  Bitmap tp = BitmapFactory.decodeResource(this.getResources(), R.drawable.tp1);
  Bitmap tr = BitmapFactory.decodeResource(this.getResources(), R.drawable.tr1);
  Bitmap vs = BitmapFactory.decodeResource(this.getResources(), R.drawable.vs1);
  
  gridArray.add(new Item(ar,"Augmented Reality"));
  gridArray.add(new Item(im,"Information Map"));
  gridArray.add(new Item(ds,"Daily Scheduler"));
  gridArray.add(new Item(tr,"Translate"));
  gridArray.add(new Item(tp,"Take A Picture"));
  gridArray.add(new Item(vs,"View Slideshow"));
  gridArray.add(new Item(lb,"Leader Board"));
  gridArray.add(new Item(di,"Delete All Images"));
 
  gridView = (GridView) findViewById(R.id.gridView1);
  Display display = getWindowManager().getDefaultDisplay();
  gridView.setColumnWidth((int) (display.getWidth()/3));
  customGridAdapter = new CustomGridViewAdapter(this, R.layout.row_grid, gridArray);
  gridView.setAdapter(customGridAdapter);
  gridView.setOnItemClickListener(new OnItemClickListener() {

   @Override
   public void onItemClick(AdapterView<?> arg0, View v, int position,
     long arg3) {
	   
	   switch(position){
	   
	   case 0:
		   i = new Intent(getBaseContext(),AugmentedRealityActivity.class);
		   startActivity(i);
		   break;
   		
	   case 1:
		   if (manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
		   i= new Intent(getBaseContext(),com.example.infomap.InfoMap.class);
		   startActivity(i);
		   }
		   else Toast.makeText(getBaseContext(), "Please enable GPS before continuing", Toast.LENGTH_SHORT).show();
		   break;
	   
	   case 2:
		   if (manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
			   i= new Intent(getBaseContext(),com.example2.schedulerui.MainActivity.class);
			   startActivity(i);
			   }
			   else Toast.makeText(getBaseContext(), "Please enable GPS before continuing", Toast.LENGTH_SHORT).show();
			   break;
		  
	   case 3:
		   i = new Intent();
		   i.setAction(Intent.ACTION_VIEW);
		   i.setComponent(new ComponentName("com.google.android.apps.translate",
                   "com.google.android.apps.translate.TranslateActivity"));
		   try{
		   startActivity(i);
		   }
		   catch(Exception e){
			   Toast.makeText(getBaseContext(), "Please install Google Translate from the Play Store", Toast.LENGTH_SHORT).show();
			   Intent intent = new Intent(Intent.ACTION_VIEW);
			   intent.setData(Uri.parse("market://details?id=com.google.android.apps.translate"));
			   startActivity(intent);
		   }
		   break;
		   
	   case 4:
		   i = new Intent(getApplicationContext(),com.quandrum.atos.TakePictureActivity.class);
		   startActivity(i);
		   break;
	   
	   case 5:
		   File g=com.quandrum.Json.FilePathSetter.getImageDirectory(getApplicationContext());
		   String[] glist=g.list();
		   if(glist.length>0){
		   i = new Intent(getApplicationContext(),com.deitel.slideshow.Slideshow.class);
		   startActivity(i);
		   }
		   else {
			   Toast.makeText(context,"Sorry, no images to display.",Toast.LENGTH_SHORT).show();
		   }
		   break;
		   
	   case 6:
		   //Toast.makeText(getBaseContext(), "Leaderboard", Toast.LENGTH_SHORT).show();
		   i = new Intent(getApplicationContext(),LeaderboardActivity.class);
		   startActivity(i);
		   break;
	   
	   case 7:
		  
		   AlertDialog.Builder builder = new AlertDialog.Builder(context);
		   builder.setTitle("Delete All Images");  
		   builder.setMessage("Are you sure you want to delete all the images you have taken? This action cannot be undone.");   
		   builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {  
		      public void onClick(DialogInterface dialog, int id) {  
		        MainMenu2.this.delete(com.quandrum.Json.FilePathSetter.getImageDirectory(getApplicationContext()));
		   }  
		   });
		   builder.setNegativeButton("No", new DialogInterface.OnClickListener() {  
			      public void onClick(DialogInterface dialog, int id) {
			      dialog.cancel();
			   }  
			   });
		   AlertDialog alert2 = builder.create();
		   alert2.show();
		   
	   }//End Switch
	   
    //Toast.makeText(getApplicationContext(),gridArray.get(position).getTitle(), Toast.LENGTH_SHORT).show();
    
   }
  });
 }

protected void delete(File f) {
	String[] flist=f.list();
	for(int i=0;i<flist.length;i++){
	    System.out.println(" "+f.getAbsolutePath());
	    File temp=new File(f.getAbsolutePath()+"/"+flist[i]);
	    if(temp.isDirectory()){
	       delete(temp) ;
	       temp.delete();
	    }else{
	    temp.delete();
	    }
	}
	Toast.makeText(this,"Successfully Deleted",Toast.LENGTH_SHORT).show();
	// TODO Auto-generated method stub
	
}
}