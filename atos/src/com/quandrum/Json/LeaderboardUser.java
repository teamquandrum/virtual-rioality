package com.quandrum.Json;

public class LeaderboardUser {
	String name = "";
	int id;
	String iemi;
	int points;
	public LeaderboardUser(String name, int id, String iemi, int points) {
		super();
		this.name = name;
		this.id = id;
		this.iemi = iemi;
		this.points = points;
	}
	public String getName() {
		return name;
	}
	public int getId() {
		return id;
	}
	public String getIemi() {
		return iemi;
	}
	public int getPoints() {
		return points;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setIemi(String iemi) {
		this.iemi = iemi;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	
}
