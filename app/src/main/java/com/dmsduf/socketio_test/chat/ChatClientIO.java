package com.dmsduf.socketio_test.chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.dmsduf.socketio_test.SharedSettings;
import com.dmsduf.socketio_test.data_list.ChattingModel;
import com.dmsduf.socketio_test.data_list.UserModel;
import com.google.gson.Gson;

import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.transports.WebSocket;
import okhttp3.OkHttpClient;

public class ChatClientIO {
    public static Socket socket;
    OkHttpClient okHttpClient;
    Context context;
    String TAG = "chat_client_io";
    String S2C = "server_to_client";
    String url_local = "http://192.168.56.1:5000";
    String url_EY = "https://15.165.252.235:3001";
    SharedSettings sharedSettings;
    public static Gson gson;



    public void initIO(Context context) {
        this.context = context;
        sharedSettings = new SharedSettings(context,"user_info");
        gson = new Gson();
        init_default_SSL(); //SSL관련 (https)

        init_socket_settings(); //소켓연결준비

        init_default_events(); //기본적인 소켓 연결관련 함수선언

        init_socket_events(); //채팅관련 소켓 이벤트관련 함수선언

        socket.connect();

    }

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
                emit_socket("user_login",gson.toJson(new UserModel(sharedSettings.get_something_string("user_nickname"),sharedSettings.get_something_int("user_idx"))));
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

        socket.on(S2C + "message", args -> {
            Log.d(TAG, "메세지받음!" + (String) args[0]);
            String data = (String) args[0];
            ChattingModel chattingModel = gson.fromJson(data,ChattingModel.class);
            //현재 유저가 들어가 있는 채팅방 idx를 검사한다.
            int current_user_room = sharedSettings.get_something_int("current_room_idx");
            //시점에 따라 푸시메시지를 보낼지 , 채팅방 목록을 업데이트 할지 , 푸시알람을 보낼지 선택 하도록 한다.
            if(chattingModel.getRoom_idx()==current_user_room) {          //현재 채팅방 안에 있고 , 접속한 채팅방 idx와 같다면 채팅메세지업데이트리시버
            Intent intent = new Intent("go_chatroom");
            intent.putExtra("message", data);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });
    }

    public static void emit_socket(String guide, Object object) {
        String C2S = "client_to_server";

        Log.d("socket.emit!",guide+"::"+object.toString());
        switch (guide) {
            case "user_login":
                socket.emit(C2S+"user_login",object);
                break;
            case "user_logout":
                socket.emit(C2S+"user_logout",object);
            case "join_room":
                socket.emit(C2S + "join_room", object);
                break;

            case "leave_room":
                if (socket != null && socket.connected()) {
                    socket.emit(C2S + "leave_room", object);
                }
                break;
            case "send_message":
                socket.emit(C2S + "message", object);
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
