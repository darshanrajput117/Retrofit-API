package com.example.retrofit_api.Utility;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.multidex.MultiDexApplication;

import com.example.retrofit_api.Model.LoginModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.lang.reflect.Type;

public class Globals extends MultiDexApplication {

    static SharedPreferences sharedPreferences;
    Context context;
    SharedPreferences.Editor editor;

    public static String userDatatoJSON(LoginModel userDetails) {
        if (userDetails == null) {
            return null;
        }
        Type mapType = new TypeToken<LoginModel>() {
        }.getType();
        Gson gson = new Gson();
        return gson.toJson(userDetails, mapType);
    }

    public static LoginModel toUserData(String params) {
        if (params == null)
            return null;

        Type mapType = new TypeToken<LoginModel>() {
        }.getType();
        Gson gson = new Gson();
        return gson.fromJson(params, mapType);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    public int getGridValue() {
        return getSharedPref().getInt(Constant.gridvalue, 0);
    }

    public void setGridValue(int gridValue) {
        getEditor().putInt(Constant.gridvalue, gridValue);
        getEditor().commit();
    }

    public LoginModel getLoginData() {
        return toUserData(getSharedPref().getString(Constant.USER_MAP, null));
    }

    public void setLoginData(LoginModel userDetails) {
        getEditor().putString(Constant.USER_MAP, userDatatoJSON(userDetails));
        getEditor().commit();
    }

    public SharedPreferences.Editor getEditor() {
        return editor = (editor == null) ? getSharedPref().edit() : editor;
    }

    public SharedPreferences getSharedPref() {
        return sharedPreferences = (sharedPreferences == null) ? getSharedPreferences(Constant.secrets, Context.MODE_PRIVATE) : sharedPreferences;
    }
}
