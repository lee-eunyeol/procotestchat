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

        emit_socket(ChatRoomActivity.this,"get_rooms", sharedSettings.get_something_int("user_idx"), new Ack() {
            @Override
            public void call(Object... args) {

                Type type = new TypeToken<List<ChattingModel>>() {}.getType();
                ArrayList<ChattingModel> chat_datas = new ArrayList<>();
                chat_datas = gson.fromJson(args[0].toString(),type);
                List<TagModel> tagModels = new ArrayList<>();
                tagModels.add(new TagModel(1,"태그1"));
                tagModels.add(new TagModel(2,"태그2"));
                tagModels.add(new TagModel(3,"태그3"));
                List<ChatRoomModel> list = new ArrayList<>();
                Log.d("앙",args[0].toString());

                for (int i = 0 ; i < chat_datas.size();i++){
                ChatRoomModel ChatRoomModel = new ChatRoomModel(chat_datas.get(i).getChatroom_idx(),chat_datas.get(i).getChatroom_idx(),"테스트"+i,tagModels
                        ,"https://volae.ga/photo_test","이름"+i,chat_datas.get(i).getContent(),chat_datas.get(i).getCreated_at(),1);
                ChatRoomModel.setRoom_name("방이름"+chat_datas.get(i).getChatroom_idx());
                list.add(ChatRoomModel);

                }
              chatRoomAdapter.setChatRoomModel(list);
                chatRoomAdapter.notify_with_handler();
            }
        });
    }

    public void chat(View view){
    }

}
