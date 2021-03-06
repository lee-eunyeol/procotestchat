package com.dmsduf.socketio_test.data_list;

import java.util.ArrayList;

public class UserStateModel {
    int user_idx;  // 사용자의 인덱스
    String state; //상태명 (on,off)
    String state_data; // 상태 관련정보
    ArrayList<Integer> announce_users; //알릴 친구 idx리스트


    public int getUser_idx() {
        return user_idx;
    }

    public void setUser_idx(int user_idx) {
        this.user_idx = user_idx;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public ArrayList<Integer> getAnnounce_users() {
        return announce_users;
    }

    public void setAnnounce_users(ArrayList<Integer> announce_users) {
        this.announce_users = announce_users;
    }

    public UserStateModel(int user_idx, String state, ArrayList<Integer> announce_users) {
        this.user_idx = user_idx;
        this.state = state;
        this.announce_users = announce_users;
    }



}
