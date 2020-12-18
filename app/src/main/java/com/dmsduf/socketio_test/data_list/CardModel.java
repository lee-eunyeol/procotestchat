package com.dmsduf.socketio_test.data_list;

public class CardModel {
    int creator_idx; //카드만든사람 인덱스
    int promise_state; //약속상태
    int ticket_idx; //티켓 인덱스
    int current_people_count; //현재 인원수
    int max_people_count; //최대 인원수
    String kinds; //카드종류
    String title; // 카드제목
    String introduce; //카드소개
    String address; //약속장소
    Double map_lat; //약속 위도/경도
    Double map_lng;
    String promise_at; //약속시간
    String expiration_at; //만료기한
    String created_at; //카드생성시간
    String updated_at;//카드수정시간

    public int getCreator_idx() {
        return creator_idx;
    }

    public void setCreator_idx(int creator_idx) {
        this.creator_idx = creator_idx;
    }

    public int getPromise_state() {
        return promise_state;
    }

    public void setPromise_state(int promise_state) {
        this.promise_state = promise_state;
    }

    public int getTicket_idx() {
        return ticket_idx;
    }

    public void setTicket_idx(int ticket_idx) {
        this.ticket_idx = ticket_idx;
    }

    public int getCurrent_people_count() {
        return current_people_count;
    }

    public void setCurrent_people_count(int current_people_count) {
        this.current_people_count = current_people_count;
    }

    public int getMax_people_count() {
        return max_people_count;
    }

    public void setMax_people_count(int max_people_count) {
        this.max_people_count = max_people_count;
    }

    public String getKinds() {
        return kinds;
    }

    public void setKinds(String kinds) {
        this.kinds = kinds;
    }

    public String getPromise_at() {
        return promise_at;
    }

    public void setPromise_at(String promise_at) {
        this.promise_at = promise_at;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getMap_lat() {
        return map_lat;
    }

    public void setMap_lat(Double map_lat) {
        this.map_lat = map_lat;
    }

    public Double getMap_lng() {
        return map_lng;
    }

    public void setMap_lng(Double map_lng) {
        this.map_lng = map_lng;
    }

    public String getExpiration_at() {
        return expiration_at;
    }

    public void setExpiration_at(String expiration_at) {
        this.expiration_at = expiration_at;
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


    public CardModel(int creator_idx, int promise_state, int ticket_idx, int current_people_count, int max_people_count, String kinds, String promise_at, String title, String introduce, String address, Double map_lat, Double map_lng, String expiration_at, String created_at, String updated_at) {
        this.creator_idx = creator_idx;
        this.promise_state = promise_state;
        this.ticket_idx = ticket_idx;
        this.current_people_count = current_people_count;
        this.max_people_count = max_people_count;
        this.kinds = kinds;
        this.promise_at = promise_at;
        this.title = title;
        this.introduce = introduce;
        this.address = address;
        this.map_lat = map_lat;
        this.map_lng = map_lng;
        this.expiration_at = expiration_at;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }


}


