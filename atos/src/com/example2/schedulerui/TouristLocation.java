package com.example2.schedulerui;

import com.google.android.gms.maps.model.LatLng;

public class TouristLocation {
	LatLng loc;
	String name;
	
	public TouristLocation(LatLng l, String n){
		loc=l;
		name=n;
	}

}
