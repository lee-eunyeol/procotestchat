package com.dmsduf.socketio_test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dmsduf.socketio_test.chat.ChatClientIO;
import com.dmsduf.socketio_test.data_list.UpdateUserModel;
import com.dmsduf.socketio_test.data_list.UserModel;

import java.util.ArrayList;

import io.socket.client.Ack;

import static com.dmsduf.socketio_test.chat.ChatClientIO.emit_socket;
import static com.dmsduf.socketio_test.chat.ChatClientIO.gson;
import static com.dmsduf.socketio_test.chat.ChatClientIO.is_mainfriend;

public class FriendAcitvity extends AppCompatActivity {
    TextView textView;
    SharedSettings sharedSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_acitvity);

        sharedSettings = new SharedSettings(this, "user_info");

        textView = findViewById(R.id.update_text);
        LocalBroadcastManager.getInstance(this).registerReceiver(user_update_reciver, new IntentFilter("update_user"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        is_mainfriend = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        is_mainfriend = false;
    }
    //유저 상태가 업데이트 되었을떄
    private BroadcastReceiver user_update_reciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String update_string = intent.getStringExtra("update_user");
            update_items(update_string);

        }};



    public void update_items(String item){
        //원래는 여기서 친구 카드중 하나 찾아서 어뎁터 업데이트 시키면 됨
        textView.setText(item);
    }

    //상태 알리기 버튼눌렀을때
    public void update_state(View v){
        ArrayList<Integer> announce_users = new ArrayList<>();
        int user_idx = sharedSettings.get_something_int("user_idx");
        announce_users.add(2);
        announce_users.add(3);
        UpdateUserModel updateUserModel = new UpdateUserModel(user_idx,"online",announce_users);
        String item  = gson.toJson(updateUserModel);
        emit_socket(FriendAcitvity.this, "update_user_state", item, new Ack() {
            @Override
            public void call(Object... args) {

            }
        });
    }

    public void chat_TO(View v){

        ChatClientIO.emit_socket(FriendAcitvity.this,"check_room", gson.toJson(new UserModel(2, 5)), new Ack() {
            @Override
            public void call(Object... args) {

            }
        });

    }
}
