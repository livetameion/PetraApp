package com.yagna.petra.app.Util;

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


	public static void saveTerminalId(SharedPreferences prefs, String result) {
		Editor editor = prefs.edit();
		editor.putString("terminal_id", result);
		editor.commit();

	}

	public static String getTerminalId(SharedPreferences prefs) {
		String result = null;
		try {
			result = prefs.getString("terminal_id", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public static void saveTraningMode(SharedPreferences prefs, String result) {
		Editor editor = prefs.edit();
		editor.putString("training", result);
		editor.commit();

	}

	public static String getTraningMode(SharedPreferences prefs) {
		String result = null;
		try {
			result = prefs.getString("training", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void saveMerchantId(SharedPreferences prefs, String result) {
		Editor editor = prefs.edit();
		editor.putString("m_id", result);
		editor.commit();

	}

	public static String getMerchantId(SharedPreferences prefs) {
		String result = null;
		try {
			result = prefs.getString("m_id", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void saveMerchantP100Id(SharedPreferences prefs, String result) {
		Editor editor = prefs.edit();
		editor.putString("p100_id", result);
		editor.commit();

	}

	public static String getMerchantP100Id(SharedPreferences prefs) {
		String result = null;
		try {
			result = prefs.getString("p100_id", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public static void saveMerchantVatNo(SharedPreferences prefs, String result) {
		Editor editor = prefs.edit();
		editor.putString("vat_no", result);
		editor.commit();

	}

	public static String getMerchantVatNo(SharedPreferences prefs) {
		String result = null;
		try {
			result = prefs.getString("vat_no", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public static void saveMerchantCountry(SharedPreferences prefs, String result) {
		Editor editor = prefs.edit();
		editor.putString("c_code", result);
		editor.commit();

	}

	public static String getMerchantCountry(SharedPreferences prefs) {
		String result = null;
		try {
			result = prefs.getString("c_code", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void saveMerchantCurrency(SharedPreferences prefs, String result) {
		Editor editor = prefs.edit();
		editor.putString("curncy_code", result);
		editor.commit();

	}

	public static String getMerchantCurrency(SharedPreferences prefs) {
		String result = null;
		try {
			result = prefs.getString("curncy_code", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void saveMerchantPaymentMode(SharedPreferences prefs, String result) {
		Editor editor = prefs.edit();
		editor.putString("payment_mode", result);
		editor.commit();

	}

	public static String getMerchantPAymentMode(SharedPreferences prefs) {
		String result = null;
		try {
			result = prefs.getString("payment_mode", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void saveMerchantFname(SharedPreferences prefs, String result) {
		Editor editor = prefs.edit();
		editor.putString("m_f_nam", result);
		editor.commit();

	}

	public static String getMerchantFname(SharedPreferences prefs) {
		String result = null;
		try {
			result = prefs.getString("m_f_nam", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public static void saveMerchantLname(SharedPreferences prefs, String result) {
		Editor editor = prefs.edit();
		editor.putString("m_l_nam", result);
		editor.commit();

	}


	public static String getMerchantLname(SharedPreferences prefs) {
		String result = null;
		try {
			result = prefs.getString("m_l_nam", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public static void saveMerchantCompany(SharedPreferences prefs, String result) {
		Editor editor = prefs.edit();
		editor.putString("m_compny", result);
		editor.commit();
	}


	public static String getMerchantCompany(SharedPreferences prefs) {
		String result = null;
		try {
			result = prefs.getString("m_compny", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public static void saveTaxRates(SharedPreferences prefs, String result) {
		Editor editor = prefs.edit();
		editor.putString("m_taxrate", result);
		editor.commit();
	}


	public static String getTaxRates(SharedPreferences prefs) {
		String result = null;
		try {
			result = prefs.getString("m_taxrate", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public static void savePrefferdId(SharedPreferences prefs, String result) {
		Editor editor = prefs.edit();
		editor.putString("preferred_id", result);
		editor.commit();
	}


	public static String getPrefferdId(SharedPreferences prefs) {
		String result = null;
		try {
			result = prefs.getString("preferred_id", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void setverifyotp(SharedPreferences prefs, String result) {
		Editor editor = prefs.edit();
		editor.putString("otp", result);
		editor.commit();

	}

	public static String getverifyotp(SharedPreferences prefs) {
		String result = null;
		try {
			result = prefs.getString("otp", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public static void setpasscode(SharedPreferences prefs, String result) {
		Editor editor = prefs.edit();
		editor.putString("passcode", result);
		editor.commit();

	}

	public static String getpasscode(SharedPreferences prefs) {
		String result = null;
		try {
			result = prefs.getString("passcode", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}



	// Profile Picture Preference
	public static void savelogo(SharedPreferences prefs, String profilePic) {
		Log.e("logo_url",profilePic);

		Editor editor = prefs.edit();
		editor.putString("logo_url", profilePic);
		editor.commit();
	}

	public static String getlogo(SharedPreferences prefs) {
		String profilePic;
		profilePic = prefs.getString("logo_url", "");
		return profilePic;
	}

	public static void saveAuthToken(SharedPreferences prefs, String profilePic) {
		Editor editor = prefs.edit();
		editor.putString("authToken", profilePic);
		editor.commit();
	}
	public static String getAuthToken(SharedPreferences prefs) {
		String profilePic;
		profilePic = prefs.getString("authToken", "");
		return profilePic;
	}


	public static void saveUser_id(SharedPreferences prefs, String profilePic) {
		Editor editor = prefs.edit();
		editor.putString("user_id", profilePic);
		editor.commit();
	}


	public static String getuser_id(SharedPreferences prefs) {
		String profilePic;
		profilePic = prefs.getString("user_id", "");
		return profilePic;
	}
	public static void saveusername(SharedPreferences prefs, String Useraccount) {
		Editor editor = prefs.edit();
		editor.putString("username", Useraccount);
		editor.commit();
	}

	public static String getusername(SharedPreferences prefs) {
		String profilePic;
		profilePic = prefs.getString("username", null);
		return profilePic;
	}
	public static void saveMeterarray(SharedPreferences prefs, String profilePic) {
		Editor editor = prefs.edit();
		editor.putString("meterarray", profilePic);
		editor.commit();
	}

	public static String getMeterarray(SharedPreferences prefs) {
		String profilePic;
		profilePic = prefs.getString("meterarray", "");
		return profilePic;
	}

	public static void saveskip(SharedPreferences prefs, String profilePic) {
		Editor editor = prefs.edit();
		editor.putString("skip", profilePic);
		editor.commit();
	}

	public static String getskip(SharedPreferences prefs) {
		String profilePic;
		profilePic = prefs.getString("skip", "");
		return profilePic;
	}
	public static void savedate(SharedPreferences prefs, String profilePic) {
		Editor editor = prefs.edit();
		editor.putString("date", profilePic);
		editor.commit();
	}

	public static String getdate(SharedPreferences prefs) {
		String profilePic;
		profilePic = prefs.getString("date", "");
		return profilePic;
	}

	public static void SaveNotificationarray(SharedPreferences prefs, String regId) {
		Editor editor = prefs.edit();
		editor.putString("notification", regId);
		editor.commit();
	}

	public static String GetNotificationarray(SharedPreferences prefs) {
		String strcity = null;
		try {
			strcity = prefs.getString("notification", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strcity;
	}


	public static void saveBuilderToken(SharedPreferences common_mypref, int id) {
		Editor editor = common_mypref.edit();
		editor.putInt("BuilderToken", id);
		editor.commit();
	}

	public static int getBuilderToken(SharedPreferences prefs) {
		int profilePic;
		profilePic = prefs.getInt("BuilderToken", 0);
		return profilePic;

	}


	public static void saveUseremail(SharedPreferences prefs, String profilePic) {
		Log.e("savingemail",""+profilePic);
		Editor editor = prefs.edit();
		editor.putString("email", profilePic);
		editor.commit();
	}


	public static String getUseremail(SharedPreferences prefs) {
		String profilePic;
		profilePic = prefs.getString("email", "");
		return profilePic;
	}

	public static void saveZipcode(SharedPreferences prefs, String profilePic) {
		Editor editor = prefs.edit();
		editor.putString("zip_code", profilePic);
		editor.commit();
	}


	public static String getZipcode(SharedPreferences prefs) {
		String profilePic;
		profilePic = prefs.getString("zip_code", "");
		return profilePic;
	}

	public static void saveLanguage(SharedPreferences prefs, String merchant_language) {
		Editor editor = prefs.edit();
		editor.putString("m_language", merchant_language);
		editor.commit();
	}

	public static String getLanguage(SharedPreferences prefs) {
		String profilePic;
		profilePic = prefs.getString("m_language", "");
		return profilePic;
	}

	public static void saveLayout(SharedPreferences prefs, String merchant_language) {
		Editor editor = prefs.edit();
		editor.putString("layout", merchant_language);
		editor.commit();
	}

	public static String getLayout(SharedPreferences prefs) {
		String profilePic;
		profilePic = prefs.getString("layout", "");
		return profilePic;
	}
	public static void saveTourguide(SharedPreferences prefs, String merchant_language) {
		Editor editor = prefs.edit();
		editor.putString("tour_guide", merchant_language);
		editor.commit();
	}

	public static String getTourguide(SharedPreferences prefs) {
		String profilePic;
		profilePic = prefs.getString("tour_guide", "");
		return profilePic;
	}

	public static void saveCategories(SharedPreferences prefs, String merchant_language) {
		Editor editor = prefs.edit();
		editor.putString("category", merchant_language);
		editor.commit();
	}

	public static String getCategories(SharedPreferences prefs) {
		String profilePic;
		profilePic = prefs.getString("category", "");
		return profilePic;
	}

	public static void saveHasCustomInvoice(SharedPreferences prefs, String merchant_language) {
		Editor editor = prefs.edit();
		editor.putString("is_cust_invoice", merchant_language);
		editor.commit();
	}

	public static String getHasCustomInvoice(SharedPreferences prefs) {
		String profilePic;
		profilePic = prefs.getString("is_cust_invoice", "");
		return profilePic;
	}

	public static void saveHasCustomVoucher(SharedPreferences prefs, String merchant_language) {
		Editor editor = prefs.edit();
		editor.putString("is_cust_voucher", merchant_language);
		editor.commit();
	}

	public static String getHasCustomVoucherdate(SharedPreferences prefs) {
		String profilePic;
		profilePic = prefs.getString("is_cust_voucher", "");
		return profilePic;
	}


	public static void saveExclVat(SharedPreferences prefs, String param) {
		Editor editor = prefs.edit();
		editor.putString("excl_vat", param);
		editor.commit();
	}

	public static String getExclVat(SharedPreferences prefs) {
		String profilePic;
		profilePic = prefs.getString("excl_vat", "");
		return profilePic;
	}


	public static void saveVPDiscount(SharedPreferences common_mypref, String s) {
		Editor editor = common_mypref.edit();
		editor.putString("vp_discount", s);
		editor.commit();
	}
	public static String getVPDiscount(SharedPreferences prefs) {
		String profilePic;
		profilePic = prefs.getString("vp_discount", "10");
		return profilePic;
	}

	public static void saveCharityPlace(SharedPreferences common_mypref, String s) {
		Editor editor = common_mypref.edit();
		editor.putString("charity_place", s);
		editor.commit();
	}
	public static String getCharityPlace(SharedPreferences common_mypref) {
		String profilePic;
		profilePic = common_mypref.getString("charity_place", "");
		return profilePic;

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
