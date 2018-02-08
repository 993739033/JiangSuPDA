package com.mingnong.scanappnew.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;


import com.mingnong.scanappnew.MyApplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by wyw on 2016/7/12.
 */

public class SPUtils {
    private static SPUtils instance;
    private static final String SP_NAME = "scan";
    private static final String TAG = SPUtils.class.getSimpleName();
    private SharedPreferences sp;
    private Context mContext;

    private SPUtils(Context mContext) {
        sp = mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized SPUtils getInstance() {
        if (instance == null) {
            instance = new SPUtils(MyApplication.getContext());
        }
        return instance;
    }


    public void saveData(String key, Object object) {
        String type = object.getClass().getSimpleName();
        SharedPreferences.Editor edit = sp.edit();
        Log.e(TAG, type);
        if ("String".equals(type)) {
            edit.putString(key, (String) object);
        } else if ("Integer".equals(type)) {
            edit.putInt(key, (Integer) object);
        } else if ("Boolean".equals(type)) {
            edit.putBoolean(key, (Boolean) object);
        } else if ("Float".equals(type)) {
            edit.putFloat(key, (Float) object);
        } else if ("Long".equals(type)) {
            edit.putLong(key, (Long) object);
        }
        Log.e(TAG + "saveData", object.toString());
        edit.apply();
    }

    public <T> T getData(String key, T defaultObj, Class<T> type) {
        String typeString = type.getSimpleName();
        if ("String".equals(typeString)) {
            return (T) sp.getString(key, (String) defaultObj);
        } else if ("Integer".equals(typeString)) {
            return (T) Integer.valueOf(sp.getInt(key, (Integer) defaultObj));
        } else if ("Boolean".equals(typeString)) {
            return (T) Boolean.valueOf(sp.getBoolean(key, (Boolean) defaultObj));
        } else if ("Float".equals(typeString)) {
            return (T) Float.valueOf(sp.getFloat(key, (Float) defaultObj));
        } else if ("Long".equals(typeString)) {
            return (T) Long.valueOf(sp.getLong(key, (Long) defaultObj));
        }
        return null;
    }

    /**
     * sp保存对象
     */
    public void saveObjectData(String key, Object object) {
        String objectBase64 = "";
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            oos.flush();
            objectBase64 = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
                if (oos != null) {
                    oos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        sp.edit().putString(key, objectBase64).apply();
    }

    /**
     * 取出对象
     */
    public Object getObjectData(String key) {
        Object obj = null;
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try {
            String objectBase64 = sp.getString(key, "");
            if (TextUtils.isEmpty(objectBase64)) {
                throw new NullPointerException("获取了空值" + key);
            }
            bais = new ByteArrayInputStream(Base64.decode(objectBase64.getBytes(), Base64.DEFAULT));
            ois = new ObjectInputStream(bais);
            obj = ois.readObject();
        } catch (Exception e) {
            removeKey(key);
        } finally {
            try {
                if (bais != null) {
                    bais.close();
                }
                if (ois != null) {
                    ois.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return obj;
    }

    public void removeKey(String key) {
        sp.edit().remove(key).apply();
    }
}
