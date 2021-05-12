package com.example.pxisjavaver;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class SharedPrefManager {
    private static SharedPrefManager instance;
    private static Context ctx;

    private static final String SHARED_PREF_NAME = "mysharedpref";
    private static final String KEY_PXU = "pxu";
    private static final String KEY_PXU_USERAUTH = "userAuth";
    private static final String KEY_PXU_EMAIL = "email";
    private static final String KEY_PXU_FNAME = "FirstName";
    private static final String KEY_PXU_MNAME = "MiddleName";
    private static final String KEY_PXU_LNAME = "LastName";
    private static final String KEY_PXU_AGE = "Age";
    private static final String KEY_PXU_ADDRESS = "Address";
    private static final String KEY_PXU_BDATE = "BirthDate";
    private static final String KEY_PXU_GENDER = "Gender";
    private static final String KEY_PXU_ID = "drPxuId";


    private SharedPrefManager(Context context) {
        ctx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefManager(context);
        }
        return instance;
    }

    public boolean userLogin(String pxu, String userAuth, String email, String FirstName, String MiddleName,
                             String LastName, String Age,String Address, String BirthDate, String Gender){

        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_PXU,pxu);
        editor.putString(KEY_PXU_USERAUTH,userAuth);
        editor.putString(KEY_PXU_EMAIL, email);
        editor.putString(KEY_PXU_FNAME,FirstName);
        editor.putString(KEY_PXU_MNAME,MiddleName);
        editor.putString(KEY_PXU_LNAME,LastName);
        editor.putString(KEY_PXU_AGE,Age);
        editor.putString(KEY_PXU_ADDRESS,Address);
        editor.putString(KEY_PXU_BDATE,BirthDate);
        editor.putString(KEY_PXU_GENDER,Gender);

        editor.apply();
        return true;
    }

    public boolean pxId(String pxId){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_PXU_ID,pxId);
        editor.apply();
        return true;
    }

    public boolean isLoggedIn(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if(sharedPreferences.getString(KEY_PXU, null)!=null){
            return true;
        }
        return false;
    }

    public boolean logout(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }

    public String getKeyPxu(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_PXU,null);
    }

    public String getKeyPxuUserAuth(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_PXU_USERAUTH,null);
    }

    public String getKeyPxuEmail(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_PXU_EMAIL,null);
    }

    public String getKeyPxuFname(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_PXU_FNAME,null);
    }

    public String getKeyPxuMname(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_PXU_MNAME,null);
    }

    public String getKeyPxuLname(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_PXU_LNAME,null);
    }

    public String getKeyPxuAge(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_PXU_AGE,null);
    }

    public String getKeyPxuAddress(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_PXU_ADDRESS,null);
    }

    public String getKeyPxuBdate(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_PXU_BDATE,null);
    }

    public String getKeyPxuGender(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_PXU_GENDER,null);
    }
    public String getKeyPxuId(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_PXU_ID,null);
    }
}
