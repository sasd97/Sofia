package sasd97.github.com.translator.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Alexander Dadukin on 21.04.2016.
 */

public final class Prefs {

    private static final String TAG = Prefs.class.getCanonicalName();
    private static final String APP_PREFERENCES = "sofia.app";

    private static SharedPreferences.Editor editor;
    private static SharedPreferences sharedPreferences;

    private Prefs() {
    }

    public static void init(Context context) {
        sharedPreferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static <T> void put(String key, T value) {

        if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof String) {
            editor.putString(key, (String) value);
        }

        Log.d(TAG, String.format("Putted key %1$s with value %2$s", key, value.toString()));
        editor.apply();
    }

    public static SharedPreferences get() {
        return sharedPreferences;
    }

    public static SharedPreferences.Editor edit() {
        return editor;
    }
}
