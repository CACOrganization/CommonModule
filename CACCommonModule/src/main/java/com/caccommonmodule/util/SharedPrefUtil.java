package com.caccommonmodule.util;

/**
 * Created by ac on 2017/1/24.
 */

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class SharedPrefUtil {

    private static volatile SharedPrefUtil sSharedPrefUtil;

    private SharedPreferences mSharedPref;
    private static String ShredPrefKeyName = "104group";

    public static SharedPrefUtil getInstance(Context context) {
        if (sSharedPrefUtil == null) {
            synchronized (SharedPrefUtil.class) {
                if (sSharedPrefUtil == null) {
                    sSharedPrefUtil = new SharedPrefUtil(context);
                }
            }
        }
        return sSharedPrefUtil;
    }

    public static SharedPrefUtil newInstance(Context context, String mShredPrefKeyName) {
        ShredPrefKeyName = mShredPrefKeyName;
//        if (sSharedPrefUtil == null) {
//            synchronized (SharedPrefUtil.class) {
//                if (sSharedPrefUtil == null) {
                    sSharedPrefUtil = new SharedPrefUtil(context);
//                }
//            }
//        }
        return sSharedPrefUtil;
    }

    public void putObject(String key, Object obj) {
        SharedPreferences.Editor editor = mSharedPref.edit();

        if (obj instanceof String) {
            editor.putString(key, (String) obj);
        } else if (obj instanceof Boolean) {
            editor.putBoolean(key, (boolean) obj);
        } else if (obj instanceof Float) {
            editor.putFloat(key, (float) obj);
        } else if (obj instanceof Integer) {
            editor.putInt(key, (Integer) obj);
        } else if (obj instanceof Long) {
            editor.putLong(key, (Long) obj);
        } else if (obj instanceof Set) {
            editor.putStringSet(key, (Set<String>) obj);
        }

        editor.commit();
    }

    public SharedPreferences.Editor getEditor() {
        return mSharedPref.edit();
    }

    public SharedPreferences getSharedPref() {
        return mSharedPref;
    }

    private SharedPrefUtil(Context context) {
        mSharedPref = context.getApplicationContext().getSharedPreferences(ShredPrefKeyName, Context.MODE_PRIVATE);
    }

    public void put(String key, Object obj) {
        putObject(key, obj);
    }

//    private static SharedPreferences getSharedPreferences() {
//        SharedPreferences sharedPref = SharedPrefUtil.newInstance(ShredPrefKeyName).getSharedPref();
//        return sharedPref;
//    }

    public String getStr(String key) {
        return mSharedPref.getString(key, "");
    }

    public boolean getBoolean(String key) {
        return mSharedPref.getBoolean(key, false);
    }

    public Float getFloat(String key) {
        return mSharedPref.getFloat(key, 0);
    }

    public Integer getInt(String key) {
        return mSharedPref.getInt(key, 0);
    }

    public Long getLong(String key) {
        return mSharedPref.getLong(key, 0);
    }

    public Set<String> getSet(String key) {
        return mSharedPref.getStringSet(key, new HashSet<String>());
    }
}