package com.pos.petra.app.Util;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class PrefUtil {
	public static void saveLoginData(SharedPreferences prefs, String result) {
		Editor editor = prefs.edit();
		editor.putString("log_in", result);
		editor.commit();

	}

	public static String getLoginData(SharedPreferences prefs) {
		String result = null;
		try {
			result = prefs.getString("log_in", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}


	public static String getLanguage(SharedPreferences prefs) {
		String profilePic;
		profilePic = prefs.getString("m_language", "");
		return profilePic;
	}




	public static void saveVPDiscount(SharedPreferences common_mypref, String s) {
		Editor editor = common_mypref.edit();
		editor.putString("vp_discount", s);
		editor.commit();
	}


	public static void saveCharityPlace(SharedPreferences common_mypref, String s) {
		Editor editor = common_mypref.edit();
		editor.putString("charity_place", s);
		editor.commit();
	}


	public static void saveSearch(SharedPreferences prefs, String search) {
		Editor editor = prefs.edit();
		editor.putString("search", search);
		editor.commit();
	}

	public static String getSearch(SharedPreferences prefs) {
		String profilePic;
		profilePic = prefs.getString("search", "");
		return profilePic;
	}



}
