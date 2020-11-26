package com.dmsduf.socketio_test.data_list;

import java.util.List;

public class ChattingModel {
    int room_idx; //참여중인 방
    int user_idx; //보낸사람 idx
    String type;
    List<Integer> read_people;
    public int getRoom_name() {
        return room_idx;
    }

    public void setRoom_name(int room_idx) {
        this.room_idx = room_idx;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    String nickname; //보낸사람 닉네임
    String message; //메세지내용

    public List<Integer> getRead_people() {
        return read_people;
    }

    public void setRead_people(List<Integer> read_people) {
        this.read_people = read_people;
    }

    public int getRoom_idx() {
        return room_idx;
    }

    public void setRoom_idx(int room_idx) {
        this.room_idx = room_idx;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type =type;
    }
    public void set_pendingType(String type) {
        this.type = this.type+type;
    }

    public ChattingModel(int room_idx, int user_idx, String type, String nickname, String message, String time,List<Integer> read_people) {
        this.room_idx = room_idx;
        this.user_idx = user_idx;
        this.type = type;
        this.nickname = nickname;
        this.message = message;
        this.time = time;
        this.read_people = read_people;
    }

    String time; //보낸시간

}
