package com.dmsduf.socketio_test.data_list;

import java.io.Serializable;

public class RoomModel implements Serializable {
    private int idx;
    private int creator_idx;
    private String type;
    private int room_people_count;

    public RoomModel(int idx, int creator_idx, String type, int room_people_count) {
        this.idx = idx;
        this.creator_idx = creator_idx;
        this.type = type;
        this.room_people_count = room_people_count;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public int getCreator_idx() {
        return creator_idx;
    }

    public void setCreator_idx(int creator_idx) {
        this.creator_idx = creator_idx;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRoom_people_count() {
        return room_people_count;
    }

    public void setRoom_people_count(int room_people_count) {
        this.room_people_count = room_people_count;
    }
}
