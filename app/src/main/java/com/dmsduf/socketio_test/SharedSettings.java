package com.dmsduf.socketio_test;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.dmsduf.socketio_test.data_list.ChatRoomModel;
import com.dmsduf.socketio_test.data_list.ChattingModel;
import com.dmsduf.socketio_test.data_list.UserChatModel;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.dmsduf.socketio_test.chat.ChatClientIO.gson;

public class SharedSettings {
    Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences sharedPreferences_chat;
    private SharedPreferences sharedPreferences_chatroom;
    private SharedPreferences.Editor editor;
    private SharedPreferences.Editor chat_editor;
    private SharedPreferences.Editor chatroom_editor;
    String TAG = "쉐어드";
    public SharedSettings(Context context, String shared_name){
        this.context = context;
        Log.d(TAG,context.getPackageName());
        sharedPreferences = context.getSharedPreferences(shared_name,Context.MODE_PRIVATE);
        sharedPreferences_chat = context.getSharedPreferences("user_chat",Context.MODE_PRIVATE);
        sharedPreferences_chatroom  = context.getSharedPreferences("user_chatroom",Context.MODE_PRIVATE);
        chat_editor = sharedPreferences_chat.edit();
        editor = sharedPreferences.edit();
        chatroom_editor = sharedPreferences_chatroom.edit();
    }
    //받은 채팅메시지를 쉐어드에 저장하는 부분
    public String get_chatroom_info(String room_idx){
        return  sharedPreferences_chatroom.getString(room_idx,"없음");

    }
    public List<Object> get_chatroom_all(){
        List<Object> a = Arrays.asList(sharedPreferences_chatroom.getAll().values().toArray());

        return a;
    }
    public void set_chatroom_info(String chatroom_idx ,String chatroom_model){
        chatroom_editor.putString(chatroom_idx,chatroom_model);
        chatroom_editor.commit();
    }

    public void change_file(String file_name){
        this.sharedPreferences =context.getSharedPreferences(file_name,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    //받은 채팅메시지를 쉐어드에 저장하는 부분
    public String get_chatroom_messages(String room_idx){
        return  sharedPreferences_chat.getString(room_idx,"없음");

    }
    //채팅메시지하나만왔을떄작업하는부분
    public void set_chatroom_messages(String room_idx, String chattingModels){

        Log.d(TAG,"방번호: "+room_idx+"저장되는메시지 : "+chattingModels);
        String messages = get_chatroom_messages(room_idx);
        if(messages.equals("없음")) {
            chat_editor.putString(room_idx,chattingModels);
            chat_editor.commit();
        }
      else {
            Type type = new TypeToken<List<ChattingModel>>() {}.getType();
            ArrayList<ChattingModel> chat_data = gson.fromJson(messages, type);

            chat_data.add(gson.fromJson(chattingModels,ChattingModel.class));
            chat_editor.putString(room_idx,gson.toJson(chat_data));
            chat_editor.commit();
        }
    }
    ///전체메시지저장하는부분
    public void set_chatroom_message(String room_idx, String chattingModels){
        Log.d(TAG,"추가하는메시지"+chattingModels);


        Type type = new TypeToken<List<ChattingModel>>() {}.getType();
        ArrayList<ChattingModel> chat_data = gson.fromJson(chattingModels, type);

        chat_editor.putString(room_idx,gson.toJson(chat_data));
        chat_editor.commit();

    }
    //쉐어드에 저장되어있는 메시지를 읽음처리 해주는 구간
    public void update_chatroom_message(UserChatModel userChatModel,String room_idx){
        ChatRoomModel chatRoomModel = gson.fromJson(sharedPreferences_chatroom.getString(room_idx,"없음"), ChatRoomModel.class);
        chatRoomModel.setuser(userChatModel);
        chatroom_editor.putString(room_idx,gson.toJson(chatRoomModel));
        chatroom_editor.commit();

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

        chat_editor.clear();
        chat_editor.commit();
        chatroom_editor.clear();
        chatroom_editor.commit();

    }
}
