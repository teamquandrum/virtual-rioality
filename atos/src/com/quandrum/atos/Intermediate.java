package com.quandrum.atos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class Intermediate extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intermediate);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.intermediate, menu);
		return true;
	}
	
	public void camera(View v){
		Intent i = new Intent(getApplicationContext(),com.quandrum.atos.TakePictureActivity.class);
		startActivity(i);
	}
	
	public void slideshow(View v){
		Intent i = new Intent(getApplicationContext(),com.deitel.slideshow.Slideshow.class);
		startActivity(i);
	}

}
