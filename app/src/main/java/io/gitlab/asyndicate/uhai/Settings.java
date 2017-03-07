package io.gitlab.asyndicate.uhai;

import android.content.Context;
import android.util.Log;


import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Scanner;


public class Settings {

    private Context mContext;

    private static Settings mInstance;
    private static boolean isGuest = true;

    private Settings(Context context) {
        mContext = context;
    }

    public static boolean isGuestMode() {
        return isGuest;
    }

    public static void setGuestMode(boolean status) {
        isGuest = status;
    }

    private boolean getBoolean(String guest, boolean placeholder) {
        return placeholder;
    }

    private JSONObject getSession() {
        try {
            String me = "";
            FileInputStream in = new FileInputStream(mContext.getFilesDir() + "/me.json");
            Scanner s = new Scanner(in);
            while (s.hasNext()) {
                me += s.nextLine();
            }
            return new JSONObject(me);
        } catch (Exception e) {
            return new JSONObject();
        }
    }

    public static Settings getInstance() {
        return mInstance;
    }

    public static Settings getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Settings(context);
        }
        return mInstance;
    }

    public static String getString(String key) {
        try {
            return Settings.getInstance().getSession().getString(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getString(String key, String placeholder) {
        try {
            return Settings.getInstance().getSession().getString(key);
        } catch (Exception ignored) {
            return placeholder;
        }
    }

    public static JSONObject getJSONObject(String key) {
        try {
            return Settings.getInstance().getSession().getJSONObject(key);
        } catch (Exception e) {
            return null;
        }
    }

    public Context getContext() {
        return mContext;
    }

    public void putString(String name, String name2) {
        try {
            FileOutputStream in = new FileOutputStream(mContext.getFilesDir() + "/me.json");
            in.write(getSession().put(name, name2).toString().getBytes());
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
