package com.dmsduf.socketio_test;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.dmsduf.socketio_test.data_list.ChattingModel;

import java.util.ArrayList;
import java.util.List;

public class SharedSettings {
    Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences sharedPreferences_chat;
    private SharedPreferences.Editor editor;
    private SharedPreferences.Editor chat_editor;
    String TAG = "쉐어드";
    public SharedSettings(Context context, String shared_name){
        this.context = context;
        Log.d(TAG,context.getPackageName());
        sharedPreferences = context.getSharedPreferences(shared_name,Context.MODE_PRIVATE);
        sharedPreferences_chat = context.getSharedPreferences("user_chat",Context.MODE_PRIVATE);
        chat_editor = sharedPreferences_chat.edit();
        editor = sharedPreferences.edit();
    }

    public void change_file(String file_name){
        this.sharedPreferences =context.getSharedPreferences(file_name,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    //받은 채팅메시지를 쉐어드에 저장하는 부분
    public String get_chatroom_messages(String room_idx){
        return  sharedPreferences_chat.getString(room_idx,"없음");

    }
    public void set_chatroom_messages(String room_idx, String chattingModels){
        chat_editor.putString(room_idx,chattingModels);
        chat_editor.commit();

    }
    //쉐어드에 저장되어있는 메시지를 읽음처리 해주는 구간
    public void update_chatroom_message(String user_idx,String room_idx,String read_last_idx){
        String messages = get_chatroom_messages(room_idx);
        if(messages.equals("없음")){
            return;
        }
        else{

        }

    }
    //------------
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
