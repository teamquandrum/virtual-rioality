package com.quandrum.Json;

import com.quandrum.atos.AugmentedLayout;
import com.quandrum.atos.AugmentedRealityActivity;

public class Business extends AugmentedRealityLocation{
	private int id;
	private String name;
	private String type;
	private String sdesc;
	private String brochure;
	private String pricelist;
	private double lat;
	private double lon;
	private String address;
	private double rating;
	private int prated;
	public Business(int id, String name, String type, String sdesc,
			String brochure, String pricelist, double lat, double lon,
			String address, double rating, int prated) {
		super(lat,lon);
		super.type=AugmentedRealityLocation.TYPE_BUSINESS;
		this.id = id;
		this.name = name;
		this.type = type;
		this.sdesc = sdesc;
		this.brochure = brochure;
		this.pricelist = pricelist;
		this.lat = lat;
		this.lon = lon;
		this.address = address;
		this.rating = rating;
		this.prated = prated;
	}
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getType() {
		return type;
	}
	public String getSdesc() {
		return sdesc;
	}
	public String getBrochure() {
		return brochure;
	}
	public String getPricelist() {
		return pricelist;
	}
	public double getLat() {
		return lat;
	}
	public double getLon() {
		return lon;
	}
	public String getAddress() {
		return address;
	}
	public double getRating() {
		return rating;
	}
	public int getPrated() {
		return prated;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setSdesc(String sdesc) {
		this.sdesc = sdesc;
	}
	public void setBrochure(String brochure) {
		this.brochure = brochure;
	}
	public void setPricelist(String pricelist) {
		this.pricelist = pricelist;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setRating(double rating) {
		this.rating = rating;
	}
	public void setPrated(int prated) {
		this.prated = prated;
	}
	

}
