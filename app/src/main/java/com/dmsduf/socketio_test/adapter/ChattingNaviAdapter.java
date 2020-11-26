package com.dmsduf.socketio_test.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.dmsduf.socketio_test.R;
import com.dmsduf.socketio_test.data_list.RoomModel;
import com.dmsduf.socketio_test.SharedSettings;


import java.util.ArrayList;
import java.util.List;


//채팅방의 참여자들을 보여주는 어댑터로써
//채팅방안에서 네비게이션 바 클릭시, 참여자들이 나오도록 함
public class ChattingNaviAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    String TAG = "NAVI_ADAPTER";
    List<String> user_nickname_list;
    List<String> user_email_list;
    RoomModel RoomModel;

    SharedSettings SharedSettings;
    String email;
    String nickname;
    String title;
    String room_code;

    public List<String> getUser_nickname_list() {
        return user_nickname_list;
    }

    public void setUser_nickname_list(List<String> user_nickname_list) {
        this.user_nickname_list = user_nickname_list;
    }
public void remove_user_nickname_list(String nickname){

        for(int i =0 ; i<this.user_nickname_list.size();i++){
            if (this.user_nickname_list.get(i).equals(nickname)){
               this.user_nickname_list.remove(i);
               this.user_email_list.remove(i);
            }
        }


}
public void add_user_nickname_list(String nickname,String email){
        this.user_nickname_list.add(nickname);
        this.user_email_list.add(email);
}
    public ChattingNaviAdapter(Context context, RoomModel RoomModel, String title, String room_code) {
        this.context = context;
        this.RoomModel = RoomModel;
        this.user_nickname_list = new ArrayList<>();
        this.user_email_list = new ArrayList<>();
        this.title = title;
        this.room_code = room_code;
        SharedSettings = new SharedSettings(context, "user_info");
        this.email = SharedSettings.get_something_string("user_email");
        this.nickname = SharedSettings.get_something_string("user_nickname");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_chatting_room_participant, parent, false);

        parti_view_holder viewholder = new parti_view_holder(view);
        return viewholder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((parti_view_holder)holder).chatting_navi_user_name.setText(user_nickname_list.get(position));
        //나일경우 "나"라는 표시를 함
        if (user_nickname_list.get(position).equals(nickname)){
            ((parti_view_holder)holder).chatting_navi_user_me.setVisibility(View.VISIBLE);

        }
        else{
            ((parti_view_holder)holder).chatting_navi_user_me.setVisibility(View.INVISIBLE);
        }
        //방장일경우 왕관모양의 표시를


    }

    @Override
    public int getItemCount() {
        return user_nickname_list.size();
    }
    public class parti_view_holder extends RecyclerView.ViewHolder {
        TextView chatting_navi_user_name;
        TextView chatting_navi_user_me;
        ImageView chatting_navi_room_maker;
        ConstraintLayout chatting_navi_layout;
        public parti_view_holder(@NonNull View itemView) {
            super(itemView);
            chatting_navi_layout = itemView.findViewById(R.id.chatting_navi_layout);
            chatting_navi_room_maker = itemView.findViewById(R.id.chatting_navi_room_maker);
            chatting_navi_user_me = itemView.findViewById(R.id.chatting_navi_user_state);
            chatting_navi_user_name = itemView.findViewById(R.id.chatting_navi_user_name);
        }
    }

    //방장권한을 갖고있는사람이 롱클릭시 선택할 수 있는 기능이있음
    private void show_dialog(int position){
        CharSequence[]  items = {"방장위임","추방하기"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            if(items[which].equals("방장위임")){
                final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
                builder.setTitle("방장위임하기");
                builder.setMessage(user_nickname_list.get(position)+"님에게 방장을 위임하시겠어요?");
                builder.setPositiveButton("아니오",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();

                            }
                        });
                builder.setNegativeButton("예",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                chown_to_user(position,dialog);

                            }
                        });
                builder.show();
            }
            else if(items[which].equals("추방하기")){
                dialog_exile dialog_exile = new dialog_exile(context,position,user_nickname_list.get(position));
                dialog_exile.dialog_open();
            }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
    //유저 추방하기
    public void exile_user(int position,String why_text,Dialog dialog){


    }
    //유저를 추방하게되면 서버에 메세지를 보내어주는 코드
    public void send_to_server_speaker(String where_come,String nickname,String email,String why_text){

    }
    //유저 방장위임
    public void chown_to_user(int position,DialogInterface dialog){

    }
    //방장이 채팅네비게이션드로어에서 추방하기 버튼을 눌렀을때 생기는 다이얼로그로 추방하기 선택시
    //유저의 추방사유를 선택할수있는 체크박스에 맞춰 채팅이감감
     class dialog_exile{
        Context context;
        Dialog dialog;
        int position;
        String user_nickname;
        public dialog_exile(Context context,int position,String user_nickname){
            this.context = context;
        this.position = position;
        this.user_nickname = user_nickname;
        }

        public void dialog_open(){
            dialog = new Dialog(context);
            //다이얼로그 타이틀바 없애기
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            //다이얼로그 레이아웃 설정
            dialog.setContentView(R.layout.dialog_exile_user);
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            //다이얼로그 크기조절
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(params);
            dialog.show();
            Button ok_button = dialog.findViewById(R.id.exile_okbutton);

            CheckBox exile_box0 = dialog.findViewById(R.id.exile_box0);
            CheckBox exile_box1 = dialog.findViewById(R.id.exile_box1);
            CheckBox exile_box2 = dialog.findViewById(R.id.exile_box2);
            CheckBox exile_box3 = dialog.findViewById(R.id.exile_box3);
            CheckBox exile_box4 = dialog.findViewById(R.id.exile_box4);

            TextView exile_text = dialog.findViewById(R.id.exile_text);
            exile_text.setText("정말 "+user_nickname+"님을 추방하시겠어요?");


            ok_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<String> check_list = new ArrayList<>();
                    check_list = get_box_checked(exile_box0,check_list);
                    check_list = get_box_checked(exile_box1,check_list);
                    check_list = get_box_checked(exile_box2,check_list);
                    check_list = get_box_checked(exile_box3,check_list);
                    check_list = get_box_checked(exile_box4,check_list);
                    String send_string = "";
                    for(String text :check_list){
                        send_string = send_string+"\n"+text;
                    }
                    exile_user(position,send_string,dialog);
                }
            });
            Button cancel_button = dialog.findViewById(R.id.exile_canelbutton);
            cancel_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

        }

        public ArrayList<String> get_box_checked(CheckBox checkbox,ArrayList<String> check_list){
            if(checkbox.isChecked()){
                check_list.add(checkbox.getText().toString());
            }
            return check_list;
        }

    }
}

