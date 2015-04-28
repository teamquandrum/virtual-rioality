package com.quandrum.Json;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONParser {
	static String businessFilePath="";
	static String touristLocationFilePath ="";
	static String contactDetailsFilePath="";
	static String discountsFilePath="";
	public static ArrayList<Business> getAllBusinesses()
	{
			try {
				Scanner sc = new Scanner(new File(businessFilePath));
				String json="";
				while(sc.hasNext()){
					json+=sc.nextLine();
				}
				JSONObject obj = new JSONObject(json);
				JSONArray result = obj.getJSONArray("result");
				ArrayList<Business> businessList = new ArrayList<Business>();
				for(int i=0;i<result.length();i++){
					Business b = getBusinessFromJSON(result.getJSONObject(i));
					businessList.add(b);
				}
				sc.close();
				return businessList;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
	}
	public static Business getBusinessFromJSON(JSONObject obj){
		try{	
			
			int id = obj.getInt("id");
			String type = obj.getString("type");
			String brochure = obj.getString("brochure");
			String pricelist = obj.getString("pricelist");
			String sdesc = obj.getString("sdesc");
			String address = obj.getString("address");
			String name = obj.getString("name");
			double lat = obj.getDouble("latitude");
			double lon = obj.getDouble("longitude");
			double rating = obj.getDouble("rating");
			int prated = obj.getInt("prated");
			Business b = new Business(id, name, type, sdesc, brochure, pricelist, lat, lon, address, rating, prated);
			return b;
		}
		catch(JSONException e){
			e.printStackTrace();
			return null;
		}
	}
	public static ArrayList<TouristLocation> getAllTouristLocations()
	{
			try {
				Scanner sc = new Scanner(new File(touristLocationFilePath));
				String json="";
				while(sc.hasNext()){
					json+=sc.nextLine();
				}
				JSONObject obj = new JSONObject(json);
				JSONArray result = obj.getJSONArray("result");
				ArrayList<TouristLocation> touristLocationList = new ArrayList<TouristLocation>();
				for(int i=0;i<result.length();i++){
					TouristLocation tl = getTouristLocationFromJSON(result.getJSONObject(i));
					touristLocationList.add(tl);
				}
				sc.close();
				return touristLocationList;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
	}
	public static TouristLocation getTouristLocationFromJSON(JSONObject obj){
		try{	
			Log.e("getTouristLocationFromJSON",obj.toString());
			
			int id = obj.getInt("id");
			String description = obj.getString("description");
			String address = obj.getString("address");
			String name = obj.getString("name");
			int importance = obj.getInt("importance");
			double lat = obj.getDouble("latitude");
			double lon = obj.getDouble("longitude");
			TouristLocation tl = new TouristLocation(id, name, description, importance, address, lat, lon);
			return tl;
		}
		catch(JSONException e){
			e.printStackTrace();
			return null;
		}
	}
	public static ArrayList<AugmentedRealityLocation> getAllLocations(){
		ArrayList<Business> businesses = getAllBusinesses();
		ArrayList<TouristLocation> touristLocations = getAllTouristLocations();
		ArrayList<AugmentedRealityLocation> ac = new ArrayList<AugmentedRealityLocation>(businesses.size()+touristLocations.size());
		for(int i = 0;i<businesses.size();i++)
		{
			ac.add((AugmentedRealityLocation)businesses.get(i));
		}
		for(int i=0;i<touristLocations.size();i++)
		{
			ac.add((AugmentedRealityLocation)touristLocations.get(i));
		}
		return ac;
	}
	public static ArrayList<Discount> getAllDiscounts(){
		try{
			//String path=discountsFilePath;
			Scanner sc = new Scanner(new File(discountsFilePath));
			String json = "";
			while(sc.hasNext()){
				json+=sc.nextLine();
			}
			JSONObject obj = new JSONObject(json);
			JSONArray result = obj.getJSONArray("result");
			ArrayList<Discount> dl = new ArrayList<Discount>();
			for(int i=0;i<result.length();i++){
				Discount d = getDiscountFromJSON(result.getJSONObject(i));
				dl.add(d);
			}
			sc.close();
			return dl;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	public static Discount getDiscountFromJSON(JSONObject obj){
		try{
			int id = obj.getInt("id");
			int bid = obj.getInt("businessid");
			String description = obj.getString("description");
			Discount d = new Discount(id, bid, description);
			return d;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	public static ArrayList<ContactDetails> getContactDetails(){
		try{
			//String path = contactDetailsFilePath;
			Scanner sc = new Scanner(new File(contactDetailsFilePath));
			String json = "";
			while(sc.hasNext()){
				json+=sc.nextLine();
			}
			JSONObject obj = new JSONObject(json);
			JSONArray result = obj.getJSONArray("result");
			ArrayList<ContactDetails> cdl = new ArrayList<ContactDetails>();
			for(int i=0;i<result.length();i++){
				ContactDetails cd = getContactDetailsFromJSON(result.getJSONObject(i));
				cdl.add(cd);
			}
			sc.close();
			return cdl;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
	}
	public static ContactDetails getContactDetailsFromJSON(JSONObject obj){
		try{
			int id = obj.getInt("id");
			int bid = obj.getInt("businessid");
			String type = obj.getString("type");
			String value = obj.getString("value");
			ContactDetails d = new ContactDetails(id, bid, type, value);
			return d;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * get business based on id
	 * @param id id of business
	 * @return Business object if found, null otherwise
	 */
	public static Business getBusinessById(int id){
		ArrayList<Business> bl = getAllBusinesses();
		for(int i=0;i<bl.size();i++){
			Business b = bl.get(i);
			if(b.getId()==id){
				return b;
			}
		}
		return null;
	}
	public static TouristLocation getTouristLocationById(int id){
		ArrayList<TouristLocation> tll = getAllTouristLocations();
		for(int i=0;i<tll.size();i++){
			TouristLocation tl = tll.get(i);
			if(tl.getId()==id){
				return tl;
			}
		}
		return null;
	}
	public static ArrayList<ContactDetails> getContactDetailsByBusinessId(int bid){
		ArrayList<ContactDetails> cdl = getContactDetails();
		ArrayList<ContactDetails> chosenCdl = new ArrayList<ContactDetails>();
		for(int i=0;i<cdl.size();i++){
			if(cdl.get(i).getBid()==bid){
				chosenCdl.add(cdl.get(i));
			}
		}
		return chosenCdl;
	}
	public static ArrayList<Discount> getDiscountsByBusinessId(int bid){
		ArrayList<Discount> dl = getAllDiscounts();
		ArrayList<Discount> chosenDl = new ArrayList<Discount>();
		for(int i=0;i<dl.size();i++){
			if(dl.get(i).getBid()==bid){
				chosenDl.add(dl.get(i));
			}
		}
		return chosenDl;
	}
}
