package com.dmsduf.socketio_test.chat;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.dmsduf.socketio_test.Notification_EY;
import com.dmsduf.socketio_test.R;
import com.dmsduf.socketio_test.SharedSettings;
import com.dmsduf.socketio_test.data_list.ChatRoomModel;
import com.dmsduf.socketio_test.data_list.ChattingModel;
import com.dmsduf.socketio_test.data_list.SharList;
import com.dmsduf.socketio_test.data_list.UserChatModel;
import com.dmsduf.socketio_test.data_list.UserModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.transports.WebSocket;
import okhttp3.OkHttpClient;

public class ChatClientIO extends Service {
    public static Socket socket;
    public static Boolean is_chatroom = false;  //채팅방 목록을 보고있는지 보고있지 않은지 확인 하는변수 MainActivty에서 사용함
    public static Boolean is_mainfriend = false;
    public static Boolean is_chatting_room = false;
    public static int current_room_idx = -1; //현재 들어와있는 채팅방의 위치
    OkHttpClient okHttpClient;

    String TAG = "client-io_service";
    String S2C = "server_to_client";
    String url_local = "http://192.168.56.1:3001";
    String url_EY = "https://15.165.252.235:3001";
    String url_iwinv = "http://49.247.214.168:3001";
    Notification_EY notification;
    SharedSettings sharedSettings;

    public static Gson gson;

    public ChatClientIO() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "oncreate 서비스");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy 서비스가 꺼짐");
        //소켓이 연결되어있는 상태라면 소켓 연결을 끊도록 한다.
        if (socket.connected()) {
            socket.disconnect();
        }
        ;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand 서비스시작");
        notification = new Notification_EY(this);
        sharedSettings = new SharedSettings(this, "user_info");

//        sharedSettings.clear();

        gson = new Gson();

        init_default_SSL(); //SSL관련 (https)

        init_socket_settings(); //소켓연결준비

        init_default_events(); //기본적인 소켓 연결관련 함수선언

        init_socket_events(); //채팅관련 소켓 이벤트관련 함수선언

        socket.connect();


        return super.onStartCommand(intent, flags, startId);

    }

