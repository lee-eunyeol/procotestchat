package com.dmsduf.socketio_test.chat;


import android.annotation.SuppressLint;
import android.app.ActivityManager;
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


import com.dmsduf.socketio_test.R;
import com.dmsduf.socketio_test.SharedSettings;
import com.dmsduf.socketio_test.adapter.ChattingAdapter;
import com.dmsduf.socketio_test.adapter.ChattingNaviAdapter;
import com.dmsduf.socketio_test.data_list.ChattingModel;
import com.dmsduf.socketio_test.data_list.RoomModel;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.socket.client.Ack;

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

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_room);
        //유저 정보 들어간 쉐어드
        sharedSettings = new SharedSettings(this,"user_info");
        Intent intent = getIntent();
        nickname = sharedSettings.get_something_string("user_nickname");
        room_idx = intent.getIntExtra("room_idx",3853432);
        user_idx = sharedSettings.get_something_int("user_idx");
        gson = new Gson();


        //채팅관련 브로드케스트리시버등록
        LocalBroadcastManager.getInstance(this).registerReceiver(MessageReceiver, new IntentFilter("go_chatroom"));

        chat_recyclerview = findViewById(R.id.chatting_recyclerview);

        //유저 idx함께 보내준다.
        chat_data = new ArrayList<>();

        //TODO 서버와 통신해서 ChatroomModel 불러와야 한다. or 쉐어드
        RoomModel roomModel = new RoomModel(1,1,"일반",2);
        ChattingAdapter = new ChattingAdapter(this,chat_data,user_idx,roomModel);
        chat_recyclerview.setAdapter(ChattingAdapter);
        chat_recyclerview.setLayoutManager(new LinearLayoutManager(this));

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
//        ActivityManager mngr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
//        Log.d(TAG, "현재 최상단 스택" +  mngr.getAppTasks().get(0).getTaskInfo().topActivity.toString());
        String message = chatting_text.getText().toString();
        List<Integer> read_people = new ArrayList<>();
        read_people.add(user_idx);  //자신은 읽은상태에서 바로 보내지도록 설정
        long send_timemills = System.currentTimeMillis();
        Date mReDate = new Date(send_timemills);
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatDate = mFormat.format(mReDate);
        ChattingModel ChattingModel = new ChattingModel(room_idx,user_idx,"채팅"+"[pending]",nickname,message,formatDate,read_people,send_timemills);
        ChatClientIO.emit_socket("send_message",gson.toJson(ChattingModel), new Ack() {
            @Override
            public void call(Object... args) {
                //args[0]:콜백내용  args[1]:서버에서 보낸메시지
                Log.d(TAG,"[callback]"+args[0]+"/"+args[1]);
                switch (String.valueOf(args[0])){
                    case "[success]메세지보내기" :   //메세지를 보내고 나서 성공적으로 메세지를 보냈다고 서버에게 응답을 받는다면 채팅메세지를 업데이트
                        ChattingModel chattingModel = gson.fromJson(args[1].toString(),ChattingModel.class);
                        ChattingAdapter.set_message_success(chattingModel);
                        break;
                    case "[server_error]메세지보내기":
                        ChattingAdapter.set_message_error(Long.parseLong(args[1].toString()));
                        break;
                }

            }
        });

        ChattingAdapter.add_front_message(ChattingModel);

        sharedSettings.change_file("chatrooms");
        sharedSettings.set_something_string(String.valueOf(ChattingModel.getRoom_idx()),gson.toJson(ChattingModel));


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


        }};



    //생명주기-----------------------
    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onStart() {
        super.onStart();
        //채팅방 알람설정메소드
        //리시버에서 채팅을 받았을떄 이 recive_position에 따라서 채팅방을 업데이트 할지 채팅방 목록을 업데이트 할지 노티피케이션을띄울지 정할수 있게 구분자를 줌
        //onstart에서 chatting이라는 값을 줘서 리시버에서는 채팅방을 업데이트 할수 있도록함

//채팅방에 접속하자마자 알림 이걸바인드서비스에서 해줘야할것같은데
        Log.d(TAG, "소켓상태");
        //네비게이션 드로어 업데이트
        get_user_data();
    }
    @Override
    protected void onPause() {
        super.onPause();

    }
    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ChatClientIO.emit_socket("leave_room", room_idx, new Ack() {
            @Override
            public void call(Object... args) {

            }
        });

    }

}
