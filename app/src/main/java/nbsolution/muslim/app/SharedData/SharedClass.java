package nbsolution.muslim.app.SharedData;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedClass {

    public static String[] methods = {
            "Muslim World League",
            "Islamic Society of North America",
            "Egyptian General Authority of Survey",
            "Umm Al-Qura University, Makkah",
            "University of Islamic Sciences, Karachi",
            "Institute of Geophysics, University of Tehran",
            "Shia Ithna-Ashari, Leva Institute, Qum",
            "Gulf Region",
            "Kuwait",
            "Qatar",
            "Majlis Ugama Islam Singapura, Singapore",
            "Union Organization islamic de France",
            "Diyanet İşleri Başkanlığı, Turkey",
            "Spiritual Administration of Muslims of Russia"
    };

    public static double lat = 0.0;
    public static double lng = 0.0;
    static SharedPreferences preferences;

    public static int getLocationFlag(Context context){
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt("islocation", 0);
    }

    public static int getMethod(Context context){
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt("method", 2);
    }

    public static int getCounter(Context context){
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt("counter", 0);
    }

    public static int getTasbeenSound(Context context){
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt("tasbeensound", 1);
    }

    public static int getAlarmStatus(Context context, int pos){
        int value = 0;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        switch (pos){
            case 1:
                value = preferences.getInt("fajralarm", 1);
                break;
            case 2:
                value = preferences.getInt("zohralarm", 1);
                break;
            case 3:
                value = preferences.getInt("asaralarm", 1);
                break;
            case 4:
                value = preferences.getInt("maghribalarm", 1);
                break;
            case 5:
                value = preferences.getInt("ishaalarm", 1);
                break;
        }
        return value;
    }

    public static void setAlarmStatus(Context context, int pos, int val){
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        switch (pos){
            case 1:
                editor.putInt("fajralarm", val);
                editor.apply();
                editor.commit();
                break;
            case 2:
                editor.putInt("zohralarm", val);
                editor.apply();
                editor.commit();
                break;
            case 3:
                editor.putInt("asaralarm", val);
                editor.apply();
                editor.commit();
                break;
            case 4:
                editor.putInt("maghribalarm", val);
                editor.apply();
                editor.commit();
                break;
            case 5:
                editor.putInt("ishaalarm", val);
                editor.apply();
                editor.commit();
                break;
        }
    }
    public static void setDialogPermission(Context context,Boolean permission){
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isDialogPermission",permission);
        editor.apply();
        editor.commit();
    }
    public static Boolean getDialogPermission(Context context, String query) {
        boolean value = false;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (query.equals("isDialogPermission")) {
            value = preferences.getBoolean("isDialogPermission", false);
        }
        return value;
    }
    public static void setPermission(Context context,Boolean permission){
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isPermissionGranted",permission);
        editor.apply();
        editor.commit();
    }
    public static Boolean getPermission(Context context, String query) {
        boolean value = false;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (query.equals("isPermissionGranted")) {
            value = preferences.getBoolean("isPermissionGranted", false);
        }
        return value;
    }
    public static void setManualLocation(Context context, String city, String country){
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("country", country);
        editor.putString("city", city);
        editor.apply();
        editor.commit();
    }
    public static String getManualLocationDetails(Context context, String query) {
        String value = "null";
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (query.equals("country")) {
            value = preferences.getString("country", "");
        } else if (query.equals("city")) {
            value = preferences.getString("city", "");
        }
        return value;
    }
    public static void setManualLatLng(Context context, String lat, String lng){
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("lat", lat);
        editor.putString("lng", lng);
        editor.apply();
        editor.commit();
    }
    public static String getManualLatLng(Context context, String query) {
        String value = "null";
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (query.equals("lat")) {
            value = preferences.getString("lat", "");
        } else if (query.equals("lng")) {
            value = preferences.getString("lng", "");
        }
        return value;
    }


    public static String getLocationDetails(Context context, String query) {
        String value = "null";
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (query.equals("country")) {
            value = preferences.getString("country", "");
        } else if (query.equals("city")) {
            value = preferences.getString("city", "");
        } else if (query.equals("latitude")) {
            value = preferences.getString("latitude", "");
        } else if (query.equals("longitude")) {
            value = preferences.getString("longitude", "");
        }
        return value;
    }

    public static void setLocationFlag(Context context, String country, String city, String lat, String lng, int locationflag){
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("islocation", locationflag);
        editor.putString("country", country);
        editor.putString("city", city);
        editor.putString("latitude", lat);
        editor.putString("longitude", lng);
        editor.apply();
        editor.commit();
    }

    public static void setMethod(Context context, int method){
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("method", method);
        editor.apply();
        editor.commit();
    }

    public static void saveCounter(Context context, int value){
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("counter", value);
        editor.apply();
        editor.commit();
    }

    public static void saveTasbeenSound(Context context, int value){
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("tasbeensound", value);
        editor.apply();
        editor.commit();
    }

}
