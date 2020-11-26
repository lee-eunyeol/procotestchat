package com.dmsduf.socketio_test.adapter;

import android.content.Context;
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

import java.util.List;

public class ChattingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context context;
    List<ChattingModel> chat_data;

    final static int my_chat = 1;
    final static int opponent_chat = 2;
    final static int speaker = 3;

    String TAG = "채팅어댑터";
    int my_idx;

    public void add_message(ChattingModel ChattingModel){
        chat_data.add(ChattingModel);
        notifyDataSetChanged();

    }
    public ChattingAdapter(final Context context, final List<ChattingModel> data, int my_idx) {
        this.context = context;
        this.chat_data = data;
        this.my_idx = my_idx;


    }
    @Override
    public int getItemViewType(int position) {

        ChattingModel data_ = chat_data.get(position);
        if(data_.getUser_idx()==my_idx){
            return my_chat;
        }
        else{
            return  opponent_chat;
        }

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "어뎁터뷰홀더");
        if (viewType == my_chat) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_chat_me, parent, false);

            my_chat_view_holder viewholder = new my_chat_view_holder(view);
            return viewholder;
        }
        else if(viewType == speaker){
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_chat_speaker, parent, false);
            speaker_view_holer viewHoler = new speaker_view_holer(view);
            return viewHoler;
        }
        else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_chat_op, parent, false);

            op_chat_view_holder viewholder = new op_chat_view_holder(view);
            return viewholder;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case my_chat:
                ((my_chat_view_holder) holder).chat.setText(chat_data.get(position).getMessage());
                ((my_chat_view_holder) holder).time.setText(chat_data.get(position).getTime());

                break;
            case opponent_chat:
                ((op_chat_view_holder) holder).chat.setText(chat_data.get(position).getMessage());
                ((op_chat_view_holder) holder).recycler_op_name.setText(chat_data.get(position).getNickname());


                ((op_chat_view_holder) holder).time.setText(chat_data.get(position).getTime());
            break;

            case speaker:
                if(chat_data.get(position).getMessage().contains("추방되셨습니다")){
                    //추방사유가 적혀있을떄 가져옴
                    String message =   chat_data.get(position).getMessage().split("추방되셨습니다")[0];
                 String why =    chat_data.get(position).getMessage().split("추방되셨습니다")[1];
                    ((speaker_view_holer)holder).speaker_text.setText(message+"추방되셨습니다.");
                    ((speaker_view_holer)holder).exit_layout.setVisibility(View.VISIBLE);
                    ((speaker_view_holer)holder).why_texe.setText("추방사유");
                    ((speaker_view_holer)holder).exit_why.setText(why);
                }
                else if(chat_data.get(position).getMessage().contains("수정 되었습니다.")){

                    String[] why =    chat_data.get(position).getMessage().split("되었습니다.")[1].split("!!");
                    ((speaker_view_holer)holder).speaker_text.setText("방 내용이 수정되었습니다.");
                    ((speaker_view_holer)holder).exit_layout.setVisibility(View.VISIBLE);
                    ((speaker_view_holer)holder).why_texe.setText("수정 내역");
                    ((speaker_view_holer)holder).exit_why.setText("제목: "+why[0]+"\n관련 언어 및 분야: "+why[2]+"\n최대인원 수: "+why[1]);
                }
                else{
                    ((speaker_view_holer)holder).exit_layout.setVisibility(View.GONE);
                ((speaker_view_holer)holder).speaker_text.setText(chat_data.get(position).getMessage());
                }
                break;
        }


    }






    @Override
    public int getItemCount() {
        Log.d(TAG, chat_data.size()+"개수");
        return chat_data.size();
    }




    public class my_chat_view_holder extends RecyclerView.ViewHolder {
        TextView chat;
        TextView time;



        public my_chat_view_holder(@NonNull View itemView) {
            super(itemView);
            this.chat = itemView.findViewById(R.id.recycler_chat_me);
            this.time = itemView.findViewById(R.id.recycler_chat_me_time);

        }

    }

    public class op_chat_view_holder extends RecyclerView.ViewHolder {
        TextView chat;
        TextView time;
        TextView recycler_op_name;


        public op_chat_view_holder(@NonNull View itemView) {
            super(itemView);
            this.recycler_op_name = itemView.findViewById(R.id.recycler_op_name);
            this.chat = itemView.findViewById(R.id.recycler_op_chat);
            this.time = itemView.findViewById(R.id.recycler_op_time);


        }}
        public class speaker_view_holer extends RecyclerView.ViewHolder{
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




