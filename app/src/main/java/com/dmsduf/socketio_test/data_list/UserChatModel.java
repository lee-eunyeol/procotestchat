package com.dmsduf.socketio_test.data_list;

public class UserChatModel {
    int idx;
    String nickname;
    String profile_photo_path;
    int read_start_idx;
    int read_last_idx;
    String deleted_at;

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getProfile_photo_path() {
        return profile_photo_path;
    }

    public void setProfile_photo_path(String profile_photo_path) {
        this.profile_photo_path = profile_photo_path;
    }

    public int getRead_last_idx() {
        return read_last_idx;
    }

    public void setRead_last_idx(int read_last_idx) {
        this.read_last_idx = read_last_idx;
    }

    public int getRead_start_idx() {
        return read_start_idx;
    }

    public void setRead_start_idx(int read_start_idx) {
        this.read_start_idx = read_start_idx;
    }

    public UserChatModel(int idx, String nickname, String profile_photo_path, int read_last_idx, int read_start_idx,String deleted_at) {
        this.idx = idx;
        this.nickname = nickname;
        this.profile_photo_path = profile_photo_path;
        this.read_last_idx = read_last_idx;
        this.read_start_idx = read_start_idx;
        this.deleted_at = deleted_at;
    }


}
