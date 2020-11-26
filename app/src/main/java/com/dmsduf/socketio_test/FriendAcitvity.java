package com.dmsduf.socketio_test;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.dmsduf.socketio_test.chat.ChatClientIO;
import com.dmsduf.socketio_test.data_list.UserModel;

import io.socket.client.Ack;

import static com.dmsduf.socketio_test.chat.ChatClientIO.gson;

public class FriendAcitvity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_acitvity);
    }
    public void chat_TO(View v){

        ChatClientIO.emit_socket("check_room", gson.toJson(new UserModel(2, 5)), new Ack() {
            @Override
            public void call(Object... args) {

            }
        });

    }
}
