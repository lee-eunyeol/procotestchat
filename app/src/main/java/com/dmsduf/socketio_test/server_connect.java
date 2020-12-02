package com.dmsduf.socketio_test;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


//서버통신 목적용 클래스
//volley를 이용하였으며 ,대부분 post요청을 함
//주로 서버의 데이터베이스에 접근하여 데이터를 요청 또는 수정을 하는데 이용한다.


public class server_connect {
    String TAG = "서버 통신";
    Context context;
    //서버url
    String url = "https://volae.ga/";
    Gson gson;
    //예를들어 내가 다른클래스에서 server_connect 클래스를 사용할때,
    //post형식으로 서버에 데이터를 전달 하기위해 사용하는 변수
    Map<String, String> post_items;

    //주로 서버 통신이 완료된 이후 ui를 업데이트 해야할 때 사용하는 변수,
    //예를 들어 채팅방의 채팅내역을 불러올때 채팅내역데이터를 받는 서버통신이 완료된후 어댑터를 다시 갱신해줘야하기 떄문에
    //chatting_room 클래스의 리사이클러뷰 어댑터를 object로 전달해주어 onresponse에서 어댑터를 다시 갱신할때 이용한다.
    Object object;

    //쉐어드에 저장되어있는 유저의 정보를 불러올때 사용하는 클래스로 이메일과 닉네임을 불러올때 사용한다.
    SharedSettings SharedSettings;
    public server_connect(Context context) {
        this.context = context;
        SharedSettings = new SharedSettings(context, "user_info");
    }
    //post방식으로 서버 요청하는 메소드
    public void send_post_request(String directory, final Map<String, String> post_items, final Object object, final String kinds) {
        gson = new Gson();
        String request_url = url + directory;
        Log.d(TAG, "요청url: " + request_url);
        RequestQueue queue = Volley.newRequestQueue(context);
        this.object = object;
        this.post_items = post_items;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, request_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, kinds + ":" + response);
                        //kinds는 서버에서 어떤 종류의 post요청에 대한 응답인지 알려주는 변수
                        switch (kinds) {
                            case "유저정보받기":

                                break;

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "서버통신에러" + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = post_items;
                return params;
            }
        };
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }
    //채팅방 방장이 유저를 추방하기 위하여 서버에 유저닉네임과 채팅방 번호를 보낸 후 디비에서 채팅방 번호에 맞는채팅방을 찾아 유저정보를 제거함



}
