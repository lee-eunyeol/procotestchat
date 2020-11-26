package com.dmsduf.socketio_test.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
//
//import com.github.nkzawa.socketio.client.IO;
//import com.github.nkzawa.socketio.client.Socket;

import com.dmsduf.socketio_test.R;
import com.dmsduf.socketio_test.SharedSettings;
import com.dmsduf.socketio_test.adapter.ChatRoomAdapter;
import com.dmsduf.socketio_test.data_list.ChatRoomModel;
import com.dmsduf.socketio_test.data_list.TagModel;

import java.util.ArrayList;
import java.util.List;

//import io.socket.client.IO;
//import io.socket.client.Socket;
//import io.socket.emitter.Emitter;

public class ChatRoomActivity extends AppCompatActivity {
    EditText nickname;
    EditText room_name;
    SharedSettings sharedSettings;

//    String URL = "";
//    Socket socket;
//    {
//        try{
//            socket = IO.socket(URL);
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//    }
    RecyclerView recyclerView;
    ChatRoomAdapter chatRoomAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        recyclerView = findViewById(R.id.recycler_chatting_room);
        sharedSettings = new SharedSettings(this,"user_info");
        List<ChatRoomModel> chatRoomModels = make_dummy();
        chatRoomAdapter = new ChatRoomAdapter(this,chatRoomModels);
        recyclerView.setAdapter(chatRoomAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



    }







    public void chat(View view){
    }
    public List<ChatRoomModel> make_dummy(){
        List<ChatRoomModel> list = new ArrayList<>();
        List<TagModel> tagModels = new ArrayList<>();
        tagModels.add(new TagModel(1,"태그1"));
        tagModels.add(new TagModel(2,"태그2"));
        tagModels.add(new TagModel(3,"태그3"));

        for (int i  = 0 ; i<10;i++){

            ChatRoomModel ChatRoomModel = new ChatRoomModel(i,i,"테스트"+i,tagModels
                    ,"https://volae.ga/photo_test","이름"+i,"메세지","시간");
            ChatRoomModel.setRoom_name("방이름"+i);
            list.add(ChatRoomModel);
        }
        return list;
    }
}
