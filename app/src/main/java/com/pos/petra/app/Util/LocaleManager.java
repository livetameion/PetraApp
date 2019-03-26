package com.pos.petra.app.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import java.util.Locale;

public class LocaleManager {

    public static Context setLocale(Context c) {
       return setNewLocale(c, getLanguage(c));
    }

    public static Context setNewLocale(Context c, String language) {
      //  persistLanguage(c, language);
        return updateResources(c, language);
    }

    public static String getLanguage(Context c) {
        SharedPreferences common_mypref = c.getSharedPreferences(
                "user", 0);
        return PrefUtil.getLanguage(common_mypref);
    }

    private static void persistLanguage(Context c, String language) {  }

    private static Context updateResources(Context context, String language) {


        Locale locale = new Locale(language.toLowerCase());
        Locale.setDefault(locale);
        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        if (Build.VERSION.SDK_INT >= 17) {
            config.setLocale(locale);
            context = context.createConfigurationContext(config);
        } else {
            config.locale = locale;
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
        return context;
    }
}
