package com.dmsduf.socketio_test.data_list;

public class RoomModel {
    String room_title;
    String room_code;
    String max_count;
    String current_count;
    String interested;
    String room_maker_email;
    String room_maker_nickname;


    public String getRoom_title() {
        return room_title;
    }

    public void setRoom_title(String room_title) {
        this.room_title = room_title;
    }

    public String getRoom_code() {
        return room_code;
    }

    public void setRoom_code(String room_code) {
        this.room_code = room_code;
    }

    public String getMax_count() {
        return max_count;
    }

    public void setMax_count(String max_count) {
        this.max_count = max_count;
    }

    public String getCurrent_count() {
        return current_count;
    }

    public void setCurrent_count(String current_count) {
        this.current_count = current_count;
    }

    public String getInterested() {
        return interested;
    }

    public void setInterested(String interested) {
        this.interested = interested;
    }

    public String getMade_time() {
        return made_time;
    }

    public void setMade_time(String made_time) {
        this.made_time = made_time;
    }

    public String getUsers_nickname() {
        return users_nickname;
    }

    public void setUsers_nickname(String users_nickname) {
        this.users_nickname = users_nickname;
    }

    String made_time;
    String users_nickname;

    public String getRoom_maker_email() {
        return room_maker_email;
    }

    public void setRoom_maker_email(String room_maker_email) {
        this.room_maker_email = room_maker_email;
    }

    public String getRoom_maker_nickname() {
        return room_maker_nickname;
    }

    public void setRoom_maker_nickname(String room_maker_nickname) {
        this.room_maker_nickname = room_maker_nickname;
    }

    String users_email;

    public String getUsers_email() {
        return users_email;
    }

    public void setUsers_email(String users_email) {
        this.users_email = users_email;
    }

    public RoomModel(String room_title, String room_code, String max_count, String current_count, String interested, String made_time, String users_nickname, String users_email, String room_maker_email, String room_maker_nickname) {
        this.room_title = room_title;
        this.room_code = room_code;
        this.max_count = max_count;
        this.current_count = current_count;
        this.interested = interested;
        this.made_time = made_time;
        this.users_nickname = users_nickname;
        this.users_email = users_email;
        this.room_maker_email = room_maker_email;
        this.room_maker_nickname = room_maker_nickname;
    }
}
