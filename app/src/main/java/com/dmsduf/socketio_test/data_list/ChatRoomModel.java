package com.dmsduf.socketio_test.data_list;

import android.util.Log;

import java.util.List;

public class ChatRoomModel {
    int idx; //채팅방의 인덱스
    int card_idx;  //모임 인덱스
    int creator_idx; //채팅방 만든사람 인덱스
    String kinds; //채팅방 종류 (일반채팅,친구와채팅)
    String creator_nickname; //만든사람 닉네임
    String room_name; //방 제목
    List<TagModel> card_tag_list; //태그
    List<UserChatModel> chatroom_users; //채팅방에 참여중인 인원 내역
    CardModel card; //채팅방의 카드정보
    String created_at;// 채팅방이 만들어진 시간
    String updated_at; // 채팅방 수정된 시간




    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public int getCard_idx() {
        return card_idx;
    }

    public void setCard_idx(int card_idx) {
        this.card_idx = card_idx;
    }

    public int getCreator_idx() {
        return creator_idx;
    }

    public void setCreator_idx(int creator_idx) {
        this.creator_idx = creator_idx;
    }

    public String getKinds() {
        return kinds;
    }

    public void setKinds(String kinds) {
        this.kinds = kinds;
    }



    public String getCreator_nickname() {
        return creator_nickname;
    }

    public void setCreator_nickname(String creator_nickname) {
        this.creator_nickname = creator_nickname;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }



    public List<TagModel> getCard_tag_list() {
        return card_tag_list;
    }

    public void setCard_tag_list(List<TagModel> card_tag_list) {
        this.card_tag_list = card_tag_list;
    }

    public List<UserChatModel> getChatroom_users() {
        return chatroom_users;
    }
    public UserChatModel getuser(int user_idx){
        for(UserChatModel userChatModel :this.chatroom_users){
            if(userChatModel.getIdx()==user_idx){
                return userChatModel;
            }
        }
        return new UserChatModel(-1,"없음","아",-1,-1,"-1");

    }
    public void setuser(UserChatModel userChatModeling){
        for(int i = 0;i<this.chatroom_users.size();i++){
            if(this.chatroom_users.get(i).getIdx()==userChatModeling.getIdx()){
                this.chatroom_users.set(i,userChatModeling);

                Log.d("아아ㅏ아","바꿀거찾음");
            }
        }


    }

    public void setChatroom_users(List<UserChatModel> chatroom_users) {
        this.chatroom_users = chatroom_users;
    }

    public CardModel getCard() {
        return card;
    }

    public void setCard(CardModel card) {
        this.card = card;
    }



    public ChatRoomModel(int idx, int card_idx, int creator_idx, String kinds, String creator_nickname, String room_name, String created_at, String updated_at, List<TagModel> card_tag_list, List<UserChatModel> chatroom_users, CardModel card) {
        this.idx = idx;
        this.card_idx = card_idx;
        this.creator_idx = creator_idx;

        this.kinds = kinds;

        this.creator_nickname = creator_nickname;
        this.room_name = room_name;
        this.created_at = created_at;
        this.updated_at = updated_at;

        this.card_tag_list = card_tag_list;
        this.chatroom_users = chatroom_users;
        this.card = card;
    }
}
