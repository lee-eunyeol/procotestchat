package com.dmsduf.socketio_test.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.dmsduf.socketio_test.R;
import com.dmsduf.socketio_test.SharedSettings;
import com.dmsduf.socketio_test.chat.ChattingActivity;
import com.dmsduf.socketio_test.data_list.ChatRoomModel;
import com.dmsduf.socketio_test.data_list.ChattingModel;

import java.util.List;

import io.socket.client.Ack;

import static com.dmsduf.socketio_test.chat.ChatClientIO.emit_socket;

public class ChatRoomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<ChatRoomModel> chatRoomModel;
    Context context;
    String TAG = "ChatRoomAdapter";
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
    }
    Handler handler;



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
                SharedSettings sharedSettings = new SharedSettings(context,"user_info");
                emit_socket("join_room", chatRoomModel.get(position).getChatroom_idx(), new Ack() {
                    @Override
                    public void call(Object... args) {
                        Log.d(TAG,args[0].toString());

                        int room_idx = chatRoomModel.get(position).getChatroom_idx();
                        sharedSettings.set_something_string("room_idx"+room_idx,args[0].toString());
                        Intent intent  = new Intent(context, ChattingActivity.class);
                        intent.putExtra("room_idx",room_idx);

                        context.startActivity(intent);


                        //입장하는 순간 방 idx를 저장한다.
                        sharedSettings.set_something_int("current_room_idx",room_idx);

                    }

                });
                String nickname = sharedSettings.get_something_string("user_nickname");
                int user_idx = sharedSettings.get_something_int("user_idx");
                Toast.makeText(context, "입장한 방"+chatRoomModel.get(position).getChatroom_idx()+"\n닉네임: "+nickname+"\n유저번호: "+user_idx, Toast.LENGTH_SHORT).show();

            }
        });
        ((view_holder)holder).creator.setText(chatRoomModel.get(position).getCreator_nickname());
        ((view_holder)holder).room_title.setText(chatRoomModel.get(position).getRoom_name());
        ((view_holder)holder).time.setText(chatRoomModel.get(position).getTime());

    }
    public void update_new_message(ChattingModel chattingModel){
        for (int i= 0 ; i<chatRoomModel.size();i++){
            Log.d(TAG,"바"+chatRoomModel.get(i).getChatroom_idx()+"/"+chattingModel.getRoom_idx());
            if (chatRoomModel.get(i).getChatroom_idx()==chattingModel.getRoom_idx()){
                Log.d(TAG,"바뀌어야할 채팅방 찾음");
                chatRoomModel.get(i).setLast_message(chattingModel.getContent());
                chatRoomModel.get(i).setTime(chattingModel.getCreated_at());

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
