package com.quandrum.Json;

public abstract class AugmentedRealityLocation {
	public final static int TYPE_BUSINESS=1;
	public final static int TYPE_TOURIST_LOCATION=2;
	public double lon;
	public double lat;
	public int type;
	public AugmentedRealityLocation(double lat, double lon)
	{
		this.lat=lat;
		this.lon=lon;
	}
	public double getLatitude()
	{
		return lat;
	}
	public double getLongitude()
	{
		return lon;
	}
	public void setLatitude(double lat)
	{
		lat=lat;
	}
	public void setLongitude(double lon)
	{
		lon=lon;
	}
}
