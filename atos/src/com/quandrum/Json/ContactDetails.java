package com.quandrum.Json;

public class ContactDetails {
	private int id;
	private int bid;
	private String type;
	private String value;
	
	public ContactDetails(int id, int bid, String type, String value) {
		super();
		this.id = id;
		this.bid = bid;
		this.type = type;
		this.value = value;
	}
	public int getId() {
		return id;
	}
	public int getBid() {
		return bid;
	}
	public String getType() {
		return type;
	}
	public String getValue() {
		return value;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setBid(int bid) {
		this.bid = bid;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setValue(String value) {
		this.value = value;
	}
}