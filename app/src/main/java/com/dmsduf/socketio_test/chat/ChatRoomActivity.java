package com.dmsduf.socketio_test.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
//
import static com.dmsduf.socketio_test.chat.ChatClientIO.emit_socket;
import static com.dmsduf.socketio_test.chat.ChatClientIO.gson;
import static com.dmsduf.socketio_test.chat.ChatClientIO.is_chatroom;
import static com.dmsduf.socketio_test.chat.ChatClientIO.socket;

import com.dmsduf.socketio_test.R;
import com.dmsduf.socketio_test.SharedSettings;
import com.dmsduf.socketio_test.adapter.ChatRoomAdapter;
import com.dmsduf.socketio_test.data_list.ChatRoomModel;
import com.dmsduf.socketio_test.data_list.ChattingModel;
import com.dmsduf.socketio_test.data_list.TagModel;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.Ack;


public class ChatRoomActivity extends AppCompatActivity {

    SharedSettings sharedSettings;


    RecyclerView recyclerView;
    ChatRoomAdapter chatRoomAdapter;
    TextView testtext;

    List<ChatRoomModel> chatRoomModels;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        recyclerView = findViewById(R.id.recycler_chatting_room);
        sharedSettings = new SharedSettings(this,"user_info");
        chatRoomModels = new ArrayList<>();
        chatRoomAdapter = new ChatRoomAdapter(this,chatRoomModels);
        recyclerView.setAdapter(chatRoomAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        LocalBroadcastManager.getInstance(this).registerReceiver(MessageReceiver, new IntentFilter("go_chatroom"));


        testtext = findViewById(R.id.testtext);
    }
    //##메세지를 받았을떄
    private BroadcastReceiver MessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra("message");
            Log.d("리시버", "방목록에서메세지 받음" + msg);
            ChattingModel chattingModel = gson.fromJson(msg,ChattingModel.class);
           chatRoomAdapter.update_new_message(chattingModel);


        }};

    @Override
    protected void onPause() {
        super.onPause();
        is_chatroom = false;

    }

    @Override
    protected void onResume() {
        super.onResume();
        is_chatroom = true;


    }

    public void chat(View view){
    }

}
