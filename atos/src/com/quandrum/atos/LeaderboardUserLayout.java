package com.quandrum.atos;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LeaderboardUserLayout extends RelativeLayout{
	String mName;
	String mPoints;
	Context mContext;
	public TextView nameTv;
	public TextView pointsTv;
	
	public LeaderboardUserLayout(Context c, String name, String points){
		super(c);
		mContext=c;
		mName=name;
		mPoints=points;
		initialiseTextViews();
		setName();
		setPoints();
	}
	public void setName(){
		nameTv.setText(mName);
		nameTv.setTextColor(getResources().getColor(R.color.white));
		nameTv.setTextSize(18);
		nameTv.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
	}
	public void setPoints(){
		pointsTv.setText(mPoints);
		pointsTv.setTextColor(getResources().getColor(R.color.white));
		pointsTv.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
		pointsTv.setTextSize(18);
	}
	public void initialiseTextViews(){
		nameTv = new TextView(mContext);
		nameTv.setGravity(Gravity.CENTER_HORIZONTAL);
		nameTv.setId(1);
		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		rlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rlp.addRule(RelativeLayout.LEFT_OF,2);
		this.addView(nameTv,rlp);
		pointsTv = new TextView(mContext);
		RelativeLayout.LayoutParams rlp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		rlp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		this.addView(pointsTv,rlp2);
		pointsTv.setId(2);
	}
}
