package com.superc.lib.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesUtil {

    public final static String CAMERA_CONFIG = "Camera_config";

    /**
     * 获取SharedPreferences实例对象
     *
     * @param context
     * @return
     */
    public static SharedPreferences getSharedPreference(Context context, String sharedName) {
        return context.getSharedPreferences(sharedName, Context.MODE_PRIVATE);
    }

    /**
     * 保存一个Boolean类型的值！
     *
     * @param context
     * @param key
     * @param value
     * @return
     */
    public static boolean putBoolean(Context context, String sharedName, String key, Boolean value) {
        if (context == null) return false;
        SharedPreferences sharedPreference = getSharedPreference(context, sharedName);
        Editor editor = sharedPreference.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    /**
     * 保存一个int类型的值！
     *
     * @param context
     * @param key
     * @param value
     * @return
     */
    public static boolean putInt(Context context, String sharedName, String key, int value) {
        if (context == null) return false;
        SharedPreferences sharedPreference = getSharedPreference(context, sharedName);
        Editor editor = sharedPreference.edit();
        editor.putInt(key, value);
        return editor.commit();
    }


    /**
     * 保存一个float类型的值！
     *
     * @param context
     * @param key
     * @param value
     * @return
     */
    public static boolean putFloat(Context context, String sharedName, String key, float value) {
        if (context == null) return false;
        SharedPreferences sharedPreference = getSharedPreference(context, sharedName);
        Editor editor = sharedPreference.edit();
        editor.putFloat(key, value);
        return editor.commit();
    }

    /**
     * 保存一个long类型的值！
     *
     * @param context
     * @param key
     * @param value
     * @return
     */
    public static boolean putLong(Context context, String sharedName, String key, long value) {
        if (context == null) return false;
        SharedPreferences sharedPreference = getSharedPreference(context, sharedName);
        Editor editor = sharedPreference.edit();
        editor.putLong(key, value);
        return editor.commit();
    }

    /**
     * 保存一个String类型的值！
     *
     * @param context
     * @param key
     * @param value
     * @return
     */
    public static boolean putString(Context context, String sharedName, String key, String value) {
        if (context == null) return false;
        SharedPreferences sharedPreference = getSharedPreference(context, sharedName);
        Editor editor = sharedPreference.edit();
        editor.putString(key, value);
        return editor.commit();
    }


    /**
     * 获取String的value
     *
     * @param context
     * @param key      名字
     * @param defValue 默认值
     * @return
     */
    public static String getString(Context context, String sharedName, String key, String defValue) {
        if (context == null) return defValue;
        SharedPreferences sharedPreference = getSharedPreference(context, sharedName);
        return sharedPreference.getString(key, defValue);
    }

    /**
     * 获取int的value
     *
     * @param context
     * @param key      名字
     * @param defValue 默认值
     * @return
     */
    public static int getInt(Context context, String sharedName, String key, int defValue) {
        if (context == null) return defValue;
        SharedPreferences sharedPreference = getSharedPreference(context, sharedName);
        return sharedPreference.getInt(key, defValue);
    }

    /**
     * 获取float的value
     *
     * @param context
     * @param key      名字
     * @param defValue 默认值
     * @return
     */
    public static float getFloat(Context context, String sharedName, String key, Float defValue) {
        if (context == null) return defValue;
        SharedPreferences sharedPreference = getSharedPreference(context, sharedName);
        return sharedPreference.getFloat(key, defValue);
    }

    /**
     * 获取boolean的value
     *
     * @param context
     * @param key      名字
     * @param defValue 默认值
     * @return
     */
    public static boolean getBoolean(Context context, String sharedName, String key, Boolean defValue) {
        if (context == null) return defValue;
        SharedPreferences sharedPreference = getSharedPreference(context, sharedName);
        return sharedPreference.getBoolean(key, defValue);
    }

    /**
     * 获取long的value
     *
     * @param context
     * @param key      名字
     * @param defValue 默认值
     * @return
     */
    public static long getLong(Context context, String sharedName, String key, long defValue) {
        if (context == null) return defValue;
        SharedPreferences sharedPreference = getSharedPreference(context, sharedName);
        return sharedPreference.getLong(key, defValue);
    }

    /**
     * 删除Key对应的内容
     *
     * @param context
     * @param key     名字
     * @return
     */
    public static boolean remove(Context context, String sharedName, String key) {
        if (context == null) return false;
        SharedPreferences sharedPreference = getSharedPreference(context, sharedName);
        Editor editor = sharedPreference.edit();
        editor.remove(key);
        return editor.commit();
    }

    /**
     * 清除SharedPreferences中的所有值！
     *
     * @param context
     * @return
     */
    public static boolean clearAll(Context context, String sharedName) {
        if (context == null) return false;
        SharedPreferences sharedPreference = getSharedPreference(context, sharedName);
        Editor editor = sharedPreference.edit();
        editor.clear();
        return editor.commit();
    }
}
