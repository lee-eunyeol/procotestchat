package com.dmsduf.socketio_test.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import static com.dmsduf.socketio_test.chat.ChatClientIO.current_room_idx;
import static com.dmsduf.socketio_test.chat.ChatClientIO.gson;

import com.dmsduf.socketio_test.R;
import com.dmsduf.socketio_test.SharedSettings;
import com.dmsduf.socketio_test.chat.ChattingActivity;
import com.dmsduf.socketio_test.data_list.ChatRoomModel;
import com.dmsduf.socketio_test.data_list.ChattingModel;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ChatRoomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<ChatRoomModel> chatRoomModel;
    Context context;
    String TAG = "ChatRoomAdapter";
    SharedSettings sharedSettings;
    final Runnable sr= new Runnable() {
        public void run() {
            notifyDataSetChanged();
        }

    };
    //리사이클러뷰 업데이트 오류떄문에 쓴것.  https://gogorchg.tistory.com/entry/Android-Cannot-call-this-method-while-RecyclerView-is-computing-a-layout-or-scrolling
    public void notify_with_handler(){
        handler.post(sr);
    }
    public ChatRoomAdapter( Context context,List<ChatRoomModel> chatRoomModel) {
        this.chatRoomModel = chatRoomModel;
        this.context = context;
        this.handler = new Handler();
        sharedSettings = new SharedSettings(context,"user_info");

    }
    Handler handler;


    public List<ChatRoomModel> getChatRoomModel() {
        return chatRoomModel;
    }

    public void setChatRoomModel(List<ChatRoomModel> chatRoomModel) {
        this.chatRoomModel = chatRoomModel;
        notify_with_handler();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reclerview_chat_list,parent,false);

        ChatRoomAdapter.view_holder view_holder =new ChatRoomAdapter.view_holder(view);
        return view_holder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((view_holder)holder).last_message.setText(chatRoomModel.get(position).getLast_message());
        ((view_holder)holder).chat_list_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //소켓에 방번호 주어서 방 입장신호 emit하기
//                SharedSettings sharedSettings = new SharedSettings(context,"user_info");
                //입장하는 순간 방 idx를 벼수에 담는다..
                current_room_idx  = chatRoomModel.get(position).getIdx();

//                sharedSettings.set_something_int("current_room_idx",current_room_idx);

                Intent intent  = new Intent(context, ChattingActivity.class);
                context.startActivity(intent);



            }
        });
        ((view_holder)holder).creator.setText(chatRoomModel.get(position).getCreator_nickname());
        ((view_holder)holder).room_title.setText(chatRoomModel.get(position).getRoom_name());
        ((view_holder)holder).time.setText(chatRoomModel.get(position).getCreated_at());
        String messages = sharedSettings.get_chatroom_messages(String.valueOf(chatRoomModel.get(position).getIdx()));

        Type type = new TypeToken<List<ChattingModel>>() {
        }.getType();

        if(messages.equals("없음")){Log.d(TAG,"안읽은메시지오류");}
        else{
            int none_see_count = 0;
            ArrayList<ChattingModel> chat_datas = gson.fromJson(messages,type);


            for(int i = chat_datas.size()-1;i>=0;i--){
                Log.d(TAG,"비교"+chat_datas.get(i).getIdx()+"vs"+chatRoomModel.get(position).getRead_last_idx());
                //메시지 idx가 이사람이 가장최근의 읽은 idx보다 크면 안읽은거 작으면 읽은거
                if (chat_datas.get(i).getIdx()<=chatRoomModel.get(position).getRead_last_idx()){
                    break;
                }
                else{

                    none_see_count = none_see_count+1;
                }

            }
            Log.d(TAG,none_see_count+"");
            ((view_holder)holder).recycler_chat_list_see_count.setText(none_see_count+"");
        }


    }
    public void update_new_message(ChattingModel chattingModel){
        for (int i= 0 ; i<chatRoomModel.size();i++){
            Log.d(TAG,"바"+chatRoomModel.get(i).getIdx()+"/"+chattingModel.getChatroom_idx());
            if (chatRoomModel.get(i).getIdx()==chattingModel.getChatroom_idx()){
                Log.d(TAG,"바뀌어야할 채팅방 찾음");
                chatRoomModel.get(i).setLast_message(chattingModel.getContent());
                chatRoomModel.get(i).setCreated_at(chattingModel.getCreated_at());

                break;
            }
        }
        notify_with_handler();
    }
    @Override
    public int getItemCount() {
        return chatRoomModel.size();
    }

    public class view_holder extends RecyclerView.ViewHolder{

        TextView time;
        TextView last_message;
        TextView creator;
        TextView room_title;
        TextView room_interest;
        ConstraintLayout chat_list_layout;
        TextView recycler_chat_list_see_count;

        public view_holder(@NonNull View itemView) {
            super(itemView);
            this.room_interest = itemView.findViewById(R.id.recycler_chat_list_room_interest);
            this.time = itemView.findViewById(R.id.recycler_chat_list_time);
            this.last_message = itemView.findViewById(R.id.recycler_chat_list_text);
            this.chat_list_layout = itemView.findViewById(R.id.recycler_chat_list_layout);
            this.recycler_chat_list_see_count = itemView.findViewById(R.id.recycler_chat_list_see_count);
            this.creator = itemView.findViewById(R.id.recycler_chat_list_creator);
            this.room_title = itemView.findViewById(R.id.recycler_chat_list_room_title);

        }
    }
}
