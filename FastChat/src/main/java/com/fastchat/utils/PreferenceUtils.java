package com.fastchat.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.fastchat.Model.User;
 import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;



public class PreferenceUtils {

	private static SharedPreferences sp;
	private static String DEFAULT_SHARED="zdjc";
	public static SharedPreferences getPreferences(Context context) {
		if (sp == null) {
			sp = context.getSharedPreferences(DEFAULT_SHARED, Context.MODE_PRIVATE);
		}
		return sp;
	}

	public static boolean getBoolean(Context context, String key) {
		return getBoolean(context, key, false);
	}

	public static boolean getBoolean(Context context, String key, boolean defValue) {
		SharedPreferences sp = getPreferences(context);
		return sp.getBoolean(key, defValue);
	}

	public static void putBoolean(Context context, String key, boolean value) {
		SharedPreferences sp = getPreferences(context);
		Editor edit = sp.edit();
		edit.putBoolean(key, value);
		edit.commit();
	}


	public static String getString(Context context, String key) {
		return getString(context, key, null);
	}


	public static String getString(Context context, String key, String defValue) {
		SharedPreferences sp = getPreferences(context);
		return sp.getString(key, defValue);
	}


	public static void putString(Context context, String key, String value) {
		SharedPreferences sp = getPreferences(context);
		Editor edit = sp.edit();
		edit.putString(key, value);
		edit.commit();
	}


	public static int getInt(Context context, String key) {
		return getInt(context, key, -1);
	}


	public static int getInt(Context context, String key, int defValue) {
		SharedPreferences sp = getPreferences(context);
		return sp.getInt(key, defValue);
	}


	public static void putInt(Context context, String key, int value) {
		SharedPreferences sp = getPreferences(context);
		Editor edit = sp.edit();
		edit.putInt(key, value);
		edit.commit();
	}


	public static void putLong(Context context, String key, long value) {
		SharedPreferences sp = getPreferences(context);
		Editor edit = sp.edit();
		edit.putLong(key, value);
		edit.commit();
	}


	public static long getLong(Context context, String key, long defValue) {
		SharedPreferences sp = getPreferences(context);
		return sp.getLong(key, defValue);
	}

	public static void clear(Context context){
		SharedPreferences preferences = getPreferences(context);
		preferences.edit().clear().apply();
	}


	private static final String USERID = "userId";
	public static void setUserId(int value){
		if(null != sp){
			sp.edit().putInt(USERID, value).commit();
		}
	}

	public static int getuserId(){
		if(null != sp){
			return sp.getInt(USERID,0);
		}
		return -1000;
	}


	/**
	 * keys
	 */
	private static final String KEYS = "keys";
	public static void setKeys(String value){
		if(null != sp){
			sp.edit().putString(KEYS, value).commit();
		}
	}

	public static String getKeys(){
		if(null != sp){
			return sp.getString(KEYS,"");
		}
		return "";
	}


	/**
	 * loginFor
	 */
	private static final String LOGINFOR = "loginFor";
	public static void setLoginFor(String value){
		if(null != sp){
			sp.edit().putString(LOGINFOR, value).commit();
		}
	}

	public static String getLoginFor(){
		if(null != sp){
			return sp.getString(LOGINFOR, "");
		}
		return "";
	}

	/**
	 * isLogin
	 */
	private static final String ISLOGIN = "isLogin";
	public static void setIsLogin(boolean value){
		if(null != sp){
			sp.edit().putBoolean(ISLOGIN, value).commit();
		}
	}

	public static boolean getIsLogin(){
		if(null != sp){
			return sp.getBoolean(ISLOGIN, false);
		}
		return false;
	}

	/**
	 * isFBLogout
	 */
	private static final String ISFBLOGOUT = "isFBLogout";
	public static void setIsFBLogout(boolean value){
		if(null != sp){
			sp.edit().putBoolean(ISFBLOGOUT, value).commit();
		}
	}

	public static boolean getIsFBLogout(){
		if(null != sp){
			return sp.getBoolean(ISFBLOGOUT, true);
		}
		return true;
	}

	/**
	 * isSettingHeadImage
	 */
	private static final String ISSETTINGHEADIMAGE = "isSettingHeadImage";
	public static void setIsSettingHeadImage(boolean value){
		if(null != sp){
			sp.edit().putBoolean(ISSETTINGHEADIMAGE, value).commit();
		}
	}

	public static boolean getIsSettingHeadImage(){
		if(null != sp){
			return sp.getBoolean(ISSETTINGHEADIMAGE, true);
		}
		return true;
	}

	// save data in sharedPrefences
	public static void setSharedOBJECT(Context context, String key,
									   Object value) {
		clearDataCall(context);
		SharedPreferences sharedPreferences =  context.getSharedPreferences(
				"M", Context.MODE_PRIVATE);
		Editor prefsEditor = sharedPreferences.edit();
		Gson gson = new Gson();
		String json = gson.toJson(value);
		prefsEditor.putString(key, json);
		prefsEditor.apply();
	}

	// get data from sharedPrefences
	public static User getSharedOBJECT(Context context, String key) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"M", Context.MODE_PRIVATE);
 		String json = sharedPreferences.getString(key, "");
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new StringReader(json));
		reader.setLenient(true);
		return gson.fromJson(reader, User.class);
	}

	public static void clearDataCall(Context context){
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"M", Context.MODE_PRIVATE);
		sharedPreferences.edit().clear().apply();
	}

}