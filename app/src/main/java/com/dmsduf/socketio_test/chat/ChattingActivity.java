package com.dmsduf.socketio_test.chat;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.dmsduf.socketio_test.AckWithTimeOut;
import com.dmsduf.socketio_test.R;
import com.dmsduf.socketio_test.SharedSettings;
import com.dmsduf.socketio_test.adapter.ChattingAdapter;
import com.dmsduf.socketio_test.adapter.ChattingNaviAdapter;
import com.dmsduf.socketio_test.data_list.ChattingModel;
import com.dmsduf.socketio_test.data_list.RoomModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.socket.client.Ack;

import static com.dmsduf.socketio_test.chat.ChatClientIO.current_room_idx;
import static com.dmsduf.socketio_test.chat.ChatClientIO.socket;


public class ChattingActivity extends AppCompatActivity {
    RecyclerView chat_recyclerview;
    ChattingAdapter ChattingAdapter;
    ArrayList<ChattingModel> chat_data;

    // 서버 접속 여부를 판별하기 위한 변수
    boolean isConnect = false;
    EditText chatting_text;
    ProgressDialog pro;

    //알람체크박스
    CheckBox chat_alarm_checkbox;

    // 어플 종료시 스레드 중지를 위해...
    boolean isRunning = false;
    // 서버와 연결되어있는 소켓 객체

    //네비게이션 뷰 선언부
    //드로어 레이아웃은 리니어 레이아웃으로 구성되어있으며, 레이아웃안에 대화참여자들을 볼수 있는 리사이클러뷰가존재한다
    LinearLayout navigation_layout;
    DrawerLayout drawer_layout;
    Toolbar toolbar;
    DrawerToggle toggle;
    RecyclerView navi_participant_recyclerview;
    ChattingNaviAdapter adapter;


    TextView navi_title;
    TextView navi_language;
    TextView navi_participant;
    ImageView chatting_navi_room_exit;


    String TAG = "소켓아이오";
//    static ChatClientIO ChatClientIO;

    Gson gson;
    String nickname;
    int room_idx;
    int user_idx;
    RoomModel roomModel;
    SharedSettings sharedSettings;
    SharedSettings sharedSettings_chat;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_room);
        //유저 정보 들어간 쉐어드
        sharedSettings = new SharedSettings(this,"user_info");
        sharedSettings_chat = new SharedSettings(this, "user_chat");

        //TODO 여기 방 번호 가져오는 부분 문제 있을 수 있음.
        nickname = sharedSettings.get_something_string("user_nickname");
        room_idx = current_room_idx;
        user_idx = sharedSettings.get_something_int("user_idx");
        gson = new Gson();

        //채팅관련 브로드케스트리시버등록
        //메시지를 받았을때
        LocalBroadcastManager.getInstance(this).registerReceiver(MessageReceiver, new IntentFilter("go_chatingroom"));
        //다른사람이 채팅방에 입장했을때
        LocalBroadcastManager.getInstance(this).registerReceiver(joinroomReceiver, new IntentFilter("user_join"));

        //데이터연결이 끊겼다가 다시 돌아왔을때
        LocalBroadcastManager.getInstance(this).registerReceiver(connectReceiver, new IntentFilter("socket_connected"));

        chat_recyclerview = findViewById(R.id.chatting_recyclerview);



        //체크박스 세팅
        chat_alarm_checkbox = findViewById(R.id.alarm_checkbox_chatting);
        chatting_text = findViewById(R.id.editText);
        //네비게이션 드로어 선언부  , 드로어는 리니어레이아웃으로 구성되어 있음
        //서버에 방 코드를 입력해서 참여자 정보를 받아옴
        init_navi_items();
    }

    Button navi_editbutton;
    //네비게이션 드로어 init
    @SuppressLint("RestrictedApi")
    public void init_navi_items() {
        navi_editbutton = findViewById(R.id.room_edit_button);
        navi_participant_recyclerview = findViewById(R.id.recyclerview_navi_participant);
        navi_title = findViewById(R.id.navi_title);
        navi_language = findViewById(R.id.navi_language);
        navi_participant = findViewById(R.id.navi_participant);
        chatting_navi_room_exit = findViewById(R.id.chatting_navi_room_exit);
        drawer_layout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        navigation_layout = findViewById(R.id.daw);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toggle = new DrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer_layout.removeDrawerListener(toggle);
        drawer_layout.addDrawerListener(toggle);
        toggle.syncState();
        navigation_layout.setOnCreateContextMenuListener(this);
        get_user_data();
    }
    //네비게이션 드로어의 데이터를 업데이트 하는것
    public void get_user_data(){
        //서버에 방 코드를 보내서 현재 방에 대하 정보를 요청한다. 정보를 받으면 리사이클러뷰와 방정보를 세팅한다.
    }



    //##메세지를 보냈을떄
    //전송버튼 눌렀을때 채팅 텍스트 뷰의 입력문장을 가져온 후 채팅서버에 보낸다.
    //보낸채팅메세지 내용은 없어지도록 한다.
    public void send_message(View v) {
        Log.d(TAG,"메세지보내기버튼클릭");

        String message = chatting_text.getText().toString();
//        List<Integer> read_people = new ArrayList<>();
//        read_people.add(user_idx);  //자신은 읽은상태에서 바로 보내지도록 설정
        long send_timemills = System.currentTimeMillis();
        Date mReDate = new Date(send_timemills);
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatDate = mFormat.format(mReDate);

        //임시 채팅생성
        ChattingModel ChattingModel = new ChattingModel(999999,room_idx,user_idx,"채팅"+"[pending]",String.valueOf(user_idx),nickname,message,formatDate,send_timemills);
        ChatClientIO.emit_socket(ChattingActivity.this,"send_message",gson.toJson(ChattingModel), new Ack() {
            @Override
            public void call(Object... args) {
                //args[0]:콜백내용  args[1]:서버에서 보낸메시지
                Log.d(TAG,"[callback]"+args[0]+"/"+args[1]);
//                Log.d(TAG,"[callback]"+args[2]);
                switch (String.valueOf(args[0])){
                    case "[success]메세지보내기" :   //메세지를 보내고 나서 성공적으로 메세지를 보냈다고 서버에게 응답을 받는다면 채팅메세지를 업데이트
                        ChattingModel chattingModel = gson.fromJson(args[1].toString(),ChattingModel.class);
//                        ChattingUsersRead chattingUsersRead = gson.fromJson(args[2].toString(),ChattingUsersRead.class);
//                        Log.d(TAG,chattingUsersRead.getRead_items().size()+"사이즈");
                        ChattingAdapter.set_message_success(chattingModel);
                        break;
                    case "[server_error]메세지보내기":
                        ChattingAdapter.set_message_error(Long.parseLong(args[1].toString()));
                        break;
                }

            }
        });

        ChattingAdapter.add_front_message(ChattingModel);

        //TODO 문제 채팅저장부분
        sharedSettings_chat.set_something_string(String.valueOf(ChattingModel.getChatroom_idx()),gson.toJson(ChattingModel));

        chat_recyclerview.scrollToPosition(ChattingAdapter.getChat_data().size()-1);
        chatting_text.setText("");
    }


    //##메세지를 받았을떄
    private BroadcastReceiver MessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra("message");
            Log.d("리시버", "메세지 받음" + msg);
            ChattingModel ChattingModel = gson.fromJson(msg, ChattingModel.class);

            ChattingAdapter.add_message(ChattingModel);
            chat_recyclerview.scrollToPosition(ChattingAdapter.getChat_data().size()-1);


        }};
    //##메세지를 받았을떄
    private BroadcastReceiver joinroomReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int read_last_idx = Integer.parseInt(intent.getStringExtra("read_last_idx"));
            int user_idx = Integer.parseInt(intent.getStringExtra("user_idx"));

            ChattingAdapter.change_message_user_in(read_last_idx,user_idx);


        }};
    //##소켓이 연결되어 서버에게서 메시지를 받아온다.
    private BroadcastReceiver connectReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            join_room_to_server();


        }};


