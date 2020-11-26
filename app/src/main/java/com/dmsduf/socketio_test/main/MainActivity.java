package com.dmsduf.socketio_test.main;

import android.app.ActivityGroup;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.dmsduf.socketio_test.FriendAcitvity;
import com.dmsduf.socketio_test.R;
import com.dmsduf.socketio_test.SharedSettings;
import com.dmsduf.socketio_test.chat.ChatClientIO;
import com.dmsduf.socketio_test.chat.ChatRoomActivity;


public class MainActivity extends ActivityGroup {

    String TAG = "메인액티비티";
    TabHost main_tab_host;


    SharedSettings shared_settings;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //유저 닉네임 ,idx 가져오기
        SharedSettings shared_settings = new SharedSettings(this,"user_info");
        int user_idx = shared_settings.get_something_int("user_idx");
        String user_nickname = shared_settings.get_something_string("user_nickname");


        setupTabHost();

        setup_tab(new TextView(this), "친구");
        setup_tab(new TextView(this),"채팅");


        main_tab_host.setCurrentTab(0);
        //토큰저장
//        Log.d(tag_firebase,"온크리에잇에서 토큰저장메소드시작");
//        updateToken(FirebaseInstanceId.getInstance().getToken());
        init_socketio_service();

    }
    //소켓아이오를 연결하는 채팅 서비스를 시작한다.
    public void init_socketio_service(){

        Intent intent = new Intent(this, ChatClientIO.class);
        this.startService(intent);

    }
    private void setupTabHost()
    {

        main_tab_host =findViewById(R.id.tab_host) ;
        main_tab_host.setup(this.getLocalActivityManager());
    }
    private void setup_tab(final View view, final String tag)
    {
        View tabview = createTabView(main_tab_host.getContext(), tag);

        // TabSpec은 공개된 생성자가 없으므로 직접 생성할 수 없으며, TabHost의 newTabSpec메서드로 생성
        TabHost.TabSpec spec = main_tab_host.newTabSpec(tag).setIndicator(tabview);

        if(tag.equals("친구"))
            spec.setContent(new Intent(this, FriendAcitvity.class));
        else if(tag.equals("채팅"))
            spec.setContent(new Intent(this, ChatRoomActivity.class));


        main_tab_host.addTab(spec);

    }

    // Tab에 나타날 View를 구성
    private static View createTabView(final Context context, final String text)
    {

        // layoutinflater를 이용해 xml 리소스를 읽어옴
        View view = LayoutInflater.from(context).inflate(R.layout.tab_layout, null);
        ImageView img;

        if(text.equals("홈"))
        {

            img = view.findViewById(R.id.tab_image);
            img.setImageResource(R.drawable.tab_select_home);

        }
        else if(text.equals("QnA"))
        {
            img = view.findViewById(R.id.tab_image);
            img.setImageResource(R.drawable.tab_select_qna);
        }
        else if(text.equals("채팅"))
        {
            img = view.findViewById(R.id.tab_image);
            img.setImageResource(R.drawable.tab_select_chat);
        }
        else if(text.equals("설정"))
        {
            img = view.findViewById(R.id.tab_image);
            img.setImageResource(R.drawable.tab_select_mypage);
        }
        TextView tv =  view.findViewById(R.id.tab_name);
        tv.setText(text);
        return view;
    }


    @Override
    protected void onStart() {
        super.onStart();




    }
    //TODO FCM
//파이어베이스에 내토큰을 저장한다
    String my_key;
    public static String tag_firebase = "파이어베이스 메세지서비스";
//    public void updateToken(String token) {
//                        Log.d(tag_firebase ,"토큰받음"+token);
//                        my_key=shared_settings.get_something_string("user_email").split("@")[0];
//                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tokens");
//                        notification_token mToken = new notification_token(token);
//                        Log.d(tag_firebase,"메인액티비티에서 내 토큰을 파이어베이스에저장: "+my_key);
//                        ref.child(my_key).setValue(mToken);
//
//                        // Log and toast
//
//    }


}

