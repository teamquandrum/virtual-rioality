package com.example2.schedulerui;

import com.google.android.gms.maps.model.LatLng;


public class FixedLocation {
	LatLng loc;	
	int start_hour,start_minute,end_hour,end_minute;
	String name;
	
	public FixedLocation(LatLng l,int sh, int sm, int eh, int em, String n){
		loc=l;
		start_hour=sh;
		start_minute=sm;
		end_hour=eh;
		end_minute=em;
		name=n;
	}

}