//    public void initIO() {
//
//        sharedSettings = new SharedSettings(this,"user_info");
//        gson = new Gson();
//        init_default_SSL(); //SSL관련 (https)
//
//        init_socket_settings(); //소켓연결준비
//
//        init_default_events(); //기본적인 소켓 연결관련 함수선언
//
//        init_socket_events(); //채팅관련 소켓 이벤트관련 함수선언
//
//        socket.connect();
//
//    }

    @SuppressLint("TrustAllX509TrustManager")
    private final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[]{};
        }

        public void checkClientTrusted(X509Certificate[] chain,
                                       String authType) {
        }

        public void checkServerTrusted(X509Certificate[] chain,
                                       String authType) {
        }
    }};

    //SSL 관련 선언
    public void init_default_SSL() {
        SSLContext sslcontext = null;
        try {
            sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(null, trustAllCerts, null);
        } catch (
                NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        HostnameVerifier myHostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        okHttpClient = new OkHttpClient.Builder()
                .hostnameVerifier(myHostnameVerifier)
                .sslSocketFactory(sslcontext.getSocketFactory(), (X509TrustManager) trustAllCerts[0]).build(); // default settings for all sockets

    }

    public void init_socket_settings() {

        IO.setDefaultOkHttpWebSocketFactory(okHttpClient);
        IO.setDefaultOkHttpCallFactory(okHttpClient);
        IO.Options opts = new IO.Options();

        String tokenuser1 = getResources().getString(R.string.token1);
        String tokenuser2 =  getResources().getString(R.string.token);;
        String token76 = getResources().getString(R.string.token76);


        sharedSettings.set_something_string("token3", tokenuser2);
        opts.query = "token=" + tokenuser2;

        opts.transports = new String[] { WebSocket.NAME };


        try {

            socket = IO.socket(url_local, opts);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    //소켓IO 기본적인 연결이벤트 ON 및 소켓연결
    public void init_default_events() {


        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d(TAG, "EVENT_CONNECT");
                Log.d(TAG, "연결되었습니다!!");

                Log.d(TAG, is_chatroom + "연결됐을때 채팅방에있는지?");
                //만약 어떤 채팅방에 들어온 상태에서 소켓연결이 됬을경우 방 참여도 해준다. 
                ActivityManager mngr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                String topstack_name = mngr.getAppTasks().get(0).getTaskInfo().topActivity.getShortClassName();
                if (topstack_name.equals(".chat.ChattingActivity")) {

                    Intent intent = new Intent("socket_connected");
                    LocalBroadcastManager.getInstance(ChatClientIO.this).sendBroadcast(intent);

                }
            }
        });
        socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d(TAG, " EVENT_DISCONNECT: disconnected from the server");
            }
        });
        socket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d(TAG, "EVENT_CONNECT_ERROR");


            }
        });

        socket.on(Socket.EVENT_CONNECT_TIMEOUT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d(TAG, "EVENT_CONNECT_TIMEOUT");
            }
        });
        socket.on(Socket.EVENT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d("서버응답에러", args[0].toString());
            }
        });

    }


    public void init_socket_events() {
        Log.d(TAG, "socketinit");
        //메세지 받기
        //채팅방/앱 나감 or 채팅화면이 아님에 따라 변경하기/채팅방목록
        //엑티비티 매니져 현재 최상단 스택의 위치를 알기위해 사용
        ActivityManager mngr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        String topstack_name = mngr.getAppTasks().get(0).getTaskInfo().topActivity.getClassName();
        //상대방 카드가 업데이트 됬을때 받는 이벤트
        socket.on(S2C+"update_card",args -> {
           Log.d(TAG,"카드 업데이트"+args[0].toString());
        });
        socket.on(S2C+"banish_user",args -> {
           Log.d("banish_user","추방당했어요;;"+args[0].toString());
           socket.emit("client_to_serverbanished_room",args[0].toString());
        });
        //소켓연결완료시  받는 이벤트
        socket.on(S2C + "connect_complete", args -> {
            //
            //채팅방정보저장
            Log.d("ChattingModel",args[1].toString());
            Log.d("ChatRoomModel",args[0].toString());
            if (!args[0].toString().equals("no_chatrooms")) {
                Type chatroom_type = new TypeToken<List<ChatRoomModel>>() {
                }.getType();
                ArrayList<ChatRoomModel> chatRoomModels = gson.fromJson(args[0].toString(), chatroom_type);
                for (ChatRoomModel chatRoomModel : chatRoomModels) {
                    sharedSettings.set_chatroom_info(String.valueOf(chatRoomModel.getIdx()), gson.toJson(chatRoomModel));
                }

            }
            else{Log.d(TAG,"받은채팅방정보없음");}
            //메시지내역저장
            if (args[1].toString().equals("no_messages")) {
            Log.d(TAG,"받은메시지정보없음");
            }
            else {
                try {
                    JSONObject jsonObject = new JSONObject(args[1].toString());
                    Iterator i = jsonObject.keys();
                    while (i.hasNext()) {
                        String key = i.next().toString();
                        sharedSettings.set_chatroom_message(key, jsonObject.get(key).toString());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
        String C2S = "client_to_server";

        socket.on(S2C + "message", args -> {
            Log.d(TAG, "메세지받음!" + (String) args[0]);

            String data = (String) args[0];
            ChattingModel chattingModel = gson.fromJson(data, ChattingModel.class);
           //----
            //메시지온사람 idx업데이트
            if(chattingModel.getUser_idx()==-3){}
            else{
            ChatRoomModel chatRoomModel = gson.fromJson(sharedSettings.get_chatroom_info(String.valueOf(chattingModel.getChatroom_idx())),ChatRoomModel.class);
            chatRoomModel.getuser(chattingModel.getUser_idx()).setRead_last_idx(chattingModel.getIdx());

            if (chattingModel.getChatroom_idx() == current_room_idx && is_chatting_room) {
                Log.d(TAG,"채팅방보고있어서 내꺼 업데이트");
                chatRoomModel.getuser(sharedSettings.get_something_int("user_idx")).setRead_last_idx(chattingModel.getIdx());
                socket.emit(C2S+"user_read",gson.toJson(chatRoomModel.getuser(sharedSettings.get_something_int("user_idx"))),chattingModel.getChatroom_idx(),true);
            }
           sharedSettings.set_chatroom_info(String.valueOf(chatRoomModel.getIdx()),gson.toJson(chatRoomModel));
            }
            //새로 받은 메시지를 쉐어드에 추가한다.
            save_chat_data(chattingModel);

            //현재 유저가 들어가 있는 채팅방 idx를 검사한다.


            Log.d(TAG, "최상단 스택: " + topstack_name);

            //시점에 따라 푸시메시지를 보낼지 , 채팅방 목록을 업데이트 할지 , 푸시알람을 보낼지 선택 하도록 한다.
            if (chattingModel.getChatroom_idx() == current_room_idx && is_chatting_room) {    //현재 채팅방 안에 있고 ,받은 메세지가 현 채팅방에 온거라면 채팅메세지업데이트리시버
                Log.d(TAG, "채팅방에 있어서 채팅창 업데이트");
                Intent intent = new Intent("go_chatingroom");
                intent.putExtra("message", data);
                LocalBroadcastManager.getInstance(ChatClientIO.this).sendBroadcast(intent);
            }
            //채팅방안에는 없지만 소켓이 연결되어있고 채팅목록을 보고있는 상태라면 채팅방 목록 업데이트
            else if (is_chatroom && socket.connected()) {
                Log.d(TAG, "채팅방목록업데이트");
                Intent intent = new Intent("go_chatroom");
                intent.putExtra("message", data);
                LocalBroadcastManager.getInstance(ChatClientIO.this).sendBroadcast(intent);


                //메시지를 받은 채팅방안에는 없지만 소켓이 연결되어있고 채팅목록 , 채팅방에도 없다면 노티피케이션
            } else if (!is_chatroom && socket.connected() && !is_chatting_room) {
                Log.d(TAG, "노티피케이션주기");
                notification.show_notification("ChattingActivity", chattingModel);
            }


        });


        //다른사람이 채팅방에 참여했다는 알림을 준다.
        socket.on(S2C + "user_read", args -> {
            Log.d(TAG,"이사람 읽었으니 메시지 업데이트해줄것"+args[0].toString());
            String chatroom_idx = args[1].toString();
            UserChatModel userChatModel = gson.fromJson(args[0].toString(),UserChatModel.class);
            update_read_last_idx(chatroom_idx,userChatModel);
            //만약 지금 업데이트된 채팅방을 보고 있다면 메시지를 실시간으로 업데이트 합니다.
            if (is_chatting_room && current_room_idx == Integer.parseInt(chatroom_idx)) {
                int myindex = sharedSettings.get_something_int("user_idx");
            Log.d(TAG,"내가 채팅방을 읽고 있었기때문에 나도 최신메시지idx를 업데이트 합니다."+"내인덱슽는"+myindex);
            UserChatModel userChatModel1 = gson.fromJson(sharedSettings.get_chatroom_info(chatroom_idx),ChatRoomModel.class).getuser(myindex);
            update_read_last_idx(chatroom_idx,userChatModel1);

            }
        });
        //친구들중 한명의 상태가 바뀌었을때 알림받는곳
        socket.on(S2C + "update_user_state", args -> {

            Log.d(TAG, "어떤사람이 업데이트되었어용" + is_mainfriend);
            if (is_mainfriend) {

                Intent intent = new Intent("update_user");
                //가장 최근에 받은 메시지 idx를 보내기
                intent.putExtra("state", args[0].toString());
                intent.putExtra("update_content", args[1].toString());
                LocalBroadcastManager.getInstance(ChatClientIO.this).sendBroadcast(intent);
            }


        });


    }
    //불러온 userchatmodel을 알맞게 읽음처리
    private void update_read_last_idx(String chatroom_idx,UserChatModel userChatModel){
        ChatRoomModel chatRoomModel = gson.fromJson(sharedSettings.get_chatroom_info(chatroom_idx),ChatRoomModel.class);
        chatRoomModel.setuser(userChatModel);
        sharedSettings.set_chatroom_info(chatroom_idx,gson.toJson(chatRoomModel));
    }

    //메시지를 받았을때 , 채팅 메시지를 알맞게 저장하는 메소드
    private void save_chat_data(ChattingModel chattingModel) {
        int room_idx = chattingModel.getChatroom_idx();
        Type type = new TypeToken<List<ChattingModel>>() {
        }.getType();
        ArrayList<ChattingModel> chat_datas = new ArrayList<>();

        //메시지 읽음/안읽음 로직
        //처음 메시지를 받았을떄 채팅방에 있는 상태가 아니라면  안읽은메시지로 저장이 된다.
        //그 방에대한 않읽은 메시지 내역이 저장되어 있는경우 안읽은 메시지를추가 한다.
        if (!sharedSettings.get_chatroom_messages(String.valueOf(room_idx)).equals("없음")) {
            Log.d(TAG, "받은거" + sharedSettings.get_chatroom_messages(String.valueOf(room_idx)));
            chat_datas = gson.fromJson(sharedSettings.get_chatroom_messages(String.valueOf(room_idx)), type);
            chat_datas.add(chattingModel);
        }
        //쉐어드에 저장된 메시지가 전혀없을경우 (첫메시지일경우)
        else {
            chat_datas.add(chattingModel);
        }
        //새로 온 메시지 추가
        sharedSettings.set_chatroom_message(String.valueOf(room_idx), gson.toJson(chat_datas));

    }

    public static void emit_socket(Context context, String guide, Object object, Ack ack) {
        if (socket.connected()) {
            String C2S = "client_to_server";
            String TAG = "socket.emit";

            Log.d("socket.emit!", guide + "::" + object.toString());
            switch (guide) {
                case "user_login":
                    socket.emit(C2S + "user_login", object, ack);

                    break;
                case "user_logout":
                    socket.emit(C2S + "user_logout", object);
                case "join_room":
                    Log.d(TAG, "joinroom");
                    socket.emit(C2S + "join_room", object, ack);
                    break;

                case "outfocus_room":
                    socket.emit(C2S + "outfocus_room", object);
                    break;
                case  "leave_room":
                    socket.emit(C2S+"leave_room",object,ack);
                    break;
                case "send_message":
                    socket.emit(C2S + "message", object, ack);
                    break;
                case "check_room":
                    socket.emit(C2S + "check_room", object);
                    break;
                case "disconnect_socket":
                    if (socket != null && socket.connected()) {
//                    socket.emit(C2S + "leave_room", object);
                        socket.disconnect();
                        socket.close();
                    }
                    break;
                case "update_user_state":
                    socket.emit(C2S + "update_user_state", object, ack);
                    break;
                case "get_rooms":
                    socket.emit(C2S + "get_rooms", object, ack);
                    break;
                case "make_chatroom":
                    socket.emit(C2S + "make_chatroom", object, "1");
                    break;
            }
        } else {
            Log.d("소켓연결", "소켓연결이 끊긴상태!");
        }
    }

    ;

    //안드로이드에서 메세지 보내기 C 는 client S는 server
    public void send_message(String message) {

    }


    //    //소켓객체 연결끊기
//    public void leave_room(String room_idx) {
//
//    }
//    public  void join_room(String room_idx){
//
//
//    }


//    //채팅관련 인터페이스
//    public interface chat_Interface {
//        void get_message(String message);
//
//        void join_room(String room_num);
//
//    }


    //--------------------------------------------

}