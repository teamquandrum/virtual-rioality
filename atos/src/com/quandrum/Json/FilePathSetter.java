package com.quandrum.Json;

import java.io.File;
import java.util.UUID;

import android.content.Context;
import android.net.Uri;

/**
 * sets paths for JSONParser class. Also used to retrieve paths for individual storage of businesses, touristlocations etc.
 * @author USER
 *
 */
public class FilePathSetter {
	public static void setFilePaths(Context context){
		JSONParser.businessFilePath = getBusinessFilePath(context);
		JSONParser.contactDetailsFilePath = getContactDetailsFilePath(context);
		JSONParser.discountsFilePath = getDiscountsFilePath(context);
		JSONParser.touristLocationFilePath = getTouristLocationFilePath(context);
	}
	public static String getBusinessFilePath(Context context){
		File dir = context.getDir("JSON", 0);
		return new File(dir.getAbsolutePath()+File.separator+"business.json").getAbsolutePath();
	}
	public static String getContactDetailsFilePath(Context context){
		File dir = context.getDir("JSON", 0);
		return new File(dir.getAbsolutePath()+File.separator+"contactdetails.json").getAbsolutePath();
	}
	public static String getDiscountsFilePath(Context context){
		File dir = context.getDir("JSON", 0);
		return new File(dir.getAbsolutePath()+File.separator+"discounts.json").getAbsolutePath();
	}
	public static String getTouristLocationFilePath(Context context){
		File dir = context.getDir("JSON", 0);
		return new File(dir.getAbsolutePath()+File.separator+"touristlocation.json").getAbsolutePath();
	}
	public static String getDoneCheckFilePath(Context context){
		File dir = context.getDir("JSON", 0);
		return new File(dir.getAbsolutePath()+File.separator+"done.json").getAbsolutePath();
	}
	public static Uri getNextImagePath(Context c){
		String uuid = UUID.randomUUID().toString();
		File dir = c.getDir("images",0);
		File f = new File(dir.getAbsolutePath()+File.separator+uuid.substring(0, 10)+".jpg");
		return Uri.fromFile(f);
	}
	public static File getImageDirectory(Context c){
		String uuid = UUID.randomUUID().toString();
		File dir = c.getDir("images",0);
		File f = new File(dir.getAbsolutePath()+File.separator+uuid.substring(0, 10)+".jpg");
		f=f.getParentFile();
		return f;
	}
}
