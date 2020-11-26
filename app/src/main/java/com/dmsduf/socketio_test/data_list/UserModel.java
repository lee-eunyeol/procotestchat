package com.dmsduf.socketio_test.data_list;

public class UserModel {
    String nickname;
    int idx;
    int op_idx;
    String a;

    public  UserModel(String nickname, int idx) {
        this.nickname = nickname;
        this.idx = idx;
    }
    public  UserModel(int idx,int op_idx){
        this.idx = idx;
        this.op_idx=op_idx;

    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }
}
