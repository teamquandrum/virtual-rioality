package com.quandrum.Json;


public class TouristLocation extends AugmentedRealityLocation{
	private int id;
	private String name;
	private String description;
	private int importance;
	private String address;
	public TouristLocation(int id, String name,	String description, int importance, String address, double lat,
			double lon) {
		super(lat,lon);
		super.type=AugmentedRealityLocation.TYPE_TOURIST_LOCATION;
		this.id = id;
		this.name = name;
		this.description = description;
		this.importance = importance;
		this.address = address;
		this.lat = lat;
		this.lon = lon;
	}
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	public int getImportance() {
		return importance;
	}
	public String getAddress() {
		return address;
	}
	public double getLat() {
		return lat;
	}
	public double getLon() {
		return lon;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setImportance(int importance) {
		this.importance = importance;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	private double lat;
	private double lon;
}
