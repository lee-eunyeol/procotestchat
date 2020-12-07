package com.dmsduf.socketio_test.data_list;

import java.util.List;

public class ChatRoomModel {
    int card_idx;  //모임 인덱스
    int chatroom_idx;

    String chatrooom_type; //채팅방 타입 (일반채팅,친구와채팅,비 친구와 채팅)
    List<TagModel> card_tag;
    String creator_profile;
    String creator_nickname;
    String room_name;
    String created_at;
    String content;
    int none_read_count;

    public int getNone_read_count() {
        return none_read_count;
    }

    public void setNone_read_count(int none_read_count) {
        this.none_read_count = none_read_count;
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }


    public ChatRoomModel(int card_idx, int chatroom_idx, String chatrooom_type, List<TagModel> card_tag, String creator_profile, String creator_nickname, String content, String created_at,int none_read_count) {
        this.card_idx = card_idx;
        this.chatroom_idx = chatroom_idx;
        this.chatrooom_type = chatrooom_type;
        this.card_tag = card_tag;
        this.creator_profile = creator_profile;
        this.creator_nickname = creator_nickname;
        this.content = content;
        this.created_at = created_at;
        this.none_read_count = none_read_count;

    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }



}
