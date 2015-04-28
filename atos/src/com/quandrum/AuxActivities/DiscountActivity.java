package com.quandrum.AuxActivities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quandrum.Json.Discount;
import com.quandrum.Json.JSONParser;
import com.quandrum.atos.R;

public class DiscountActivity extends Activity{
	int mId;
	ArrayList<Discount> disc;
	LinearLayout mll;
	@Override
	protected void onCreate(Bundle saved){
		super.onCreate(saved);
		setContentView(R.layout.activity_discount);
		mll = (LinearLayout)findViewById(R.id.ll);
		Intent i = getIntent();
		mId = i.getIntExtra("id", 1);
		
		showDiscounts();
	}
	public void showDiscounts(){
		disc = JSONParser.getDiscountsByBusinessId(mId);
		if(disc==null){
			TextView tv = new TextView(this);
			tv.setText("No discounts");
			tv.setTextColor(getResources().getColor(R.color.white));
			mll.addView(tv);
			return;
		}
		for(int i=0;i<disc.size();i++){
			TextView tv = new TextView(this);
			tv.setText(disc.get(i).getDescription());
			tv.setTextColor(getResources().getColor(R.color.white));
			tv.setTextSize(18);
			mll.addView(tv);
		}
	}
}
