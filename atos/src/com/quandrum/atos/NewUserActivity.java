package com.quandrum.atos;

import java.io.File;
import java.io.PrintWriter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NewUserActivity extends Activity {
	EditText name_et;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_user);
		name_et = (EditText)findViewById(R.id.name_et);
	}
	public void entered(View v){
		try{
		File dir = this.getDir("user", 0);
		File f= new File(dir,"name");
		PrintWriter pw = new PrintWriter(f);
		pw.print(name_et.getText().toString());
		pw.flush();
		pw.close();
		finish();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	@Override
	public void onBackPressed(){
		Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
	}
}	
