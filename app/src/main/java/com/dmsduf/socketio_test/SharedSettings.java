package com.dmsduf.socketio_test;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedSettings {
    Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    String TAG = "쉐어드";
    public SharedSettings(Context context, String shared_name){
        this.context = context;
        Log.d(TAG,context.getPackageName());
        sharedPreferences = context.getSharedPreferences(shared_name,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void change_file(String file_name){
        this.sharedPreferences =context.getSharedPreferences(file_name,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    public String get_something_string(String item){
        String items = sharedPreferences.getString(item,"없음");
        return items;
    }
    public void set_something_string(String name,String item){
        editor.putString(name,item);
        editor.commit();
    }
    public void set_something_boolean(String name,Boolean T_F){
        editor.putBoolean(name,T_F);
        editor.commit();
    }
    public Boolean get_something_boolean(String item){
        Boolean T_F = sharedPreferences.getBoolean(item,false);
        return T_F;
    }
    public int get_something_int(String item){
        int items = sharedPreferences.getInt(item,386);
        return items;

    }
    public void set_something_int(String name, int item){
        editor.putInt(name,item);
        editor.commit();
    }
    public void clear(){
        editor.clear();
        editor.commit();
    }
}
