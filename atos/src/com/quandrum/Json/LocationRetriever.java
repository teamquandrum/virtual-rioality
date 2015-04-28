package com.quandrum.Json;

import java.util.ArrayList;
/**
 * Used to return the list of businesses and tourist locations around by parsing JSON
 * @author USER
 *
 */
public class LocationRetriever {
	public static ArrayList<Business> getBusinesses()//TODO
	{		
		//BusinessJSON b = new BusinessJSON(1,"Rajalaxmi Wines",13.036662, 77.593433);
		Business b = new Business(1, "Rajalakshmi Wines", "Bar", "wifi", "", "", 13.036662, 77.593433, "Sumangali Seva Ashrama Road, Hebbal, Bangalore - 32", 4.4, 10);
		ArrayList<Business> arb = new ArrayList<Business>();
		arb.add(b);
		return arb;
	}
	public static ArrayList<TouristLocation> getTouristLocations()//TODO
	{
		//TouristLocation tl = new TouristLocation(1, "The House Opposite Abraham's", 13.035988, 77.593715);
		//TouristLocation tl2 = new TouristLocation(2,"The Cricket Ground",12.934562,77.533990);
		TouristLocation tl = new TouristLocation(1, "The House Opposite Abraham's", "Monument to Abraham", 3, "Behind SS apts, S S A Road, Hebbal, Bangalore - 32",13.035988 , 77.593715);
		ArrayList<TouristLocation> atl = new ArrayList<TouristLocation>();
		atl.add(tl);
		//atl.add(tl2);
		return atl;
	}
	public static ArrayList<AugmentedRealityLocation> getAllLocations()
	{
		ArrayList<Business> businesses = getBusinesses();
		ArrayList<TouristLocation> touristLocations = getTouristLocations();
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
}
