package com.dmsduf.socketio_test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.dmsduf.socketio_test.chat.ChatClientIO;
import com.dmsduf.socketio_test.data_list.ChatRoomModel;
import com.dmsduf.socketio_test.data_list.UserStateModel;
import com.dmsduf.socketio_test.data_list.UserModel;

import java.util.ArrayList;

import io.socket.client.Ack;

import static com.dmsduf.socketio_test.chat.ChatClientIO.emit_socket;
import static com.dmsduf.socketio_test.chat.ChatClientIO.gson;
import static com.dmsduf.socketio_test.chat.ChatClientIO.is_mainfriend;
import static com.dmsduf.socketio_test.chat.ChatClientIO.socket;

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
            String state = intent.getStringExtra("state");
            String update_string = intent.getStringExtra("update_content");

            update_items(state,update_string);

        }};



    public void update_items(String state , String item){
        switch (state) {
            case "make_card":
                //카드만들기의 경우 카드객체 클래스 받을것
                textView.setText("카드만들기"+item);
                break;
            case "delete_card":
                textView.setText("카드삭제하기"+item);
                break;
            case "online" : case "offline":
            //원래는 여기서 친구 카드중 하나 찾아서 어뎁터 업데이트 시키면 됨
                textView.setText("상태업데이트하기"+item);
                break;

        }
    }

    //상태 알리기 버튼눌렀을때
    public void update_state(View v){
        ArrayList<Integer> announce_users = new ArrayList<>();
        int user_idx = sharedSettings.get_something_int("user_idx");
        announce_users.add(2);
        announce_users.add(3);
        UserStateModel userStateModel = new UserStateModel(user_idx,"online",announce_users);
        String item  = gson.toJson(userStateModel);
        emit_socket(FriendAcitvity.this, "update_user_state", item, new Ack() {
            @Override
            public void call(Object... args) {

            }
        });
    }
    public void make_room(View v){
        emit_socket(FriendAcitvity.this, "make_chatroom", gson.toJson(new UserModel(1,sharedSettings.get_something_string("user_nickname"),"프로필")), new Ack() {
            @Override
            public void call(Object... args) {

            }
        });
    }
    String C2S = "client_to_server";
    public void leave_room(View v){
        socket.emit( C2S+"leave_room", gson.toJson(new UserModel(2, sharedSettings.get_something_string("user_nickname"), "프로필")), 5, new Ack() {
            @Override
            public void call(Object... args) {
                Log.d("방 나감", args[0].toString());

            }
        });

    }


    public void deligate_leader(View v){
        socket.emit(C2S + "delegation_leader", gson.toJson(new UserModel(1, "방장", "프로필")),
                gson.toJson(new UserModel(2, "클라", "프로필")),5, new Ack() {
                    @Override
                    public void call(Object... args) {
                        Log.d("리더위임",args[0].toString());
                    }
                });
    }
    public void chat_TO(View v){

        ChatClientIO.emit_socket(FriendAcitvity.this,"check_room", gson.toJson(new UserModel(2, sharedSettings.get_something_string("user_nickname"),"프로필")), new Ack() {
            @Override
            public void call(Object... args) {

            }
        });
    }
    public void edit_card(View v){
        //채팅방 정보 쉐어드에서 불러오기
        ChatRoomModel chatRoomModel = gson.fromJson(sharedSettings.get_chatroom_info("5"),ChatRoomModel.class);
        //채팅방의 제목이 바뀌었을때
        chatRoomModel.getCard().setTitle("바뀐카드제목");
        //채팅서버에 바뀐 내용 json으로 변환하여 전송
        socket.emit(C2S+"update_card",gson.toJson(chatRoomModel));

    }
}
