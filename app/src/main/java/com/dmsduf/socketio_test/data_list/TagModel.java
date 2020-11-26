package com.dmsduf.socketio_test.data_list;

public class TagModel {
    int idx;

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TagModel(int idx, String name) {
        this.idx = idx;
        this.name = name;
    }

    String name;
}
