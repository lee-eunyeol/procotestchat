package com.dmsduf.socketio_test.data_list;

public class UserModel {
    int idx;
    String nickname;
    String profile_photo_path;





    public String getProfile_photo_path() {
        return profile_photo_path;
    }

    public void setProfile_photo_path(String profile_photo_path) {
        this.profile_photo_path = profile_photo_path;
    }

    public UserModel(int idx, String nickname, String profile_photo_path) {
        this.idx = idx;
        this.nickname = nickname;
        this.profile_photo_path = profile_photo_path;
    }

    public  UserModel(int idx){
        this.idx = idx;
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
