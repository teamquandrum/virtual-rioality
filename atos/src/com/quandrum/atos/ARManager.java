package com.quandrum.atos;

import java.util.ArrayList;
import java.util.List;

import com.quandrum.Json.AugmentedRealityLocation;

/**
 * Used to maintain list of locations close to user as specified in constructor arguments.
 * One AugmentedRealityActivity should have one ARManager instance
 * All distances in km
 * @author USER
 *
 */
public class ARManager {
	/* mOld.. are the locations used for the last mCloseLocs calculations. 
	 * Once distance(mOld, mCurrent) > mRecalcDistance, mOld is changed to mCurrent and 
	 * closeLocs are calculated again.
	 */
	double mOldLat, mOldLon, mCurrentLat, mCurrentLon, mMaxRadius,mRecalcDistance;
	List<AugmentedRealityLocation> mAllLocs;
	ArrayList<AugmentedRealityLocation> mCloseLocs;
	
	/**
	 * 
	 * @param oldLat
	 * @param oldLon
	 * @param currentLat
	 * @param currentLon
	 * @param maxRadius
	 * @param recalcDistance
	 * @param allLocs
	 */
	public ARManager(double oldLat,double oldLon,double maxRadius,double recalcDistance, List<AugmentedRealityLocation> allLocs)
	{
		mOldLat = oldLat;
		mOldLon = oldLon;
		mMaxRadius = maxRadius;
		mRecalcDistance = recalcDistance;
		mAllLocs=allLocs;
		mCloseLocs = calculateCloseLocations(oldLat, oldLon, maxRadius, allLocs);
	}
	public void setCurrentLocation(double lat, double lon)
	{
		this.mCurrentLat = lat;
		this.mCurrentLon = lon;
		double dist = LatLonCalculator.getDistance(mOldLat, mOldLon, mCurrentLat, mCurrentLon);
		if(dist>mRecalcDistance)
		{
			mOldLat=mCurrentLat;
			mOldLon=mCurrentLon;
			mCloseLocs = calculateCloseLocations(mOldLat,mOldLon,mMaxRadius,mAllLocs);
		}
	}
	/**
	 * taxing function. Goes through entire list of businesses, shortlisting closest ones.
	 * @param lat
	 * @param lon
	 * @param maxRadius
	 * @param allLocs
	 * @return
	 */
	protected ArrayList<AugmentedRealityLocation> calculateCloseLocations(double lat, double lon, double maxRadius, List<AugmentedRealityLocation> allLocs)
	{
		ArrayList<AugmentedRealityLocation> closeLoc = new ArrayList<AugmentedRealityLocation>();
		for(int i=0;i<allLocs.size();i++)
		{
			AugmentedRealityLocation tloc = allLocs.get(i);
			double dist = LatLonCalculator.getDistance(lat, lon, tloc.getLatitude(), tloc.getLongitude());
			if(dist<maxRadius)
			{
				closeLoc.add(tloc);
			}
		}
		return closeLoc;
	}
	public ArrayList<AugmentedRealityLocation> getCloseLocations()
	{
		return mCloseLocs;
	}
	public double getCurrentLatitude()
	{
		return mCurrentLat;
	}
	public double getCurrentLongitude()
	{
		return mCurrentLon;
	}
}
