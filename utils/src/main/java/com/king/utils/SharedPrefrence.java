package com.king.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class SharedPrefrence {
    private static SharedPreferences preferences;

    public static void init(Context context, String prefsName) {
        preferences = context.getSharedPreferences(prefsName, 0);
    }

    public static void addSPListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        if (preferences != null) {
            preferences.registerOnSharedPreferenceChangeListener(listener);
        }
    }

    public static void removeSPListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        if (preferences != null) {
            preferences.unregisterOnSharedPreferenceChangeListener(listener);
        }
    }


    public static void save(String key, Object value) {
        if (preferences == null) {
            throw new RuntimeException("you should use init() first before use this method");
        }
        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences.Editor editor = preferences.edit();
        if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof Set) {
            editor.putStringSet(key, (Set<String>) value);
        } else {
            throw new IllegalArgumentException("value must be type of Boolean Int Long Float String Set<String>");
        }
        // Commit the edits!
        editor.commit();
    }

    public static String getString(String key) {
        if (preferences == null) {
            throw new RuntimeException("you should use init() first before use this method");
        }
        return preferences.getString(key, "");
    }

    public static Boolean getBoolean(String key) {
        if (preferences == null) {
            throw new RuntimeException("you should use init() first before use this method");
        }
        return preferences.getBoolean(key, false);
    }

    public static Boolean getBoolean(String key, Boolean defaultValue) {
        if (preferences == null) {
            throw new RuntimeException("you should use init() first before use this method");
        }
        return preferences.getBoolean(key, defaultValue);
    }

    public static Integer getInt(String key) {
        if (preferences == null) {
            throw new RuntimeException("you should use init() first before use this method");
        }
        return preferences.getInt(key, 0);
    }

    public static Long getLong(String key) {
        if (preferences == null) {
            throw new RuntimeException("you should use init() first before use this method");
        }
        return preferences.getLong(key, 0);
    }

    public static Float getFloat(String key) {
        if (preferences == null) {
            throw new RuntimeException("you should use init() first before use this method");
        }
        return preferences.getFloat(key, 0);
    }

    public static Set<String> getStringSet(String key) {
        if (preferences == null) {
            throw new RuntimeException("you should use init() first before use this method");
        }
        return preferences.getStringSet(key, new HashSet<String>());
    }

}
