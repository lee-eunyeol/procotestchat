package com.dmsduf.socketio_test.data_list;

import android.util.Log;

public class ChattingModel {
    int idx;
    int chatroom_idx; //참여중인 방
    int user_idx; //보낸사람 idx
    String kinds;  //채팅메시지종류
    String read_users;   //읽은 사람들
    String TAG = "ChattingModel";
    String nickname; //보낸사람 닉네임
    String content; //메세지내용
    int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ChattingModel(int idx, int chatroom_idx, int user_idx, String kinds, String read_users, String nickname, String content, String created_at, Long front_created_at) {
        this.idx = idx;
        this.chatroom_idx = chatroom_idx;
        this.user_idx = user_idx;
        this.kinds = kinds;
        this.read_users = read_users;

        this.nickname = nickname;
        this.content = content;
        this.created_at = created_at;
        this.front_created_at = front_created_at;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    String created_at;  //서버에서 보낸 시간
    Long front_created_at;  //프론트에서 실제로 보낸 시간

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }


    public int getRoom_name() {
        return chatroom_idx;
    }

    public void setRoom_name(int room_idx) {
        this.chatroom_idx = room_idx;
    }

    public int getUser_idx() {
        return user_idx;
    }

    public void setUser_idx(int user_idx) {
        this.user_idx = user_idx;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getRead_users() {
        return read_users;
    }

    public void setRead_users(String read_users) {
        this.read_users = read_users;
    }
    public void setRead_count_plus(int user_idx){
        this.read_users = this.read_users+","+user_idx;

    }

    public int getChatroom_idx() {
        return chatroom_idx;
    }

    public void setChatroom_idx(int chatroom_idx) {
        this.chatroom_idx = chatroom_idx;
    }

    public String getKinds() {
        return kinds;
    }

    public void setKinds(String kinds) {
        this.kinds = kinds;
    }
    public void set_pendingType(String type) {
        this.kinds = this.kinds +type;
    }
    public void remove_pendingType(){
        this.kinds = this.kinds.replace("[pending]","");
        Log.d(TAG,this.kinds +"[remove_pendingType]");
    }
    public void add_errorType(){
        this.kinds = this.kinds.replace("[pending]","[error]");
        Log.d(TAG,this.kinds +"[add_errorType]");
    }

    public Long getFront_created_at() {
        return front_created_at;
    }

    public void setFront_created_at(Long front_created_at) {
        this.front_created_at = front_created_at;
    }





}
