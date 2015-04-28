package com.quandrum.atos;

public class LatLonCalculator {
	
	/**
	 * returns the distance between two latitudes and longitudes in km
	 * @param lat1 latitude of first coordinate
	 * @param lon1 longitude of first coordinate
	 * @param lat2 latitude of first coordinate
	 * @param lon2 longitude of second coordinate
	 * @return distance between supplied lat long
	 */
	public static double getDistance(double lat1, double lon1, double lat2, double lon2)
	{
		double theta = lon1 - lon2;
		double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
		dist = Math.acos(dist);
		dist = Math.toDegrees(dist);
		dist = dist*60*1.1515*1.609344;
		return Math.abs(dist*1000);
	}
	/**
	 * returns the angle with north in degrees between two lat-long pairs, assuming travel from first pair to second pair
	 * @param lat1
	 * @param lon1
	 * @param lat2
	 * @param lon2
	 * @return angle with north in degrees (-180 to +180)
	 */
	public static double getBearing(double lat1, double lon1, double lat2, double lon2)
	{
		double longitude1 = lon1;
		double longitude2 = lon2;
		
		double latitude1 = Math.toRadians(lat1);
		double latitude2 = Math.toRadians(lat2);
		double longDiff= Math.toRadians(longitude2-longitude1);
		double y= Math.sin(longDiff)*Math.cos(latitude2);
		double x=Math.cos(latitude1)*Math.sin(latitude2)-Math.sin(latitude1)*Math.cos(latitude2)*Math.cos(longDiff);

		return (Math.toDegrees(Math.atan2(y, x)));
	}
}
