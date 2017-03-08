package io.gitlab.asyndicate.uhai;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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

    public void LoadImage(String path, int width, PayloadRunnable runnable) {
        new LoadImageTask(mContext, path, width, runnable).execute();
    }

    public void LoadImage(int res, int width, PayloadRunnable runnable) {
        Log.d("Image", "attempting to decode image");
        new LoadImageTask(mContext, res, width, runnable).execute();
    }

    public class LoadImageTask extends AsyncTask<Void, Void, Object> {

        private int requestWidth;
        private int imageResource;
        private String imagePath;
        private Context mContext;
        private Bitmap bitmap;
        private PayloadRunnable afterRunable;

        public LoadImageTask(Context context, int resource, int requestWidth, PayloadRunnable afterRubbale) {
            this.mContext = context;
            this.imageResource = resource;
            this.requestWidth = requestWidth;
            this.afterRunable = afterRubbale;
        }

        public LoadImageTask(Context context, String path, int requestWidth, PayloadRunnable afterRubbale) {
            this.mContext = context;
            this.imagePath = path;
            this.requestWidth = requestWidth;
            this.afterRunable = afterRubbale;
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            try {
                int w = requestWidth;
                int h = 0;
                BitmapFactory.Options op = new BitmapFactory.Options();
                op.inJustDecodeBounds = true;
                if (imageResource != 0) {
                    BitmapFactory.decodeResource(mContext.getResources(), imageResource, op);
                } else {
                    BitmapFactory.decodeFile(imagePath, op);
                }

                if (w != 0) {
                    h = w * op.outHeight / op.outWidth;
                } else if (h != 0) {
                    w = h * op.outWidth / op.outHeight;
                } else {
                    w = 1;
                    h = 1;
                }
                op.inSampleSize = Math.max(op.outWidth / w, op.outHeight / h);
                op.inJustDecodeBounds = false;

                if (imageResource != 0) {
                    bitmap = BitmapFactory.decodeResource(mContext.getResources(), imageResource, op);
                } else {
                    bitmap = BitmapFactory.decodeFile(imagePath, op);
                }
                return bitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            if (afterRunable != null && bitmap != null) {
                Log.d("Image", "Image decoded running  afterCallback");
                afterRunable.run(bitmap);
            } else {
                Log.d("Image", "Invalid image");
            }
        }
    }
}
