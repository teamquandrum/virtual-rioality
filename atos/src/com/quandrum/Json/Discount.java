package com.quandrum.Json;

public class Discount {
	private int id;
	private int bid;
	private String description;
	public Discount(int id, int bid, String description) {
		this.id = id;
		this.bid = bid;
		this.description = description;
	}
	public int getId() {
		return id;
	}
	public int getBid() {
		return bid;
	}
	public String getDescription() {
		return description;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setBid(int bid) {
		this.bid = bid;
	}
	public void setDescription(String description) {
		this.description = description;
	}	
}
