package com.dmsduf.socketio_test.data_list;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomModel {
    int card_idx;  //모임 인덱스
    int chatroom_idx;
    String chatrooom_type; //채팅방 타입 (일반채팅,친구와채팅,비 친구와 채팅)
    List<TagModel> card_tag;
    String creator_profile;
    String creator_nickname;
    String room_name;

    public int getCard_idx() {
        return card_idx;
    }

    public void setCard_idx(int card_idx) {
        this.card_idx = card_idx;
    }

    public int getChatroom_idx() {
        return chatroom_idx;
    }

    public void setChatroom_idx(int chatroom_idx) {
        this.chatroom_idx = chatroom_idx;
    }

    public String getChatrooom_type() {
        return chatrooom_type;
    }

    public void setChatrooom_type(String chatrooom_type) {
        this.chatrooom_type = chatrooom_type;
    }

    public List<TagModel> getCard_tag() {
        return card_tag;
    }

    public void setCard_tag(List<TagModel> card_tag) {
        this.card_tag = card_tag;
    }

    public String getCreator_profile() {
        return creator_profile;
    }

    public void setCreator_profile(String creator_profile) {
        this.creator_profile = creator_profile;
    }

    public String getCreator_nickname() {
        return creator_nickname;
    }

    public void setCreator_nickname(String creator_nickname) {
        this.creator_nickname = creator_nickname;
    }

    public String getLast_message() {
        return last_message;
    }

    public void setLast_message(String last_message) {
        this.last_message = last_message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    String last_message;

    public ChatRoomModel(int card_idx, int chatroom_idx, String chatrooom_type, List<TagModel> card_tag, String creator_profile, String creator_nickname, String last_message, String time) {
        this.card_idx = card_idx;
        this.chatroom_idx = chatroom_idx;
        this.chatrooom_type = chatrooom_type;
        this.card_tag = card_tag;
        this.creator_profile = creator_profile;
        this.creator_nickname = creator_nickname;
        this.last_message = last_message;
        this.time = time;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    String time;

}
