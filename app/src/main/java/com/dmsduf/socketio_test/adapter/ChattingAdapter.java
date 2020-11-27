package com.dmsduf.socketio_test.adapter;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dmsduf.socketio_test.R;
import com.dmsduf.socketio_test.data_list.ChattingModel;
import com.dmsduf.socketio_test.data_list.RoomModel;

import java.util.List;

public class ChattingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<ChattingModel> chat_data;
    RoomModel roomModel;
    final static int my_chat = 1;
    final static int opponent_chat = 2;
    final static int speaker = 3;

    String TAG = "채팅어댑터";
    int my_idx;
    Handler handler;
    final Runnable r= new Runnable() {
        public void run() {
            notifyDataSetChanged();
        }

    };

    //리사이클러뷰 업데이트 오류떄문에 쓴것.  https://gogorchg.tistory.com/entry/Android-Cannot-call-this-method-while-RecyclerView-is-computing-a-layout-or-scrolling
    public void notify_with_handler(){
        handler.post(r);
    }
    //TODO 프론트에서 보낸시간을 기준으로 메세지를 체크 하고있는데 이게 맞을까?

    //성공적으로 메세지를 보냈을 경우 해당하는 메세지를 보냈던 정확한 시간(currenttimemills)을 찾아서 업데이트 시켜준다.
    public void set_message_success(ChattingModel server_msg) {
        for (int i = 0; i < chat_data.size(); i++) {

            if (chat_data.get(i).getFront_time() - server_msg.getFront_time() == 0) {  //프론트가 보냈던 시간과 맞는 데이터를 찾은 후 메시지를 바꾼다.
                chat_data.set(i,server_msg);
                notify_with_handler();
                break;
            }

        }


    }

    //메세지보내기가 실패했을 경우 실패 메세지를 띄운다.
    public void set_message_error(Long time) {
        for (int i = 0; i < chat_data.size(); i++) {
            if (chat_data.get(i).getFront_time() - time == 0) {
                chat_data.get(i).add_errorType();
                notify_with_handler();
                break;
            }
        }


    }

    //내가 메세지를 받았을 경우 : 시간을 보낸 정확한 시간을 가지고 키값에 넣는다.
    public void add_message(ChattingModel ChattingModel) {


        chat_data.add(ChattingModel);

        notifyDataSetChanged();

    }

    //내가 메세지를 보냈을경우 : 시간을 보낸 정확한 시간을 가지고 키값에 넣는다.
    public void add_front_message(ChattingModel ChattingModel) {

        chat_data.add(ChattingModel);
        notifyDataSetChanged();

    }

    public ChattingAdapter(final Context context, final List<ChattingModel> data, int my_idx, RoomModel roomModel) {
        this.context = context;
        this.chat_data = data;
        this.my_idx = my_idx;
        this.roomModel = roomModel;
        this.handler = new Handler();


    }

    @Override
    public int getItemViewType(int position) {

        ChattingModel data_ = chat_data.get(position);
        if (data_.getUser_idx() == my_idx) {
            return my_chat;
        } else {
            return opponent_chat;
        }

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == my_chat) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_chat_me, parent, false);

            my_chat_view_holder viewholder = new my_chat_view_holder(view);
            return viewholder;
        } else if (viewType == speaker) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_chat_speaker, parent, false);
            speaker_view_holer viewHoler = new speaker_view_holer(view);
            return viewHoler;
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_chat_op, parent, false);

            op_chat_view_holder viewholder = new op_chat_view_holder(view);
            return viewholder;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        int current_peoples = roomModel.getRoom_people_count();
        int read_users = chat_data.get(position).getRead_people().size();
        int none_read_count = current_peoples - read_users;

        switch (holder.getItemViewType()) {

            case my_chat:
                ((my_chat_view_holder) holder).chat.setText(chat_data.get(position).getMessage());
                ((my_chat_view_holder) holder).time.setText(chat_data.get(position).getTime());
                //읽음처리
                if (chat_data.get(position).getType().contains("[pending]")) {
                    ((my_chat_view_holder) holder).count.setText("대기중");

                } else if (chat_data.get(position).getType().contains("[error]")) {
                    ((my_chat_view_holder) holder).count.setText("에러");
                } else {
                    if (none_read_count == 0) {
                        ((my_chat_view_holder) holder).count.setText("");
                    } else {
                        ((my_chat_view_holder) holder).count.setText(String.valueOf(none_read_count));
                    }
                }


                break;
            case opponent_chat:
                ((op_chat_view_holder) holder).chat.setText(chat_data.get(position).getMessage());
                ((op_chat_view_holder) holder).recycler_op_name.setText(chat_data.get(position).getNickname());

                //읽음처리
                if (chat_data.get(position).getType().contains("[pending]")) {
                    Log.d(TAG, "소켓연결이 안되서 메세지 안보내짐");
                    ((op_chat_view_holder) holder).count.setText("대기중");

                } else {
                    if (none_read_count == 0) {
                        ((op_chat_view_holder) holder).count.setText("");
                    } else {
                        ((op_chat_view_holder) holder).count.setText(String.valueOf(none_read_count));
                    }
                }
                ((op_chat_view_holder) holder).time.setText(chat_data.get(position).getTime());
                break;

            case speaker:
                if (chat_data.get(position).getMessage().contains("추방되셨습니다")) {
                    //추방사유가 적혀있을떄 가져옴
                    String message = chat_data.get(position).getMessage().split("추방되셨습니다")[0];
                    String why = chat_data.get(position).getMessage().split("추방되셨습니다")[1];
                    ((speaker_view_holer) holder).speaker_text.setText(message + "추방되셨습니다.");
                    ((speaker_view_holer) holder).exit_layout.setVisibility(View.VISIBLE);
                    ((speaker_view_holer) holder).why_texe.setText("추방사유");
                    ((speaker_view_holer) holder).exit_why.setText(why);
                } else if (chat_data.get(position).getMessage().contains("수정 되었습니다.")) {

                    String[] why = chat_data.get(position).getMessage().split("되었습니다.")[1].split("!!");
                    ((speaker_view_holer) holder).speaker_text.setText("방 내용이 수정되었습니다.");
                    ((speaker_view_holer) holder).exit_layout.setVisibility(View.VISIBLE);
                    ((speaker_view_holer) holder).why_texe.setText("수정 내역");
                    ((speaker_view_holer) holder).exit_why.setText("제목: " + why[0] + "\n관련 언어 및 분야: " + why[2] + "\n최대인원 수: " + why[1]);
                } else {
                    ((speaker_view_holer) holder).exit_layout.setVisibility(View.GONE);
                    ((speaker_view_holer) holder).speaker_text.setText(chat_data.get(position).getMessage());
                }
                break;
        }


    }


    @Override
    public int getItemCount() {

        return chat_data.size();
    }


    public class my_chat_view_holder extends RecyclerView.ViewHolder {
        TextView chat;
        TextView time;
        TextView count;


        public my_chat_view_holder(@NonNull View itemView) {
            super(itemView);
            this.chat = itemView.findViewById(R.id.recycler_chat_me);
            this.time = itemView.findViewById(R.id.recycler_chat_me_time);
            this.count = itemView.findViewById(R.id.recycler_chat_me_count);

        }

    }

    public class op_chat_view_holder extends RecyclerView.ViewHolder {
        TextView chat;
        TextView time;
        TextView recycler_op_name;
        TextView count;


        public op_chat_view_holder(@NonNull View itemView) {
            super(itemView);
            this.recycler_op_name = itemView.findViewById(R.id.recycler_op_name);
            this.chat = itemView.findViewById(R.id.recycler_op_chat);
            this.time = itemView.findViewById(R.id.recycler_op_time);
            this.count = itemView.findViewById(R.id.recycler_op_count);

        }
    }

    public class speaker_view_holer extends RecyclerView.ViewHolder {
        TextView speaker_text;
        CardView exit_layout;
        TextView exit_why;
        TextView why_texe;

        public speaker_view_holer(@NonNull View itemView) {
            super(itemView);
            why_texe = itemView.findViewById(R.id.why_texe);
            exit_why = itemView.findViewById(R.id.exit_why);
            exit_layout = itemView.findViewById(R.id.exit_layout);
            speaker_text = itemView.findViewById(R.id.speaker_chat);
        }
    }

}




