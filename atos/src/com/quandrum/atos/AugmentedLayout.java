package com.quandrum.atos;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quandrum.Json.AugmentedRealityLocation;
import com.quandrum.atos.R;

public class AugmentedLayout extends LinearLayout{

	int mId;
	String mName;
	//int mType;
	Context mContext;
	TextView tvName;
	ArrayList<View> alTv;
	AugmentedRealityLocation mARLoc;
	/**
	 * name and distance are always required. args[] is array of strings to be displayed one below the other
	 * @param context
	 * @param name
	 * @param distance in m
	 * @param args
	 */
	//TODO consider adding an ellipses based View parameter to fill in extra views supplied
	public AugmentedLayout(Context context, int id, String name, double distance, String args[], View v[], AugmentedRealityLocation loc) {
		super(context);
		mId= id;
		mARLoc = loc;
		mContext = context;
		this.setOrientation(LinearLayout.VERTICAL);
		tvName = new TextView(context);
		this.addView(tvName);
		mName = name;
		alTv = new ArrayList<View>();
		setName(name,tvName);
		addDistance(distance);
		if(args!=null)
		{
			for(int i = 0;i<args.length;i++)
			{
				addNewTextView(args[i]);
			}
		}
		if(v!=null)
		{
			for(int i=0;i<v.length;i++){
				this.addView(v[i]);
			}
		}
		this.setBackgroundResource(R.color.BlackTranslucent);
	}
	public void setName(String name, TextView tv)
	{
		if(mContext==null)
		{
			System.out.println("Context NULL");
		}
		tv.setTextColor(mContext.getResources().getColor(R.color.white));
		tv.setTextSize(22);
		tv.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		tv.setText(mName);
		tv.setGravity(Gravity.CENTER_HORIZONTAL);
	}
	public void addDistance(double distance)
	{
		TextView tv = new TextView(mContext);
		alTv.add(tv);
		tv.setTextColor(getResources().getColor(R.color.white));
		tv.setText(distance+"m");
		this.addView(tv);
	}
	public void addNewTextView(String text)
	{
		TextView tv = new TextView(mContext);
		alTv.add(tv);
		tv.setTextColor(getResources().getColor(R.color.white));
		tv.setText(""+text);
		this.addView(tv);
	}
	public void addNormalView(View v){
		alTv.add(v);
		this.addView(v);
	}
	public void reset(int id, String name, double distance, String args[], View v[], AugmentedRealityLocation loc){
		removeOtherViews();
		mId= id;
		mName = name;
		mARLoc=loc;
		setName(name,tvName);
		addDistance(distance);
		if(args!=null)
		{
			for(int i = 0;i<args.length;i++)
			{
				addNewTextView(args[i]);
			}
		}
		if(v!=null)
		{
			for(int i=0;i<v.length;i++){
				addNormalView(v[i]);
			}
		}
	}
	public void removeOtherViews(){
		for(int i=alTv.size()-1;i>=0;i--){
			this.removeView(alTv.get(i));
			alTv.remove(i);
		}
		alTv = new ArrayList<View>();
	}
	public int getId(){
		return mId;
	}
	public String getName(){
		return mName;
	}
	public int getAugmentedRealityLocationType(){
		return mARLoc.type;
	}
}
