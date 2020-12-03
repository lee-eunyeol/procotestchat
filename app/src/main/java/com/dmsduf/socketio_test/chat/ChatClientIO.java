package com.dmsduf.socketio_test.chat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.dmsduf.socketio_test.Notification_EY;
import com.dmsduf.socketio_test.SharedSettings;
import com.dmsduf.socketio_test.data_list.ChattingModel;
import com.dmsduf.socketio_test.data_list.UserModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

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
    OkHttpClient okHttpClient;

    String TAG = "client-io_service";
    String S2C = "server_to_client";
    String url_local = "http://192.168.56.1:3001";
    String url_EY = "https://15.165.252.235:3001";
    String url_iwinv =  "http://49.247.214.168:3001";
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
        opts.transports = new String[]{WebSocket.NAME};
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
                emit_socket("user_login", gson.toJson(new UserModel(sharedSettings.get_something_string("user_nickname"), sharedSettings.get_something_int("user_idx"))), new Ack() {
                    @Override
                    public void call(Object... args) {

                    }
                });
                Log.d(TAG,is_chatroom+"연결됐을때 채팅방에있는지?");
                //만약 어떤 채팅방에 들어온 상태에서 소켓연결이 됬을경우 방 참여도 해준다.
                ActivityManager mngr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                String topstack_name = mngr.getAppTasks().get(0).getTaskInfo().topActivity.getShortClassName();
                if(topstack_name.equals(".chat.ChattingActivity")){

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


    }

    public void init_socket_events() {
        Log.d(TAG, "socketinit");
        //메세지 받기
        //채팅방/앱 나감 or 채팅화면이 아님에 따라 변경하기/채팅방목록
        //엑티비티 매니져 현재 최상단 스택의 위치를 알기위해 사용
        ActivityManager mngr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        socket.on(S2C + "message", args -> {
            Log.d(TAG, "메세지받음!" + (String) args[0]);
            String data = (String) args[0];
            ChattingModel chattingModel = gson.fromJson(data, ChattingModel.class);

            //새로 받은 메시지를 쉐어드에 추가한다.
            save_chat_data(chattingModel);

            //현재 유저가 들어가 있는 채팅방 idx를 검사한다.
            int current_user_room = sharedSettings.get_something_int("current_room_idx");
            String topstack_name = mngr.getAppTasks().get(0).getTaskInfo().topActivity.getShortClassName();
            Log.d(TAG, "최상단 스택: " + topstack_name);

            //시점에 따라 푸시메시지를 보낼지 , 채팅방 목록을 업데이트 할지 , 푸시알람을 보낼지 선택 하도록 한다.
            if (chattingModel.getRoom_idx() == current_user_room && topstack_name.equals(".chat.ChattingActivity")) {    //현재 채팅방 안에 있고 ,받은 메세지가 현 채팅방에 온거라면 채팅메세지업데이트리시버
                Log.d(TAG, "채팅방에 있어서 채팅창 업데이트");
                Intent intent = new Intent("go_chatingroom");
                intent.putExtra("message", data);
                LocalBroadcastManager.getInstance(ChatClientIO.this).sendBroadcast(intent);
            }
            //채팅방안에는 없지만 소켓이 연결되어있고 채팅목록을 보고있는 상태라면 채팅방 목록 업데이트
            else if ( is_chatroom && socket.connected() ) {
                Log.d(TAG, "채팅방목록업데이트");
                Intent intent = new Intent("go_chatroom");
                intent.putExtra("message", data);
                LocalBroadcastManager.getInstance(ChatClientIO.this).sendBroadcast(intent);


                //메시지를 받은 채팅방안에는 없지만 소켓이 연결되어있고 채팅목록 , 채팅방에도 없다면 노티피케이션
            } else if (!is_chatroom && socket.connected()) {
                Log.d(TAG, "노티피케이션주기");
                notification.show_notification("ChattingActivity", chattingModel);
            }


        });
        //다른사람이 채팅방에 참여했다는 알림을 준다.
        socket.on(S2C + "user_in_room", args -> {
            Intent intent = new Intent("user_join");
            //가장 최근에 받은 메시지 idx를 보내기
            intent.putExtra("read_last_idx", args[0].toString());
            LocalBroadcastManager.getInstance(ChatClientIO.this).sendBroadcast(intent);


        });

    }
    //메시지를 받았을때 , 채팅 메시지를 알맞게 저장하는 메소드
    private void save_chat_data(ChattingModel chattingModel) {
        int room_idx = chattingModel.getRoom_idx();
        Type type = new TypeToken<List<ChattingModel>>() {}.getType();
        ArrayList<ChattingModel> chat_datas = new ArrayList<>();
        //그 방에대한 메시지 내역이 저장되어 있는경우
        if(!sharedSettings.get_something_string("room_idx"+room_idx).equals("없음")) {
            chat_datas = gson.fromJson(sharedSettings.get_something_string("room_idx" + room_idx), type);
            chat_datas.add(chattingModel);
        }
        //쉐어드에 저장된 메시지가 전혀없을경우 ( 첫메시지일경우)
        else{
            chat_datas.add(chattingModel);
        }
        //새로 온 메시지 추가
        sharedSettings.set_something_string("room_idx" + room_idx,gson.toJson(chat_datas));

    }

    public static void emit_socket(String guide, Object object, Ack ack) {
        String C2S = "client_to_server";
        String TAG = "socket.emit";
        Log.d("socket.emit!", guide + "::" + object.toString());
        switch (guide) {
            case "user_login":
                socket.emit(C2S + "user_login", object);
                break;
            case "user_logout":
                socket.emit(C2S + "user_logout", object);
            case "join_room":
                Log.d(TAG,"joinroom");
                socket.emit(C2S + "join_room", object,ack);
                break;

            case "leave_room":
                if (socket != null && socket.connected()) {
                    socket.emit(C2S + "leave_room", object);
                }
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