public void join_room_to_server(){
    //소켓이 연결되어있을경우에만 업데이트
    Log.d("join_room",socket.connected()+"");

    if(socket.connected()) {
        ChatClientIO.emit_socket(ChattingActivity.this,"join_room", room_idx,new AckWithTimeOut(3000){
                    @Override
                    public void call(Object... args) {
                        if(args!=null){
                            if(args[0].toString().equalsIgnoreCase("No Ack")){
                                Log.d(TAG,"응답 없음");
                            }else{
                                Log.d(TAG, args[0].toString());
                                Type type = new TypeToken<List<ChattingModel>>() {
                                }.getType();
                                sharedSettings_chat.set_something_string("room_idx" + room_idx, args[0].toString());
                                ArrayList<ChattingModel> chat_data = gson.fromJson(args[0].toString(), type);
                                ChattingAdapter.setChat_data(chat_data);
                                //입장하는 순간 방 idx를 저장한다.
                                current_room_idx = room_idx;
//                                sharedSettings_chat.set_something_int("current_room_idx", room_idx);
                            }}
                    }
                }
        );
    }
}
    //생명주기-----------------------
    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onStart() {
        super.onStart();
        //유저 idx함께 보내준다.
        //저장되어 있던 채팅데이터 및 채팅방 데이터를 받아온다.
        Type type = new TypeToken<List<ChattingModel>>() {}.getType();

        if(!sharedSettings_chat.get_something_string("room_idx"+room_idx).equals("없음")) {
            chat_data = gson.fromJson(sharedSettings_chat.get_something_string("room_idx" + room_idx), type);
        }
        else{
            chat_data = new ArrayList<>();
        }
        //TODO 서버와 통신해서 ChatroomModel 불러와야 한다. or 쉐어드
        RoomModel roomModel = new RoomModel(1,1,"일반",3);
        ChattingAdapter = new ChattingAdapter(this,chat_data,user_idx,roomModel);
        chat_recyclerview.setAdapter(ChattingAdapter);
        chat_recyclerview.setLayoutManager(new LinearLayoutManager(this));

        //TODO 현재 데이터 전체를 가져오는데 나중엔 부분적으로 업데이트된 메시지만 가져오도록 하는게 바람직할듯
        //통신을 통해서 추가적으로 데이터 전체를 가져온다.
        join_room_to_server();
    }
    @Override
    protected void onPause() {
        super.onPause();

    }
    @Override
    protected void onStop() {
        super.onStop();
        ChatClientIO.emit_socket(ChattingActivity.this,"leave_room", room_idx, new Ack() {
            @Override
            public void call(Object... args) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

}
