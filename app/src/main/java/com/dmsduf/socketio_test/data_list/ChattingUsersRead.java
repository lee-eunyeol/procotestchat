package com.dmsduf.socketio_test.data_list;

import java.util.ArrayList;

public class ChattingUsersRead {
    private int room_idx;
    ArrayList<ReadItem> read_items;

    public int getRoom_idx() {
        return room_idx;
    }

    public void setRoom_idx(int room_idx) {
        this.room_idx = room_idx;
    }

    public ArrayList<ReadItem> getRead_items() {
        return read_items;
    }

    public void setRead_items(ArrayList<ReadItem> read_items) {
        this.read_items = read_items;
    }

    public ChattingUsersRead(int room_idx, ArrayList<ReadItem> read_items) {
        this.room_idx = room_idx;
        this.read_items = read_items;
    }

public class ReadItem{
        int read_start_idx;
        int read_last_idx;

        public int getRead_start_idx() {
            return read_start_idx;
        }

        public void setRead_start_idx(int read_start_idx) {
            this.read_start_idx = read_start_idx;
        }

        public int getRead_last_idx() {
            return read_last_idx;
        }

        public void setRead_last_idx(int read_last_idx) {
            this.read_last_idx = read_last_idx;
        }
    }
}
